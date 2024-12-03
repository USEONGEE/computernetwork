package com.example.compusernetwork.domain.game;

import com.example.compusernetwork.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
public class GameState {
    private Member currentPlayer;
    private Member opponent;
    private Map<String, Integer> playerHP;
    private int targetNumber;
    private List<Integer> numberCards;
    private Map<String, String> expressions; // 플레이어별 수식

    public GameState(Member player1, Member player2) {
        this.currentPlayer = player1;
        this.opponent = player2;
        this.playerHP = new HashMap<>();
        playerHP.put(player1.getName(), 100);
        playerHP.put(player2.getName(), 100);
        this.expressions = new HashMap<>();
        generateNewChallenge();
    }

    public void generateNewChallenge() {
        Random random = new Random();
        targetNumber = random.nextInt(100) + 1;
        numberCards = random.ints(5, 1, 10).boxed().toList();
        expressions.put(currentPlayer.getName(), "");
    }

    public void switchTurn() {
        Member temp = currentPlayer;
        currentPlayer = opponent;
        opponent = temp;
        generateNewChallenge();
    }

}
