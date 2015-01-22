package mmeent.java.main.connection.render;

import mmeent.java.main.connection.board.Board;

/**
 * Created by Matthias on 21/12/2014.
 */
public class TextBoardRenderer implements Renderer {
    private Board board;
    private int screen_width;
    private int screen_height;

    public TextBoardRenderer(Board board){
        this.board = board;
        this.screen_width = board.getWidth();
        this.screen_height = board.getHeight();
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public void addChatMessage(String msg) {
        System.out.println(msg);
    }

    @Override
    public void render() {
        String s = "";
        byte[] fields = this.board.getFields();
        for (int i = this.screen_height - 1; i >= 0; i--) {
            String row = "| ";
            for (int j = 0; j < this.screen_width; j ++) {
                row += fields[j + i*screen_width];
                row += " | ";
            }
            s += row + "\n";
        }

        System.out.println(s);
    }

    @Override
    public void addErrorMessage(String id, String msg) {
        System.out.println("Error at " + id + " : " + msg);
    }

    @Override
    public void addMessage(String msg){
        System.out.println(msg);
    }

    public static void main(String[] args) {
        Board board = new Board();
        TextBoardRenderer renderer = new TextBoardRenderer(board);
        renderer.render();
    }
}
