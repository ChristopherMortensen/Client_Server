package InstantMessagingProgram_Server;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {

    //== Fields
    //== GUI - Components
    private JTextField userText;
    private JTextArea chatWindow;
    //== Streams and server-related objects
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    //== Constructor
    public Server() {
        super("Buckys Instant Messenger");
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) { //== This is here the actual message is sent.
                        sendMessage(event.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        add(userText, BorderLayout.NORTH);
        userText.setFont(new Font("Calibri", Font.PLAIN, 18));
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(500, 300);
        chatWindow.setEditable(false);
        chatWindow.setFont(new Font("Calibri", Font.BOLD, 18));
        setVisible(true);
    }

    //== Set up and run the server
    public void startRunning() {
        try { //== only 100 people are allowed to wait for this port = (backlog = 100)
            server = new ServerSocket(6789, 100); //==(port_num, backlog) 
            while (true) {
                try {
                    waitForConnection(); //== Wait for someone to connect with
                    setupStreams(); //== When someone is connected --> set up the streams
                    whileChatting(); //== Runs as long as the parts are communicating
                } catch (EOFException eofException) {
                    showMessage("\n Server ended the connection!");
                } finally {
                    closeCrap(); //== close almost everything.
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //== Wait for connection, then display connection information
    private void waitForConnection() throws IOException {
        showMessage(" Waiting for someone to connect... \n");
        //== accept() - returns a Socket, until some client has connected
        connection = server.accept();
        showMessage(" Now connected to " + connection.getInetAddress().getHostName());
    }

    //== Get stream to send and receive data
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n streas are now setup! \n");
    }

    //== During the chat conversation
    private void whileChatting() throws IOException {
        String message = " You are now Connected!  ";
        sendMessage(message);
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException classNotFoundException) {
                showMessage("\n idk wft that user sent!");
            }
        } while (!message.equals("CLIENT - END"));
    }

    //== Close streams and sockets after you are done chatting
    private void closeCrap() {
        showMessage("\n Closing connections... \n");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //== Send a message to client
    private void sendMessage(String message) {
        try {
            output.writeObject("SERVER - " + message);
            output.flush();
            showMessage("\nSERVER - " + message);
        } catch (IOException ioException) {
            chatWindow.append("\n ERROR: DUDE I CANT SEND THAT MESSAGE");
        }
    }

    //== updates chatWindow
    private void showMessage(final String text) {
        //==Im gonna set aside a thread that updates parts of the gui
        //== In our case, the text inside the chatWindow
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        chatWindow.append(text);
                    }
                }
        );
    }

    //== Let the user type stuff into their box
    private void ableToType(final boolean trueOrFalse) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        userText.setEditable(trueOrFalse);
                    }
                }
        );
    }
}
