package mmeent.java.main.connection.render;

import mmeent.java.main.connection.board.Board;

/**
 * Created by Matthias on 21/12/2014.
 */
public class BoardRenderer {
    private Board board;
    private int screen_width;
    private int screen_height;

    public BoardRenderer(Board board, int screen_width, int screen_height){
        this.board = board;
        this.screen_width = screen_width;
        this.screen_height = screen_height;
    }
}
