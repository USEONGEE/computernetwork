package com.example.compusernetwork.domain.game;

import com.example.compusernetwork.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GameRoom {
    private String roomId;
    private Member player1;
    private Member player2;
    private GameState gameState;

    public GameRoom(Member player1, Member player2) {
        this.roomId = UUID.randomUUID().toString();
        this.player1 = player1;
        this.player2 = player2;
        this.gameState = new GameState(player1, player2);
    }

}
