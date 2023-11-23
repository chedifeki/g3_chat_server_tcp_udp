package multicast.app.receiver;

import java.util.Scanner;

public class Reader extends Thread
{

    private UDPMulticastClient client;


    public Reader(UDPMulticastClient client) {
        this.client = client;
    }

    @Override
    public synchronized void run() {
        System.out.print("Write 'Exit' to leave the group \n");
            Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        if (message.equalsIgnoreCase("exit")) {
            System.out.println("Exit has been requested" );
            client.disconnect();
        }
    }
}
