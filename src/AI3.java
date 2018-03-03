import java.util.ArrayList;

/**
 * Capture most AI
 *
 * Picks the move that captures most opposition pieces
 */
public class AI3 extends AI {

    //Constructor
    public AI3(GamePiece colour, boolean gui) {

        super(colour, gui);
        name = "Capture most";
    }

    //Method to return a move which captures the most enemy pieces
    @Override
    public Move move(ArrayList<Move> validMoves) {

        //Checks that a valid move is possible
        if (validMoves.size() > 0) {

            //Calls the method required by the superclass with null
            super.move(null);

            //Initialises an integer to count the maximum amount of opposition pieces which
            //can be taken by any move
            int maxPiecesTaken = 0;
            //Initialises an ArrayList to store the best moves
            ArrayList<Move> maxPiecesTakenMoves = new ArrayList<Move>();

            //Iterates through all valid moves
            for (Move move: validMoves) {
                //Checks if the number of opposition pieces which will be taken is greater than the current maximum
                if (move.getFlippedPieces().size() > maxPiecesTaken) {
                    //If so, a new ArrayList of the best moves is initialised
                    maxPiecesTakenMoves = new ArrayList<Move>();
                    //The current move is added to the ArrayList
                    maxPiecesTakenMoves.add(move);
                    //The current maximum number of pieces able to be taken is updated
                    maxPiecesTaken = move.getFlippedPieces().size();
                }
                //Checks if the number of opposition pieces which will be taken is equal to the current maximum
                else if (move.getFlippedPieces().size() == maxPiecesTaken) {
                    //If so the move is added to the ArrayList of best moves
                    maxPiecesTakenMoves.add(move);
                }
            }
            //A random move is returned from the ArrayList of the possible best moves
            return pickRandomMove(maxPiecesTakenMoves);
        }
        //If no valid move is possible null is returned to the Game class and the move is passed
        return null;
    }
}
