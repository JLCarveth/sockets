import java.io.*;
import java.net.*;

/**
 * VersionClient connects to the VersionServer to recieve 
 * the software version.
 */
public class VersionClient {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java VersionClient <hostname>");
        }
        
        // Same port as used by VersionServer
        int port = 7923;
        // Hostname is provided by the command line args
        String host = args[0];

        try (
            Socket socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
        ) {
            System.out.println(in.readLine());
            socket.close();
        } catch (UnknownHostException uhe) {
            System.err.println("Unkown host " + host);
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println("IO error on " + port + ". " + ioe.getMessage());
            System.exit(1);
        }
    }
}