package chat.app.udp.client;

import java.io.*;
import java.net.*;
import java.util.*;


class MessageSender implements Runnable {
    public final static int PORT = 7331;
    private DatagramSocket sock;
    private String hostname;

    MessageSender(DatagramSocket s, String h) {
        sock = s;
        hostname = h;
    }

    private void sendMessage(String s) throws Exception {
        byte buf[] = s.getBytes();
        InetAddress address = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        sock.send(packet);
    }

    private void signUp() {
        System.out.print("Enter your Username : ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        try {
            sendMessage("SIGNUP:" + name);
            ChatClient.name = name;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        do {
            try {
                while (!ChatClient.registred) {
                    signUp();
                    Thread.sleep(1000);
                }

                System.out.println("Start the chat or type 'exit' to leave:");

                String message;
                do {
                    message = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(message)) {
                        sendMessage("EXIT:" + ChatClient.name);
                        // Exit the MessageSender thread
                        return;
                    } else if (message.startsWith("@")) {
                        String receiver = message.substring(0, message.indexOf(' '));
                        message = message.substring(message.indexOf(' '));
                        if (message.length() > 0) {
                            sendMessage(receiver + "[" + ChatClient.name + "]" + message);
                        }
                    } else {
                        sendMessage("[" + ChatClient.name + "]" + message);
                    }
                } while (!"exit".equalsIgnoreCase(message));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (ChatClient.connected);
    }
}

class MessageReceiver implements Runnable {
    DatagramSocket sock;
    byte buf[];

    MessageReceiver(DatagramSocket s) {
        sock = s;
        buf = new byte[1024];
    }

    public void run() {
        while (ChatClient.connected) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                sock.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                if (received.equals("OK")) {
                    ChatClient.registred = true;
                    System.out.println("Welcome " + ChatClient.name + " 🙂");
                } else if (received.equals("NOT_AVAILABLE")) {
                    System.out.println("Username already exist 🙁 please choose new one ");
                } else if (received.startsWith("EXIT:")) {
                    String exitedUser = received.substring(received.indexOf(':') + 1);
                    System.out.println("User " + exitedUser + " has left the room");
                    // Exit the MessageReceiver thread
                    return;
                } else {
                    System.out.println(received);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

public class ChatClient {

    public static boolean registred = false;
    public static String name;
    public static boolean connected = true;
    public static void main(String args[]) throws Exception {
        String host = "localhost";

        DatagramSocket socket = new DatagramSocket();
        MessageReceiver r = new MessageReceiver(socket);
        MessageSender s = new MessageSender(socket, host);
        Thread rt = new Thread(r);
        Thread st = new Thread(s);
        rt.start();
        st.start();
    }
}
