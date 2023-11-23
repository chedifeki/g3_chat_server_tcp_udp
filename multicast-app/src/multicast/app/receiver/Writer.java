package multicast.app.receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class Writer extends Thread {
    private MulticastSocket multicastSocket;



    public Writer( MulticastSocket multicastSocket) {
        this.multicastSocket = multicastSocket;

    }

    @Override
    public void run() {
        try {
            System.out.println("Client is Listening.");
            byte[] buffer = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("packet = " + packet);

            while (true) {
                multicastSocket.receive(packet);
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                System.out.println("[Multicast UDP message received]>> " + msg);
            }

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
