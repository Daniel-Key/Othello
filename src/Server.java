import java.io.*;
import java.net.*;

/**
 * Class containing the methods required to set up a server connection to a client
 */
public class Server implements IConnection {

    //Required variables
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;

    //Method to initiate a connection with the client
    //Called from the Menu class in the startGame() method
    public boolean connect() {

        //Sets gameAccepted initially to false
        boolean gameAccepted = false;

        //Uses a try-catch method to deal with input or output exceptions
        try {

            //Opens a Java socket
            ServerSocket server = new ServerSocket(10006);
            System.out.println("Waiting for client connection");

            //The client is set to the correct socket if they accept within 7 seconds
            client = server.accept();
            client.setSoTimeout(7000);
            System.out.println("Client connection accepted");

            //The server can now be closed again as a connection had been established
            server.close();

            //Sets the input and output to allow communication across the connection
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);

            //Implements the first connection protocol
            //Receives the client's first message and sets it to a String
            String connectionTest = readInput();
            //If the message is the first line of the protocol, the server responds
            if (connectionTest.equals("hello")) {
                System.out.println("Client: "  + connectionTest);
                sendString("hello");
            }

            //Implements the game request protocol
            //Sets the client's game request message to a String
            String gameRequest = readInput();
            if (gameRequest.equals("new game")) {
                //Prints the IP address of the client to console so that it can be checked
                System.out.println("Game request from " + client.getInetAddress().getHostName());
                //Sends a message to the client to let it know that the game is accepted
                sendString("accept");
                //Sets the gameAccepted boolean to true
                gameAccepted = true;
            }
        }
        catch (IOException ioe) {
            System.out.println("Input or output exception");
        }
        //Returns a boolean value for whether or not the game has been accepted
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
            client.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to send a string to the client
    public void sendString(String output) {

        //Print out what is being sent for testing purposes and then send the string
        System.out.println("Sent: " + output);
        this.output.println(output);
        this.output.flush();
    }

    //Method to take an input from a client
    public String readInput() throws IOException {

        //Read the line and print it before returning it for testing purposes
        String inputString = input.readLine();
        System.out.println("Received: " + inputString);
        return inputString;
    }
}
