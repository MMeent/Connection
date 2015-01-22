package mmeent.java.main.connection.render;

import sun.text.normalizer.Utility;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;


import javax.imageio.ImageIO;
import javax.swing.*;

/*
public class GUIRenderer {
    public static void main(String[] args) {
        SecondFrame f = new SecondFrame("Draw and Fill");
        f.init();
    }
}

class SecondFrame extends JFrame{
    Image redButton;
    Image yellowButton = new BufferedImage("yellowButton.png").getImage();




    SecondFrame(String title) {
        super(title);
    }

    private JPanel mainPanel;
    private GridBagConstraints gbc = new GridBagConstraints();
    private GridBagLayout gbLayout = new GridBagLayout();

    void init() {
        mainPanel = new JPanel();
        mainPanel.setLayout(gbLayout);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setContentPane(mainPanel);
        gbc.gridheight = 1;

        mainPanel.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                Point mousePosition;
                mousePosition = mainPanel.getMousePosition();
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

        });

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D circle1 = (Graphics2D) g;

        circle1.drawImage(yellowButton,1,1,null);
    }
}*/