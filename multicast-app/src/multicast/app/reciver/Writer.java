package multicast.app.reciver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Writer implements Runnable {
    private MulticastSocket multicastSocket;
    private ReaderRep readerRep;


    public Writer(MulticastSocket multicastSocket, ReaderRep readerRep) {
        this.multicastSocket = multicastSocket;
        this.readerRep = readerRep;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client is Listning.");

            while (true) {
                if (readerRep.isExitRequested()) {
                    System.out.println("Leaving the multicast group.");
                    multicastSocket.leaveGroup(InetAddress.getByName("230.0.0.0"));
                    multicastSocket.close();
                    break;
                }
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                System.out.println("[Multicast UDP message received]>> " + msg);

            }

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
