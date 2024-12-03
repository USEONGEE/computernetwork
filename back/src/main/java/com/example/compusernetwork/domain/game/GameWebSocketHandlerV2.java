package com.example.compusernetwork.domain.game;

import com.example.compusernetwork.domain.game.message.ErrorMessage;
import com.example.compusernetwork.domain.game.message.GameMessage;
import com.example.compusernetwork.domain.member.Member;
import com.example.compusernetwork.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

import static com.example.compusernetwork.domain.game.message.ErrorMessage.INVALID_EXPRESSION;
import static com.example.compusernetwork.domain.game.message.GameMessageType.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GameWebSocketHandlerV2 {

    private final MemberRepository userRepository;
    private final MatchService matchService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ExpressionParser parser = new ExpressionParser();


    // 매칭 요청 처리
    @MessageMapping("/match")
    @SendTo("/topic/matched")
    public GameMessage handleMatch(StompHeaderAccessor accessor) {
        log.info("Match request received");
        String username = getUsername(accessor);
        Optional<Member> optionalMember = userRepository.findById(username);
        if(optionalMember.isEmpty()) return new GameMessage(ERROR, ErrorMessage.USER_NOT_EXIST.getMessage());
        Member user = optionalMember.get();

        GameRoom gameRoom = matchService.match(user);
        if (gameRoom != null) {
            return new GameMessage(MATCHED, gameRoom);
        }
        return new GameMessage(MATCHED_NOT_YET, null);
    }

    // MOVE 메시지 처리
    @MessageMapping("/add-expression")
    @SendTo("/topic/add-expression")
    public GameMessage handleAddExpression(GameMessage gameMessage, StompHeaderAccessor accessor) {
        String username = getUsername(accessor);

        GameRoom gameRoom = matchService.getGameRoom(gameMessage.getRoomId());
        if (gameRoom == null) return new GameMessage(ERROR, ErrorMessage.ROOM_NOT_EXIST.getMessage());

        GameState gameState = gameRoom.getGameState();
        if (!gameState.getCurrentPlayer().getName().equals(username)) {
            return new GameMessage(ERROR, ErrorMessage.NOT_YOUR_TURN.getMessage());
        }

        // 수식 업데이트
        String expression = gameState.getExpressions().getOrDefault(username, "");
        expression += gameMessage.getContent();
        gameState.getExpressions().put(username, expression);

        return new GameMessage(ADD_EXPRESSION, expression);
    }

    @MessageMapping("/remove-expression")
    @SendTo("/topic/remove-expression")
    public GameMessage handleRemoveExpression(GameMessage gameMessage, StompHeaderAccessor accessor) {
        String username = getUsername(accessor);

        GameRoom gameRoom = matchService.getGameRoom(gameMessage.getRoomId());
        if (gameRoom == null) return new GameMessage(ERROR, ErrorMessage.ROOM_NOT_EXIST.getMessage());

        GameState gameState = gameRoom.getGameState();
        if (!gameState.getCurrentPlayer().getName().equals(username)) {
            return new GameMessage(ERROR, ErrorMessage.NOT_YOUR_TURN.getMessage());
        }

        // 수식 업데이트
        String expression = gameState.getExpressions().getOrDefault(username, "");
        if (expression.length() > 0) {
            expression = expression.substring(0, expression.length() - 1);
        }
        gameState.getExpressions().put(username, expression);
        log.info("Expression removed: {}", expression);

        return new GameMessage(REMOVE_EXPRESSION, expression);
    }

    @MessageMapping("/remove-all-expression")
    @SendTo("/topic/remove-all-expression")
    public GameMessage handleRemoveAllExpression(GameMessage gameMessage, StompHeaderAccessor accessor){
        String username = getUsername(accessor);

        GameRoom gameRoom = matchService.getGameRoom(gameMessage.getRoomId());
        if (gameRoom == null) return new GameMessage(ERROR, ErrorMessage.ROOM_NOT_EXIST.getMessage());

        GameState gameState = gameRoom.getGameState();
        if (!gameState.getCurrentPlayer().getName().equals(username)) {
            return new GameMessage(ERROR, ErrorMessage.NOT_YOUR_TURN.getMessage());
        }

        // 수식 업데이트
        gameState.getExpressions().put(username, "");

        return new GameMessage(REMOVE_ALL_EXPRESSION, "");
    }

    // ATTACK 메시지 처리
    @MessageMapping("/attack")
    public GameMessage handleAttack(GameMessage gameMessage, StompHeaderAccessor accessor) {
        log.info("attack");
        String attacker = getUsername(accessor);

        // 게임 방 가져오기
        GameRoom gameRoom = matchService.getGameRoom(gameMessage.getRoomId());
        if (gameRoom == null) {
            sendToUser(attacker, new GameMessage(ERROR, ErrorMessage.ROOM_NOT_EXIST.getMessage()));
            return null;
        }

        GameState gameState = gameRoom.getGameState();

        // 현재 플레이어인지 확인
        if (!gameState.getCurrentPlayer().getName().equals(attacker)) {
            sendToUser(attacker, new GameMessage(ERROR, ErrorMessage.NOT_YOUR_TURN.getMessage()));
            return null;
        }

        // 공격 처리 로직
        try {
            String expression = gameState.getExpressions().get(attacker);
            if (expression == null || expression.isEmpty()) {
                sendToUser(attacker, new GameMessage(ERROR, INVALID_EXPRESSION.getMessage()));
                return null;
            }

            double result = evaluateExpression(expression);

            if (Math.abs(result - gameState.getTargetNumber()) < 0.0001) {
                // 공격 성공
                String defender = gameState.getOpponent().getName();
                int defenderHP = gameState.getPlayerHP().get(defender) - 20;
                gameState.getPlayerHP().put(defender, defenderHP);

                // 공격자와 수비자에게 메시지 전송
                sendToUser(attacker, new GameMessage(ATTACK_SUCCESS, defenderHP));
                sendToUser(defender, new GameMessage(ATTACKED, defenderHP));

                if (defenderHP <= 0) {
                    // 게임 종료
                    sendToUser(attacker, new GameMessage(GAME_OVER, "You Win!"));
                    sendToUser(defender, new GameMessage(GAME_OVER, "You Lose!"));
                } else {
                    // 턴 전환
                    gameState.switchTurn();
                    sendToUser(gameState.getCurrentPlayer().getName(), new GameMessage(YOUR_TURN, gameState));
                }
            } else {
                // 공격 실패
                log.info("Attack failed");
                String defender = gameState.getOpponent().getName();
                gameState.switchTurn();
//                sendToUser(gameState.getCurrentPlayer().getName(), new GameMessage(YOUR_TURN, gameState));
                return new GameMessage(ATTACK_FAIL, gameState.getPlayerHP().get(defender));


            }
        } catch (Exception e) {
            sendToUser(attacker, new GameMessage(ERROR, INVALID_EXPRESSION.getMessage()));
            gameState.switchTurn();
            sendToUser(gameState.getCurrentPlayer().getName(), new GameMessage(YOUR_TURN, gameState));
        }
        return null;
    }

    private void sendToUser(String username, GameMessage message) {
        messagingTemplate.convertAndSendToUser(username, "/queue/private", message);
    }

    // 수식 평가 메서드
    private double evaluateExpression(String expression) throws Exception {
        return parser.evaluate(expression);
    }

    private String getUsername(StompHeaderAccessor accessor) {
        return (String) accessor.getSessionAttributes().get("username");
    }
}
