/**
 * Class containing a constructor, getters and setters for a board square
 */
public class Square {

    private GamePiece gamePiece;
    //This field represents the value of a square to the player who plays there
    private int weighting;
    //This field shows whether or not a piece can be retaken- if not it is 'fixed'
    private boolean fixed;

    //Constructor for an empty square
    public Square() {
        gamePiece = GamePiece.NONE;
        fixed = false;
    }

    //Constructor for a square with a counter in it
    public Square(GamePiece gamePiece) {
        this.gamePiece = gamePiece;
    }

    //Getters and setters for GamePieces
    public GamePiece getGamePiece() {
        return gamePiece;
    }

    public void setGamePiece(GamePiece gamePiece) {
        this.gamePiece = gamePiece;
    }

    public int getWeighting() {
        return weighting;
    }

    public void setWeighting(int weighting) {
        this.weighting = weighting;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}
