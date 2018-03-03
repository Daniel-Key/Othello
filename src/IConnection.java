import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Interface to allow a game to be played across the network between 2 players
 */
public interface IConnection {

    //Methods to be implemented in the client and server classes
    boolean connect();

    void close();

    void sendString(String output);

    String readInput() throws IOException;

    //Getters and setters for server and client input
    BufferedReader getInput();

    PrintWriter getOutput();

}
