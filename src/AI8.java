import java.util.ArrayList;

/**
 * Variable Positional AI - targets fixed moves
 *
 * Same as the variable positional AI but picks moves that increase the number of fixed squares
 */
public class AI8 extends AI {

    //Variables store if a corner is taken by this player
    private boolean A1 = false;
    private boolean A8 = false;
    private boolean H1 = false;
    private boolean H8 = false;

    //Constructor to set the name and type of the AI
    public AI8(GamePiece colour, boolean gui) {

        super(colour, gui);
        playerType = PlayerType.AI;
        name = "Variable Positional - targets fixed moves";
    }

    //Method used to pick the best move based on positional weighting
    @Override
    public Move move(ArrayList<Move> validMoves) {

        //Checks that a valid move is possible
        if (validMoves.size() > 0) {

            //Check the corners and set the squares to be fixed
            checkCorners(game.getBoard());
            setFixed(game.getBoard());

            //Calls the method required by the superclass with null
            super.move(null);

            //Initialises an ArrayList to store the best moves
            ArrayList<Move> bestMoves = new ArrayList<Move>();

            //Sets the value of the current best move to -1
            int bestMoveValue = -1;

            //Set the initial fixed squares to 0 and then iterate through counting them
            int initialFixed = 0;
            for (Square[] row: game.getBoard()) {
                for (Square square: row) {
                    if (square.isFixed()) {
                        initialFixed++;
                    }
                }
            }

            //Loops through every valid move
            for (Move move: validMoves) {

                //Initialise a new board to check fixed squares and copy the old board into it
                Square[][] board = new Square[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board.length; j++) {
                        board[i][j] = new Square(game.getBoard()[i][j].getGamePiece());
                    }
                }

                //Make the move on the new board
                board[move.getX()][move.getY()].setGamePiece(colour);
                for (int i = 0; i < move.getFlippedPieces().size(); i++) {
                    board[move.getFlippedPieces().get(i).getX()][move.getFlippedPieces().get(i).getY()].setGamePiece(colour);
                }

                //Check the corners and set the squares to be fixed
                checkCorners(board);
                setFixed(board);

                //Count the new fixed squares
                int newFixed = 0;
                for (Square[] row: board) {
                    for (Square square: row) {
                        if (square.isFixed()) {
                            newFixed++;
                        }
                    }
                }

                //If more squares would be fixed, add the number of newly fixed squares to the weighting
                if (newFixed > initialFixed) {
                    game.getBoard()[move.getX()][move.getY()].setWeighting(Constants.CORNER_WEIGHTING + newFixed - initialFixed);
                }
            }

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
    private void checkCorners(Square[][] board) {

        if (board[0][0].getGamePiece() == colour) {
            A1 = true;
        }
        else {
            A1 = false;
        }
        if (board[0][Constants.BOARD_SIZE - 1].getGamePiece() == colour) {
            H1 = true;
        }
        else {
            H1 = false;
        }
        if (board[Constants.BOARD_SIZE - 1][0].getGamePiece() == colour) {
            A8 = true;
        }
        else {
            A8 = false;
        }
        if (board[Constants.BOARD_SIZE - 1][Constants.BOARD_SIZE - 1].getGamePiece() == colour) {
            H8 = true;
        }
        else {
            H8 = false;
        }
    }

    //Method to set squares to be fixed based on the which corners the player has
    private void setFixed(Square[][] board) {

        if (A1) {

            //Top left requires no rotations
            setFixedSquares(board);
        }
        if (H1) {

            //Top right requires one rotation
            setFixedSquares(rotateBoard(board, 1));
        }
        if (H8) {

            //Bottom right requires two rotations
            setFixedSquares(rotateBoard(board, 2));
        }
        if (A8) {

            //Bottom left requires three rotations
            setFixedSquares(rotateBoard(board, 3));
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