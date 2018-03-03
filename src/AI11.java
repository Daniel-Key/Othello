import java.util.ArrayList;

/**
 * Move reducer with variable positional
 *
 * Picks a move based on positional scores while also trying to reduce
 * the number of possible opponent moves the following turn
 * Reweights squares based on corners it has
 */
public class AI11 extends AI {

    //Variables store if a corner is taken by this player
    private boolean A1 = false;
    private boolean A8 = false;
    private boolean H1 = false;
    private boolean H8 = false;

    //Constructor to set the name and type of the AI
    public AI11(GamePiece colour, boolean gui) {

        super(colour, gui);
        playerType = PlayerType.AI;
        name = "Move reducer with variable positional";
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

            //Check the corners and set the squares to be fixed
            checkCorners();
            setFixed();

            //Initialises two ArrayLists of the best possible moves and the opponent's possible moves
            ArrayList<Move> bestMoves = new ArrayList<Move>();
            ArrayList<Move> oppositionMoves = new ArrayList<Move>();
            //Initialises the number of possible opponent moves and sets it initially to 0
            int oppositionMoveNumber = 0;

            //Iterates through all valid moves
            for (Move move: validMoves) {

                //Initialises an integer and sets the current score for this move to 0
                int currentBestScore = 0;
                int positionScore = 0;
                int oppositionMoveScore = 0;
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
                positionScore = positionScores[move.getX()][move.getY()];
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
                        oppositionMoveScore += 10;
                        break;
                    //If the opponent has 4 or less moves the move score is incremented by a smaller amount,
                    //as it is desirable but not worth playing in a terrible position for
                    case 1:
                        oppositionMoveScore += 4;
                        break;
                    case 2:
                        oppositionMoveScore += 3;
                        break;
                    case 3:
                        oppositionMoveScore += 2;
                        break;
                    case 4:
                        oppositionMoveScore += 1;
                        break;
                }

                //Picks the best score based on combining double the opposition move score with the position weighting
                currentBestScore = 2 * oppositionMoveScore + positionScore;

                //Checks if the current move score is greater than the current best score
                if (currentBestScore > bestScore) {
                    //If so the current move score becomes the best move score
                    bestScore = currentBestScore;
                    //A new ArrayList is created to hold the best moves
                    bestMoves = new ArrayList<Move>();
                    //The current move is added to the ArrayList
                    bestMoves.add(move);
                }
                //Checks if the current move score is equal to the best move score
                else if (currentBestScore == bestScore) {
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
            setFixedSquares(flipBoard(game.getBoard()));
        }
        if (H1) {

            //Top right requires one rotation
            setFixedSquares(rotateBoard(game.getBoard(), 1));
            setFixedSquares(flipBoard(rotateBoard(game.getBoard(), 1)));
        }
        if (H8) {

            //Bottom right requires two rotations
            setFixedSquares(rotateBoard(game.getBoard(), 2));
            setFixedSquares(flipBoard(rotateBoard(game.getBoard(), 2)));
        }
        if (A8) {

            //Bottom left requires three rotations
            setFixedSquares(rotateBoard(game.getBoard(), 3));
            setFixedSquares(flipBoard(rotateBoard(game.getBoard(), 3)));
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

    //Method to flip the board
    private Square[][] flipBoard(Square[][] board) {

        //Initialise a new board to hold the flipped board
        Square[][] newBoard = new Square[board.length][board.length];

        //Loop through the board mapping (x, y) -> (y, x)
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                newBoard[j][i] = board[i][j];
            }
        }

        //Return the flipped board
        return newBoard;
    }
}