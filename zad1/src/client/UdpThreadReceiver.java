package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpThreadReceiver extends Thread {
    private final DatagramSocket datagramSocket;
    private byte[] recvBuffer = new byte[1024];

    public UdpThreadReceiver(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run() {

        try {
            while(!datagramSocket.isClosed()) {
                DatagramPacket recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
                datagramSocket.receive(recvPacket);
                String msg = new String(recvPacket.getData());
                System.out.println("UDP message: " + msg);
                System.out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (datagramSocket != null) datagramSocket.close();
        }
    }
}
