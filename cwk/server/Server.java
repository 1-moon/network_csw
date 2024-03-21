/*
 *   This file for implementing Server application
 *   including use of thread pool and log file output.
 */

import java.net.*;              // This library provides networking applications
import java.io.*;               // This library gives I/O data stream
import java.util.ArrayList;
import java.util.concurrent.*;  // Utility classes commonly useful in concurrent programming.

public class Server {
    public static Operation operation;   // create Operation class object
//    public static String item;         // the item on the auction
    public static ArrayList<String> items;    // the items on the auction
    public static double bid;   // bid amount on the item

    public static void main (String[] args) throws IOException{

        operation = new Operation();
        ServerSocket server = null;     // socket for server program
        ExecutorService service = null;

        // server app choose the port number 6101    / turn on first
        try {
            server = new ServerSocket(6101);
        } catch(IOException e){
            System.err.println("Could not listen on port 6101");
            System.exit(-1);
        }

        // Use an Executor to manage a fixed thread-pool with 30 connection
        service = Executors.newFixedThreadPool(30);

        // server waits(listens) for client's connection request
        while (true){

            Socket client = server.accept();    // return socket to communicate with clients
            service.submit(new ClientHandler(client));
        }

    }
}
