package multicast.app.receiver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class UDPMulticastClient {
    MulticastSocket ms ;
    Thread reader, writer;
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
            ms.joinGroup(GROUP);

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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            reader.interrupt();
            writer.interrupt();
            try{
                reader.join();
                writer.join();
                System.out.println("Threads are interrupted");
            }catch (Exception e){
                //
            }
            ms.close();
        }

    public static void main(String[] args) {
            UDPMulticastClient client = new UDPMulticastClient();
        }
    }




