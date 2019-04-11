/**
 * ChatClient connects to a ChatServer
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.JTextArea;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class ChatClient {
    // GUI elements
    private JFrame frame;
    private JTextField field;
    private JTextArea textarea;

    // In / out with the server socket
    private Scanner in;
    private PrintWriter out;
    // Server connection info
    private String address;
    private int port;

    public ChatClient(String address, int port) {
        this.address = address;
        this.port = port;
        frame = new JFrame("Chat @ " + address);
        field = new JTextField("",50);
        textarea = new JTextArea(16,50);
        frame.getContentPane().add(textarea, BorderLayout.PAGE_START);
        frame.getContentPane().add(field, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textarea.setEditable(false);
        field.setEditable(false);
        field.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println(field.getText());
                field.setText("");
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    private String getName () {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE
        );
    }

    /**
     * The main ChatClient logic loop.
     * Establishes a socket connection to the server
     */
    private void run () {
        try (
            Socket socket = new Socket(address,port);
            Scanner userIn = new Scanner(System.in);
        ) {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                System.out.println(line);
                if (line.startsWith("MESSAGE")) {
                    textarea.append(line.substring(7) + "\n");
                } else if (line.startsWith("SELECTNAME")) {
                    out.println(getName());
                } else if (line.startsWith("NAMEGOOD")) {
                    field.setEditable(true);
                } else {
                    System.out.println("New Line: " + line);
                }
            }
        } catch (IOException e) {
            textarea.setText("Could not connect.");
        }
    }

    public static void main(String[] args) {
        if (args.length > 2) {
            System.err.println("Usage: java ChatClient <hostname> [<port>]");
        }
        int port;
        if (args.length==2) port = Integer.parseInt(args[1]);
        else port = 2288;

        String host = args[0];
        ChatClient client = new ChatClient(host,port);
        client.run();
    }
}