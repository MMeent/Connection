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

    /**
     * Constructor of <code>Game</code>
     * @param players Array of players that will join the <code>Game</code>
     */
    public Game(LocalPlayer[] players){
        this.board = new Board();
        this.players = players;
        this.playerAmount = players.length;
        this.renderer = new TextBoardRenderer(board);
        for(LocalPlayer player: players){
            player.setGame(this);
        }
    }

    /**
     * Constructor of <code>Game</code> with custom <code>Board</code> size
     * @param players Array Players that will join the <code>Game</code>
     * @param width Width of the board
     * @param height Height of the board
     */
    public Game(LocalPlayer[] players, short width, short height){
        this.board = new Board(width, height);
        this.players = players;
        this.renderer = new TextBoardRenderer(board);
    }

    /**
     * Constructor of <code>Game</code> with custom <code>Board</code> size en custom row length
     * @param players Array of Players that will join the game
     * @param width WIdth of the <code>Board</code>
     * @param height Height of the <code>Board</code>
     * @param length Length of the row needed to win the <code>Game</code>
     */
    public Game(LocalPlayer[] players, short width, short height, short length){
        this.board = new Board(width, height, length);
        this.players = players;
        this.renderer = new TextBoardRenderer(board);
    }

    /**
     * Constructor of <code>Game</code> with a <code>Board</code> as parameter
     * @param players Array of players that will join the <code>Game</code>
     * @param board The <code>Board</code> on which the <code>Game</code> will be played
     */
    public Game(LocalPlayer[] players, Board board){
        this.players = players;
        this.board = board.deepCopy();
        this.renderer = new TextBoardRenderer(board);
    }

    /**
     * Functions that returns the <code>Board</code> of this <code>Game</code>
     * @return Return the <code>Board</code> of this <code>Game</code>
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Function that starts the <code>Game</code.>
     */
    public void play() {
        int turn = -1;
        boolean a = true;
        while(a) {
            turn ++;
            renderer.render();
            players[turn % 2].getMove(turn).makeMove();
            for(LocalPlayer p : players) {
                if(board.hasFour(p.getId())){
                    a = false;
                }
            }
        }
        System.out.println("The winner of the game is: " + players[turn % 2].getName());
    }

    public static void main(String[] args) {
        LocalPlayer player1 = LocalPlayer.get("Henk", (byte) 1);
        LocalPlayer player2 = LocalPlayer.get("Sjaak", (byte) 2);
        LocalPlayer[] players = {player1,player2};
        Game game = new Game(players);
        game.play();
    }
}
