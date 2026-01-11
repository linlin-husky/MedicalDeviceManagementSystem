/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author linlinfan
 */

import javax.swing.*;
import java.awt.*;


public class BackgroundPanel extends JPanel {
    
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
            this.setLayout(new CardLayout()); // Important so it works like the original container
        } catch (Exception e) {
            System.err.println("Background image load fail：" + imagePath);
            e.printStackTrace();
        }
             setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image stretched to fill the panel
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
}
