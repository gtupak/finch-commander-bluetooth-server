/**
 * Created by gtapuc on 2/16/2016.
 * Inspired from http://www.miniware.net/mobile/articles/viewarticle.php?id=22
 */

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.DataInputStream;
import java.io.IOException;

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
            int b = BUFFER_SIZE;
            while(true){
                try {
                    bytes = din.read(buffer, bytes, BUFFER_SIZE - bytes);
                    String asdf = new String(buffer);
                    System.out.println(asdf);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {System.out.println("Exception Occured: " + e.toString());}
    }

    public static void main (String args[]){
        EchoServer echoServer = new EchoServer();
    }
}
