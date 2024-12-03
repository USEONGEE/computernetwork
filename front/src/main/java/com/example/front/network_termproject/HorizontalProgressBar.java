package com.example.front.network_termproject;

import javax.swing.*;
import java.awt.*;

public class HorizontalProgressBar extends JComponent {
    private int progress = 0; // 현재 진행 값 (0 ~ 100)

    public void setProgress(int progress) {
        this.progress = progress;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 안티앨리어싱 활성화
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 컴포넌트 크기
        int width = getWidth();
        int height = getHeight();

        // 배경 바
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRoundRect(0, height / 4, width, height / 2, 20, 20);

        // 진행 표시 색상 계산 (녹색 → 빨간색)
        Color startColor = new Color(0, 153, 76); // 녹색
        Color endColor = new Color(255, 0, 0);    // 빨간색
        float ratio = 1 - (progress / 100f);      // 진행률 반전 (0%일 때 빨간색, 100%일 때 녹색)
        int red = (int) (startColor.getRed() * (1 - ratio) + endColor.getRed() * ratio);
        int green = (int) (startColor.getGreen() * (1 - ratio) + endColor.getGreen() * ratio);
        int blue = (int) (startColor.getBlue() * (1 - ratio) + endColor.getBlue() * ratio);
        Color dynamicColor = new Color(red, green, blue);

        // 진행 표시
        int progressWidth = (int) (width * (progress / 100.0));
        g2d.setColor(dynamicColor);
        g2d.fillRoundRect(0, height / 4, progressWidth, height / 2, 20, 20);

        // 남은 시간 텍스트
        String text = (progress * 30 / 100) + "s"; // 30초 기준 남은 시간 표시
        g2d.setFont(new Font("Arial", Font.BOLD, height / 2));
        g2d.setColor(Color.BLACK);
        FontMetrics metrics = g2d.getFontMetrics();
        int textX = (width - metrics.stringWidth(text)) / 2;
        int textY = (height + metrics.getAscent() - metrics.getDescent()) / 2;
        g2d.drawString(text, textX, textY);
    }
}
