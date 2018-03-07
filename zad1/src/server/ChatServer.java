package server;

import utils.ChatUser;
import utils.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;


public class ChatServer {

    private HashSet<ChatUser> chatUsers = new HashSet<>();

    public void runServer() throws IOException {
        System.out.println("The chat server is running.");
        Thread udp = new Thread(new UDPHandler(chatUsers));
        udp.start();
        try (ServerSocket listener = new ServerSocket(Utils.PORT)) {
            while (true) {
                new TCPClientHandler(chatUsers, listener.accept()).start();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new ChatServer().runServer();
    }
}