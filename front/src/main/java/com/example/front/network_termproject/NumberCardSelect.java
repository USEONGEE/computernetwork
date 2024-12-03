package com.example.front.network_termproject;
import java.awt.*;
import javax.swing.*;

public class NumberCardSelect {

    public JPanel getNumberCardSelect() {
        JPanel numberCardSelectPanel = new JPanel();
        numberCardSelectPanel.setLayout(null); // 절대 배치
        numberCardSelectPanel.setBackground(Color.DARK_GRAY);
        numberCardSelectPanel.setPreferredSize(new Dimension(480, 360));

        // 버튼 생성 및 위치 설정
        int buttonWidth = 120;
        int buttonHeight = 240;
        int gap = 30; // 버튼 간격
        int startX = (480 - (3 * buttonWidth + 2 * gap)) / 2; // 중앙 정렬 계산

        for (int i = 0; i < 3; i++) {
            JButton cardButton = new JButton("Card " + (i + 1));
            cardButton.setBackground(Color.YELLOW);
            cardButton.setFont(new Font("Arial", Font.BOLD, 20));
            cardButton.setBounds(startX + i * (buttonWidth + gap), 60, buttonWidth, buttonHeight);
            cardButton.addActionListener(e -> {
                // 버튼 클릭 시 동작 (NumberCardSelect 창 닫기)
                SwingUtilities.getWindowAncestor(numberCardSelectPanel).dispose();
            });
            numberCardSelectPanel.add(cardButton);
        }

        return numberCardSelectPanel;
    }
}
