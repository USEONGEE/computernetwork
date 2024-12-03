package com.example.front.network_termproject;

import com.example.front.network_termproject.client.CallbackMessage;
import com.example.front.network_termproject.client.GameClientV2;
import com.example.front.network_termproject.client.GameRoom;
import com.example.front.network_termproject.client.MessageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * GameUI는 1 대 1 수학 배틀 게임의 메인 사용자 인터페이스를 나타냅니다.
 */
public class GameUI extends JFrame implements MessageListener {
    // 클래스 필드
	private HorizontalProgressBar circularTimer;
	private CustomPopup CustomPopup;
	private int timeRemaining = 90;
	private Timer countdownTimer;
	
    private JLabel randomNumberLabel;
    private JLabel player1ImageLabel, player1NameLabel;
    private JLabel player2ImageLabel, player2NameLabel;
    private JProgressBar player1LifeBar, player2LifeBar;

    private JTextField player1FormulaField, player2FormulaField;
    private GameButton[] cardButtons;
    private GameButton[] operatorButtons;
    private GameButton attackButton, eraseButton, eraseAllButton;

    private JLabel roundLabel; // 라운드 표시 라벨

    private int player1Life = 100; // 플레이어 1의 체력
    private int player2Life = 100; // 플레이어 2의 체력
    private Random random = new Random();

    private GameButton selectedCardButton = null; // 선택된 카드 버튼 추적
    private int currentRound = 1; // 현재 라운드 번호
    private boolean isPlayer1Turn = true; // 턴 추적 플래그  // 공격을 하면 턴이 바뀌고, 공격을 당하거나 방어를 하면 턴이 바뀜

    private GameRoom gameRoom;
    private final GameClientV2 gameClient = GameClientV2.getInstance();
    /**
     * GameUI를 생성하고 모든 컴포넌트를 초기화합니다.
     */
    public GameUI(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
        isFirst();
        setTitle("1 vs 1 Math Battle Game");

        gameClient.addMessageListener(this);
        // 3초 대기
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        setExtendedState(JFrame.MAXIMIZED_BOTH); // 전체 화면 모드
        setUndecorated(true); // 타이틀 바 제거
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 배경 패널 초기화
        BackgroundPanel backgroundPanel = new BackgroundPanel(getClass().getResource("/Assets/BattlePage.gif").toString()); // 경로 조정 필요
        
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel); // 배경 패널을 컨텐츠 패널로 설정
        
        // 컴포넌트 초기화
        initializeComponents();

