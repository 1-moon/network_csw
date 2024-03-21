/*
*
*       This file is designed to specify protocol request and implement biding system
*           (server)
*/

import java.util.ArrayList;
import java.util.Arrays;

public class ProtocolBox {
    // protocol state field
    private static final int WAITING = 0;
    private static final int SHOW = 1;
    private static final int ITEM = 2;
    private static final int BID = 3;
    private static final int INVALID = 4;


    private int state = WAITING;        // initialising the state
    private String biddingItem= null;   // current item on the auction
    private double bidAmount = 0;       // buffer for storing current bid
    public String action (String inputState){
        String outputState = null;

        // determine which state client chose
        if (state == WAITING){
            if (inputState.equals("show")){
                state = SHOW;
            } else if (inputState.equals("item")) {
                state = ITEM;
            } else if (inputState.equals("bid")){
                state = BID;
            } else {
                state = INVALID;
            }
        }

        //  execute show command
        if (state == SHOW){
            if (inputState.equals("show")){
                outputState = "show start";
            } else if (inputState.equals("send item data")) {
                for (int i=0; i< Server.items.size(); i++)
                    outputState = Server.items.get(i) + ' ' + String.format("%f", Server.bid);
            } else if (inputState.equals("wrong command") || (inputState.equals("success"))) {
                outputState = "quit";
                state = WAITING;
            }
            
        }

        //  execute item command
        if (state == ITEM){
            if (inputState.equals("item")){
                outputState = "item start";
            } else if (!isDuplicate(inputState) ){         // inputState = <item_name>
                Server.items = new ArrayList<>(Arrays.asList(inputState, "0.0"));
                outputState = "not duplicate";
            } else if (inputState.equals("wrong command")) {
                outputState = "quit";
                state = WAITING;
            }
        }

        //  execute bid command
        if (state == BID){              // bid(o) table 10.0
            if (inputState.equals("bid")){
                outputState = "bid start";
            } else if (isDuplicate(inputState)) {   // bid table(o) 10.0
                outputState = "valid item";
                biddingItem = inputState;
            } else if (isExceed(inputState)){       // bid table 10.0(o)
                for(int i=0; i<Server.items.size(); i++){
                    if( Server.items.get(i).equals(biddingItem))
                        Server.items = new ArrayList<>(Arrays.asList(biddingItem, inputState));
                }
                outputState = "bid exceeded";
            } else if (inputState.equals("wrong command")) {
                outputState = "quit";
                state = WAITING;
            }
        }

        if (state == INVALID){
            outputState = "invalid command";
            state = WAITING;
        }
        return outputState;
    }

    // duplicated item check
    private boolean isDuplicate (String item){
        for (int i=0; i < Server.items.size(); i++ ){
            if (Server.items.get(i).equals(item))
                return true;
        }
        return false;
    }
    // exceeded bid check
    private boolean isExceed(String bids){
        if ( bidAmount <=Double.parseDouble(bids)){
            return true;
        } else
            return false;
    }
}
