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

    public ComputerPlayerSmart(String name, byte id) {
        this.name = name;
        this.id = id;
    }

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
    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte getId() {
        return id;
    }

    @Override
    public void setId(byte id) {
        this.id = id;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public Connection getConnection(){
        if(ConnectServer.isServer) return ConnectServer.server.getConnection(this);
        if(ConnectClient.isClient) return ConnectClient.connection;
        return null;
    }
}
