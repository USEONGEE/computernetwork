package com.example.front.network_termproject.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
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
    }
}
