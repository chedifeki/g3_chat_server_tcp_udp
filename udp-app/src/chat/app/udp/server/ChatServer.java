package chat.app.udp.server;
import chat.app.udp.client.ClientInfo;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer extends Thread {
    public final static int PORT = 7331;
    private final static int BUFFER = 1024;

    private DatagramSocket socket;
    private HashMap<String, ClientInfo> existingClients;

    public ChatServer() throws IOException {
        socket = new DatagramSocket(PORT);
        existingClients = new HashMap<String, ClientInfo>();
    }

    public void run() {
        byte[] buf = new byte[BUFFER];
        System.out.println("Server started");
        
        while (true) {
            try {
                Arrays.fill(buf, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                existingClients.forEach((key, value) -> {
                    System.out.println(key + " recc " + value);
                });

                String content = new String(buf, 0, packet.getLength());

                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                if (content.startsWith("SIGNUP")) {
                    String name = content.substring(content.indexOf(':') + 1);
                    if (existingClients.containsKey(name)) {
                        byte[] data = ("NOT_AVAILABLE").getBytes();
                        packet = new DatagramPacket(data, data.length, clientAddress, clientPort);
                        socket.send(packet);
                    } else {
                        System.out.println(name + " has joined the room");
                        byte[] data = ("OK").getBytes();
                        packet = new DatagramPacket(data, data.length, clientAddress, clientPort);
                        ClientInfo client = new ClientInfo(clientAddress, clientPort);
                        existingClients.put(name, client);
                        socket.send(packet);

                    }
                } else if (content.startsWith("@")) {
                    String receiverName = content.substring(1, content.indexOf('['));
                    int startIndex = content.indexOf('[');
                    int endIndex = content.indexOf(']');
                    String clientName = content.substring(startIndex + 1, endIndex);
                    String clientMessage = content.substring(endIndex + 1);
                    ClientInfo receiverInfo = existingClients.get(receiverName);
                    if (receiverInfo != null) {
                        InetAddress cl = receiverInfo.getInetAdr();
                        int pr = receiverInfo.getPort();
                        byte[] data = (clientName + " has sent you a private message: " + clientMessage).getBytes();
                        packet = new DatagramPacket(data, data.length, cl, pr);
                        socket.send(packet);
                    }
                } else {
                    int startIndex = content.indexOf('[');
                    int endIndex = content.indexOf(']');

                    if (startIndex != -1 && endIndex != -1) {

                        String clientName = content.substring(startIndex + 1, endIndex);
                        String clientMessage = content.substring(endIndex + 1);

                        System.out.println(clientName + " : " + clientMessage);
                        byte[] data = (clientName + " : " + clientMessage).getBytes();
                        existingClients.forEach((key, value) -> {
                            InetAddress cl = value.getInetAdr();
                            int cp = value.getPort();

                            if (!(clientAddress.equals(cl) && (cp == clientPort))) {
                                try {
                                    socket.send(new DatagramPacket(data, data.length, cl, cp));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                        });
                    }
                }

                if (content.startsWith("EXIT:")) {
                    String exitedUser = content.substring(content.indexOf(':') + 1);
                    existingClients.remove(exitedUser);
                    System.out.println("User " + exitedUser + " has left the room");
                }

            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String args[]) throws Exception {
        ChatServer s = new ChatServer();

        s.start();
    }
}
