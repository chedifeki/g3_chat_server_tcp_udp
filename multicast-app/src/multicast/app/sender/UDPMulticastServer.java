package multicast.app.sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class UDPMulticastServer {
    public static void sendUDPMessage(String message, String ipAddress, int port) throws IOException {

        try (DatagramSocket socket = new DatagramSocket(port)) {
            InetAddress group = InetAddress.getByName(ipAddress);
            byte[] msg = message.getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, group, 4321);
            socket.send(packet);
        }
    }

    public static void main(String[] args) throws IOException {
        boolean b = true;
        Scanner scanner = new Scanner(System.in);
            while (b) {
                System.out.print("Lire un message au clavier : ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    b = false;

                    System.out.println("Exiting");
                } else {
                    System.out.println("sending message");
                    sendUDPMessage(message, "230.0.113.1", 8881);
                }
            }
    }
}