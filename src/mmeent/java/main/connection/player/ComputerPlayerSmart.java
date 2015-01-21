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
        Map<Integer, WinLoss> options = new HashMap<Integer, WinLoss>();
        for(int i = 0; i < rows.size(); i++) {
            options.put(i,determineMove(players, turn, boardCopy, searchDepth));
        }
        int best = 0;
        for(int i = 0; i < rows.size(); i++) {
            if(options.get(i).betterThan(options.get(best))) best = i;
        }
        return new Move(this.game.getPlayers().get(this.id), (short) best, turn);
    }

    public WinLoss determineMove(List<Byte> players, int turn, Board board, int depth) {
        Board boardCopy = board.deepCopy();
        WinLoss wl = new WinLoss(0, 0);
        for(int i = 0; i < board.getWidth(); i ++) {
            Move move = new Move(players.get(turn % players.size()), (short) i, turn, boardCopy);
            if(!move.isValid()) continue;
            move.makeMove();
            if(boardCopy.hasWinner()){
                if(boardCopy.getWinner() == players.get(turn % players.size())) wl = wl.add(new WinLoss(1, 0));
                wl = wl.add(new WinLoss(0, 1));
            } else if(depth > 0){
                wl = wl.add(determineMove(players, ++turn, boardCopy, --depth));
            }
            boardCopy = board.deepCopy();
        }
        return wl;
    }

    private class WinLoss{
        public final int win;
        public final int loss;
        public WinLoss(int win, int loss){
            this.win = win;
            this.loss = loss;
        }

        public WinLoss add(WinLoss w){
            return new WinLoss(this.win + w.win, this.loss + w.loss);
        }

        public boolean betterThan(WinLoss w){
            return this.loss <= w.loss && this.win >= w.win;
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
