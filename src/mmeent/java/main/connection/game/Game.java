package mmeent.java.main.connection.game;

import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.exception.ConnectFourException;
import mmeent.java.main.connection.player.*;
import mmeent.java.main.connection.board.Board;
import mmeent.java.main.connection.render.Renderer;
import mmeent.java.main.connection.render.TextBoardRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthias on 20/12/2014.
 */
public class Game extends Thread{
    private Board board;
    private Map<Byte, Player> players;
    private int playerAmount;
    private int turn = 0;
    private List<Player> spectators = new ArrayList<Player>();
    private Renderer renderer;

    /**
     * Constructor of <code>Game</code>
     * @param players Array of players that will join the <code>Game</code>
     */
    public Game(Player[] players){
        this(players, (short) 7, (short) 6);
    }

    /**
     * Constructor of <code>Game</code> with custom <code>Board</code> size
     * @param players Array Players that will join the <code>Game</code>
     * @param width Width of the board
     * @param height Height of the board
     */
    public Game(Player[] players, short width, short height){
        this(players, width, height, (short) 4);
    }

    /**
     * Constructor of <code>Game</code> with custom <code>Board</code> size en custom row length
     * @param players Array of Players that will join the game
     * @param width WIdth of the <code>Board</code>
     * @param height Height of the <code>Board</code>
     * @param length Length of the row needed to win the <code>Game</code>
     */
    public Game(Player[] players, short width, short height, short length){
        this(players, new Board(width, height, length));
    }

    /**
     * Constructor of <code>Game</code> with a <code>Board</code> as parameter
     * @param players Array of players that will join the <code>Game</code>
     * @param board The <code>Board</code> on which the <code>Game</code> will be played
     */
    public Game(Player[] players, Board board){
        this.board = board.deepCopy();
        this.renderer = new TextBoardRenderer(this.board);
        this.players = new HashMap<Byte, Player>();
        this.playerAmount = players.length;
        byte i = 0;
        for(Player player: players){
            player.setId(++i);
            player.setGame(this);
            this.players.put(i, player);
        }
    }

    /**
     * Functions that returns the <code>Board</code> of this <code>Game</code>
     * @return Return the <code>Board</code> of this <code>Game</code>
     */
    public Board getBoard() {
        return this.board;
    }

    public Map<Byte,Player> getPlayers() {
        return players;
    }

    /**
     * Function that starts the <code>Game</code.>
     */
    public void play() {
        int turn = 0;
        boolean a = true;
        while(a) {
            this.renderer.render();
            this.players.get((byte) (turn % this.playerAmount + 1)).getMove(turn++).makeMove();
            a = a && !this.getBoard().hasWinner();
        }
        this.renderer.render();
        System.out.println("The winner of the game is: " + players.get(this.board.getWinner()).getName());
    }

    public void run(){
        this.play();
    }

    public static void main(String[] args) {
        Player player1 = new LocalPlayer("Henk", (byte) 1);
        Player player2 = new ComputerPlayerSmart("Sjaak", (byte) 2);
        Player[] players = {player1,player2};
        Game game = new Game(players);
        game.play();
    }

    public int getTurn(){
        return this.turn;
    }

    public void move(Move move) throws ConnectFourException{
        if(!move.isValid()) throw new ConnectFourException("You have to be the active player");
        move.makeMove();
    }

    public Player getActivePlayer(){
        return this.players.get((byte) (this.turn % this.playerAmount));
    }
}
