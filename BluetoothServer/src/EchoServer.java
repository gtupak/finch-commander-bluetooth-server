/**
 * Created by gtapuc on 2/16/2016.
 * Inspired from http://www.miniware.net/mobile/articles/viewarticle.php?id=22
 */

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.DataInputStream;

public class EchoServer {
    public final UUID uuid = new UUID("27012f0c68af4fbf8dbe6bbaf7aa432a", false);
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
            while(true){
                String cmd = "";
                char c;
                while (((c = din.readChar()) > 0) && (c != '\n')){
                    cmd = cmd + c;
                }
                System.out.println("Received " + cmd);
            }
        }
        catch (Exception e) {System.out.println("Exception Occured: " + e.toString());}
    }

    public static void main (String args[]){
        EchoServer echoServer = new EchoServer();
    }
}
