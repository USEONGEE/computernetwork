package com.example.front.network_termproject;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

@Component
public class SplashScreen extends JFrame {
    private float opacity = 0.0f; // 초기 투명도
    private boolean fadingIn = true; // Fade In 상태 플래그
    private JLabel imageLabel;

    public SplashScreen() {
        setTitle("Splash Screen");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // 전체화면 모드
        setUndecorated(true); // 타이틀 바 제거
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 리사이즈된 이미지 추가
        URL resource = getClass().getResource("/Assets/Image.png");
        System.out.println("resource = " + resource);
        ImageIcon originalIcon = new ImageIcon(resource);
        Image resizedImage = originalIcon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH); // 400x300 크기로 리사이즈
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        imageLabel = new JLabel(resizedIcon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity)); // 투명도 설정
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(imageLabel);

        // 화면 표시
        setVisible(true);

        // Fade 효과 시작
        startFadeEffect();
    }

    private void startFadeEffect() {
        Timer fadeTimer = new Timer(50, null); // 50ms 간격으로 실행
        fadeTimer.addActionListener(e -> {
            if (fadingIn) {
                opacity += 0.05f; // Fade In
                if (opacity >= 1.0f) { // Fade In 완료
                    opacity = 1.0f;
                    fadingIn = false;

                    // 2초 정지 후 Fade Out 시작
                    Timer pauseTimer = new Timer(1000, pauseEvent -> {
                        fadingIn = false; // Fade Out으로 전환
                        fadeTimer.start(); // Fade Timer 재시작
                    });
                    pauseTimer.setRepeats(false);
                    pauseTimer.start();

                    fadeTimer.stop(); // 일시정지
                }
            } else {
                opacity -= 0.03f; // Fade Out
                if (opacity <= 0.0f) { // Fade Out 완료
                    opacity = 0.0f;
                    fadeTimer.stop(); // Fade Timer 정지
                    dispose(); // SplashScreen 닫기
                    SwingUtilities.invokeLater(() -> new LogIn().setVisible(true)); // LogIn 화면 열기
                }
            }
            imageLabel.repaint(); // 이미지 다시 그리기
        });
        fadeTimer.start();
    }

}
