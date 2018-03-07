package server;

import utils.ChatUser;
import utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

class TCPClientHandler extends Thread {

    private final HashSet<ChatUser> chatUsers;
    private final Socket clientSocket;

    private ChatUser currentUser;

    public TCPClientHandler(HashSet<ChatUser> chatUsers, Socket clientSocket) {
        this.chatUsers = chatUsers;
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            String clientName;
            BufferedReader in;
            PrintWriter out;

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {
                out.println(Utils.requestNameHeader);
                clientName = in.readLine();
                if (clientName == null || clientName.equals("")) {
                    continue;
                }
                synchronized (chatUsers) {

                    currentUser = new ChatUser(clientName, out, clientSocket.getInetAddress());

                    if (!chatUsers.contains(currentUser)){
                        chatUsers.add(currentUser);
                        System.out.println("User " + clientName + " joined the chat");
                        break;
                    }
                }
                out.println(Utils.messageHeader);
                out.println("Nickname not valid");
            }

            out.println(Utils.acceptNameHeader);

            while (true) {
                String input = in.readLine();
                if (input == null) {
                    continue;
                }
                for(ChatUser chatUser: chatUsers){
                    if(!chatUser.equals(currentUser)){
                        chatUser.getTcpWriter().println(Utils.messageHeader);
                        chatUser.getTcpWriter().println(clientName + ": " + input);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (currentUser != null) {
                chatUsers.remove(currentUser);
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}