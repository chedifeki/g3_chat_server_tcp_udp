package chat.app.tcp.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{
    private String serverName = "localhost";
    private int serverPort = 8081;
    private Socket socket;
    private Thread thread;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ChatClientThread client;



    public Client() {
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Client started on port " + socket.getLocalPort() + "...");
            System.out.println("Connected to server " + socket.getRemoteSocketAddress());
            dis = new DataInputStream(System.in);
            dos = new DataOutputStream(socket.getOutputStream());
            client = new ChatClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        // start chatting
        while (thread != null) {
            chat();
        }
    }

    private void chat() {
        try {

            dos.writeUTF(dis.readLine());
            dos.flush();
            System.out.print("> ");
            // Sleep, because this thread must wait ChatClientThread to show the message first
            try {
                thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Error : " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Sending error : " + e.getMessage());
            stop();
        }
    }

    public void handleMessage(String message) {
        if (message.equals("exit")) {
            stop();
        } else {
            System.out.println(message);
            System.out.print("> ");
        }
    }

    public void stop() {
        try {
            thread = null;
            dis.close();
            dos.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing : " + e.getMessage());
        }
        client.close();
    }

    public static void main(String args[]) {
        Client client = new Client();
    }
}
