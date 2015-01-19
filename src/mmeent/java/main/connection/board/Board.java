package mmeent.java.main.connection.board;

/**
 * Created by Matthias on 20/12/2014.
 */
public class Board {
    public static final short STANDARD_WIDTH = 7;
    public static final short STANDARD_HEIGHT = 6;
    public static final int STANDARD_LENGTH = 4;
    private final short width;
    private final short height;

    private byte[] fields;
    private int[] heights;

    private boolean hasFour = false;

    private final int rowLength;

    public Board(short width, short height, int rowLength){
        this.width = width;
        this.height = height;

        this.fields = new byte[width * height];

        this.heights = new int[width];
        this.rowLength = rowLength;
    }

    public Board(short width, short height){
        this(width, height, STANDARD_LENGTH);
    }

    public Board(){
        this(STANDARD_WIDTH, STANDARD_HEIGHT, STANDARD_LENGTH);
    }

    public short getWidth(){
        return this.width;
    }

    public short getHeight(){
        return this.height;
    }

    public byte getField(int i){
        return this.isField(i) ? this.fields[i] : 0;
    }

    public byte getField(int x, int y){
        return this.isField(x, y) ? this.fields[x + y * this.width] : 0;
    }

    public int getIndex(int x, int y) {
        return x + (y) * this.width;
    }

    public boolean rowIsFull(int column) {
        if(this.heights[column] == this.height) {
            return true;
        }
        return false;
    }

    public boolean isField(int x, int y){
        return 0 <= x && x < this.width && 0 <= y && y < this.height;
    }

    public boolean isField(int i){
        return 0 <= i && i < this.fields.length;
    }

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

    public byte[] getFields(){
        return this.fields;
    }

    public boolean move(short x, byte player){
        if(this.heights[x] >= this.height) return false;
        int y = this.heights[x];
        this.fields[x + (y * this.width)] = player;
        this.heights[x] += 1;
        this.checkAround(x, y, player);
        return true;
    }

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

    public boolean checkAround(int x, int y, byte player){
        int[] towin = {this.rowLength,this.rowLength,this.rowLength,this.rowLength};
        for(int i = -this.rowLength + 1; i < this.rowLength; i++){
            towin[0] = this.getField(x + i,y) == player || towin[0] <= 0 ? --towin[0] : this.rowLength;
            towin[1] = this.getField(x + i, y + i) == player || towin[0] <= 0 ? --towin[1] : this.rowLength;
            towin[2] = this.getField(x, y + i) == player || towin[0] <= 0 ? --towin[2] : this.rowLength;
            towin[3] = this.getField(x - i, y + i) == player || towin[0] <= 0 ? --towin[3] : this.rowLength;
        }
        for(int i: towin){
            if(i < 0) {
                return true;
            }
        }
        return false;
    }

    public Board deepCopy(){
        Board board = new Board(this.width, this.height, this.rowLength);
        for (int i = 0; i < this.fields.length; i++) {
            board.move(i, this.fields[i]);
        }
        return board;
    }

    public void move(int i, byte val){
        if(isField(i)) this.fields[i] = val;
    }
}
