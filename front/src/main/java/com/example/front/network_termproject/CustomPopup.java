package com.example.front.network_termproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomPopup extends JDialog {
    /**
     * 커스텀 팝업 창을 생성합니다.
     * @param parent 부모 프레임
     * @param message 표시할 메시지
     * @param title 팝업 창 제목
     */
    public CustomPopup(JFrame parent, String message, String title) {
        super(parent, title, true); // 모달 설정
        setLayout(new BorderLayout());
        setSize(400, 200); // 창 크기 설정
        setLocationRelativeTo(parent); // 부모 창 중앙에 위치

        // 메시지 라벨
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(messageLabel, BorderLayout.CENTER);

        // 확인 버튼
        JButton okButton = new JButton("CLOSE");
        okButton.setFont(new Font("Arial", Font.PLAIN, 16));
        okButton.setBackground(new Color(0, 120, 215));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 창 닫기
            }
        });

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 팝업 창을 표시합니다.
     * @param parent 부모 프레임
     * @param message 표시할 메시지
     * @param title 팝업 창 제목
     */
    public static void showPopup(JFrame parent, String message, String title) {
        CustomPopup popup = new CustomPopup(parent, message, title);
        popup.setVisible(true);
    }
}
