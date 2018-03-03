import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class to check for valid moves
 */
public class MoveChecker {

    //Required variables
    private static GamePiece opposingPiece;
    private static Square[][] board;

    //Setter to set the board to the current state
    public static void setBoard(Square[][] board) {
        MoveChecker.board = board;
    }

    //Method to check for valid moves
    public static ArrayList<Move> checkMove(GamePiece gamePiece) {

        //Check what the opposing gamepiece colour is and set it
        switch (gamePiece) {
            case BLACK:
                opposingPiece = GamePiece.WHITE;
                break;
            case WHITE:
                opposingPiece = GamePiece.BLACK;
        }

        //Return the valid moves
        return checkForMove(checkFreeSpots());
    }

    //Method to check for valid moves using a set of possible locations
    private static ArrayList<Move> checkForMove(HashSet<Position> possibleLocations) {

        //Initialise an ArrayList to store valid moves
        ArrayList<Move> validMoves = new ArrayList<Move>();

        //Loop through all the possible locations
        for (Position position: possibleLocations) {

            //set that this position isn't a valid move and initialise this position as a new move with an ArrayList to store the current direction
            boolean validMove = false;
            Move currentMove = new Move(position);
            ArrayList<Position> currentDirection;

            //Loop through all the squares in a 3x3 area around the possible location
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    //Set the current direction as an empty ArrayList
                    currentDirection = new ArrayList<Position>();

                    //Check if the current direction you are looking is an opposition piece
                    if (checkSpot(position.getX() + i, position.getY() + j, opposingPiece)) {

                        //Set that you can go farther in this direction and store the new x and y, adding this new position to the current direction
                        boolean moreMoves = true;
                        int newX = position.getX() + i;
                        int newY = position.getY() + j;
                        currentDirection.add(new Position( newX, newY));

                        //While there are more moves in the current direction, keep looping
                        while (moreMoves) {

                            //Increase the new x and y again in the current direction
                            newX += i;
                            newY += j;

                            //Check a location for what gamepiece is there
                            if (checkSpot(newX, newY, GamePiece.BLACK)) {

                                //If the opposing piece is white and you found a black, make the move valid, stop iterating in this direction
                                //and add the current direction to the current move
                                if (opposingPiece == GamePiece.WHITE) {
                                    validMove = true;
                                    moreMoves = false;
                                    for (Position tile: currentDirection) {
                                        currentMove.addPiece(tile);
                                    }
                                }
                                else {

                                    //Keep iterating in the current direction and add this position to that direction
                                    currentDirection.add(new Position( newX, newY));
                                }
                            }
                            else if (checkSpot(newX, newY, GamePiece.WHITE)) {

                                //If the opposing piece is black and you found a white, make the move valid, stop iterating in this direction
                                //and add the current direction to the current move
                                if (opposingPiece == GamePiece.BLACK) {
                                    validMove = true;
                                    moreMoves = false;
                                    for (Position tile: currentDirection) {
                                        currentMove.addPiece(tile);
                                    }
                                }
                                else {

                                    //Keep iterating in the current direction and add this position to that direction
                                    currentDirection.add(new Position( newX, newY));
                                }
                            }
                            else {

                                //Set that there are no more moves if an empty tile is found
                                moreMoves = false;
                            }
                        }
                    }
                }
            }

            //If the move is valid, add the current move to the list
            if (validMove) {
                validMoves.add(currentMove);
            }
        }

        //Return the list of valid moves
        return validMoves;
    }

    //Method to make a set of positions that could be possible moves
    private static HashSet<Position> checkFreeSpots() {

        //Initialise a HashSet to store the possible locations
        HashSet<Position> possibleLocations = new HashSet<Position>();

        //Store all the opposition tiles as an ArrayList
        ArrayList<Position> oppositionTiles = getOppositionTiles();

        //Loop through all opposition tiles an d get their x and y co-ordinates
        for (Position position: oppositionTiles) {
            int x = position.getX();
            int y = position.getY();

            //Loop through all the squares in a 3x3 area around the opposition square
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    //If the square is empty, add it to the set of positions
                    if (checkSpot(x + i, y + j, GamePiece.NONE)) {
                        possibleLocations.add(new Position(x + i, y + j));
                    }
                }
            }
        }

        //Return all the free squares beside opposition tiles
        return possibleLocations;
    }

    //Method to return all the opposition tile locations on the board
    private static ArrayList<Position> getOppositionTiles() {

        //Initialise an ArrayList to store opposition tile locations
        ArrayList<Position> oppositionTiles = new ArrayList<Position>();

        //Loop through every square on the board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {

                //If the square is an opposition piece, add this position to the ArrayList
                if (board[i][j].getGamePiece() == opposingPiece) {
                    oppositionTiles.add(new Position( i, j ));
                }
            }
        }

        //Return the positions
        return oppositionTiles;
    }

    //Method to check if a certain type of gamepiece is in a supplied square
    private static boolean checkSpot(int x, int y, GamePiece gamePiece) {

        //Check if the square is inside the bounds and the gamepiece type is correct if it is
        if (x >= 0 && x < board.length && y >= 0 && y < board.length && board[x][y].getGamePiece() == gamePiece) {
            return true;
        }

        return false;
    }
}
