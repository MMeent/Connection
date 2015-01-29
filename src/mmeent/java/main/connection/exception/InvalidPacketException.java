package mmeent.java.main.connection.exception;

/**
 * Created by Matthias on 26/01/2015.
 * @author mmeent
 *
 * The exception thrown when a packet is invalid.
 */
public class InvalidPacketException extends ConnectFourException {
    public InvalidPacketException() {
        super("This packet is not valid. This is possibly " +
                "becouse your implementation of the protocol is not done correctly.");
    }

    public InvalidPacketException(String msg) {
        super(msg);
    }
}
