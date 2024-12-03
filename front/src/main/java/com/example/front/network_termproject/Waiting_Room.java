package com.example.front.network_termproject;

import com.example.front.network_termproject.client.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.sound.sampled.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Waiting_Room extends JFrame implements MessageListener {
    private JLabel leftImageLabel;   // 왼쪽 이미지 (사용자 프로필)
    private JLabel rightImageLabel; // 오른쪽 이미지 (상대방 프로필)
    private JLabel leftNameLabel;   // 왼쪽 이름
    private JLabel rightNameLabel;  // 오른쪽 이름
    private JLabel statusLabel;     // 상태 메시지 ("Waiting...")

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private String waitingMessage = "Waiting"; // 기본 메시지
    private int dotCount = 0;                 // 점의 개수
	private JFrame parentFrame;
	private String username;
	private String userImagePath;
	private String serverIP;
	private int serverPort;

    private final GameClientV2 gameClient = GameClientV2.getInstance();
	
	public Waiting_Room(JFrame parentFrame, String username, String userImagePath, String serverIP, int serverPort) {
        // STEP 5 메시지 리스너 등록
        gameClient.addMessageListener(this);
        // 3초 대기
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // STEP 6 매칭 요청
        gameClient.sendMatchRequest();

	    this.parentFrame = parentFrame; // JFrame 타입으로 수정
	    this.username = username;
	    this.userImagePath = userImagePath;
	    this.serverIP = serverIP;
	    this.serverPort = serverPort;
        
    	setTitle("Waiting Room");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(null);

        // 창을 화면 중앙에 위치
        setLocationRelativeTo(null);

        // ** 배경 패널 설정 (연한 갈색 배경) **
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(210, 180, 140)); // 연한 갈색
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // ** 중앙 상단 제목 ("1 vs 1") **
        JLabel titleLabel = new JLabel("1 vs 1", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(350, 20, 100, 30);
        backgroundPanel.add(titleLabel);

        // ** 왼쪽 프로필 (사용자) **
        leftImageLabel = new JLabel(new ImageIcon(userImagePath));
        leftImageLabel.setBounds(150, 100, 150, 150);
        leftImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        leftNameLabel = new JLabel(username, SwingConstants.CENTER);
        leftNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftNameLabel.setBounds(150, 260, 150, 30);

        backgroundPanel.add(leftImageLabel);
        backgroundPanel.add(leftNameLabel);

        // ** 오른쪽 프로필 (초기 상태: 빈 이미지) **
        rightImageLabel = new JLabel();
        rightImageLabel.setBounds(500, 100, 150, 150);
        rightImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        rightNameLabel = new JLabel("Waiting...", SwingConstants.CENTER);
        rightNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rightNameLabel.setBounds(500, 260, 150, 30);

        backgroundPanel.add(rightImageLabel);
        backgroundPanel.add(rightNameLabel);

        // ** 하단 상태 메시지 ("Waiting...") **
        statusLabel = new JLabel("Waiting...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        statusLabel.setBounds(300, 400, 200, 30);
        backgroundPanel.add(statusLabel);

        // 애니메이션 시작
        startWaitingAnimation();

        // ** Close 버튼 추가 (FadeButton 사용) **
        FadeButton closeButton = new FadeButton("Close",
                new Color(244, 67, 54), // 기본 색상
                new Color(211, 47, 47), // 클릭 색상
                new Color(239, 154, 154), // 호버 색상
                Color.WHITE // 텍스트 색상
        );
        closeButton.setBounds(350, 500, 100, 40);
        closeButton.addActionListener(e -> dispose());
        backgroundPanel.add(closeButton);
        
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //playSound(getClass().getResource("/Sounds/bbyong.wav").toString()); // 마우스 호버 시 사운드 재생
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //playSound(getClass().getResource("/Sounds/click.wav").toString()); // 클릭 시 사운드 재생
                dispose(); // 현재 창 닫기
                if (parentFrame != null) {
                    parentFrame.setVisible(true); // 호출한 프레임 표시
                }
            }
        });
        
        setVisible(true);

//        connectToServer(serverIP, serverPort);
    }

    private void startWaitingAnimation() {
        Timer waitingTimer = new Timer(500, e -> {
            dotCount = (dotCount + 1) % 5; // 점 개수 증가 (0~4로 순환)
            StringBuilder dots = new StringBuilder();
            for (int i = 0; i < dotCount; i++) {
                dots.append(".");
            }
            statusLabel.setText(waitingMessage + dots); // 메시지 업데이트
        });
        waitingTimer.start();
    }
    
    private void connectToServer(String serverIP, int serverPort) {
        try {
            socket = new Socket(serverIP, serverPort);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // 서버로 사용자 이름 전송
            writer.println("USERNAME:" + leftNameLabel.getText());

            // 서버로부터 상대방 정보 수신
            new Thread(() -> {
                try {
                    while (true) {
                        String serverMessage = reader.readLine();
                        if (serverMessage != null) {
                            if (serverMessage.startsWith("OPPONENT:")) {
                                String[] data = serverMessage.substring(9).split(":");
                                String opponentName = data[0];

                                // 상대방 프로필 업데이트
                                updateOpponentProfile(opponentName);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to the server.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateOpponentProfile(String opponentName) {
        SwingUtilities.invokeLater(() -> {
            rightNameLabel.setText(opponentName);
            statusLabel.setText("Opponent Found!"); // 상대방이 발견되었을 때 상태 업데이트
        });
    }

    @Override
    public void onMessageReceived(CallbackMessage message) {
        if(message.type().equals("MATCHED")) {
            GameRoom gameState = (GameRoom) message.message();

            SwingUtilities.invokeLater(() -> {
                new GameUI(gameState); // GameScreen은 새 화면
            });
        }
    }
}
