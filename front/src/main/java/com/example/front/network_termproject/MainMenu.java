package com.example.front.network_termproject;

import com.example.front.network_termproject.client.CallbackMessage;
import com.example.front.network_termproject.client.GameClientV2;
import com.example.front.network_termproject.client.MessageListener;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel implements MessageListener {
    private LogIn parentFrame; // LogIn 프레임 참조

    private final GameClientV2 gameClient = GameClientV2.getInstance();
    public MainMenu(LogIn parentFrame) {
        // STEP 3 메시지 리스너 등록
        gameClient.addMessageListener(this);
        // STEP 4 구독 요청
        gameClient.subscribeToGameTopics();
        // 3초 대기
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.parentFrame = parentFrame;

        setLayout(null);
        setBackground(new Color(0, 0, 0, 0)); // 투명 배경 (기존 배경 유지)

        // ** 공통 버튼 스타일 및 효과음 적용 메서드 **
        MouseAdapter hoverEffect = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                button.setFont(new Font("Arial", Font.BOLD, 36)); // 호버 시 글자 크기 증가
                button.setForeground(new Color(255, 215, 0)); // 텍스트 색상 변경 (금색 느낌)
                //playSound(getClass().getResource("/Sounds/bbyong.wav").toString()); // Hover sound 재생
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                button.setFont(new Font("Arial", Font.BOLD, 30)); // 기본 글자 크기
                button.setForeground(Color.WHITE); // 기본 텍스트 색상
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //playSound(getClass().getResource("/Sounds/click.wav").toString()); // Click sound 재생
            }
        };

        // ** "게임 시작" 버튼 **
        JButton startButton = createTransparentButton("Start Game");
        startButton.setBounds(1200, 250, 300, 50); // 우측 배치
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(MainMenu.this);
                new Waiting_Room(parentFrame, "YourUsername", getClass().getResource("/Assets/Background.jpg").toString(), "127.0.0.1", 12345);
//                parentFrame.dispose(); // 현재 창 닫기
            }
        });
        startButton.addMouseListener(hoverEffect);
        add(startButton);

        // ** "옵션" 버튼 **
        JButton optionsButton = createTransparentButton("Options");
        optionsButton.setBounds(1200, 350, 300, 50); // 우측 배치
        optionsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JOptionPane.showMessageDialog(parentFrame, "Options selected!", "Info", JOptionPane.INFORMATION_MESSAGE);
                // 옵션 로직 추가
            }
        });
        optionsButton.addMouseListener(hoverEffect);
        add(optionsButton);

        // ** "종료" 버튼 **
        JButton exitButton = createTransparentButton("Exit");
        exitButton.setBounds(1200, 450, 300, 50); // 우측 배치
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0); // 프로그램 종료
            }
        });
        exitButton.addMouseListener(hoverEffect);
        add(exitButton);
    }

    /**
     * 투명한 버튼을 생성하는 헬퍼 메서드
     *
     * @param text 버튼 텍스트
     * @return 투명 버튼
     */
    private JButton createTransparentButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 30)); // 기본 글꼴
        button.setForeground(Color.WHITE); // 기본 텍스트 색상
        button.setOpaque(false); // 투명 배경
        button.setContentAreaFilled(false); // 배경 채우기 제거
        button.setBorderPainted(false); // 테두리 제거
        button.setFocusPainted(false); // 포커스 테두리 제거
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 커서 변경
        return button;
    }

    @Override
    public void onMessageReceived(CallbackMessage message) {
        System.out.println("메시지 수신: " + message);
    }

//    /**
//     * 사운드 파일 재생 메서드
//     *
//     * @param soundPath 재생할 사운드 파일 경로
//     */
//    private void playSound(String soundPath) {
//        new Thread(() -> {
//            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(soundPath))) {
//                Clip clip = AudioSystem.getClip();
//                clip.open(audioStream);
//                clip.start();
//            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
}
