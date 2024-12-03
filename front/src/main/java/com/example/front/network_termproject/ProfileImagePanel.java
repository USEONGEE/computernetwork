package com.example.front.network_termproject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ProfileImagePanel extends JPanel {
    private BufferedImage image;

    public ProfileImagePanel(String imagePath, int width, int height) {
        try {
            // 이미지 로드
            image = ImageIO.read(new File(imagePath));
            // 이미지 크기 조정
            image = resizeImage(image, width, height);
        } catch (Exception e) {
            System.err.println("이미지를 로드할 수 없습니다: " + imagePath);
        }
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // 패널 중앙에 이미지 렌더링
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            // 이미지가 없는 경우 대체 텍스트 표시
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString("Image not available", getWidth() / 4, getHeight() / 2);
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage bufferedScaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedScaledImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();
        return bufferedScaledImage;
    }
}
