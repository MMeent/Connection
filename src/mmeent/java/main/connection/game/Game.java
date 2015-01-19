package mmeent.java.main.connection.game;

import mmeent.java.main.connection.player.LocalPlayer;
import mmeent.java.main.connection.board.Board;
import mmeent.java.main.connection.render.Renderer;
import mmeent.java.main.connection.render.TextBoardRenderer;

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
    private Renderer renderer;

    public Game(LocalPlayer[] players){
        this.board = new Board();
        this.players = players;
        this.playerAmount = players.length;
        this.renderer = new TextBoardRenderer(board);
        for(LocalPlayer player: players){
            player.setGame(this);
        }
    }

    public Game(LocalPlayer[] players, short width, short height){
        this.board = new Board(width, height);
        this.players = players;
        this.renderer = new TextBoardRenderer(board);
    }

    public Game(LocalPlayer[] players, short width, short height, short length){
        this.board = new Board(width, height, length);
        this.players = players;
        this.renderer = new TextBoardRenderer(board);
    }

    public Game(LocalPlayer[] players, Board board){
        this.players = players;
        this.board = board.deepCopy();
        this.renderer = new TextBoardRenderer(board);
    }

    public Board getBoard() {
        return board;
    }

    public void play() {
        int turn = 0;
        boolean a = true;
        while(a) {
            renderer.render();
            players[turn % 2].getMove(turn).makeMove();
            turn ++;
        }
    }

    public static void main(String[] args) {
        LocalPlayer player1 = LocalPlayer.get("Henk", (byte) 1);
        LocalPlayer player2 = LocalPlayer.get("Sjaak", (byte) 2);
        LocalPlayer[] players = {player1,player2};
        Game game = new Game(players);
        game.play();
    }
}
