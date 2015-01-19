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
    }

    public Game(LocalPlayer[] players, short height, short width){
        this.board = new Board(height, width);
        this.players = players;
        this.renderer = new TextBoardRenderer(board);
    }

    public Game(LocalPlayer[] players, short height, short width, short length){
        this.board = new Board(height, width, length);
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
            if(players[0].getName() != null) {
                System.out.println("aaa");
            }
            a = false;
        }
    }

    public static void main(String[] args) {
        LocalPlayer player1 = LocalPlayer.get("Henk");
        LocalPlayer player2 = LocalPlayer.get("Sjaak");
        LocalPlayer[] players = {player1,player2};
        Game game = new Game(players);
        game.play();
    }
}
