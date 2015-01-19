package mmeent.java.main.connection.game;

import mmeent.java.main.connection.player.LocalPlayer;
import mmeent.java.main.connection.board.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthias on 20/12/2014.
 */
public class Game {
    private Board board;
    private LocalPlayer[] players;
    private int playerAmount;
    private int turn = 0;
    private List<LocalPlayer> spectators = new ArrayList<LocalPlayer>();

    public Game(LocalPlayer[] players){
        this.board = new Board();
        this.players = players;
        this.playerAmount = players.length;
    }

    public Game(LocalPlayer[] players, short height, short width){
        this.board = new Board(height, width);
        this.players = players;
    }

    public Game(LocalPlayer[] players, short height, short widht, short lenght){
        this.board = new Board(height, widht, lenght);
        this.players = players;
    }

    public Game(LocalPlayer[] players, Board board){
        this.players = players;
        this.board = board.deepCopy();
    }
}
