package mmeent.java.main.connection.game;

import mmeent.java.main.connection.ConnectServer;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.connection.server.ServerPacket;
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
public class Game {
    /*@
        public_invariant PLAYER_MAP.length >= 2;
        public_invariant playerAmount >= 2;
        public invariant turn >= 0;
     */
    private Board board;
    private Map<Byte, Player> players;
    private int playerAmount;
    private int turn = 0;
    private List<Player> spectators = new ArrayList<Player>();
    private Renderer renderer;

    /**
     * Constructor of <code>Game</code>
     * @param argPlayers Array of PLAYER_MAP that will join the <code>Game</code>
     */
    /*@
        requires PLAYER_MAP.length == 2;
        ensures this.PLAYER_MAP == argPlayers && board.getWidth() == 7 && board.getHeight() == 6;
     */
    public Game(Player[] argPlayers) {
        this(argPlayers, (short) 7, (short) 6);
    }

    /**
     * Constructor of <code>Game</code> with custom <code>Board</code> size
     * @param argPlayers Array Players that will join the <code>Game</code>
     * @param argWidth Width of the board
     * @param argHeight Height of the board
     */
     /*@
        requires PLAYER_MAP.length == 2;
        ensures this.PLAYER_MAP == argPlayers && board.getWidth() == argWidth
        && board.getHeight() == argHeight;
     */
    public Game(Player[] argPlayers, short argWidth, short argHeight) {
        this(argPlayers, argWidth, argHeight, (short) 4);
    }

    /**
     * Constructor of <code>Game</code> with custom <code>Board</code> size en custom row length
     * @param argPlayers Array of Players that will join the game
     * @param argWidth WIdth of the <code>Board</code>
     * @param argHeight Height of the <code>Board</code>
     * @param argLength Length of the row needed to win the <code>Game</code>
     */
     /*@
        requires PLAYER_MAP.length == 2;
        ensures PLAYER_MAP == PLAYER_MAP &&
        board.getWidth()==argWidth && board.getHeight() == argHeight;
     */
    public Game(Player[] argPlayers, short argWidth, short argHeight, short argLength) {
        this(argPlayers, new Board(argWidth, argHeight, argLength));
    }

    /**
     * Constructor of <code>Game</code> with a <code>Board</code> as parameter
     * @param argPlayers Array of PLAYER_MAP that will join the <code>Game</code>
     * @param argBoard The <code>Board</code> on which the <code>Game</code> will be played
     */
     /*@
        requires PLAYER_MAP.length == 2;
        ensures PLAYER_MAP == PLAYER_MAP && board.getWidth()==width && board.getHeight() == height;
     */
    public Game(Player[] argPlayers, Board argBoard) {
        this.board = argBoard.deepCopy();
        this.renderer = new TextBoardRenderer(this.board);
        this.players = new HashMap<Byte, Player>();
        this.playerAmount = argPlayers.length;
        byte i = 0;
        for (Player player: argPlayers) {
            player.setId(++i);
            player.setGame(this);
            this.players.put(i, player);
        }
    }

    /**
     * Function that returns the <code>Board</code> of this <code>Game</code>
     * @return Return the <code>Board</code> of this <code>Game</code>
     */
    /*@
        ensures \result = this.board;
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Function that returns a map containg all the <code>Player</code>
     * @return Returns a Map with the <code>Player</code>
     */
    /*@
        ensures \result == this.PLAYER_MAP;
     */
    public Map<Byte, Player> getPlayers() {
        return players;
    }

    public void setBoard(Board argBoard) {
        this.board = argBoard;
    }

    /**
     * Function that starts the game loop.
     */
    /*@
        ensures turn >= 0;
        ensures a == this.getBoard().hasWinner();
     */
    public void play() {
        turn = 0;
        boolean a = true;
        while (a) {
            this.renderer.render();
            this.players.get((byte) (turn % this.playerAmount + 1)).getMove(turn++).makeMove();
            a = a && !this.getBoard().hasWinner();
        }
        this.renderer.render();
        System.out.println("The winner of the game is: "
                + players.get(this.board.getWinner()).getName());
    }
    
    public static void main(String[] args) {
        Player player1 = new LocalPlayer("Henk", (byte) 1);
        Player player2 = new ComputerPlayerSmart("Sjaak", (byte) 2);
        Player[] players = {player1, player2};
        Game game = new Game(players);
        game.play();
    }

    public int getTurn() {
        return this.turn;
    }

    /**
     * Make a move.
     * @param move the move to be made
     * @throws ConnectFourException throws exception if
     * it is not the PLAYER_MAP turn or the move is not valid.
     */
    public void move(Move move) throws ConnectFourException {
        if (!move.isValid()) {
            throw new ConnectFourException("You have to be the active player");
        } else {
            move.makeMove();
            if (ConnectServer.isServer) {
                Packet packet = new ServerPacket.MoveOkPacket(null, move.getSymbol(), 
                        move.getColumn(), move.getPlayer());
                for (Player p: this.getPlayers().values()) {
                    p.getConnection().send(packet);
                }
                this.turn++;
                this.getActivePlayer().getConnection().send(
                        new ServerPacket.RequestMovePacket(null));
            }
        }
    }

    public Player getActivePlayer() {
        return this.players.get((byte) (this.turn % this.playerAmount + 1));
    }
}
