import java.util.ArrayList;

/**
 * Abstract class containing the fields and methods required by all types of players
 */
//Implements Comparable to allow players to be compared and sorted
public abstract class Player implements Comparable {

    //Fields for the player
    protected GamePiece colour;
    protected PlayerType playerType;
    protected String name;

    //Fields related to the current state of the game
    protected GamePiece oppositionColour;
    protected ArrayList<Move> validMoves;
    protected Game game;

    //Coordinates of mouse clicks for a human player
    protected int xClicked;
    protected int yClicked;

    //Fields used in AI testing when large numbers of tests are run
    protected int wins;
    protected int losses;
    protected int draws;

    /**
     * WILL BE REMOVED
     */
    protected static int cornerWeighting = 2;
    protected static int middleWeighting = Constants.MIDDLE_WEIGHTING;
    protected static int edgeWeighting = 5;
    protected static int insideEdgeWeighting = Constants.INSIDE_EDGE_WEIGHTING;

    //Getters and setters for player-related fields
    public String getName() {
        return name;
    }

    public void setColour(GamePiece colour) {
        this.colour = colour;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public GamePiece getColour() {
        return colour;
    }

    //Getters and setters for AI testing
    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public double getWinPercentage() {
        return (double)wins * 100 / (wins + draws + losses);
    }

    //Constructor for a player of a given counter colour
    public Player(GamePiece colour) {
        this.colour = colour;
        setOppositionColour();
    }

    //Blank move method provided so that it can be overridden by AI classes which inherit from the Player class
    protected Move move(ArrayList<Move> validMoves) {
        return null;
    }

    //Constructor to easily set coordinates of a board square when clicked
    public void setClicked(int x, int y) {
        xClicked = x;
        yClicked = y;
    }

    //A simple method to set the correct opposition colour
    private void setOppositionColour() {
        switch (colour) {
            case BLACK:
                oppositionColour = GamePiece.WHITE;
                break;
            case WHITE:
                oppositionColour = GamePiece.BLACK;
                break;
        }
    }

    //An overridden method to allow AI players to be sorted during multiple AI testing
    @Override
    public int compareTo(Object o) {

        int wins = ((Player)o).getWins();

        if (wins > this.wins) {
            return 1;
        }
        else if (wins == this.getWins()) {
            return 0;
        }

        return -1;
    }
}
