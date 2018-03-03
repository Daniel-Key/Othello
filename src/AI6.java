import java.util.ArrayList;

/**
 * Mini-max positional AI
 *
 * Picks a move based on the positional scores of a number of future moves,
 * maximising the best move for itself and minimising the best more for the opposition
 */
public class AI6 extends AI {

    //The lookAheadDepth variable represents the number of turns the method will check through
    //A lookAheadDepth value of 2 represents 1 AI6 move and 1 opponent move
    private int lookAheadDepth = 4;
    //A boolean used to indicate whether the previous move evaluated had the best score so far
    private boolean bestMoveMark = false;
    //Initialising variables required to be called in the recursive method
    private Square[][] board;
    private int bestScore;
    private Move bestMove;

    //Constructor
    public AI6(GamePiece colour, boolean gui) {

        super(colour, gui);
        playerType = PlayerType.AI;
        name = "Mini-max positional";
    }

    //Method to initialise the board correctly and return the best move
    @Override
    public Move move(ArrayList<Move> validMoves) {
        //Checks that a valid move is possible
        if (validMoves.size() > 0) {
            //Calls the method required by the superclass with null
            super.move(null);

            //Initialises a temporary board to be passed to the calculateBestMove method
            board = new Square[Constants.BOARD_SIZE][Constants.BOARD_SIZE];

            //Iterates through each square on the board, and sets the game piece positions to the current game state
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    board[i][j] = new Square(game.getBoard()[i][j].getGamePiece());
                }
            }

            //Initialises relevant variables to starting values
            bestScore = 0;
            bestMove = null;

            //Calls the calculateBestMove method, which will set the bestMove variable if there is a valid move
            calculateBestMove(board, lookAheadDepth, 0);
            //Returns the best move found by the calculateBestMove method
            return bestMove;
        }
        //If no valid move is possible null is returned to the Game class and the move is passed
        return null;
    }

    //Method which calls itself recursively, to find the best move using a mini-max algorithm and positional weighting
    //The method arguments define the current board state,
    //the depth to which the method needs to look ahead, and the current score of the move
    private void calculateBestMove(Square[][] board, int lookAheadDepth, int currentScore) {

        //Initialises a temporary board as a 2D Square array
        Square[][] tempBoard = board;
        //Sets the board in MoveChecker to be the temporary board
        MoveChecker.setBoard(tempBoard);

        //Initialises an ArrayList of possible moves from the current board state
        //using the checkMove method in the MoveChecker class
        ArrayList<Move> possibleMoves = MoveChecker.checkMove(colour);

        //Iterates through all possible moves from this board state
        for (int i = 0; i < possibleMoves.size(); i++) {

            //Sets a local variable move to the current Move
            Move move = possibleMoves.get(i);
            //Initialises local variables for the depth and current move score
            int tempDepth = lookAheadDepth;
            int currentBestScore = currentScore;

            //Checking if this method is being called at the top of the move tree, in which case
            //the previous move can be evaluated to see if was the best so far
            if (lookAheadDepth == this.lookAheadDepth) {
                //Checks to see if the previous move was the best so far
                if (bestMoveMark) {
                    //If so, the previous move is set to the current best move
                    bestMove = possibleMoves.get(i-1);
                    //The
                    bestMoveMark = false;
                }
                //If the best score has not been set, it is set to the last possible valid move, in case there
                //is only one valid move and this method is only called once from the top of the move tree
                if (bestScore == 0) {
                    bestMove = possibleMoves.get(possibleMoves.size()-1);
                }
            }

            //Checks if it is this AI's turn
            if (((this.lookAheadDepth - tempDepth)%2) == 0) {
                //If so, the current best score is incremented by the position score of the current move
                currentBestScore += positionScores[move.getX()][move.getY()];
            }

            //Checks if it is the opponent's turn
            if (((this.lookAheadDepth - tempDepth)%2) == 1) {
                //If so, the current best score is decremented by the position score of the current move
                currentBestScore -= positionScores[move.getX()][move.getY()];
            }

            //This check prevents checking past the specified depth
            if (tempDepth > 1) {
                //If the temporary depth is greater than 1, the temporary board is updated with the current move
                tempBoard = makeMove(move, board, colour);
                //The depth is then decremented by 1
                tempDepth -= 1;
                //The method is then called again with the new board state, depth, and current score
                calculateBestMove(tempBoard, tempDepth, currentBestScore);
            }
            //Saves the best scores once the bottom of the tree is reached
            if (tempDepth == 1) {
                //Checks if the best score for the series of moves checked is greater than the current best score
                if (currentBestScore > bestScore) {
                    //If so the current move score becomes the best move score
                    bestScore = currentBestScore;
                    //The boolean bestMoveMark is set to true, to indicate that the previous move was the best yet
                    bestMoveMark = true;
                }
            }
        }

        //Sets the best move if it is the last move, and there is a valid move
        if (bestMoveMark && possibleMoves.size() > 0) {
            //Sets the best move to the last move
            bestMove = possibleMoves.get(possibleMoves.size()-1);
            //Sets bestMoveMark to false so that when the method is called for the next turn it works properly
            bestMoveMark = false;
        }
    }

    //This method makes a move and sets the board state correctly
    private Square[][] makeMove(Move move, Square[][] board, GamePiece colour) {

        //Sets the position of the move to a piece of the current player's colour
        board[move.getX()][move.getY()].setGamePiece(colour);
        //Iterates through the pieces which will be taken by this move and switches their colour
        for (int i = 0; i < move.getFlippedPieces().size(); i++) {
            board[move.getFlippedPieces().get(i).getX()][move.getFlippedPieces().get(i).getY()].setGamePiece(colour);
        }

        //Returns the board in the correct state after the move has been made
        return board;
    }
}
