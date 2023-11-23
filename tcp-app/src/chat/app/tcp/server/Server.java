package chat.app.tcp.server;

import chat.app.tcp.client.ChatClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread{
    private int port;
    private ServerSocket serverSocket;
    private Map<String, ChatServerThread> activeUsers;
    private static final  int MAX_CLIENTS = 3;

    public Server(int port) {
        this.port = port;
        this.activeUsers = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            System.out.println("Server started on port " + serverSocket.getLocalPort() + "...");
            System.out.println("Waiting for client...");
            runLoop();
        } catch (IOException e) {
            System.out.println("Can not bind to port "+ port + " : " + e);
        }

    }

    private void runLoop() {
        while(true){
            try {
                Socket client = serverSocket.accept();
                ConnectionHandler connector = new ConnectionHandler(this,client);
                connector.start();

            } catch (IOException e) {
                System.out.println("Server accept error : " + e);
                stopServer();
            }
        }
    }

    private void stopServer() {
        if (isAlive()){
            interrupt();
        }
    }


    public boolean isAvailable(String name){

        return !activeUsers.containsKey(name);
    }

    public void addThreadClient(Socket socket, String name) {
        if (activeUsers.size() < MAX_CLIENTS) {
            ChatServerThread thread = new ChatServerThread(this, socket);
            thread.start();
            activeUsers.put(name, thread);
            informUserLoggedIn(thread, name);
        } else {
            System.out.println("Client refused : maximum " + MAX_CLIENTS + " reached.");
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

}

    private void informUserLoggedIn(ChatServerThread client, String name) {
        activeUsers.forEach((cname, thread) -> {
            if (thread.equals(client)) {
                // do nothing
            } else
                thread.send(name + " just logged in! say hi :)");
        });
    }

    public void remove(ChatServerThread clientToRemove) {
        try {
            Lock lock = new ReentrantLock();
        for (Map.Entry<String, ChatServerThread> entry : activeUsers.entrySet()){
            String cname = entry.getKey();
            ChatServerThread chatThread = entry.getValue();
            lock.lock();
            if (chatThread.equals(clientToRemove)){
                chatThread.close();
                activeUsers.remove(cname, chatThread);
                break;
            }
            lock.unlock();
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getClientName(ChatServerThread client){
        for (Map.Entry<String, ChatServerThread> entry : activeUsers.entrySet()){
            String cname = entry.getKey();
            ChatServerThread chatThread = entry.getValue();
            if (chatThread.equals(client)){
                return cname;
            }
        }
        return "";
    }
    public void handle(ChatServerThread client, String input) {
        if (input.equals("exit")) {
            client.send("exit");
            remove(client);
        } else {
            if (input.startsWith("@")){
                var name = input.substring(1, input.indexOf(" "));
                var message = input.substring(input.indexOf(" ")+1);
                activeUsers.get(name).send("\n" + getClientName(client) + " says privately: " + message);
            }
            else {
                if (input.contains("/list")){
                    String activeUsersString = activeUsers.keySet().toString();
                    client.send("active users are : " + activeUsersString);
                }
                else {
                    activeUsers.forEach((name, clientthread) -> {
                        if (clientthread.equals(client)) {
                            // do nothing
                        } else
                            clientthread.send("\n" + getClientName(client) + " says : " + input);
                    });
                }
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8081);
        server.start();
    }
}
