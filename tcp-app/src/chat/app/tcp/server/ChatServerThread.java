package chat.app.tcp.server;

import java.io.*;
import java.net.Socket;

public class ChatServerThread extends Thread{

    private Server server;
    private Socket socket;
    private BufferedInputStream bis;
    private DataInputStream dis;
    private BufferedOutputStream bos;
    private DataOutputStream dos;

    public ChatServerThread(Server _server, Socket _socket) {
        super();
        server = _server;
        socket = _socket;
    }



    public void send(String message) {
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            System.out.println("Client " + socket.getRemoteSocketAddress() + " error sending : " + e.getMessage());
            server.remove(this);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Client " + socket.getRemoteSocketAddress() + " connected to server...");

            bis = new BufferedInputStream(socket.getInputStream());
            dis = new DataInputStream(bis);
            bos = new BufferedOutputStream(socket.getOutputStream());
            dos = new DataOutputStream(bos);
            while (true) {
                server.handle(this,dis.readUTF());
            }
        } catch (IOException e) {
            //System.out.println("Client " + socket.getRemoteSocketAddress() + " error reading : " + e.getMessage());
            server.remove(this);
        }
    }

    public void close() throws IOException {
        System.out.println("Client " + socket.getRemoteSocketAddress() + " disconnect from server...");
        socket.close();
        dis.close();
        dos.close();
    }

}
