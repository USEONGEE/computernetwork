package com.example.front.network_termproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * GameButton은 3D 스타일로 렌더링된 커스텀 버튼입니다.
 */
public class GameButton extends JButton {
    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean hovered = false;
    private boolean pressed = false;

    /**
     * GameButton 생성자.
     *
     * @param text         버튼 텍스트
     * @param normalColor  기본 배경 색상
     * @param hoverColor   호버 시 배경 색상
     * @param pressedColor 클릭 시 배경 색상
     */
    public GameButton(String text, Color normalColor, Color hoverColor, Color pressedColor) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.pressedColor = pressedColor;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFont(new Font("Arial", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                pressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 안티앨리어싱 활성화
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 배경 색상 결정
        Color currentColor = normalColor;
        if (pressed) {
            currentColor = pressedColor;
        } else if (hovered) {
            currentColor = hoverColor;
        }

        // 3D 효과를 위한 그라데이션
        GradientPaint gradient = new GradientPaint(0, 0, currentColor.brighter(), 0, getHeight(), currentColor.darker());
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // 그림자 효과
        if (!pressed) {
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 20, 20);
        }

        // 텍스트 그리기
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(getText())) / 2;
        int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(getForeground());
        g2.drawString(getText(), textX, textY);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(normalColor.darker());
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        g2.dispose();
    }
}
