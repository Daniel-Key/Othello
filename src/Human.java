import java.util.ArrayList;

/**
 * Class for an instantiable human player
 */
public class Human extends Player {

    //Constructor for a human player of a given counter colour
    public Human(GamePiece colour) {

        //Sets the player colour using the superclass constructor
        super(colour);
        playerType = PlayerType.HUMAN;
    }

    //Overridden method from the Player superclass
    @Override
    public Move move(ArrayList<Move> validMoves) {

        //Sets turn taken to be false
        boolean turnTaken = false;
        //Sets xClicked and yClicked to be out of the scope of legal moves,
        //so that the human move is not taken until the mouse is clicked
        xClicked = 10;
        yClicked = 10;

        //Checks that the turn has not been taken, and that there is a valid move available
        while (!turnTaken && validMoves.size() > 0) {
            //Constructs a 'for' loop to iterate through all valid moves
            for (Move move: validMoves) {
                //Checks that the move being made is the same position as the board square being clicked
                if (move.getX() == xClicked && move.getY() == yClicked) {
                    //If so the move is the correct one and is returned by the method
                    return move;
                }
            }
        }
        return null;
    }

}
