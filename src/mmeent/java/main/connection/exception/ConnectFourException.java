package mmeent.java.main.connection.exception;

/**
 * Created by Matthias on 19/01/2015.
 * @author mmeent
 *
 * The general self-defined exception in our programme
 */
public class ConnectFourException extends Exception {
    public ConnectFourException(String errMsg) {
        super(errMsg);
    }

    public ConnectFourException() {
        this("General 4Connect Exception");
    }
}
