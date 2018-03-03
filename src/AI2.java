import java.util.ArrayList;

/**
 * Positional AI
 *
 * Picks the optimum move based on position scores
 */
public class AI2 extends AI {

    //Constructor
    public AI2(GamePiece colour, boolean gui) {

        super(colour, gui);
        playerType = PlayerType.AI;
        name = "Positional Only";
    }

    //Method used to pick the best move based on positional weighting
    @Override
    public Move move(ArrayList<Move> validMoves) {

        //Checks that a valid move is possible
        if (validMoves.size() > 0) {

            //Calls the method required by the superclass with null
            super.move(null);

            //Initialises an ArrayList to store the best moves
            ArrayList<Move> bestMoves = new ArrayList<Move>();
            //Sets the value of the current best move to -1
            int bestMoveValue = -1;

            //Loops through every valid move
            for (Move move: validMoves) {
                //Sets moveValue to the board square weighting of the possible move
                int moveValue = game.getBoard()[move.getX()][move.getY()].getWeighting();
                //Checks if the current move score is greater than the current best score
                if (moveValue > bestMoveValue) {
                    //If so the current move value becomes the best move value
                    bestMoveValue = moveValue;
                    //A new ArrayList is created to hold the best moves
                    bestMoves = new ArrayList<Move>();
                    //The current move is added to the ArrayList
                    bestMoves.add(move);
                }
                //Checks if the current move score is equal to the best move score
                else if (moveValue == bestMoveValue) {
                    //If so the move is added to the ArrayList of best moves
                    bestMoves.add(move);
                }
            }

            //A random move is returned from the ArrayList of the possible best moves
            return pickRandomMove(bestMoves);
        }
        //If no valid move is possible null is returned to the Game class and the move is passed
        return null;
    }
}
