package mmeent.java.main.connection.game;

import mmeent.java.main.connection.board.Board;
import mmeent.java.main.connection.player.LocalPlayer;
import mmeent.java.main.connection.player.Player;

/**
 * Created by Matthias on 20/12/2014.
 */
public class Move {
    private short column;
    private int turn;
    private Board board;

    private Player player;
    private byte symbol;

    public Move(Player player, short column, int turn){
        this.column = column;
        this.turn = turn;
        this.player = player;
        this.symbol = player.getId();
        this.board = player.getGame().getBoard();
    }

    public Move(byte id, short column, int turn, Board board){
        this.column = column;
        this.turn = turn;
        this.symbol = id;
        this.board = board;
    }

    public byte getSymbol(){
        return this.symbol;
    }

    public Player getPlayer(){
        return this.player;
    }

    public int getTurn(){
        return this.turn;
    }

    public int getColumn(){
        return this.column;
    }

    public boolean isValid() {
        return (this.column >= 0 && this.column < this.board.getWidth() && !this.board.rowIsFull(this.column));
    }

    public void makeMove() {
        board.move((short) column, player.getId());
    }
}
