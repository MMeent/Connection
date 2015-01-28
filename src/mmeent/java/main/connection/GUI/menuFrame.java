package mmeent.java.main.connection.GUI;

import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces

// An AWT program inherits from the top-level container java.awt.Frame
public class MenuFrame extends Frame implements ActionListener, WindowListener {
    private TextField tfName; // Declare component TextField
    private Button btnMultiPlayer;   // Declare component Button
    private Button btnSinglePlayer;   // Declare component Button
    private int count = 0;     // Counter's value

    /** Constructor to setup GUI components and event handling */
    public MenuFrame() {

        setLayout(new FlowLayout());
        // "super" Frame sets its layout to FlowLayout, which arranges the components
        //  from left-to-right, and flow to next row from top-to-bottom.

        btnMultiPlayer = new Button("Multi player");   // construct Button
        add(btnMultiPlayer);                    // "super" Frame adds Button

        btnSinglePlayer = new Button("Single player");   // construct Button
        add(btnSinglePlayer);                    // "super" Frame adds Button

        tfName = new TextField("insert name", 20); // construct TextField
        tfName.setEditable(true);       // set to read-only
        add(tfName);                     // "super" Frame adds tfCount

        addWindowListener(this);

        btnSinglePlayer.addActionListener(this);

        setTitle("Connect-four");  // "super" Frame sets title
        setSize(250, 100);        // "super" Frame sets initial window size

        setVisible(true);
    }

    /** The entry main() method */
    public static void main(String[] args) {
        // Invoke the constructor to setup the GUI, by allocating an instance
        MenuFrame app = new MenuFrame();
    }

    /** ActionEvent handler - Called back upon button-click. */
    @Override
    public void actionPerformed(ActionEvent evt) {
        BoardFrame.open();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}