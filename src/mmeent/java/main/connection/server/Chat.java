package mmeent.java.main.connection.server;

import mmeent.java.main.connection.connection.server.ServerPacket;
import mmeent.java.main.connection.player.LocalPlayer;
import mmeent.java.main.connection.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthias on 19/01/2015.
 * @author mmeent;
 *
 * A class that will handle the chat and chatrooms.
 */
public class Chat {
    private Map<String, ChatRoom> rooms = new HashMap<String, ChatRoom>();
    private ChatRoom activeRoom = Chat.Lobby.get;

    /**
     * the Chat object does not need any arguments
     */
    public Chat(){
        this.rooms.put(this.activeRoom.name, this.activeRoom);
    }

    /**
     * Add a message to the chat
     *
     * @param msg The message that is sent to the currently active chatroom
     */
    public void addMessage(String msg){
        String pattern = "#.*?[ ]";
        if(msg.matches(pattern)) {
            this.activeRoom = this.getRoom(msg.split("#(.*?)[ ]")[0]);
            msg = msg.replaceFirst("#.*?[ ]", "");
        }
        this.activeRoom.addMessage(msg);
    }

    public ChatRoom getRoom(String name){
        if(!this.rooms.containsKey(name)) this.rooms.put(name, new ChatRoom(name));
        return this.rooms.get(name);
    }

    /**
     * Add a message to the specified room.
     *
     * @param room the name of the room.
     * @param msg the message sent to the room.
     */
    public void addMessage(String room, String msg){
        this.rooms.get(room).addMessage(msg);
    }

    public static class ChatRoom{
        private List<String> msgs;
        public final String name;
        private List<Player> players;

        /**
         * A chatroom constructor.
         *
         * @param name The name of the chatroom
         */
        public ChatRoom(String name){
            this.name = name;
            this.msgs = new ArrayList<String>();
        }

        /**
         * Clear the chatroom
         */
        public void clearChat(){
            this.msgs.clear();
        }

        /**
         * Add a message to the chatroom
         *
         * @param msg the message sent to the chatroom
         */
        public void addMessage(String msg){
            this.msgs.add(msg);
            ServerPacket.ChatPacket p = new ServerPacket.ChatPacket(null, msg);
            for(Player player: this.players){
                player.getConnection().send(p);
            }
        }
    }

    /**
     * @author mmeent
     *
     * A private chatroom is always nice to have.
     */
    public static class PrivateRoom extends ChatRoom{

        /**
         * Create a private chat with that player.
         *
         * @param player the player you are having a private chat with
         */
        public PrivateRoom(LocalPlayer player){
            super(player.getName());
        }

        /**
         * Add a message to this private chatroom. This is overridden becouse of possible notifications.
         *
         * @param msg the message added
         */
        public void addMessage(String msg){
            super.addMessage(msg);
        }
    }

    /**
     * @author mmeent
     *
     * A simple class to get a static lobby chatroom.
     */
    public static class Lobby{
        public static ChatRoom get = new ChatRoom("Lobby");
    }
}
