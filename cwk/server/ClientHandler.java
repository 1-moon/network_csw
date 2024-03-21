/*
*  This file is a subclass of Thread class for handling clients
*/

import java.io.*;
import java.util.*;
import java.net.*;
public class ClientHandler extends Thread{
    private Socket socket = null;

    // constructor
    public ClientHandler(Socket socket){
        super("ClientHandler");
        this.socket= socket;
    }
    public void run(){
        try {
            // socket -> server    2
            BufferedReader serverIn = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            // server -> socket    3
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true );

            // logging using internet protocol
            InetAddress inet = socket.getInetAddress();
            String receivedData = serverIn.readLine();
            Server.operation.writeLogFile(inet, receivedData);

            // initialise the protocol object for assigned client
            String inputLine, outputLine;
            ProtocolBox pb = new ProtocolBox();

            // sequential protocol
            while( (inputLine = serverIn.readLine()) != null){
                outputLine = pb.action(inputLine);
                serverOut.println(outputLine);
                if( outputLine.equals("quit"))
                    break;
            }

            // free TCP/IP connection
            serverOut.close();
            serverIn.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
