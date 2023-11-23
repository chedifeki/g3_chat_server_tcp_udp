package multicast.app.reciver;

import java.net.MulticastSocket;
import java.util.Scanner;

public class ReaderRep implements Runnable
{

    private  boolean exitRequested = false;
    private MulticastSocket multicastSocket;


    public boolean isExitRequested() {
        return exitRequested;
    }
    public ReaderRep(MulticastSocket multicastSocket) {
        this.multicastSocket = multicastSocket;
    }
    @Override
    public synchronized void run() {
        System.out.print("Write 'Exit' to leave the group \n");
            Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        if (message.equals("Exit")) {
            exitRequested = true;
        }
    }
}
