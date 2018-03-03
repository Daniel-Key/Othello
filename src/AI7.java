import java.util.ArrayList;

/**
 * Positional AI
 *
 * A very similar AI to AI2, but differentiating between edge squares close to the corners
 * and edge squares in the middle of the board
 */
public class AI7 extends AI {

    //Constructor
    public AI7(GamePiece colour, boolean gui) {

        super(colour, gui);
        playerType = PlayerType.AI;
        name = "Positional Only (A&B)";
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

    //Method to set up the weightings so that the edge squares are differentiated between 'A' and 'B'
    @Override
    public void setGame(Game game) {
        //Sets up a game with normal square weightings
        super.setGame(game);

        //Sets the extra weightings for the edges
        setASquareWeighting(game.getBoard());
        setBSquareWeighting(game.getBoard());

        //Iterates through each board square and updates the relevant weightings
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                game.getBoard()[i][j].setWeighting(positionScores[i][j]);
            }
        }
    }
}
