package mmeent.java.main.connection.render;

import mmeent.java.main.connection.board.Board;

/**
 * Created by Matthias on 21/12/2014.
 * @author mmeent
 */
public class TextBoardRenderer implements Renderer {
    private Board board;
    private int screenWidth = 0;
    private int screenHeight = 0;

    /**
     * Default constructor for <code>TextBoardRenderer</code>.
     * @param argBoard the board it has to render
     */
    public TextBoardRenderer(Board argBoard) {
        this.board = argBoard;
        if (argBoard != null) {
            this.screenWidth = board.getWidth();
            this.screenHeight = board.getHeight();
        }
    }


    /**
     * Set the board it has to render to board.
     * @param argBoard the board it has to render now
     */
    @Override
    public void setBoard(Board argBoard) {
        this.board = argBoard;
        this.screenWidth = this.board.getWidth();
        this.screenHeight = this.board.getHeight();
    }

    /**
     * Add a chat message to the renderer.
     * @param msg the message to add
     */
    @Override
    public void addChatMessage(String msg) {
        System.out.println(msg);
    }

    /**
     * Render the board.
     */
    @Override
    public void render() {
        String s = "";
        byte[] fields = this.board.getFields();
        for (int i = this.screenHeight - 1; i >= 0; i--) {
            String row = "| ";
            for (int j = 0; j < this.screenWidth; j++) {
                row += fields[j + i * screenWidth];
                row += " | ";
            }
            s += row + "\n";
        }
        System.out.println(s);
    }

    /**
     * Add an error message to the TUI.
     * @param prefix the prefix of the failing packet
     * @param msg the message sent with the error
     */
    @Override
    public void addErrorMessage(String prefix, String msg) {
        System.out.println("Error at " + prefix + " : " + msg);
    }

    /**
     * Add a message to the TUI.
     * @param msg the message to add
     */
    @Override
    public void addMessage(String msg) {
        System.out.println(msg);
    }

    public void addMessage(String msg, int color) {
        System.out.println(msg);
    }
    /**
     * A way to run this class.
     * @param args does nothing
     */
    public static void main(String[] args) {
        Board board = new Board();
        TextBoardRenderer renderer = new TextBoardRenderer(board);
        renderer.render();
    }
}
