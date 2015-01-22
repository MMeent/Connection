package mmeent.java.main.connection.game;

import mmeent.java.main.connection.board.Board;
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

    public short getColumn(){
        return this.column;
    }

    public boolean isValid() {
        return (this.column >= 0 && this.column < this.board.getWidth() && !this.board.colIsFull(this.column));
    }

    public void makeMove() {
        board.move((short) column, this.symbol);
    }

    public int getValue(byte id){
        Board b = this.board.deepCopy();
        int[] values = {0, 0, 0, 0};
        int actValue = 0;
        for(int x = 0; x < b.getWidth() - 4; x++){
            for(int y = 0; y < b.getHeight() - 4; y++){
                for(int i = 0; i < 4; i++){
                    values[0] += board.getField(x + i, y) == id ? 1 : 0;
                    values[1] += board.getField(x + i, y + i) == id ? 1 : 0;
                    values[2] += board.getField(x, y + i) == id ? 1 : 0;
                    values[3] += board.getField(x + i, y + 3 - i) == id ? 1 : 0;
                }
                actValue += values[0]/3 + values[1]/3 + values[2]/3 + values[3]/3;
                values[0] = 0;
                values[1] = 0;
                values[2] = 0;
                values[3] = 0;
            }
        }
        return actValue;
    }

    public Board getBoard(){
        return this.board;
    }
}
