package multicast.app.sender;

import java.io.IOException;
import java.net.DatagramPacket;

import java.net.MulticastSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class UDPMulticastServer {
    public static void sendUDPMessage(String message, String ipAddress, int port) throws IOException {

        try (MulticastSocket socket = new MulticastSocket(port)) {
            InetAddress group = InetAddress.getByName(ipAddress);
            byte[] msg = message.getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, group, port);
            socket.send(packet);
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        boolean b = true;
        Scanner scanner = new Scanner(System.in);
        try (MulticastSocket multicastSocket = new MulticastSocket(4321)) {
            while (b) {
                System.out.print("Lire un message au clavier : ");
                String message = scanner.nextLine();

                if (message.equals("exit")) {
                    b = false;

                    System.out.println("Exiting");
                } else {
                    sendUDPMessage(message, "230.0.0.0", 4321);
                }
            }
        }
    }
}