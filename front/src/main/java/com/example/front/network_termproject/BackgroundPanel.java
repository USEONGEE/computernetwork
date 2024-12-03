package com.example.front.network_termproject;

import javax.swing.*;
import java.awt.*;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private float alpha = 1.0f; // Opacity level

    public BackgroundPanel(String imagePath) {
        setImage(imagePath);
    }

    public void setImage(String imagePath) {
        backgroundImage = new ImageIcon(imagePath).getImage();
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); // Set opacity
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g2d.dispose();
    }
}