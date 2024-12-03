package com.example.front.network_termproject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class RoundedPanel extends JPanel {
    private final int cornerRadius;
    private final Color backgroundColor;
    private BufferedImage backgroundImage;
    private float opacity; // 투명도 (0.0 ~ 1.0)

    public RoundedPanel(int radius, Color backgroundColor, float opacity) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = backgroundColor;
        this.opacity = opacity;
        setOpaque(false); // Transparent background
    }

    public RoundedPanel(int radius, String textureImagePath, float opacity) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = null; // No fallback color if texture is used
        this.opacity = opacity;
        try {
            backgroundImage = ImageIO.read(new File(textureImagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOpaque(false); // Transparent background
    }

    public void setOpacity(float opacity) {
        if (opacity < 0.0f || opacity > 1.0f) {
            throw new IllegalArgumentException("Opacity must be between 0.0 and 1.0");
        }
        this.opacity = opacity;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Apply rounded corners
        g2d.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Set opacity for background
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        // Draw texture image if available, otherwise fallback to color
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else if (backgroundColor != null) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        g2d.dispose();
    }
}
