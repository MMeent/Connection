package mmeent.java.main.connection.player;

import mmeent.java.main.connection.ConnectClient;
import mmeent.java.main.connection.ConnectServer;
import mmeent.java.main.connection.board.Board;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;

import java.util.*;

/**
 * Created by Lars on 21-1-2015.
 */
public class ComputerPlayerSmart implements Player {
    private String name;
    private byte id;
    private Game game;
    private static final int searchDepth = 13;
    private static final int outputDepth = 3;

    /**
     * Default constructor for ComputerPlayerSmart
     * @param name the name of the computerplayer
     * @param id the id it has to play with
     */
    public ComputerPlayerSmart(String name, byte id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Get the move to play from the computerplayer
     * @param turn the turn they are in now
     * @return the move the computerplayer is doing
     */
    @Override
    public Move getMove(int turn) {
        Board boardCopy = this.getGame().getBoard().deepCopy();
        List<Byte> players = new ArrayList<Byte>(game.getPlayers().keySet());
        List<Short> rows = this.getGame().getBoard().availableCols();

        int best = Integer.MIN_VALUE;
        short v = 0;
        System.out.println(rows.size());
        for(short row: rows) {
            Board b = boardCopy.deepCopy();
            Move move = new Move(players.get(turn % players.size()), row, turn, b);
            move.makeMove();
            int score = -negamax(move, 0, 1, this.game.getPlayers().values().toArray(new Player[this.game.getPlayers().size()]), this.game.getTurn() + 1);
            System.out.println("Score: " + score +  " for col " + row);
            if(score >= best){
                best = score;
                v = row;
            }
        }

        Move move = new Move(this.game.getPlayers().get(this.id), v,  turn);
        return move.isValid() ? move : null;
    }

    /**
     * Negamax the move, up do depth searchdepth;
     *
     * @param move the last move made
     * @param depth the depth it has searched
     * @param color the color the computerplayer is playing with
     * @param ps an array with players
     * @param turn the turn it is playing
     * @return the value of the node
     */
    public int negamax(Move move, int depth, int color, Player[] ps, int turn){
        Board board = move.getBoard();
        if(depth >= searchDepth || board.hasWinner()){
            return color * move.getValue(ps[turn % ps.length].getId());
        }
        Board bc = board.deepCopy();
        List<Short> cols = board.availableCols();
        if(cols.size() == 0) return color * move.getValue(ps[turn % ps.length].getId());
        int b = Integer.MIN_VALUE;
        ++depth;
        for(short i: cols){
            Move m = new Move(ps[turn % ps.length].getId(), i, turn, bc);
            if(!m.isValid()) continue;
            m.makeMove();
            int val = -negamax(m, depth, -color, ps, turn + 1);
            if(val >= b) b = val;
        }
        if(depth <= outputDepth) System.out.println("R: " + b + " at depth " + depth);
        return b;
    }
/*
    public int determineMove(List<Byte> players, int turn, Board board, int depth) {
        Board boardCopy = board.deepCopy();
        int in = 0;

        for(int i = 0; i < board.getWidth(); i ++) {
            Move move = new Move(players.get(turn % players.size()), (short) i, turn, boardCopy);
            if(!move.isValid()) continue;
            move.makeMove();
            if(boardCopy.hasWinner() || depth == 0){

            } else if(depth > 0){
                in += determineMove(players, ++turn, boardCopy, --depth);
            }
            boardCopy = board.deepCopy();
        }
        return -in;
    }
*/

    /**
     * Get the name of the computerplayer
     * @return the name of the player
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the ID of the player
     * @return the id of the player
     */
    @Override
    public byte getId() {
        return id;
    }

    /**
     * set the ID of the player to <code>id</code>
     * @param id the id that is being set
     */
    @Override
    public void setId(byte id) {
        this.id = id;
    }

    /**
     * Get the game the computerplayer is currently playing
     * @return the game the player is playing
     */
    @Override
    public Game getGame() {
        return game;
    }

    /**
     * Set the game of the player to <code>game</code>
     * @param game the game that the player will be playing
     */
    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Get the connection of this player
     * @return the connection
     */
    @Override
    public Connection getConnection(){
        if(ConnectServer.isServer) return ConnectServer.server.getConnection(this);
        if(ConnectClient.isClient) return ConnectClient.connection;
        return null;
    }
}
