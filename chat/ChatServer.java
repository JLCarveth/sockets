/**
 * ChatServer is a multi-threaded server that allows for multiple clients 
 * to communicate amongst themselves.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private ServerSocket serverSocket;
    private Boolean isStopped = true;

    private static ArrayList<Worker> workers = new ArrayList<>();
    public static int port = 2288;

    /**
     * Initializes the serverSocket object,
     * and indicates the server is now running.
     */
    private void openServerSocket () {
        try {
            this.serverSocket = new ServerSocket(port);
            this.isStopped = false;
        } catch (IOException e) {
            System.err.println("Could not open server on port " + port);
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Returns if the server has stopped
     * @return true if the server has stopped.
     */
    public boolean isStopped () {
        return this.isStopped;
    }

    /**
     * 
     * @param args the port on which connections will be accepted
     */
    public static void main(String[] args) {
        if (!(args.length <= 1)) {
            System.err.println("Usage: java ChatServer [<port>]");
            System.exit(1);
        }

        if (args.length == 1) port = Integer.parseInt(args[0]);

        ExecutorService pool = Executors.newFixedThreadPool(500);
        ChatServer cs = new ChatServer();
        cs.openServerSocket();
        while (true) {
            try {
                Worker w = new Worker(cs.serverSocket.accept());
                pool.execute(w);
            } catch (IOException e) {
                if (cs.isStopped()) {
                    System.err.println("Server has stopped.");
                }
            }
        }
        
    }

    /**
     * Broadcast a message from one client to all other clients.
     * @param message the message to be broadcast
     * @param username the username of the client sending the message
     */
    public static void broadcast (String message, String username) {
        for (Worker w : workers) {
            w.writer.println("MESSAGE" + username + ": " + message);
        }
    }

    /** 
     * Returns true if the provided name is already taken by a Worker / Client
     * @param username
     */
    public static boolean nameTaken (String username) {
        boolean taken = false;
        for (Worker w : workers) {
            if (w.getUsername().equals(username)) taken = true;
        }

        return taken;
    }
    /**
     * The Worker class represents a single client connection to the ChatServer
     */
    private static class Worker implements Runnable {
        private String username = null;
        private PrintWriter writer;
        private BufferedReader reader;
        private Socket client;

        public Worker (Socket client) {
            System.out.println("New connection from " + client.getInetAddress());
            this.client = client;
        }

        public void run () {
            try {
                writer = new PrintWriter(client.getOutputStream(), true);
                reader = new BufferedReader(
                    new InputStreamReader(client.getInputStream())
                );

                // Get a username
                while (username == null) {
                    writer.println("SELECTNAME");
                    username = reader.readLine();
                    if (username == null || username.isEmpty()) {
                        return;
                    }

                    if (ChatServer.nameTaken(username)) {
                        writer.println("MESSAGE Server: Username is already taken.");
                        username = null;
                    } else {
                        writer.println("NAMEGOOD");
                        writer.println("MESSAGE Server: Welcome " + username);
                        workers.add(this);
                    }            
                }
                
                broadcast((username + " has joined."), "Server");

                while (true) {
                    String input = reader.readLine();
                    if (input.toLowerCase().equals("#exit")) {
                        writer.write("Goodbye.");
                        return;
                    }
                    broadcast(input, username);
                }
            } catch (IOException e) {
                System.err.println("Error communicating with client. "
                 + e.getMessage());
            } finally {
                if (username != null) {
                    broadcast(username + " has left.", username);
                    workers.remove(this);
                }
                try {client.close();} catch (IOException e) {
                    System.err.println("Error closing connection to client.");
                }
            }
        }

        public String getUsername () {
            return username;
        }
    }
}