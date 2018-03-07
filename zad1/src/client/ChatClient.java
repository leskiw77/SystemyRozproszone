package client;

import utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class ChatClient {

    private void run() throws IOException {

        Socket socket = new Socket("localhost", Utils.PORT);
        PrintWriter serverTCPWriter = new PrintWriter(socket.getOutputStream(), true);

        DatagramSocket udpServerSocket = new DatagramSocket();
        MulticastSocket multicastSocket = new MulticastSocket(Utils.multicastPort);

        BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String line = serverInput.readLine();
            switch (line){
                case Utils.requestNameHeader:
                    enterName(consoleReader, serverTCPWriter);
                    break;
                case Utils.acceptNameHeader:
                    Thread sender = new SenderThread(consoleReader, udpServerSocket, serverTCPWriter, multicastSocket);
                    Thread udpReceiver = new UdpThreadReceiver(udpServerSocket);
                    Thread multicastReceiver = new MulticastThreadReceiver(multicastSocket);

                    udpReceiver.start();
                    multicastReceiver.start();
                    sender.start();
                    break;
                case Utils.messageHeader:
                    line = serverInput.readLine();
                    System.out.println(line);
                    break;
                default:
                    throw new IllegalStateException("Unknown header from server: " + line);
            }
        }
    }

    private void enterName(BufferedReader consoleReader, PrintWriter serverTCPWriter) {
        String myNick = null;
        System.out.print("Please, enter your nickname: ");
        try {
            myNick = consoleReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverTCPWriter.println(myNick);
    }


    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.run();
    }
}