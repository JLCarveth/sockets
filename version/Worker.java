import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Worker class to enable multi-threaded functionality on the VersionServer
 * The Worker will create a 
 */
public class Worker implements Runnable {
    /**
     * The client to which the worker will send data
     */
    private Socket endpoint;
    /**
     * The data to send to the client
     */
    private String data;

    public Worker (Socket endpoint, String data) {
        this.endpoint = endpoint;
        this.data = data;
    }

    /**
     * The action to be executed by the Worker thread.
     */
    @Override
    public void run () {
        try (
            PrintWriter out = new PrintWriter(endpoint.getOutputStream(), true);
         ){
             out.println(data);
         } catch (IOException e) {
            System.out.println("Error communicating with the client." + e.getMessage());
        }
    }
}