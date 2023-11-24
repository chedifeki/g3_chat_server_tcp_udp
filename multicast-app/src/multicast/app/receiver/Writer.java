package multicast.app.receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;

public class Writer extends Thread {
    private MulticastSocket multicastSocket;



    public Writer( MulticastSocket multicastSocket) {
        this.multicastSocket = multicastSocket;

    }

    public void closeSocket(){
        multicastSocket.close();
    }
    @Override
    public void run() {
        try {

            byte[] buffer = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (!Thread.interrupted()) {
                multicastSocket.receive(packet);
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                System.out.println("[Multicast UDP message received]>> " + msg);
            }

        } catch (SocketException e){
            //
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
