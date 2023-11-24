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
        System.out.println("------------- Hello ! -------------\n"
                +"Welcome to the Multicast App. Here are some basic instructions :"
                +"\n\t- Type disconnect to stop receiving Multicast Messages "
                +"\n\t- Type connect to rejoin the Multicast room"
                +"\n\t- Type exit to terminate the program"
                +"\n------------- Have a Good Multicast! -------------");
        System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Exit requested");
                    client.exit();
                    break;
                }
                if (message.equalsIgnoreCase("disconnect")) {
                    System.out.println("Disconnect has been requested");
                    client.disconnect();
                }
                if (message.equalsIgnoreCase("connect")) {
                    System.out.println("Connect has been requested");
                    client.connect();
                }
            }
    }
}
