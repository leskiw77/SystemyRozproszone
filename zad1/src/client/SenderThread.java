package client;

import utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

import static java.net.InetAddress.getByName;

class SenderThread extends Thread {

    private final String mockedMultimediaMessage = "Mocked Multimedia Message";
    private final String mockedMultimediaMulticastMessage = "Mocked Multimedia Message using Multicast";

    private final BufferedReader consoleReader;
    private final DatagramSocket udpServerSocket;
    private final PrintWriter serverTCPWriter;
    private final MulticastSocket multicastSocket;

    SenderThread(BufferedReader consoleReader, DatagramSocket udpServerSocket, PrintWriter serverTCPWriter, MulticastSocket multicastSocket) {
        this.consoleReader = consoleReader;
        this.udpServerSocket = udpServerSocket;
        this.serverTCPWriter = serverTCPWriter;
        this.multicastSocket = multicastSocket;
    }

    public void run() {
        while (true) {
            String message;
            try {
                message = consoleReader.readLine();

                switch (message){
                    case Utils.udpCommand:
                        System.out.println("UDP message send");

                        //TODO: change to global address
                        InetAddress address = getByName("localhost");

                        byte[] dataToSendUDP = mockedMultimediaMessage.getBytes();
                        DatagramPacket packetToSend = new DatagramPacket(dataToSendUDP, dataToSendUDP.length, address, Utils.PORT);
                        udpServerSocket.send(packetToSend);
                        break;
                    case Utils.multicastCommand:
                        System.out.println("Multicast message send");

                        byte[] dataToSendMulticast = mockedMultimediaMulticastMessage.getBytes();
                        InetAddress group = InetAddress.getByName(Utils.multicastIP);
                        DatagramPacket packet = new DatagramPacket(dataToSendMulticast, dataToSendMulticast.length, group, Utils.multicastPort);
                        multicastSocket.send(packet);
                        break;
                    default:
                        if(!message.isEmpty())
                            serverTCPWriter.println(message);
                }

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}