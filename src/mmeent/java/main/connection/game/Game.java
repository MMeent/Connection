package mmeent.java.main.connection.game;

import mmeent.java.main.connection.player.Player;
import mmeent.java.main.connection.board.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthias on 20/12/2014.
 */
public class Game {
    private Board board;
    private Player[] players;
    private int turn = 0;
    private List<Player> spectators = new ArrayList<Player>();

    public Game(Player[] players){
        this.board = new Board();
        this.players = players;
    }

    public Game(Player[] players, int height, int width){
        this.board = new Board(height, width);
        this.players = players;
    }

    public Game(Player[] players, int height, int widht, int lenght){
        this.board = new Board(height, widht, lenght);
        this.players = players;
    }

    public Game(Player[] players, Board board){
        this.players = players;
        this.board = board.deepCopy();
    }
}
