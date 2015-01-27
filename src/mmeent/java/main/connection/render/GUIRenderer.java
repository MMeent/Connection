package mmeent.java.main.connection.render;

import sun.text.normalizer.Utility;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import javax.imageio.*;
import javax.swing.*;


import javax.imageio.ImageIO;
import javax.swing.*;

public class GUIRenderer {
    public static void main(String[] args) {
        SecondFrame f = new SecondFrame("Draw and Fill");
        f.init();
    }
}

class SecondFrame extends JFrame{



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
        JButton button1 = new JButton("");
        button1.setSize(30,40);
        button1.setLocation(20,10);
        mainPanel.add(button1);
        JButton button2 = new JButton("");
        button2.setSize(30,40);
        button2.setLocation(90,10);
        mainPanel.add(button2);



        BufferedImage yellowButton = null;
        BufferedImage redButton = null;
        BufferedImage whiteButton = null;
        URL url1 = getClass().getResource("redButton.png");
        URL url2 = getClass().getResource("yellowButton.png");
        URL url3 = getClass().getResource("whiteButton.png");
        try {
                yellowButton = ImageIO.read(new File(url1.getPath()));
                redButton = ImageIO.read(new File(url2.getPath()));
                whiteButton = ImageIO.read(new File(url3.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.paint(g);
        Graphics2D circle1 = (Graphics2D) g;
        circle1.setColor(Color.blue);
        circle1.fillRect(0, 80, 510, 440);

        for(int col = 0; col < 6; col ++) {
            for(int row = 0; row < 7; row++) {
                int y = col * 70 + 80 + 20;
                int x = row * 70 + 20;
                circle1.drawImage(whiteButton,x,y,null);
            }
        }
    }
}
