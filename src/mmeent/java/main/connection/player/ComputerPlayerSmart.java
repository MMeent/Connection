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
    private static final int searchDepth = 5;

    public ComputerPlayerSmart(String name, byte id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public Move getMove(int turn) {
        Board boardCopy = this.getGame().getBoard().deepCopy();
        List<Byte> players = new ArrayList<Byte>(game.getPlayers().keySet());
        List<Integer> rows = this.getGame().getBoard().availableRows();
        Map<Integer,Map.Entry<Integer,Integer>> options = new HashMap<Integer,Map.Entry<Integer,Integer>>();
        for(int i = 0; i < rows.size(); i ++) {
            options.put(i,determineMove(players, turn, boardCopy, searchDepth));
        }

        int bestOption = 0;
    }

    public Map.Entry<Integer,Integer> determineMove(List<Byte> players, int turn,Board board, int depth) {
        Board boardCopy = board.deepCopy();
        for(int i = 0; i < board.getWidth(); i ++) {
            Move move = new Move(players.get(turn % players.size()), (short) i, turn, boardCopy);
            if(move.isValid()) move.makeMove();
            if(board.hasWinner()){
                if(board.getWinner() == players.get(turn % players.size())) return new HashSet<Integer, Integer>();
                return new HashSet<Integer, Integer>(0, 1);
            }
        }
    }

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
