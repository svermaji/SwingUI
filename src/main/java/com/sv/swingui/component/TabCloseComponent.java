/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sv.swingui.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and
 * a JButton to close the tab it belongs to
 */
public class TabCloseComponent extends JPanel {
    private Color crossTextColor, crossBkColor, rollOverTextColor, rollOverBkColor;
    private final AppTabbedPane pane;
    private int tabNum;
    private String title;
    private JLabel label;
    private boolean closable;
    private JButton tabButton;

    public TabCloseComponent(AppTabbedPane pane, int tabNum, String title) {
        this(pane, tabNum, title, true);
    }

    public TabCloseComponent(AppTabbedPane pane, int tabNum, String title, boolean closable) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.tabNum = tabNum;
        this.title = title;
        this.closable = closable;
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);

        //make JLabel read titles from JTabbedPane
        label = new AppLabel() {
            public String getText() {
                return title;
            }
        };

        add(label);
        //add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        if (closable) {
            //tab close button
            tabButton = new TabButton();
            add(tabButton);
        }
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    public boolean isClosable() {
        return closable;
    }

    public JLabel getTabLabel() {
        return label;
    }

    public JButton getTabButton() {
        return tabButton;
    }

    public void setColors(Color rollOverColor, Color rollOverBkColor,
                          Color crossColor, Color crossBkColor) {
        setCrossTextColor(crossColor);
        setCrossBkColor(crossBkColor);
        setRollOverTextColor(rollOverColor);
        setRollOverBkColor(rollOverBkColor);
    }

    public void setCrossTextColor(Color c) {
        this.crossTextColor = c;
        setTabLabelColor(c);
        if (isClosable()) {
            tabButton.setForeground(c);
        }
    }

    public void setCrossBkColor(Color c) {
        this.crossBkColor = c;
        if (isClosable()) {
            tabButton.setBackground(c);
        }
    }

    public void setRollOverTextColor(Color rollOverTextColor) {
        this.rollOverTextColor = rollOverTextColor;
    }

    public void setRollOverBkColor(Color rollOverBkColor) {
        this.rollOverBkColor = rollOverBkColor;
    }

    public void setTabLabelColor(Color lblColor) {
        label.setForeground(lblColor);
    }

    private class TabButton extends AppButton implements ActionListener {
        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Close");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent -- commenting for bk color
            //setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            int i = tabNum;
            if (i != -1) {
                pane.remove(i);
                tabRemoved();
            }
        }

        //we don't want to update UI for this button
        public void updateUI() {
        }

        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));

            // rollover and text color handled by mouse listener
            g2.setColor(crossTextColor);
            if (getModel().isRollover()) {
                g2.setColor(rollOverTextColor);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

    public void tabRemoved() {
        // use to override the event of removal
    }

    public int getTabNum() {
        return tabNum;
    }

    public void setTabNum(int tabNum) {
        this.tabNum = tabNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private final MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                //button.setBorderPainted(true);
                button.setBackground(rollOverBkColor);
                button.setForeground(rollOverTextColor);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                //button.setBorderPainted(false);
                button.setBackground(crossBkColor);
                button.setForeground(crossTextColor);
            }
        }
    };

    @Override
    public String toString() {
        return "TabCloseComponent{" +
                "tabNum=" + tabNum +
                ", title=" + label.getText() +
                '}';
    }
}
