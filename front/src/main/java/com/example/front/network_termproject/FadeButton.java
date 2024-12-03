package com.example.front.network_termproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * FadeButton은 마우스 호버 및 클릭 시 색상이 변하는 커스텀 버튼 클래스입니다.
 */
public class FadeButton extends JButton {
    private final Color unClickBackground;
    private final Color clickBackground;
    private final Color hoverBackground;
    private final Color foreground;

    private final int shadowOffset = 4; // 그림자 오프셋
    private final Color shadowColor = new Color(0, 0, 0, 80); // 그림자 색상

    int paddingWidth = 15, paddingHeight = 3;

    public FadeButton(String text, Color unClickBackground, Color clickBackground, Color hoverBackground, Color foreground) {
        super(text);
        this.unClickBackground = unClickBackground;
        this.clickBackground = clickBackground;
        this.hoverBackground = hoverBackground;
        this.foreground = foreground;

        setText(text);

        Dimension dimension = getPreferredSize();
        int w = (int) dimension.getWidth() + paddingWidth * 3;
        int h = (int) dimension.getHeight() + paddingHeight * 3;

        setPreferredSize(new Dimension(w, h));
        setOpaque(false);
        setBorder(null);
        setBackground(unClickBackground);
        setForeground(foreground);
        setFocusPainted(false);

        // 기본 폰트 설정 (글자 크기 크게)
        setFont(new Font("Arial", Font.BOLD, 15)); // 15포인트 크기의 Arial Bold 폰트

        // Mouse Listener for Click and Hover Effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(clickBackground);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(unClickBackground);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(unClickBackground);
            }
        });
    }

    /**
     * 버튼의 선택 상태를 설정합니다.
     *
     * @param selected 선택 여부
     */
    public void setSelectedState(boolean selected) {
        setBackground(selected ? hoverBackground : unClickBackground);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 그림자 그리기
        g2d.setColor(shadowColor);
        g2d.fillRoundRect(shadowOffset, shadowOffset, getWidth() - shadowOffset, getHeight() - shadowOffset, 30, 30);

        // 버튼 배경 그리기
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth() - shadowOffset, getHeight() - shadowOffset, 30, 30);

        // 버튼 텍스트 그리기
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getAscent();
        g2d.setColor(getForeground());
        g2d.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 3);

        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension original = super.getPreferredSize();
        int width = (int) original.getWidth() + paddingWidth * 2;
        int height = (int) original.getHeight() + paddingHeight * 2;
        return new Dimension(width, height);
    }
}
