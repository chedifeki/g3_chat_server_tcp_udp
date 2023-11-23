package multicast.app.reciver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDPMulticastClient {
    public static void main(String[] args) {
        MulticastSocket multicastSocket;
        try {
            multicastSocket = new MulticastSocket(4321);
            multicastSocket.joinGroup(InetAddress.getByName("230.0.0.0"));

            ReaderRep readerRep = new ReaderRep(multicastSocket);
            Writer writer = new Writer(multicastSocket, readerRep);

            Thread readerThread = new Thread(readerRep);
            Thread writerThread = new Thread(writer);

            readerThread.start();
            writerThread.start();

            try {
                readerThread.join();
                writerThread.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("UDPMulticastClient exiting.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}