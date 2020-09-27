package com.sv.swingui;

import javax.swing.*;
import java.awt.*;

/**
 * Wrapper class for JFrame
 */
public class AppFrame extends JFrame {

    public AppFrame() {
        Font baseFont = new Font("Dialog", Font.PLAIN, 12);
        setFont(baseFont);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);
        setForeground(Color.black);
        setIconImage(new ImageIcon("/icons/app-icon.png").getImage());
        setLayout(new FlowLayout());
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
