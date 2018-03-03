import java.util.ArrayList;

/**
 * Class containing a constructor and relevant fields and methods for a move
 */
public class Move {

    //Fields relevant to a move
    private int x;
    private int y;

    //ArrayList of positions of the pieces which will be changed by a move
    private ArrayList<Position> flippedPieces;

    //This represents the value of a move, which differs depending on which
    //AI class and weighting system is used
    private int moveScore;

    //Constructor for a move
    public Move(Position position) {

        x = position.getX();
        y = position.getY();

        //Setting flippedPieces to a new ArrayList
        flippedPieces = new ArrayList<Position>();
    }

    //Returns a move in the format required by the protocol for a server-client game
    public String sendMove() {
        char xCoord = (char)(x + 97);
        int yCoord = y + 1;
        return "move " + xCoord + yCoord;
    }

    //Getters and setters for move fields
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ArrayList<Position> getFlippedPieces() {
        return flippedPieces;
    }

    public int getMoveScore() {
        return moveScore;
    }

    public void addPiece(Position piece) {
        flippedPieces.add(piece);
    }

    public void setMoveScore(int moveScore) {
        this.moveScore = moveScore;
    }

}
