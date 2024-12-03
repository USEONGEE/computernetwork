package com.example.compusernetwork.domain.game.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GameMessage {
    private GameMessageType type; // 메시지 타입 (MATCHED, MOVE, ATTACK 등)
    private Object content; // 메시지 내용
    private String roomId; // 게임 방 ID


    public GameMessage(GameMessageType type, Object content) {
        this.type = type;
        this.content = content;
    }
}
