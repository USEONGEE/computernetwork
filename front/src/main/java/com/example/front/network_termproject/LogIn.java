package com.example.front.network_termproject;

import com.example.front.network_termproject.client.GameClientV2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LogIn extends JFrame {

    private GameClientV2 gameClient = GameClientV2.getInstance();


    private BackgroundPanel backgroundPanel;
    private List<String> backgroundImages; // Background image paths
    private int currentImageIndex = 0; // Current background index
    private float alpha = 1.0f; // Opacity level for fading
    private boolean fadingOut = true; // Whether it is fading out
    private final GameClientV2 gameClientV2 = GameClientV2.getInstance();

    public LogIn() {
        setTitle("Fantasy Math Game - Log In");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen mode
        setUndecorated(true); // Remove title bar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Background images list
        backgroundImages = new ArrayList<>();
//        backgroundImages.add(getClass().getResource("/Assets/LogInUI_001.gif").toString());
//        backgroundImages.add(getClass().getResource("/Assets/LogInUI_002.gif").toString());
//        backgroundImages.add(getClass().getResource("/Assets/LogInUI_003.gif").toString());
//        backgroundImages.add(getClass().getResource("/Assets/LogInUI_004.gif").toString());
//        backgroundImages.add(getClass().getResource("/Assets/LogInUI_005.jpg").toString());
//        backgroundImages.add(getClass().getResource("/Assets/LogInUI_006.png").toString());
        backgroundImages.add(getClass().getResource("/Assets/LogInUI_006.png").toString());
        backgroundImages.add(getClass().getResource("/Assets/LogInUI_006.png").toString());
        backgroundImages.add(getClass().getResource("/Assets/LogInUI_006.png").toString());
        backgroundImages.add(getClass().getResource("/Assets/LogInUI_006.png").toString());
        backgroundImages.add(getClass().getResource("/Assets/LogInUI_006.png").toString());
        backgroundImages.add(getClass().getResource("/Assets/LogInUI_006.png").toString());


        // Initialize background panel
        backgroundPanel = new BackgroundPanel(backgroundImages.get(currentImageIndex));
        backgroundPanel.setLayout(null); // Absolute positioning
        add(backgroundPanel);
        
        // ** 팀원 이름 추가 **
        // 팀원 이름을 여러 줄로 표시하기 위해 HTML 사용
        String teamMembers = "<html>6조:<br>201835436 김주호<br>201932117 노유성<br>202135596 현관<br>202234887 노현웅<br>202334273 김예서</html>"; // 실제 팀원 이름으로 변경하세요
        JLabel teamLabel = new JLabel(teamMembers);
        teamLabel.setFont(new Font("Arial", Font.BOLD, 20)); // 폰트와 크기 설정
        teamLabel.setForeground(Color.YELLOW); // 텍스트 색상 설정

        // 팀원 텍스트 크기 측정
        Dimension teamSize = teamLabel.getPreferredSize();

        // 화면 크기 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // 팀원 텍스트의 x 위치 (화면 중앙)
        int teamX = 20;

        // 팀원 텍스트의 y 위치 (상단에서 적절히 배치, 예: 30 픽셀)
        int teamY = 120; // 상단에서 30 픽셀 떨어진 위치로 설정

        // 원하는 너비 설정 (예: 화면 너비의 50%)
        int desiredWidth = (int)(screenSize.width * 0.5);
        int desiredHeight = 150; // 예시로 충분한 높이 설정

        // JLabel의 위치와 크기 설정
        teamLabel.setBounds(teamX, teamY, desiredWidth, desiredHeight);

        // 배경 패널에 팀원 레이블 추가
        backgroundPanel.add(teamLabel);

        // ** 가천대 이미지 추가 (크기 조정 포함) **
        // 이미지 로드
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Assets/Image2.png"));

        // 원하는 크기로 조정 (예: 너비 200px, 높이 100px)
        int desiredWidth1 = 100;
        int desiredHeight1 = 100;

        // 이미지 크기 조정
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(desiredWidth1, desiredHeight1, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // JLabel 생성 및 아이콘 설정
        JLabel logoLabel = new JLabel(scaledIcon);

        // 화면 크기 가져오기
        Dimension screenSize1 = Toolkit.getDefaultToolkit().getScreenSize();

        // 이미지의 x 위치 (화면 중앙)
        int x = 15;

        // y 위치 (상단에서 약간 떨어진 위치, 예: 50 픽셀)
        int y = 20;

        // JLabel의 위치와 크기 설정
        logoLabel.setBounds(x, y, desiredWidth1, desiredHeight1);

        // 배경 패널에 이미지 레이블 추가
        backgroundPanel.add(logoLabel);
        
        // ** 화면 중간에 Logo 추가 **
        // 이미지 로드
        ImageIcon logoIcon2 = new ImageIcon(getClass().getResource("/Assets/Logo.png"));

        // 원하는 크기로 이미지 크기 조정
        int logoWidth2 = 500;
        int logoHeight2 = 150;
        Image scaledLogoImage2 = logoIcon2.getImage().getScaledInstance(logoWidth2, logoHeight2, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon2 = new ImageIcon(scaledLogoImage2);

        // JLabel 생성 및 아이콘 설정
        JLabel logoLabel2 = new JLabel(scaledLogoIcon2);

        // 화면 크기 가져오기
        Dimension screenSize2 = Toolkit.getDefaultToolkit().getScreenSize();

        // 로고를 화면 중앙에 배치
        int logoX = (screenSize2.width - logoWidth2) / 2;
        int logoY = 110;

        // JLabel의 위치와 크기 설정
        logoLabel2.setBounds(logoX, logoY, logoWidth2, logoHeight2);

        // 배경 패널에 로고 추가
        backgroundPanel.add(logoLabel2);
        

        // ** 세련된 Main Panel **
        JPanel mainPanel = new RoundedPanel(30, new Color(139, 69, 19), 0.5f); // 나무 색상, 50% 불투명
        mainPanel.setBounds(1100, 290, 350, 150); // Positioned to the right
        mainPanel.setLayout(null);

        // ID Field with Placeholder
        PlaceholderTextField idField = new PlaceholderTextField();
        idField.setBounds(20, 30, 200, 40); // 위치 및 크기 설정
        idField.setFont(new Font("Arial", Font.ROMAN_BASELINE, 18)); // 세련된 폰트
        idField.setPlaceholder("Enter your ID"); // Placeholder 설정
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2), // 흰색 테두리
            BorderFactory.createEmptyBorder(0, 0, 0, 0) // 내부 여백
        ));
        idField.setBackground(new Color(0, 0, 0, 0)); // 투명 배경
        mainPanel.add(idField); // 메인 패널에 추가


        // pwField 선언부
        PlaceholderPasswordField pwField = new PlaceholderPasswordField();
        pwField.setBounds(20, 70, 200, 40); // 위치 및 크기 설정
        pwField.setFont(new Font("Arial", Font.ROMAN_BASELINE, 16)); // 세련된 글꼴 설정
        pwField.setPlaceholder("Enter your password"); // Placeholder 설정
        pwField.setForeground(Color.BLACK); // 텍스트 색상
        pwField.setBackground((new Color(0, 0, 0, 0))); // 필드 배경색
        pwField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2), 
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
            ));
        mainPanel.add(pwField); // 패널에 추가



        // LogInButton 사용
        LogInButton loginButton = new LogInButton("Log In");
        loginButton.setBounds(240, 25, 100, 90);

        mainPanel.add(idField);
        mainPanel.add(pwField);
        mainPanel.add(loginButton);
        backgroundPanel.add(mainPanel);

        // Bottom Buttons
        LogInButton registerButton = new LogInButton("Register");
        registerButton.setBounds(1100, 450, 100, 50);

        LogInButton findButton = new LogInButton("ID/PW");
        findButton.setBounds(1220, 450, 100, 50);

        LogInButton exitButton = new LogInButton("Exit");
        exitButton.setBounds(1340, 450, 100, 50);

        backgroundPanel.add(registerButton);
        backgroundPanel.add(findButton);
        backgroundPanel.add(exitButton);

        // Add sound effects to buttons
        //addSoundToButton(loginButton, getClass().getResource("/Sounds/click.wav").toString(), "Sounds\\bbyong.wav");
        //addSoundToButton(registerButton, getClass().getResource("/Sounds/click.wav").toString(), "Sounds\\bbyong.wav");
        //addSoundToButton(findButton, getClass().getResource("/Sounds/click.wav").toString(), "Sounds\\bbyong.wav");
        //addSoundToButton(exitButton, getClass().getResource("/Sounds/click.wav").toString(), "Sounds\\bbyong.wav");

        loginButton.addActionListener(e -> {
            String id = idField.getText().trim(); // Remove leading/trailing spaces
            String password = new String(pwField.getPassword()).trim(); // Remove leading/trailing spaces

            // id/pw validation
            if (id.equals(((PlaceholderTextField) idField).getPlaceholder()) || password.equals(pwField.getPlaceholder())) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please fill out both fields.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

            // TODO 로그인
            try {
                // STEP 1 로그인
                gameClient.login(id, password);
                gameClientV2.setUsername(id);
                // STEP 2 웹소켓 연결
                try {
                    gameClient.connectToWebSocket();

                } catch (Exception ex) {
                    System.out.println("웹소켓 연결 실패");
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, "Login Successful! Welcome " + id);

                // 3초 대기
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }


                transitionToMainMenu();

            } catch (Exception ex) {
                System.out.println("로그인 실패");
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid ID or Password",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        });
       
        registerButton.addActionListener(e -> {
            new Register(); // 회원가입 창 열기
        });

        
        exitButton.addActionListener(e -> System.exit(0));

        // Start fade transition
        startBackgroundTransition();

        //C:\\Users\\user\\Desktop\\MainTheme.wav
        // Play background music
        //playBGM(getClass().getResource("/Sounds/MainTheme.wav").toString());
    }

    public void transitionToMainMenu() {
        // 기존 컴포넌트 제거
        backgroundPanel.removeAll(); // 배경 패널의 모든 컴포넌트 제거
        backgroundPanel.revalidate(); // 레이아웃 갱신
        backgroundPanel.repaint(); // 화면 갱신

        // MainMenu 생성
        MainMenu mainMenu = new MainMenu(this); // MainMenu 생성
        mainMenu.setOpaque(false); // 배경 투명 설정
        mainMenu.setBounds(0, 0, backgroundPanel.getWidth(), backgroundPanel.getHeight()); // 크기 설정
        backgroundPanel.add(mainMenu); // MainMenu를 배경 패널에 추가
        backgroundPanel.revalidate(); // 레이아웃 갱신
        backgroundPanel.repaint(); // 화면 갱신
    }



	private void startBackgroundTransition() {
        Timer fadeTimer = new Timer(50, null); // Create a Timer without initial ActionListener
        fadeTimer.addActionListener(e -> {
            if (fadingOut) {
                alpha -= 0.05f; // Reduce opacity
                if (alpha <= 0.05f) { // Just before completely transparent
                    alpha = 0; // Ensure alpha doesn't go below 0
                    currentImageIndex = (currentImageIndex + 1) % backgroundImages.size(); // Move to the next image (loop back to 0 if at the end)
                    backgroundPanel.setImage(backgroundImages.get(currentImageIndex)); // Change the image
                    fadingOut = false; // Switch to fading in
                }
            } else {
                alpha += 0.05f; // Increase opacity
                if (alpha >= 1) {
                    alpha = 1; // Ensure alpha doesn't go above 1
                    fadingOut = true; // Switch to fading out

                    // Pause the Timer for a delay before the next transition
                    fadeTimer.stop();
                    Timer pauseTimer = new Timer(5000, pauseEvent -> fadeTimer.start()); // 5-second pause
                    pauseTimer.setRepeats(false); // Ensure it runs only once
                    pauseTimer.start(); // Start the pause timer
                }
            }
            backgroundPanel.setAlpha(alpha); // Update panel opacity
            backgroundPanel.repaint(); // Repaint the panel
        });
        fadeTimer.setInitialDelay(10000); // Initial delay of 3 seconds
        fadeTimer.start();
    }

//    private void playBGM(String bgmPath) {
//        System.out.println("bgmPath = " + bgmPath);
//        new Thread(() -> {
//            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(bgmPath))) {
//                Clip clip = AudioSystem.getClip();
//                clip.open(audioStream);
//                clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop BGM
//            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }

//    private void addSoundToButton(JButton button, String clickSoundPath, String hoverSoundPath) {
//        button.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                //playSound(hoverSoundPath); // Play hover sound
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                //playSound(clickSoundPath); // Play click sound
//            }
//        });
//    }
//
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

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            LogIn loginUI = new LogIn();
//            loginUI.setVisible(true);
//        });
//    }
}
