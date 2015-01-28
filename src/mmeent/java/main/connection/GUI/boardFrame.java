package mmeent.java.main.connection.GUI;

import mmeent.java.main.connection.board.Board;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.player.Player;
import mmeent.java.main.connection.render.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class BoardFrame extends JPanel implements ActionListener, mmeent.java.main.connection.render.Renderer {
    private Board board;
    private Game game;
    private Player[] players;

    public BoardFrame(Board board) {
        this.board = board;
    }

    @Override
    public void paint(Graphics g) {
        JButton btnColumn1 = new JButton("");
        btnColumn1.setSize(30,40);
        btnColumn1.setLocation(20, 10);
        btnColumn1.setActionCommand("1");
        btnColumn1.addActionListener(this);
        add(btnColumn1);
        JButton btnColumn2 = new JButton("");
        btnColumn2.setSize(30,40);
        btnColumn2.setLocation(90,10);
        btnColumn2.setActionCommand("2");
        btnColumn2.addActionListener(this);
        add(btnColumn2);
        JButton btnColumn3 = new JButton("");
        btnColumn3.setSize(30,40);
        btnColumn3.setLocation(160,10);
        btnColumn3.setActionCommand("3");
        btnColumn3.addActionListener(this);
        add(btnColumn3);
        JButton btnColumn4 = new JButton("");
        btnColumn4.setSize(30,40);
        btnColumn4.setLocation(230,10);
        btnColumn4.setActionCommand("4");
        btnColumn4.addActionListener(this);
        add(btnColumn4);
        JButton btnColumn5 = new JButton("");
        btnColumn5.setSize(30,40);
        btnColumn5.setLocation(300,10);
        btnColumn5.setActionCommand("5");
        btnColumn5.addActionListener(this);
        add(btnColumn5);
        JButton btnColumn6 = new JButton("");
        btnColumn6.setSize(30,40);
        btnColumn6.setLocation(370,10);
        btnColumn6.setActionCommand("6");
        btnColumn6.addActionListener(this);
        add(btnColumn6);
        JButton btnColumn7 = new JButton("");
        btnColumn7.setSize(30,40);
        btnColumn7.setLocation(440,10);
        btnColumn7.setActionCommand("7");
        btnColumn7.addActionListener(this);
        add(btnColumn7);

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

    public static void open(Board board, Game game) {
        JFrame frame = new JFrame("Mini Tennis");
        frame.add(new BoardFrame(board));
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("1")) {
            System.exit(0);
        }
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public void addChatMessage(String msg) {

    }

    @Override
    public void render() {

    }

    @Override
    public void addErrorMessage(String id, String msg) {

    }

    @Override
    public void addMessage(String msg) {

    }
}