        // 동적 레이아웃을 위한 컴포넌트 리스너 추가
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                applyDynamicLayout();
            }
        });

        // 타이머 초기화 및 시작
        initializeTimer();

        setVisible(true);
        applyDynamicLayout(); // 초기 레이아웃 적용
    }

    /**
     * 모든 UI 컴포넌트를 초기화합니다.
     */
    private void initializeComponents() {
        // 원형 타이머
        circularTimer = new HorizontalProgressBar();
        add(circularTimer);


        // 중앙 랜덤 숫자 라벨
        randomNumberLabel = new JLabel(String.valueOf(gameRoom.getGameState().getTargetNumber()), SwingConstants.CENTER);
        randomNumberLabel.setFont(new Font("Arial", Font.BOLD, 40));
        randomNumberLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(randomNumberLabel);

        // 플레이어 1 프로필 (좌측)
        player1ImageLabel = new JLabel();
        player1ImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        player1ImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(player1ImageLabel);

        // 플레이어 2 프로필 (우측)
        player2ImageLabel = new JLabel();
        player2ImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        player2ImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(player2ImageLabel);
        
        player1LifeBar = new JProgressBar(0, 100);
        player1LifeBar.setValue(player1Life);
        player1LifeBar.setForeground(Color.RED);
        player1LifeBar.setStringPainted(true);
        add(player1LifeBar);

        player1NameLabel = new JLabel("Player 1", SwingConstants.CENTER);
        player1NameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(player1NameLabel);

        player2LifeBar = new JProgressBar(0, 100);
        player2LifeBar.setValue(player2Life);
        player2LifeBar.setForeground(Color.RED);
        player2LifeBar.setStringPainted(true);
        add(player2LifeBar);

        player2NameLabel = new JLabel("Player 2", SwingConstants.CENTER);
        player2NameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(player2NameLabel);

        // 숫자 카드 버튼 (이미지 버튼)
        cardButtons = new GameButton[5];
        for (int i = 0; i < 5; i++) {
            // 숫자 텍스트 버튼 생성
            Integer i1 = gameRoom.getGameState().getNumberCards().get(i);
            String buttonText = String.valueOf(i1); // 1부터 9까지의 랜덤 숫자
            cardButtons[i] = new GameButton(buttonText, Color.GRAY, Color.DARK_GRAY, Color.BLACK);
            
            // 버튼 스타일 설정
            cardButtons[i].setFont(new Font("Arial", Font.BOLD, 24));
            cardButtons[i].addActionListener(new CardButtonListener());
            cardButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            // 버튼 추가
            add(cardButtons[i]);
        }


        // 연산자 버튼
        String[] operators = {"+", "-", "*", "/", "(", ")"};
        operatorButtons = new GameButton[operators.length];
        for (int i = 0; i < operators.length; i++) {
            operatorButtons[i] = new GameButton(operators[i], Color.DARK_GRAY, Color.GRAY, Color.BLACK);
            operatorButtons[i].setFont(new Font("Arial", Font.BOLD, 24));
            operatorButtons[i].addActionListener(new OperatorButtonListener());
            operatorButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            add(operatorButtons[i]);
        }

        // 수식 입력 필드
        player1FormulaField = new JTextField();
        player1FormulaField.setFont(new Font("Arial", Font.PLAIN, 24));
        player1FormulaField.setEditable(false); // 직접 타이핑 불가
        add(player1FormulaField);

        player2FormulaField = new JTextField();
        player2FormulaField.setFont(new Font("Arial", Font.PLAIN, 24));
        player2FormulaField.setEditable(false); // 직접 타이핑 불가
        add(player2FormulaField);

        // 액션 버튼
        attackButton = new GameButton("Attack", new Color(0, 120, 215), new Color(0, 150, 255), new Color(0, 180, 255));
        attackButton.setFont(new Font("Arial", Font.BOLD, 20));
        attackButton.addActionListener(e -> performAttack());
        add(attackButton);
        attackButton.setForeground(Color.BLACK);

        eraseButton = new GameButton("Erase", new Color(255, 165, 0), new Color(255, 200, 0), new Color(255, 220, 100));
        eraseButton.setFont(new Font("Arial", Font.BOLD, 20));
        eraseButton.addActionListener(e -> eraseLastInput());
        add(eraseButton);
        eraseButton.setForeground(Color.BLACK);

        eraseAllButton = new GameButton("Erase All", new Color(220, 20, 60), new Color(255, 69, 0), new Color(255, 100, 100));
        eraseAllButton.setFont(new Font("Arial", Font.BOLD, 20));
        eraseAllButton.addActionListener(e -> eraseAllInput());
        add(eraseAllButton);
        eraseAllButton.setForeground(Color.BLACK);

        // 라운드 라벨
        roundLabel = new JLabel("", SwingConstants.CENTER);
        roundLabel.setFont(new Font("Arial", Font.BOLD, 50));
        roundLabel.setForeground(Color.RED);
        roundLabel.setVisible(false);
        add(roundLabel);

        randomNumberLabel.setForeground(Color.BLACK);
        player1NameLabel.setForeground(Color.BLACK);
        player2NameLabel.setForeground(Color.BLACK);
        player1FormulaField.setForeground(Color.BLACK);
        player2FormulaField.setForeground(Color.BLACK);
    }

    /**
     * 주어진 경로에서 이미지를 로드하고 크기를 조정합니다.
     *
     * @param path   이미지 리소스 경로
     * @param width  원하는 너비
     * @param height 원하는 높이
     * @return 크기가 조정된 ImageIcon 또는 이미지가 없을 경우 null
     */
    private ImageIcon loadAndScaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(img);
            scaledIcon.setDescription(path); // 이미지 경로를 설명으로 설정
            return scaledIcon;
        } catch (Exception e) {
            System.err.println("이미지를 찾을 수 없습니다: " + path);
            return null;
        }
    }

    /**
     * 카운트다운 타이머를 초기화하고 시작합니다.
     */
    private void initializeTimer() {
        long startTime = System.currentTimeMillis(); // 타이머 시작 시간
        long totalTime = 30000; // 총 30초 (밀리초 단위)

        countdownTimer = new Timer(50, e -> { // 50ms 간격으로 UI 업데이트
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime; // 경과 시간 계산

            // 남은 시간 계산
            timeRemaining = (int) ((totalTime - elapsedTime) / 1000);

            // 남은 시간이 음수가 되지 않도록 처리
            if (timeRemaining <= 0) {
                timeRemaining = 0; // 남은 시간을 0으로 고정
                countdownTimer.stop(); // 타이머 종료
                handleTimerExpiry();
            }

            // 진행 퍼센트 갱신
            int progress = (int) ((totalTime - elapsedTime) * 100 / totalTime);
            circularTimer.setProgress(Math.max(0, progress)); // 진행률이 음수가 되지 않도록 보장
        });

        countdownTimer.start();
    }




    /**
     * 타이머 만료 시 처리 로직을 수행합니다.
     */
    private void handleTimerExpiry() {
        // 타이머 만료: 양 플레이어가 체력 10 감소
        player1Life -= 10;
        player2Life -= 10;

        player1Life = Math.max(player1Life, 0);
        player2Life = Math.max(player2Life, 0);

        player1LifeBar.setValue(player1Life);
        player2LifeBar.setValue(player2Life);

        CustomPopup.showPopup(this, "Time's up! Both players lost 10 health points.", "Notification");

        // 게임 종료 조건 확인
        checkGameOver();

        // 라운드 증가 및 라운드 라벨 표시
        currentRound++;
        showRoundLabel(currentRound);
    }

    /**
     * 플레이어의 체력에 따라 게임 종료 여부를 확인합니다.
     */
    private void checkGameOver() {
        if (player1Life <= 0 && player2Life <= 0) {
        	CustomPopup.showPopup(this, "It's a draw!", "Notification");
            dispose(); // 게임 종료
        } else if (player2Life <= 0) {
        	CustomPopup.showPopup(this, "Player 1 wins!", "Notification");
            dispose(); // 게임 종료
        } else if (player1Life <= 0) {
        	CustomPopup.showPopup(this, "Player 2 wins!", "Notification");
            dispose(); // 게임 종료
        }
    }

    /**
     * "Round X" 라벨을 화면에 표시하고 2초 후에 숨깁니다.
     *
     * @param roundNumber 현재 라운드 번호
     */
    private void showRoundLabel(int roundNumber) {
        roundLabel.setText("Round " + roundNumber);
        roundLabel.setVisible(true);

        // 2초 후에 라벨 숨기고 라운드 초기화
        Timer labelTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roundLabel.setVisible(false);
                resetRound();
                ((Timer) e.getSource()).stop();
            }
        });
        labelTimer.setRepeats(false);
        labelTimer.start();
    }

    /**
     * 다음 라운드를 위해 상태를 초기화합니다.
     */
    private void resetRound() {
        // 수식 입력 필드 초기화
        player1FormulaField.setText("");
        player2FormulaField.setText("");

        // 랜덤 숫자 초기화
        randomNumberLabel.setText(String.valueOf(random.nextInt(50) + 1));

        // 선택된 카드 버튼 해제
        if (selectedCardButton != null) {
            selectedCardButton.setSelected(false);
            selectedCardButton = null;
        }

        // 타이머 초기화
        resetTimer();
    }

    /**
     * 카운트다운 타이머를 재설정합니다.
     */
    private void resetTimer() {
        // 기존 타이머 중지
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }

        // 타이머 초기화
        timeRemaining = 90; // 남은 시간 초기화
        circularTimer.setProgress(100); // 진행률 초기화
        initializeTimer(); // 새 타이머 실행
    }


    /**
     * 현재 창 크기에 맞춰 동적으로 레이아웃을 적용합니다.
     */
    private void applyDynamicLayout() {
        int width = getWidth();
        int height = getHeight();

        // 패딩
        int padding = 50;

        // 타이머 바 위치 및 크기
        circularTimer.setBounds(width / 2 - (int) (width * 0.15), padding, (int) (width * 0.3), 40);

        // 플레이어 1 프로필 (좌측)
        int profileWidth = 150;
        int profileHeight = 150;
        player1ImageLabel.setBounds(padding, padding + 60, profileWidth, profileHeight);
        player1LifeBar.setBounds(padding, padding + 220, profileWidth, 20);
        player1NameLabel.setBounds(padding, padding + 250, profileWidth, 30);

        // 플레이어 2 프로필 (우측)
        player2ImageLabel.setBounds(width - padding - profileWidth, padding + 60, profileWidth, profileHeight);
        player2LifeBar.setBounds(width - padding - profileWidth, padding + 220, profileWidth, 20);
        player2NameLabel.setBounds(width - padding - profileWidth, padding + 250, profileWidth, 30);

        // 중앙 랜덤 숫자 라벨 위치
        int randomNumberSize = 150;
        randomNumberLabel.setBounds(width / 2 - randomNumberSize / 2, padding + 70, randomNumberSize, randomNumberSize);

        // 숫자 카드 버튼 위치 및 크기
        int shiftRight = 80;
        int cardButtonSize = 150;
        int horizontalSpacing = 20;
        int cardStartX = width / 2 - (cardButtons.length * (cardButtonSize + horizontalSpacing)) / 2 + shiftRight;
        int cardY = padding + 400;

        for (int i = 0; i < cardButtons.length; i++) {
            // y-좌표 조정
            int adjustedCardY = cardY - 100;

            // x-좌표 조정
            int adjustedCardX = cardStartX + i * (cardButtonSize + horizontalSpacing) - 50;

            // 버튼 위치 설정
            cardButtons[i].setBounds(adjustedCardX, adjustedCardY, cardButtonSize, cardButtonSize);
        }

        // 연산자 버튼 위치 및 크기
        int operatorButtonWidth = 60;
        int operatorButtonHeight = 40;
        int operatorStartX = width / 2 - (operatorButtons.length * (operatorButtonWidth + 10)) / 2;
        int operatorY = cardY + cardButtonSize + 30;
        for (int i = 0; i < operatorButtons.length; i++) {
            operatorButtons[i].setBounds(operatorStartX + i * (operatorButtonWidth + 10), operatorY, operatorButtonWidth, operatorButtonHeight);
        }

        // 수식 입력 필드 위치
        int formulaFieldWidth = (int) (width * 0.3);
        player1FormulaField.setBounds(padding, height - padding - 150, formulaFieldWidth, 50);
        player2FormulaField.setBounds(width - padding - formulaFieldWidth, height - padding - 150, formulaFieldWidth, 50);

        // 라운드 라벨 위치
        roundLabel.setBounds(width / 2 - 200, padding, 400, 100); // 너비 400, 높이 100

        // 액션 버튼 위치 및 크기
        int buttonWidth = 150;
        int buttonHeight = 50;
        int buttonY = height - padding - 70;
        int attackX = width / 2 - (buttonWidth + 30);
        int eraseX = width / 2 - (buttonWidth / 2);
        int eraseAllX = width / 2 + 30;

        attackButton.setBounds(500, buttonY, buttonWidth, buttonHeight);
        eraseButton.setBounds(700, buttonY, buttonWidth, buttonHeight);
        eraseAllButton.setBounds(900, buttonY, buttonWidth, buttonHeight);

    }

    /**
     * 공격 액션을 수행합니다. 현재 플레이어의 수식을 평가하고 공격 성공 여부를 결정합니다.
     */
    public void performAttack() {
        if (isPlayer1Turn) {
            gameClient.sendAttack(gameRoom.getRoomId());
        } else {
            CustomPopup.showPopup(this, "NOT YOUR TURN", "Notification");
        }

        // 라운드 증가 및 라운드 라벨 표시
        currentRound++;
        showRoundLabel(currentRound);
    }

    /**
     * 현재 플레이어의 수식 입력 필드에서 마지막 문자를 삭제합니다.
     */
    private void eraseLastInput() {
        if (isPlayer1Turn) {
            gameClient.sendRemoveExpression(gameRoom.getRoomId());
        } else {
            CustomPopup.showPopup(this, "NOT YOUR TURN", "Notification");
        }
    }

    /**
     * 양쪽 플레이어의 수식 입력 필드를 모두 초기화합니다.
     */
    private void eraseAllInput() {
        if (isPlayer1Turn) {
            gameClient.sendRemoveAllExpression(gameRoom.getRoomId());
        } else {
            CustomPopup.showPopup(this, "NOT YOUR TURN", "Notification");
        }
    }

    private void isFirst() {
        if (gameRoom.getGameState().getCurrentPlayer().getName().equals(gameClient.getUsername())) {
            isPlayer1Turn = true;
        } else {
            isPlayer1Turn = false;
        }
    }

    @Override
    public void onMessageReceived(CallbackMessage message) {
        SwingUtilities.invokeLater(() -> {
            if (message.type().equals("ADD_EXPRESSION") ||
                    message.type().equals("REMOVE_EXPRESSION") ||
                    message.type().equals("REMOVE_ALL_EXPRESSION")) {
                setText((String) message.message());
            }
            else if (message.type().equals("ATTACK_FAIL")) {
                attack_fail();
            }

        });
    }

    private void attack_fail() {
        if (isPlayer1Turn) {
            CustomPopup.showPopup(this, "You failed to attack.", "Notification");
        } else {
            CustomPopup.showPopup(this, "You successfully defended", "Notification");
        }
        checkGameOver();
    }

    private void setText(String text) {
        if (isPlayer1Turn) {
            player1FormulaField.setText(text);
        } else {
            player2FormulaField.setText(text);
        }
    }

    /**
     * 숫자 카드 버튼의 액션 리스너 클래스입니다.
     */
    private class CardButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GameButton source = (GameButton) e.getSource();

            // 클릭된 버튼 선택 상태 설정
            source.setSelected(true);
            selectedCardButton = source;

            // 현재 플레이어의 수식 필드에 숫자 추가
            String numberText = source.getText();
            gameClient.sendAddExpression(gameRoom.getRoomId(), numberText);
        }
    }

    /**
     * 연산자 버튼의 액션 리스너 클래스입니다.
     */
    private class OperatorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GameButton source = (GameButton) e.getSource();
            String operator = source.getText();
            gameClient.sendAddExpression(gameRoom.getRoomId(), operator);
        }
    }
}