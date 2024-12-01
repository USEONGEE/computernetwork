package com.example.compusernetwork.domain.game.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorMessage {
    NOT_YOUR_TURN("당신의 차례가 아닙니다."),
    ROOM_NOT_EXIST("존재하지 않는 room 입니다."),
    INVALID_EXPRESSION("잘못된 수식입니다."),
    USER_NOT_EXIST("존재하지 않는 사용자입니다.");

    private final String message;
}
