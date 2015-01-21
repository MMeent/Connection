package mmeent.java.main.connection.board;

import mmeent.java.main.connection.game.Move;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Matthias on 20/12/2014.
 */
public class Board {
    public static final short STANDARD_WIDTH = 7;
    public static final short STANDARD_HEIGHT = 6;
    public static final int STANDARD_LENGTH = 4;
    private final short width;
    private final short height;

    private byte winner;

    private byte[] fields;
    private int[] heights;

    private final int rowLength;

    /**
     * Constructor of <code>Board</code> for a <code>Board</code> with custom dimensions and custom row length.
     * @param width Width of the <code>Board</code>.
     * @param height Height of the <code>Board</code>.
     * @param rowLength Length of the continuous row needed to win.
     */
    public Board(short width, short height, int rowLength){
        this.width = width;
        this.height = height;

        this.fields = new byte[width * height];

        this.heights = new int[width];
        this.rowLength = rowLength;
        this.winner = 0;
    }

    /**
     * Constructor of <code>Board</code> for a <code>Board</code> with custom dimensions.
     * @param width Width of the <code>Board</code>.
     * @param height of the <code>Board</code>.
     */
    public Board(short width, short height){
        this(width, height, STANDARD_LENGTH);
    }

    /**
     * Constructor of <code>Board</code> for a <code>Board</code> with standard dimensions.
     */
    public Board(){
        this(STANDARD_WIDTH, STANDARD_HEIGHT, STANDARD_LENGTH);
    }

    /**
     * Function that returns the width of the board.
     * @return returns the width of the board.
     */
    public short getWidth(){
        return this.width;
    }

    /**
     * Function that returns the height of the board.
     * @return returns the height of the board.
     */
    public short getHeight(){
        return this.height;
    }

    /**
     * Function that returns the contents of the field corresponding to the given index.
     * @param i index of the field that has to be returned
     * @return The content of the field of which the index is given.
     */
    public byte getField(int i){
        return this.isField(i) ? this.fields[i] : 0;
    }

    /**
     * Functions that returns the contents of the field corresponding to the given coordinates.
     * @param x The column of the field that has to be returned.
     * @param y The row of the field that has to be returned.
     * @return The contents of the field corresponding to the given coordinates.
     */
    public byte getField(int x, int y){
        return this.isField(x, y) ? this.fields[x + y * this.width] : 0;
    }

    /**
     *
     * @param x Column of which the index is requested.
     * @param y Row of which the index is requested.
     * @return The index of the field.
     */
    public int getIndex(int x, int y) {
        return x + y * this.width;
    }

    /**
     * Function that checks whether the given row is full or not.
     * @param column The column that has to be checked whether it is full or not.
     * @return Returns true if the row is full. False if it is not.
     */
    public boolean rowIsFull(int column) {
        if(this.heights[column] == this.height) {
            return true;
        }
        return false;
    }

    /**
     * Function that returns a list containing the indexes of the columns in the <code>Board</code> that are not full.
     * @return Returns a list containing the indexes of the columns in the <code>Board</code> that are not full
     */
    public List<Integer> availableRows() {
        List<Integer> result = new LinkedList<Integer>();
        for(int i = 0; i < width; i  ++) {
            if(!rowIsFull(i)) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Function that checks wheter the given coordinates is a valid location in the <code>Board</code>
     * @param x column that has to be checked
     * @param y row that has to be checked
     * @return Returns true if there is indeed a field on the board with those coordinates. Returns false if it is not.
     */
    public boolean isField(int x, int y){
        return 0 <= x && x < this.width && 0 <= y && y < this.height;
    }

    /**
     * Function that checks whether the given index is a valid field.
     * @param i Index of the field which has to be validated
     * @return True if there is a field at index i. False if there is none.
     */
    public boolean isField(int i){
        return 0 <= i && i < this.fields.length;
    }

    /**
     *
     * @param start_x
     * @param start_y
     * @param dx
     * @param dy
     * @return
     */
    public byte[] getFieldsRange(int start_x, int start_y, int dx, int dy){
        if(dx < 0 || dy < 0) return null;

        byte[] r = new byte[(dx + 1) * (dy + 1)];

        for (int i = 0; i < dx; i++) {
            for(int j = 0; j < dy; j++){
                r[i + j * (dy + 1)] = this.getField(start_x + i, start_y + j);
            }
        }
        return r;
    }

    /**
     * Function that returns the array with the fields of this <Code>Board</Code>.
     * @return Returns an array with the fields on this <code>Board</code>
     */
    public byte[] getFields(){
        return this.fields;
    }

    public void move(Move move) {
        move(move.getColumn(),move.getSymbol());
    }

    /**
     * Function that takes a <code>Move</code> and changes the board accordingly.
     * @param x column where a <code>Move</code> has to be put.
     * @param player The ID of the player
     * @return Returns true if the operation has succeeded.
     */
    public boolean move(short x, byte player){
        if(this.heights[x] >= this.height) return false;
        int y = this.heights[x];
        this.fields[x + (y * this.width)] = player;
        this.heights[x] += 1;
        this.checkAround(x, y, player);
        return true;
    }

    /**
     * Function that checks whether the <code>Board</code> has a winning row of four
     * @param player Player of which it has to be checked whether he has a winning row.
     * @return
     */
    public boolean hasFour(byte player){
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(checkAround(i,j,player)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Function that checks whether the given field is part of a winning row.
     * @param x Column in which the field is.
     * @param y Row in which the field is.
     * @param player Player of which the winning row needs to be found.
     * @return Returns true if there is a winning row of the given player.
     */
    public boolean checkAround(int x, int y, byte player){
        int[] towin = {this.rowLength,this.rowLength,this.rowLength,this.rowLength};
        for(int i = -this.rowLength + 1; i < this.rowLength; i++){
            towin[0] = this.getField(x + i,y) == player || towin[0] <= 0 ? --towin[0] : this.rowLength;
            towin[1] = this.getField(x + i, y + i) == player || towin[1] <= 0 ? --towin[1] : this.rowLength;
            towin[2] = this.getField(x, y + i) == player || towin[2] <= 0 ? --towin[2] : this.rowLength;
            towin[3] = this.getField(x - i, y + i) == player || towin[3] <= 0 ? --towin[3] : this.rowLength;
        }
        System.out.println(player);
        for(int i: towin){
            if(i <= 0) {
                this.winner = player;
                return true;
            }
        }

        return false;
    }

    /**
     * Function that makes a new <code>Board</code> that is a copy of this <code>Board</code>
     * @return Copy of the <code>Board</code>
     */
    public Board deepCopy(){
        Board board = new Board(this.width, this.height, this.rowLength);
        for (int i = 0; i < this.fields.length; i++) {
            board.move(i, this.fields[i]);
        }
        return board;
    }

    /**
     * Function that makes a move in the given column.
     * @param i Column where the move has to be put.
     * @param val Mark of the player that makes the move.
     */
    public void move(int i, byte val){
        if(isField(i)) this.fields[i] = val;
    }

    /**
     * Check whether there is a winner or not
     * @return true if there is a winner;
     */
    public boolean hasWinner(){
        return this.winner != 0;
    }

    /**
     * Check who the winner is
     * @return the id of the player
     */
    public byte getWinner(){
        return this.winner;
    }
}
