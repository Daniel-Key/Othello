import java.util.ArrayList;


/**
 * Variable Positional AI
 *
 * Picks moves based on position scores and reweights squares around corners it takes
 */
public class AI9 extends AI {

    //Variables store if a corner is taken by this player
    private boolean A1 = false;
    private boolean A8 = false;
    private boolean H1 = false;
    private boolean H8 = false;

    //Constructor to set the name and type of the AI
    public AI9(GamePiece colour, boolean gui) {

        super(colour, gui);
        playerType = PlayerType.AI;
        name = "Variable Positional";
    }


    //Method used to pick the best move based on positional weighting
    @Override
    public Move move(ArrayList<Move> validMoves) {

        //Checks that a valid move is possible
        if (validMoves.size() > 0) {

            //Check the corners and set the squares to be fixed
            checkCorners();
            setFixed();

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

    //Method to check if the player controls each corner, setting a corresponding boolean to true if it does
    private void checkCorners() {

        if (game.getBoard()[0][0].getGamePiece() == colour) {

            A1 = true;
        }
        if (game.getBoard()[0][Constants.BOARD_SIZE - 1].getGamePiece() == colour) {

            H1 = true;
        }
        if (game.getBoard()[Constants.BOARD_SIZE - 1][0].getGamePiece() == colour) {

            A8 = true;
        }
        if (game.getBoard()[Constants.BOARD_SIZE - 1][Constants.BOARD_SIZE - 1].getGamePiece() == colour) {

            H8 = true;
        }
    }

    //Method to set squares to be fixed based on the which corners the player has
    private void setFixed() {

        if (A1) {

            //Top left requires no rotations
            setFixedSquares(game.getBoard());
        }
        if (H1) {

            //Top right requires one rotation
            setFixedSquares(rotateBoard(game.getBoard(), 1));
        }
        if (H8) {

            //Bottom right requires two rotations
            setFixedSquares(rotateBoard(game.getBoard(), 2));
        }
        if (A8) {

            //Bottom left requires three rotations
            setFixedSquares(rotateBoard(game.getBoard(), 3));
        }
    }

    //Method to set squares as fixed, moving out from the top left corner
    private void setFixedSquares(Square[][] board) {

        //Set up variables for the loop
        int maxDistance = board.length - 1;
        boolean reachedOppositionX;
        boolean reachedOppositionY = false;

        //Loop through each row of the board until you reach an opposition square and setting that as the max distance you can look to
        for (int i = 0; i < board.length; i++) {

            //Only loop if you haven't reached the end of your colour going down the board
            if (!reachedOppositionY) {
                reachedOppositionX = false;

                //Loop across the row until you reach the previous max distance
                for (int j = 0; j <= maxDistance; j++) {
                    if (!reachedOppositionX) {

                        //Set squares of your colour to be fixed
                        if (board[i][j].getGamePiece() == colour) {
                            board[i][j].setFixed(true);
                        }
                        else {

                            //Set the max distance and weighting of the next square along
                            reachedOppositionX = true;
                            maxDistance = j - 1;
                            if (maxDistance == 0) {
                                reachedOppositionY = true;
                            }
                            board[i][j].setWeighting(Constants.CORNER_WEIGHTING);
                        }
                    }
                }
            }
        }
    }

    //Method to rotate the board 90 degrees anti-clockwise
    private Square[][] rotateBoard(Square[][] board, int rotations) {

        //Initialise a new board to hold the rotated board
        Square[][] newBoard = new Square[board.length][board.length];

        //Loop through the board mapping (x, y) -> (y, -x)
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                newBoard[j][board.length - 1 - i] = board[i][j];
            }
        }

        //Perform another rotation if required
        if (rotations > 1) {
            newBoard = rotateBoard(newBoard, rotations - 1);
        }

        //Return the rotated board
        return newBoard;
    }
}