package com.example.front.network_termproject;

import java.awt.*;

class LogInButton extends FadeButton {
    public LogInButton(String text) {
        super(text,
                new Color(39, 174, 96), // Default color
                new Color(22, 160, 133), // Click color
                new Color(46, 204, 113), // Hover color
                Color.WHITE); // Text color
    }

    // 추가적으로 버튼의 동작이나 스타일을 변경하려면 여기서 구현 가능
}
