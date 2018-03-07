package server;

import utils.ChatUser;
import utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;

class UDPHandler extends Thread {

    private final HashSet<ChatUser> chatUsers;

    private DatagramSocket datagramSocket;

    UDPHandler(HashSet<ChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public void run() {
        try {
            datagramSocket = new DatagramSocket(Utils.PORT);
            byte[] buffer = new byte[128];

            while (true) {
                DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(receivedPacket);
                String message = new String(receivedPacket.getData(), receivedPacket.getOffset(), receivedPacket.getLength());
                System.out.println("Received message via UDP connection: " + message);
                byte[] sendBuffer = message.getBytes();

                for(ChatUser chatUser: chatUsers){
                    if(!chatUser.getInetAddress().equals(receivedPacket.getAddress())) {
                        DatagramSocket socket = new DatagramSocket();
                        DatagramPacket sendPacket =
                                new DatagramPacket(sendBuffer, sendBuffer.length, chatUser.getInetAddress(), Utils.PORT);
                        socket.send(sendPacket);
                        socket.close();
                    }
                }
            }
        } catch (IOException e) {
            datagramSocket.close();
            e.printStackTrace();
        } finally {
            datagramSocket.close();
        }
    }
}