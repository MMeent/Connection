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

    private Player player = null;
    private byte symbol;

    /**
     * General move constructor
     * @param player the player that will make a move
     * @param column the column the move will be made in
     * @param turn the turn the move is made in
     */
    public Move(Player player, short column, int turn){
        this.column = column;
        this.turn = turn;
        this.player = player;
        this.symbol = player.getId();
        this.board = player.getGame().getBoard();
    }

    /**
     * Move constructor without using a player object
     * @param id the id of the player
     * @param column the column the move is made in
     * @param turn the turn the move is made in
     * @param board the board the move is made on
     */
    public Move(byte id, short column, int turn, Board board){
        this.column = column;
        this.turn = turn;
        this.symbol = id;
        this.board = board;
    }

    /**
     * get the symbol (id) of the played move.
     * @return the id of the player that played the move
     */
    /*@
        ensures \result = this.symbol;
     */
    public byte getSymbol(){
        return this.symbol;
    }

    /**
     * Get the player corresponding to the move
     * @return the player
     */
    /*@
        ensures \result == this.player;
     */
    public Player getPlayer(){
        return this.player;
    }

    /**
     * Get the number of the turn the move is made in
     * @return the number of the turn
     */
    /*@
        ensures \result == this.turn;
     */
    public int getTurn(){
        return this.turn;
    }

    /**
     * get the column number the move is made in
     * @return the column number
     */
    /*@
        ensures \result == this.column;
     */
    public short getColumn(){
        return this.column;
    }

    /**
     * Check whether the move is valid or not
     * @return true if the move is valid
     */
    /*@
        ensures \result == (this.column >= 0 && this.column < this.board.getWidth() && !this.board.colIsFull(this.column));
     */
    public boolean isValid() {
        return (this.column >= 0 && this.column < this.board.getWidth() && !this.board.colIsFull(this.column) && !this.board.hasWinner() && !this.board.colIsFull(this.column));
    }

    /**
     * Make the move on the board
     */
    /*@
        ensures board.getField(column,board.getColumnHeight(column) - 1) == this.symbol;
     */
    public void makeMove() {
        board.move(column, this.symbol);
    }

    /**
     * Get the value of the move, given ID id
     * @param id the id to calculate the value of the move with
     * @return the value of the move
     */
    /*@

     */
    public int getValue(byte id){
        Board b = this.board.deepCopy();
        int[] posValues = {0, 0, 0, 0};
        int[] negValues = {0, 0, 0, 0};
        int actValue = 0;
        for(int x = 0; x < b.getWidth() - 4; x++){
            for(int y = 0; y < b.getHeight() - 4; y++){
                for(int i = 0; i < 4; i++){
                    posValues[0] = board.getField(x + i, y) == 1 || board.getField(x + i, y) == 0  ? ++posValues[0] : 0;
                    posValues[1] = board.getField(x + i, y + 1) == 1 || board.getField(x + i, y + 1) == 0  ? ++posValues[1] : 0;
                    posValues[2] = board.getField(x, y + 1) == 1 || board.getField(x, y + 1) == 0  ? ++posValues[2] : 0;
                    posValues[3] = board.getField(x + i, y - i) == 1 || board.getField(x + i, y - i) == 0  ? ++posValues[3] : 0;

                    negValues[0] = board.getField(x + i, y) == 2 || board.getField(x + i, y) == 0  ? ++negValues[0] : 0;
                    negValues[1] = board.getField(x + i, y + i) == 2 || board.getField(x + i, y + i) == 0  ? ++negValues[1] : 0;
                    negValues[2] = board.getField(x, y + i) == 2 || board.getField(x, y + i) == 0  ? ++negValues[2] : 0;
                    negValues[3] = board.getField(x + i, y - i) == 2 || board.getField(x + i, y - i) == 0  ? ++negValues[3] : 0;
                }
                for(int i = 0; i < posValues.length; i++){
                    actValue += negValues[i] == 0 ? Math.pow(2, posValues[i]) : 0;
                    actValue -= posValues[i] == 0 ? Math.pow(2, negValues[i]) : 0;
                }
            }
        }
        return (id == 1) ? actValue : -actValue;
    }

    /**
     * Get the <code>Board</code> of the move
     * @return the board
     */
    public Board getBoard(){
        return this.board;
    }
}
