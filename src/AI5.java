import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Move reducer AI
 *
 * Picks a move based on positional scores while also trying to reduce
 * the number of possible opponent moves the following turn
 */
public class AI5 extends AI {

    //Constructor
    public AI5(GamePiece colour, boolean gui) {

        super(colour, gui);
        playerType = PlayerType.AI;
        name = "Move reducer";
    }

    //Method to return the best move, scored using positional weighting
    //and attempting to minimise the number of moves possible for the opponent
    @Override
    public Move move(ArrayList<Move> validMoves) {

        //Checks that a valid move is possible
        if (validMoves.size() > 0) {

            //Calls the method required by the superclass with null
            super.move(null);
            //Initialises an integer to track the best move score to 0
            int bestScore = 0;

            //Initialises two ArrayLists of the best possible moves and the opponent's possible moves
            ArrayList<Move> bestMoves = new ArrayList<Move>();
            ArrayList<Move> oppositionMoves = new ArrayList<Move>();
            //Initialises the number of possible opponent moves and sets it initially to 0
            int oppositionMoveNumber = 0;

            //Iterates through all valid moves
            for (Move move: validMoves) {

                //Initialises an integer and sets the current score for this move to 0
                int currentScore = 0;
                //Initialises a 2D Square array to the correct board size
                Square[][] board = new Square[Constants.BOARD_SIZE][Constants.BOARD_SIZE];

                //Iterates through each square on the board, and sets the game piece positions to the current game state
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board.length; j++) {
                        board[i][j] = new Square(game.getBoard()[i][j].getGamePiece());
                    }
                }

                //Sets the current move position to the current player's colour- updating the board as if the move had been played
                board[move.getX()][move.getY()].setGamePiece(colour);
                //Iterates through each piece which will be taken by the move and changes them to the correct colour
                for (int i = 0; i < move.getFlippedPieces().size(); i++) {
                    board[move.getFlippedPieces().get(i).getX()][move.getFlippedPieces().get(i).getY()].setGamePiece(colour);
                }

                //Adds the positional weighting of the move to the current move score
                currentScore += positionScores[move.getX()][move.getY()];
                //Sets the temporary board as the board being used by the MoveChecker class
                MoveChecker.setBoard(board);

                //Checks which colour the opponent is, and calls the checkMove method with the appropriate colour
                //This returns all valid moves for the opponent, after the move we are currently considering, to an ArrayList
                switch (colour) {
                    case BLACK:
                        oppositionMoves = MoveChecker.checkMove(GamePiece.WHITE);
                        break;
                    case WHITE:
                        oppositionMoves = MoveChecker.checkMove(GamePiece.BLACK);
                        break;
                }

                //Sets the number of possible moves for the opponent
                oppositionMoveNumber = oppositionMoves.size();
                //Uses a switch statement to increment the move score for lower numbers of opposition moves
                switch (oppositionMoveNumber) {
                    //If the opponent has no moves the move score is drastically increased, as having two moves in a row
                    //provides more value than making a single well-positioned move in most cases
                    case 0:
                        currentScore += 10;
                        break;
                    //If the opponent has 4 or less moves the move score is incremented by a smaller amount,
                    //as it is desirable but not worth playing in a terrible position for
                    case 1:
                        currentScore += 4;
                        break;
                    case 2:
                        currentScore += 3;
                        break;
                    case 3:
                        currentScore += 2;
                        break;
                    case 4:
                        currentScore += 1;
                        break;
                }

                //Checks if the current move score is greater than the current best score
                if (currentScore > bestScore) {
                    //If so the current move score becomes the best move score
                    bestScore = currentScore;
                    //A new ArrayList is created to hold the best moves
                    bestMoves = new ArrayList<Move>();
                    //The current move is added to the ArrayList
                    bestMoves.add(move);
                }
                //Checks if the current move score is equal to the best move score
                else if (currentScore == bestScore) {
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
