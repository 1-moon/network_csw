/*
*
*   This file to implement client side for TCP/IP networking
*
*/


import java.io.*;
import java.net.*;

public class Client {

    //field area
    private Socket socket = null;
    private PrintWriter socketOutput = null;
    private BufferedReader socketInput = null;

    public String requestType;     // request(show, item, bid) typed on the command line

    public static void main(String[] args){
        // avoid for static on main
        Client client = new Client();
        // catch the request type
        client.requestType = args[0];
        for (int i=1; i< args.length; i++)
            client.requestType = client.requestType + ' ' + args[i];
        client.runClient(args);

    }
    public void runClient(String[] args) {
        // connection !
        try{
            // create socket and try to connect with server on the same machine   / turn on followed by server socket
            socket = new Socket("localhost", 6101);
            // chain a writing stream (client -> socket   1)
            socketOutput = new PrintWriter(socket.getOutputStream(), true);
            // ...
            // read data from socket (socket -> client    4)
            socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e){
            System.err.println("This host is unknown.\n");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection with host");
            System.exit(1);
        }

        String fromServer;

        socketOutput.println(requestType);

        // communication !!
        try{
            while ((fromServer = socketInput.readLine()) != null){
                // command line check
                if (fromServer.equals("quit"))
                    break;
                if (fromServer.equals("invalid command")){
                    System.out.println("invalid command please double check.");
                    break;
                }
                if (fromServer.equals("inValid")){
                    System.out.println("The command is invalid");
                    break;
                }

                if (fromServer.equals("show start")){
                    if (args.length == 1){
                        socketOutput.println("send item data");
                        fromServer = socketInput.readLine();
                        showItems(fromServer);
                        socketOutput.println("success");
                    }else {
                        socketOutput.println("wrong command");
                        System.out.println("This command is invalid ");
                    }

                } else if (fromServer.equals("item start")) {       // item <object>
                    if(args.length == 2 ){
                        socketOutput.println(args[1]);
                        fromServer = socketInput.readLine();
                        if (fromServer.equals("not duplicate")) {
                            System.out.println("Success");
                        } else {
                            System.out.println("Failure");
                            socketOutput.println("wrong command");
                        }
                    } else {
                        System.out.println("Failure: Only one item can be put on the auction");
                        socketOutput.println("wrong command");
                    }

                } else if (fromServer.equals("bid start")) {        // bid <table> <10.0>
                    if (args.length == 3 && isDouble(args[2])){     // currency check
                        socketOutput.println(args[1]);
                        fromServer = socketInput.readLine();
                        if (fromServer.equals("valid item")){       // existing item check
                            socketOutput.println(args[2]);
                            fromServer = socketInput.readLine();
                            if (fromServer.equals("bid exceeded")){ // over bidding check
                                System.out.println("Accepted");
                            } else {
                                socketOutput.println("wrong command");
                                System.out.println("Rejected");
                            }
                        } else {
                            System.out.println("Failure: this item does not exist on our auction");
                            socketOutput.println("wrong command");
                        }
                    } else {
                        System.out.println("Failure: wrong bidding currency");
                        socketOutput.println("wrong command");
                    }
                }

            }
        } catch (IOException e){
            System.err.println("I/O exception during communication between server and client");
            System.exit(1);
        }
    }

    // show all items
    private void showItems(String show){
        String[] infoOfShow = show.split("\\s+");
        String item = infoOfShow[0];
        String bid = infoOfShow[1];
        if ( infoOfShow[0] != null){
            if ( infoOfShow[1] != null){
                System.out.println( item + ":" + bid + ":" + socket.getInetAddress() );
            } else
                System.out.println( item + ": 0.0 : <no bids>");
        } else
            System.out.println("There are currently no items in this auction");

    }

    // double type check for currency
    private boolean isDouble(String money){
        if (Double.parseDouble(money) > 0)
            return true;
        else
            return false;
    }



}
