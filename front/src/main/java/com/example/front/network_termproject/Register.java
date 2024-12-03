package com.example.front.network_termproject;

import com.example.front.network_termproject.client.GameClientV2;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Register {

    private final GameClientV2 gameClient = GameClientV2.getInstance();

    public Register() {
        // Main frame setup
        JFrame frame = new JFrame("Sign Up");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500); // Sleek vertical layout
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setLayout(new BorderLayout());

        // Background panel setup with image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/Assets/Register_background.png")); // Path to the image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        backgroundPanel.setOpaque(false); // Ensure transparency
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding
        frame.add(backgroundPanel, BorderLayout.CENTER);

        // Header label
        JLabel headerLabel = new JLabel("Create Your Account");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        backgroundPanel.add(headerLabel);

        // Username field with placeholder
        PlaceholderTextField usernameField = new PlaceholderTextField();
        usernameField.setPlaceholder("Username");
        styleTextField(usernameField);
        backgroundPanel.add(usernameField);

        // Email field with placeholder
        PlaceholderTextField emailField = new PlaceholderTextField();
        emailField.setPlaceholder("Email Address");
        styleTextField(emailField);
        backgroundPanel.add(emailField);

        // Password field with placeholder
        PlaceholderTextField passwordField = new PlaceholderTextField();
        passwordField.setPlaceholder("Password");
        styleTextField(passwordField);
        backgroundPanel.add(passwordField);

        // Confirm Password field with placeholder
        PlaceholderTextField confirmPasswordField = new PlaceholderTextField();
        confirmPasswordField.setPlaceholder("Confirm Password");
        styleTextField(confirmPasswordField);
        backgroundPanel.add(confirmPasswordField);

        // Terms and Conditions checkbox
        JCheckBox termsCheckBox = new JCheckBox("I agree to the Terms and Conditions");
        termsCheckBox.setFont(new Font("Arial", Font.BOLD, 14));
        termsCheckBox.setForeground(Color.BLACK);
        termsCheckBox.setOpaque(false);
        termsCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
        backgroundPanel.add(termsCheckBox);

        // Sign Up button using FadeButton
        FadeButton registerButton = new FadeButton("Sign Up",
                new Color(76, 175, 80), // Default background
                new Color(56, 142, 60), // Clicked background
                new Color(129, 199, 132), // Hover background
                Color.WHITE // Text color
        );
        styleButton(registerButton);
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //playSound(getClass().getResource("/Sounds/bbyong.wav").toString());

            }

            @Override
            public void mousePressed(MouseEvent e) {
                //playSound(getClass().getResource("/Sounds/click.wav").toString());
                // TODO 로그인 요청 해야함.
                String username = usernameField.getText();
                String password = passwordField.getText();
                try {
                    gameClient.register(username, password);
                } catch (Exception ex) {
                    System.out.println("Register.mousePressed");
                    throw new RuntimeException(ex);
                }

            }
        });
        registerButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill out all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!termsCheckBox.isSelected()) {
                JOptionPane.showMessageDialog(frame, "You must agree to the terms and conditions.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        });
        
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
        backgroundPanel.add(registerButton);

        // Close button using FadeButton
        FadeButton closeButton = new FadeButton("Close",
                new Color(244, 67, 54), // Default background
                new Color(211, 47, 47), // Clicked background
                new Color(239, 154, 154), // Hover background
                Color.WHITE // Text color
        );
        styleButton(closeButton);
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //playSound(getClass().getResource("/Sounds/bbyong.wav").toString());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //playSound(getClass().getResource("/Sounds/click.wav").toString());
            }
        });
        closeButton.addActionListener(e -> frame.dispose());
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        backgroundPanel.add(closeButton);

        // Display the frame
        frame.setVisible(true);
    }

    private void styleTextField(PlaceholderTextField textField) {
        textField.setMaximumSize(new Dimension(300, 40));
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setForeground(Color.BLACK); // 텍스트 색상은 검정으로 설정
        textField.setBackground(new Color(210, 180, 140)); // 연한 갈색 (Tan 색상)
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(139, 69, 19), 2), // 진한 갈색 테두리
                BorderFactory.createEmptyBorder(5, 10, 5, 10)) // 안쪽 여백
        );
        textField.setOpaque(true);
    }

    private void styleButton(FadeButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void playSound(String soundPath) {
        new Thread(() -> {
            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(soundPath))) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Register());
    }
    
    
}
