package multicast.app.receiver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPMulticastClient {
    MulticastSocket ms ;
    Reader reader;
    Writer writer;
    boolean isConnected = false;
    private static  final InetAddress GROUP;

    static {
        try {
            GROUP = InetAddress.getByName("230.0.113.1");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public UDPMulticastClient() {
        try {
            ms = new MulticastSocket(4321);
            connect();

            reader = new Reader(this);
            writer = new Writer(ms);

            reader.start();
            writer.start();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }
        public void disconnect()  {
            try {
                ms.leaveGroup(GROUP);
                isConnected=false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    public static void main(String[] args) {
            UDPMulticastClient client = new UDPMulticastClient();
        }

    public void exit() {
        reader.interrupt();
        writer.closeSocket();
        writer.interrupt();
        ms.close();

    }

    public void connect() {
        try {
            if (!isConnected) {
                ms.joinGroup(GROUP);
                isConnected=true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}




