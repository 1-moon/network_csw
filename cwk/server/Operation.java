import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Operation {
    public static File logFile = null;
    public static Writer fw;
    public Operation(){
        logFile = new File("log.txt");   // create file object

        // log file error check
        if (!logFile.exists()){
            try{
                logFile.createNewFile();
            } catch (IOException exp){
                System.err.println("ERROR: log.txt can't be created by " + exp);
                System.exit(-1);
            }
        }

        // file writer error check
        try {
            fw = new FileWriter(logFile, false);    // create FileWriter object
        } catch (IOException exp){
            System.err.println("ERROR: " + exp);
            System.exit(-1);
        }

    }
    // write log file
    public void writeLogFile (InetAddress inet, String request){
        String outputFormat;

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd|hh:mm:ss");
        outputFormat = simpleDateFormat.format(date) + '|' + inet.getHostAddress() + '|' + request + ".\n";

        try {
            fw.write(outputFormat);
            fw.flush();
        } catch (IOException exp) {
            exp.printStackTrace();
        }

    }
}
