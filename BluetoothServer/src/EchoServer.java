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

    FinchCommander mFinchCommander;

    public EchoServer(){
        // Setup the Finch
        mFinchCommander = new FinchCommander();

        // Setup bluetooth
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
                    System.out.println("RECEIVED: " + received);
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
        Pattern pattern = Pattern.compile("(Move|Turn|Say) (forward|back|left|right)? ?(\\d*\\.?\\d+|one|two|three|four|five|six|seven|eight|nine|ten)?(.*)");
        Matcher matcher = pattern.matcher(toProcess);
        boolean foundMatch = matcher.matches();
        if (!foundMatch) {
             printErr();// TODO send this to client
            return;
        }

        String direction;
        double duration = 0.0;
        Command cmd;
        switch(matcher.group(1)) {
            case "Move":
                direction = matcher.group(2);
                if (!direction.equals("forward") && !direction.equals("back"))
                    return;

                try {
                    duration = parseDuration(matcher.group(3));
                } catch (NullPointerException ex) {
                    // Not a valid number
                    printErr();
                    return;
                }

                cmd = direction.equals("forward") ? Command.MOVE_FORWARD : Command.MOVE_BACKWARDS;
                mFinchCommander.processCommand(cmd, duration);

                System.out.println("MOVE: Processed: $1=" + matcher.group(1) + " $2=" + matcher.group(2) + " $3=" + matcher.group(3));
                break;

            case "Turn":
                direction = matcher.group(2);
                if (!direction.equals("right") && !direction.equals("left"))
                    return;

                try {
                    duration = parseDuration(matcher.group(3));
                } catch (NullPointerException ex) {
                    // Not a valid number
                    printErr();
                    return;
                }

                cmd = direction.equals("right") ? Command.TURN_RIGHT : Command.TURN_LEFT;
                mFinchCommander.processCommand(cmd, duration);

                System.out.println("TURN: Processing: $1=" + matcher.group(1) + " $2=" + matcher.group(2) + " $3=" + matcher.group(3));
                break;

            case "Say":
                System.out.println("SPEAK: Processing: $1=" + matcher.group(1) + "$2=" + matcher.group(4));
                break;

            default:
                System.err.println("ERROR: Found match wrongly");
        }
    }

    private double parseDuration(String dur) {
        double result;
        try {
            result = Double.parseDouble(dur);
        } catch (NumberFormatException ex) {
            // Number is in literal form
            result = Utilities.parseLiteralNumber(dur);
        }
        return result;
    }

    private void printErr() {
        System.err.println("ERROR: Move command has to be constructed like so:\n" +
                "Move [forward|backwards] <number> or \n" +
                "Turn [right|left] or \n" +
                "Speak <some words>");
    }

    public static void main (String args[]){
        EchoServer echoServer = new EchoServer();
    }
}
