import java.io.*;
import java.net.*;

/**
 * Class containing the methods required to set up a server connection to a client
 */
public class Client implements IConnection {

    //Required variables
    private Socket server;
    private BufferedReader input;
    private PrintWriter output;
    private String hostname;

    //Constructor to set the host name
    public Client(String hostname) {
        this.hostname = hostname;
    }

    //Method to initiate a connection with the server
    //Called from the Menu class in the startGame() method
    public boolean connect() {

        //Sets gameAccepted initially to false
        boolean gameAccepted = false;

        //Uses a try-catch method to deal with input or output exceptions
        try {

            //Opens a Java socket
            server = new Socket(hostname, 10006);

            //The client is set to the correct socket if they accept within 7 seconds
            server.setSoTimeout(7000);
            System.out.println("Server accepted connection");

            //Sets the input and output to allow communication across the connection
            input = new BufferedReader(new InputStreamReader(server.getInputStream()));
            output = new PrintWriter (server.getOutputStream(), true);

            //Test connection
            System.out.println("Test connection to server: ");
            //Implements the first connection protocol
            //Receives the client's first message and sets it to a String
            sendString("hello");
            String reply = readInput();
            //Request game
            if (reply.equals("hello")) {
                System.out.println("Request game from server: ");
                sendString("new game");
                //If the server accepts, set the game accepted to true
                if (readInput().equals("accept")) {
                    gameAccepted =  true;
                }
            }
        }
        catch (IOException ioe){
            System.out.println("Could not find host. Enter the correct hostname");
            ioe.printStackTrace();
        }

        return gameAccepted;
    }

    //Getter for the client input, used in SocketPlayer
    @Override
    public BufferedReader getInput() {
        return input;
    }
    //Getter for the server output, used in the Game and SocketPlayer classes
    @Override
    public PrintWriter getOutput() {
        return output;
    }

    //Method to safely close any streams and connections
    public void close() {
        //A try-catch method catches any exceptions thrown by closing the connection
        try {
            //Closes the BufferedReader and PrintWriter
            input.close();
            output.close();
            //Closes the socket, terminating the connection
            server.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to send a string to the server
    public void sendString(String output) {

        //Print out what is being sent for testing purposes and then send the string
        System.out.println("Sent: " + output);
        this.output.println(output);
        this.output.flush();
    }

    //Method to take an input from a server
    public String readInput() throws IOException {

        //Read the line and print it before returning it for testing purposes
        String inputString = input.readLine();
        System.out.println("Received: " + inputString);
        return inputString;
    }
}