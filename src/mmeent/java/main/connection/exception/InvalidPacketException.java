package mmeent.java.main.connection.exception;

/**
 * Created by Matthias on 26/01/2015.
 */
public class InvalidPacketException extends ConnectFourException {
    public InvalidPacketException(){
        super("The packet is invalid");
    }

    public InvalidPacketException(String msg){
        super(msg);
    }
}
