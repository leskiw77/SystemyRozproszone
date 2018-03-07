package client;

import utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastThreadReceiver extends Thread {

    private final MulticastSocket multicastSocket;

    public MulticastThreadReceiver(MulticastSocket multicastSocket) {
        this.multicastSocket = multicastSocket;
    }

    public void run() {
        try {
            multicastSocket.joinGroup(InetAddress.getByName(Utils.multicastIP));

            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(new byte[1024], 1024);
                multicastSocket.receive(datagramPacket);
                System.out.println("Received multicast message: " + new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

