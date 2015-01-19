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
        for (int i = 0; i < screen_height; i ++) {
            String row = "| ";
            for (int j = 0; j < screen_width; j ++) {
                row += board.getField(i,j);
                if(j - 2 < screen_width) {
                    row += " | ";
                }
            }
            s += row + "\n";
        }
        System.out.println(s);
    }

    @Override
    public void addErrorMessage(String msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) {
        Board board = new Board();
        TextBoardRenderer renderer = new TextBoardRenderer(board);
        renderer.render();
    }
}
