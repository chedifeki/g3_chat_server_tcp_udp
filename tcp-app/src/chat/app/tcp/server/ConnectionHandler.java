package chat.app.tcp.server;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler extends Thread{
    private Server server;
    private Socket socket;
    private BufferedInputStream bis;
    private DataInputStream dis;
    private BufferedOutputStream bos;
    private DataOutputStream dos;


    public ConnectionHandler(Server server, Socket socket) {
        super();
        this.server = server;
        this.socket = socket;
        try {
            bis = new BufferedInputStream(this.socket.getInputStream());
            dis = new DataInputStream(bis);
            bos = new BufferedOutputStream(this.socket.getOutputStream());
            dos = new DataOutputStream(bos);
        } catch (IOException e) {
            System.out.println("Error when creating Connection Handler :" +e);
        }
    }

    @Override
    public void run(){
        // get client name
        try {
            String cname = requestClientName();
            if (server.isAvailable(cname)){
                server.addThreadClient(socket, cname);
                sendInstructions(cname);
            }
            else {
                send(" Username already taken!\nPlease connect again with a unique username");
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // check if client name is available
        // if available : tell server to accept connection and create chat thread
        // if not available tell server to close connection.
    }

    private void sendInstructions(String name) {
         send("------------- Hello " + name  +"! -------------\n"
                +"Welcome to the Chat server. Here are some basic instructions :"
                +"\n\t- Type /list to get a list of all active users"
                +"\n\t- Type exit to quit the chat"
                +"\n\t- Type @<user> to send a private message to that user"
                +"\n------------- Have a Good Chat! -------------");

    }


    private String requestClientName() throws IOException {
       send("Please enter your name");
       return dis.readUTF();

    }

    public void send(String message) {
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            System.out.println("Client " + socket.getRemoteSocketAddress() + " error sending : " + e.getMessage());

        }
    }

}
