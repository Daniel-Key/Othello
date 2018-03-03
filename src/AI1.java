import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Random AI
 *
 * Generates a random valid move
 */
public class AI1 extends AI{

    //Constructor for the AI
    public AI1(GamePiece colour, boolean gui) {
        //Calls the constructors from the superclass
        super(colour, gui);
        playerType = PlayerType.AI;
        //Used to select the AI from the GUI, and to print out statistics from AI testing
        name = "Random";
    }

    //Overrides the required move from the superclass
    @Override
    public Move move(ArrayList<Move> validMoves) {

        //Checks if there are any valid moves- if not it returns null to the Game class, which passes the turn
        if (validMoves.size() > 0) {
            //Calls the method required by the superclass with null
            super.move(null);
            //Returns a random move from an ArrayList of valid moves
            return pickRandomMove(validMoves);
        }
        return null;
    }
}
