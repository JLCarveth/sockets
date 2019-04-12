import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * VersionServer accepts a Client connection, sends the client the current
 * software version, and closes the connection.
 */
public class VersionServer implements Runnable {
    private static String version = "0.1.0";
    public static final int port = 7923;
    private ServerSocket serverSocket;
    private boolean isStopped = true;

    /**
     * Per the Runnable inferface. Opens the Socket, begins listening
     * for connections.
     */
    @Override
    public void run () {
        openServerSocket();
        while (!isStopped()) {
            Socket client = null;
            try {
                client = this.serverSocket.accept();
                System.out.println("Connection made.");
                new Thread(new Worker(client, version)).start();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server has stopped.");
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Private function that creates the server socket.
     */
    private void openServerSocket () {
        try {
            this.isStopped = false;
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Cannot open server on port " + port);
            System.exit(1);
        }
    }

    /**
     * Return whether the server has been stopped
     * @return the if the server has stopped.
     */
    public boolean isStopped() {
        return this.isStopped;
    }

    /**
     * Start the server on its own Thread.
     */
    public static void main(String[] args) {
        VersionServer vs = new VersionServer();
        new Thread(vs).start();
    }
}