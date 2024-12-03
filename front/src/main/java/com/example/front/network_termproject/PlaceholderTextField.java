package com.example.front.network_termproject;

import javax.swing.*;
import java.awt.*;

public class PlaceholderTextField extends JTextField {
    private String placeholder;
    private Color placeholderColor = Color.GRAY; // 기본 Placeholder 색상

    public PlaceholderTextField() {
        super();
        setFont(new Font("Arial", Font.BOLD, 16));
        setForeground(Color.BLACK); // 기본 텍스트 색상
        setOpaque(false); // 완전히 투명하게 설정
        setBackground(new Color(0, 0, 0, 0)); // 배경 투명
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Padding 설정
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint(); // Placeholder 업데이트 시 다시 그리기
    }

    public Color getPlaceholderColor() {
        return placeholderColor;
    }

    public void setPlaceholderColor(Color placeholderColor) {
        this.placeholderColor = placeholderColor;
        repaint(); // Placeholder 색상 변경 시 다시 그리기
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 텍스트 필드가 비어 있으면 Placeholder 표시
        if (getText().isEmpty() && placeholder != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setFont(getFont());
            g2.setColor(placeholderColor);

            // Placeholder 텍스트 위치 설정
            Insets insets = getInsets();
            int x = insets.left + 5; // 왼쪽 여백
            int y = getHeight() / 2 + g.getFontMetrics().getAscent() / 2 - 4; // 중앙 정렬
            g2.drawString(placeholder, x, y);

            g2.dispose();
        }
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        repaint(); // 텍스트 변경 시 다시 그리기
    }
}
