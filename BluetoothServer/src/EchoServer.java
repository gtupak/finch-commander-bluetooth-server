/**
 * Created by gtapuc on 2/16/2016.
 * Inspired from http://www.miniware.net/mobile/articles/viewarticle.php?id=22
 */

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EchoServer {
    public final UUID uuid = new UUID("40850b4ade8811e5b86d9a79f06e9478", false);
    public final String name = "Echo Server";
    public final String url = "btspp://localhost:"
                                + uuid
                                + ";name=" + name
                                + ";authenticate=false;encrypt=false";


    LocalDevice local = null;
    StreamConnectionNotifier server = null;
    StreamConnection conn = null;

    public EchoServer(){
        try{
            System.out.println("Setting device to be discoverable...");
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);

            System.out.println("Start advertising service...");
            server = (StreamConnectionNotifier) Connector.open(url);

            System.out.println("Waiting for incoming connections...");
            conn = server.acceptAndOpen();

            System.out.println("Client Connected...");
            DataInputStream din = new DataInputStream(conn.openInputStream());

            final int BUFFER_SIZE = 1024;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytes = 0;
            String received = "";
            Charset charSet = Charset.forName("UTF-8");
            while(true){
                try {
                    bytes = din.read(buffer, 0, BUFFER_SIZE - bytes);
                    received = new String(buffer, 0, bytes, charSet);
                    System.out.println(received);
                    process(received);
                    buffer = new byte[BUFFER_SIZE];
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {System.out.println("Exception Occured: " + e.toString());}
    }

    /**
     * Processes the received string from the client
     */
    public void process(String toProcess) {
//        String[] commands = toProcess.split(" "); // Split by spaces
//        if (commands.length < 2)
//            return; //TODO Send feedback to client

        Pattern pattern = Pattern.compile("(Move|Turn|Say) (forward|backwards|left|right)? ?(\\d+|one|two|three|four|five|six|seven|eight|nine|ten)?(.*)");
        Matcher matcher = pattern.matcher(toProcess);
        boolean foundMatch = matcher.matches();
        if (!foundMatch) {
            System.err.println("ERROR: Move command has to be constructed like so:\n" +
                            "Move [forward|backwards] <number> or \n" +
                            "Turn [right|left] or \n" +
                            "Speak <some words>"); // TODO send this to client
            return;
        }

        switch(matcher.group(1)) {
            case "Move":
                System.out.println("MOVE: Processing: $1=" + matcher.group(1) + " $2=" + matcher.group(2) + " $3=" + matcher.group(3));
                break;

            case "Turn":
                System.out.println("TURN: Processing: $1=" + matcher.group(1) + " $2=" + matcher.group(2));
                break;

            case "Say":
                System.out.println("SPEAK: Processing: $1=" + matcher.group(1) + "$2=" + matcher.group(4));
                break;

            default:
                System.err.println("ERROR: Found match wrongly");
        }

//        switch(commands[0]) {
//            case "Move":
//                if ((commands.length != 4) && (commands[]) {
//
//                    return;
//                }
//                if (commands[1].equals("forward") ) {
//                    Integer.parseInt(commands[2]);
//                }
//        }
    }

    public static void main (String args[]){
        EchoServer echoServer = new EchoServer();
    }
}
