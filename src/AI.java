import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * AI abstract class
 */
//Extends player, allowing it to inherit the relevant methods
public abstract class AI extends Player {

    //A field to represent whether or not a GUI is being used to display the game
    protected boolean gui;
    //Initialising a 2D integer array to the correct board size
    protected int[][] positionScores = new int[Constants.BOARD_SIZE][Constants.BOARD_SIZE];

    //Constructor for any AI player
    public AI(GamePiece colour, boolean gui) {
        //Sets the colour of the player
        super(colour);
        //Sets whether or not a GUI is being used
        this.gui = gui;
        //Sets the playerType to AI
        playerType = PlayerType.AI;
    }

    //A general move method to be overwritten in instantiable AI classes
    @Override
    protected Move move(ArrayList<Move> validMoves) {
        //If a GUI is displaying the game, the AI waits a second before
        //playing its move to allow the game to be spectated
        if (gui) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Can be overwritten to return the best move
        return null;
    }

    //A method to pick a random move, if there are several moves considered optimal by the AI
    protected Move pickRandomMove(ArrayList<Move> moves) {
        return moves.get(ThreadLocalRandom.current().nextInt(0,moves.size()));
    }

    //A method which can be overwritten if the AI needs to initialise a game at a certain point
    //Used in AI7
    @Override
    public void setGame(Game game) {
        super.setGame(game);
        setWeighting(game.getBoard());
    }

    //A method to set the correct tactical weighting of board positions
    protected void setWeighting(Square[][] board) {

        //Iterates through every square and initially sets them all to the middle square weighting constant
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                positionScores[i][j] = Constants.MIDDLE_WEIGHTING;
            }
        }

        //Calls the methods below to set the correct positional weightings
        setInsideEdgeWeighting(board);

        setEdgeWeighting(board);

        setCornerWeighting(board);

        setXSquareWeighting(board);

        setCSquareWeighting(board);

        //Iterates through the game board and sets the weightings of each position correctly
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                game.getBoard()[i][j].setWeighting(positionScores[i][j]);
            }
        }
    }

    //Sets the weighting of corner squares
    private void setCornerWeighting(Square[][] board) {

        positionScores[0][0] = Constants.CORNER_WEIGHTING;
        positionScores[0][board.length - 1] = Constants.CORNER_WEIGHTING;
        positionScores[board.length - 1][0] = Constants.CORNER_WEIGHTING;
        positionScores[board.length - 1][board.length - 1] = Constants.CORNER_WEIGHTING;

    }

    //Sets the weighting of edge squares
    private void setEdgeWeighting(Square[][] board) {
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            positionScores[0][i] = Constants.EDGE_WEIGHTING;
            positionScores[i][0] = Constants.EDGE_WEIGHTING;
            positionScores[Constants.BOARD_SIZE-1][i] = Constants.EDGE_WEIGHTING;
            positionScores[i][Constants.BOARD_SIZE-1] = Constants.EDGE_WEIGHTING;
        }
    }

    //Sets the weighting of edge squares two spaces from the corners for AI7
    protected void setASquareWeighting(Square[][] board) {

        positionScores[0][2] = Constants.A_SQUARE_WEIGHTING;
        positionScores[2][0] = Constants.A_SQUARE_WEIGHTING;
        positionScores[0][board.length - 3] = Constants.A_SQUARE_WEIGHTING;
        positionScores[2][board.length - 1] = Constants.A_SQUARE_WEIGHTING;
        positionScores[board.length - 1][2] = Constants.A_SQUARE_WEIGHTING;
        positionScores[board.length - 3][0] = Constants.A_SQUARE_WEIGHTING;
        positionScores[board.length - 1][board.length - 3] = Constants.A_SQUARE_WEIGHTING;
        positionScores[board.length - 3][board.length - 1] = Constants.A_SQUARE_WEIGHTING;
    }

    //Sets the weighting of the middle two edge squares for AI7
    protected void setBSquareWeighting(Square[][] board) {

        positionScores[0][4] = Constants.B_SQUARE_WEIGHTING;
        positionScores[0][5] = Constants.B_SQUARE_WEIGHTING;
        positionScores[4][0] = Constants.B_SQUARE_WEIGHTING;
        positionScores[5][0] = Constants.B_SQUARE_WEIGHTING;
        positionScores[7][4] = Constants.B_SQUARE_WEIGHTING;
        positionScores[7][5] = Constants.B_SQUARE_WEIGHTING;
        positionScores[4][7] = Constants.B_SQUARE_WEIGHTING;
        positionScores[5][7] = Constants.B_SQUARE_WEIGHTING;
    }

    //Sets the weighting of squares one space from the edge
    private void setInsideEdgeWeighting(Square[][] board) {

        for (int i = 1; i < Constants.BOARD_SIZE-1; i++) {
            positionScores[1][i] = Constants.INSIDE_EDGE_WEIGHTING;
            positionScores[i][1] = Constants.INSIDE_EDGE_WEIGHTING;
            positionScores[Constants.BOARD_SIZE-2][i] = Constants.INSIDE_EDGE_WEIGHTING;
            positionScores[i][Constants.BOARD_SIZE-2] = Constants.INSIDE_EDGE_WEIGHTING;
        }
    }

    //Sets the weighting of the squares one square diagonally from the corner
    private void setXSquareWeighting(Square[][] board) {

        positionScores[1][1] = Constants.X_SQUARE_WEIGHTING;
        positionScores[1][board.length - 2] = Constants.X_SQUARE_WEIGHTING;
        positionScores[board.length - 2][1] = Constants.X_SQUARE_WEIGHTING;
        positionScores[board.length - 2][board.length - 2] = Constants.X_SQUARE_WEIGHTING;
    }

    //Sets the weighting of the edge squares one square from the corner
    private void setCSquareWeighting(Square[][] board) {

        positionScores[0][1] = Constants.C_SQUARE_WEIGHTING;
        positionScores[1][0] = Constants.C_SQUARE_WEIGHTING;
        positionScores[0][board.length - 2] = Constants.C_SQUARE_WEIGHTING;
        positionScores[1][board.length - 1] = Constants.C_SQUARE_WEIGHTING;
        positionScores[board.length - 1][1] = Constants.C_SQUARE_WEIGHTING;
        positionScores[board.length - 2][0] = Constants.C_SQUARE_WEIGHTING;
        positionScores[board.length - 1][board.length - 2] = Constants.C_SQUARE_WEIGHTING;
        positionScores[board.length - 2][board.length - 1] = Constants.C_SQUARE_WEIGHTING;
    }
}
