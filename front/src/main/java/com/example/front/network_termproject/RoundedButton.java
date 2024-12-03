package com.example.front.network_termproject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RoundedButton extends JButton {
    private Color backgroundColor;
    private Color textColor;
    private int cornerRadius;

    public RoundedButton(String text) {
        super(text);
        this.backgroundColor = new Color(30, 60, 90); // 기본 배경색
        this.textColor = Color.WHITE; // 기본 텍스트 색상
        this.cornerRadius = 20; // 기본 둥근 모서리 반경
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }

    public void setTextColor(Color color) {
        this.textColor = color;
        repaint();
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 배경 그리기
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // 텍스트 그리기
        g2d.setColor(textColor);
        FontMetrics fm = g2d.getFontMetrics();
        int stringWidth = fm.stringWidth(getText());
        int stringHeight = fm.getAscent();
        int x = (getWidth() - stringWidth) / 2;
        int y = (getHeight() + stringHeight) / 2 - fm.getDescent();
        g2d.drawString(getText(), x, y);

        g2d.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // 테두리 없음
    }

    // 예제 테스트용 메인 메서드
    public static void main(String[] args) {
        JFrame frame = new JFrame("Rounded Button Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(null);

        RoundedButton button = new RoundedButton("Click Me");
        button.setBounds(100, 100, 200, 50);
        button.setBackgroundColor(new Color(70, 130, 180)); // 파란 배경색
        button.setTextColor(Color.WHITE); // 흰색 텍스트

        button.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Button Clicked!"));

        frame.add(button);
        frame.setVisible(true);
    }
}
