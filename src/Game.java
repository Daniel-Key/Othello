import java.util.ArrayList;

/**
 * Class to control the game itself
 */
public class Game implements Runnable{

    //Required variables
    private Square[][] board;
    private boolean gameOver;
    private GamePiece winner;
    private Player blackPlayer;
    private Player whitePlayer;
    private int blackPieces;
    private int whitePieces;
    private GUI gui;
    private ArrayList<Move> validMoves;
    private boolean testMode;
    private PlayerType currentPlayerType;
    private IConnection connection;
    private GamePiece socketGamePiece;
    private Move lastMove;

    //Getters and setters
    public int getBlackPieces() {
        return blackPieces;
    }

    public int getWhitePieces() {
        return whitePieces;
    }

    public GamePiece getWinner() {
        return winner;
    }

    public Square[][] getBoard() {
        return board;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public ArrayList<Move> getValidMoves() {
        return validMoves;
    }

    public PlayerType getCurrentPlayerType() {
        return currentPlayerType;
    }

    public IConnection getConnection() {
        return connection;
    }

    public Move getLastMove() {
        return lastMove;
    }

    //Method to initialise the board
    private void initialiseBoard() {

        //Initialising the board with the correct dimensions
        board = new Square[Constants.BOARD_SIZE][Constants.BOARD_SIZE];

        //Filling the board with new squares
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = new Square();
            }
        }

        //Setting the board in the default configuration
        board[(Constants.BOARD_SIZE / 2) - 1][(Constants.BOARD_SIZE / 2) - 1].setGamePiece(GamePiece.WHITE);
        board[(Constants.BOARD_SIZE / 2) - 1][Constants.BOARD_SIZE / 2].setGamePiece(GamePiece.BLACK);
        board[Constants.BOARD_SIZE / 2][(Constants.BOARD_SIZE / 2) - 1].setGamePiece(GamePiece.BLACK);
        board[Constants.BOARD_SIZE / 2][Constants.BOARD_SIZE / 2].setGamePiece(GamePiece.WHITE);
    }

    //Method to initialise the players for a local game
    private void initialisePlayers(PlayerType blackPlayerType, PlayerType whitePlayerType) {

        //Setting the black player to the appropriate type
        switch (blackPlayerType) {
            case HUMAN:
                blackPlayer = new Human(GamePiece.BLACK);
                break;
            case AI:
                blackPlayer = new AI5(GamePiece.BLACK, true);
                blackPlayer.setGame(this);
                break;
        }

        //Setting the white player to the appropriate type
        switch (whitePlayerType) {
            case HUMAN:
                whitePlayer = new Human(GamePiece.WHITE);
                break;
            case AI:
                whitePlayer = new AI5(GamePiece.WHITE, true);
                whitePlayer.setGame(this);
                break;
        }
    }

    //Socket constructor
    public Game(Player player, IConnection connection, GUI gui) {

        //If the connection is a server, make the black player the socket player
        if (Server.class.isInstance(connection)) {
            whitePlayer = player;
            blackPlayer = new SocketPlayer(GamePiece.BLACK);
            socketGamePiece = GamePiece.BLACK;
        }
        //If the connection is a client, make the white player the socket player
        else {
            blackPlayer = player;
            whitePlayer = new SocketPlayer(GamePiece.WHITE);
            socketGamePiece = GamePiece.WHITE;
        }

        //Set the connection and gui and initialise the board
        this.connection = connection;
        this.gui = gui;
        testMode = false;
        gameOver = false;
        initialiseBoard();
        blackPlayer.setGame(this);
        whitePlayer.setGame(this);
        countPieces();
    }

    //GUI constructor
    public Game(PlayerType blackPlayerType, PlayerType whitePlayerType, GUI gui) {

        //Set the gui and initialise the board and players
        this.gui = gui;
        testMode = false;
        gameOver = false;
        socketGamePiece = GamePiece.NONE;
        initialiseBoard();
        initialisePlayers(blackPlayerType, whitePlayerType);
        countPieces();
    }

    //Testing constructor
    public Game(Player blackPlayer, Player whitePlayer) {

        //Set the players and initialise the board
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        gameOver = false;
        testMode = true;
        socketGamePiece = GamePiece.NONE;
        initialiseBoard();
        this.blackPlayer.setGame(this);
        this.whitePlayer.setGame(this);
        countPieces();
    }

    //Run method which contains the game loop
    public void run() {

        //Counter that can be incremented if a move is made
        int movesMade;

        //Loop while the game isn't over
        while (!gameOver) {

            movesMade = 0;

            //Take a turn for the black player and increment the moves made if it makes a move
            if (takeTurn(blackPlayer)) {
                movesMade++;
            }

            //Take a turn for the white player and increment the moves made if it makes a move
            if (takeTurn(whitePlayer)) {
                movesMade++;
            }

            //End the game if no moves were made in a single loop
            if (movesMade == 0) {
                gameOver = true;
            }
        }

        //Call gameover
        gameOver(false, GamePiece.BLACK);
    }

    //Method to take a turn for a player and return a boolean
    private Boolean takeTurn(Player player) {

        //Set the board for the move checker and get the valid moves
        MoveChecker.setBoard(board);
        validMoves = MoveChecker.checkMove(player.getColour());

        //Set the current player type and update the gui
        currentPlayerType = player.getPlayerType();
        updateGUI();

        //Make a move and count the pieces after the move
        boolean moveMade = move(player.move(validMoves) , player.getColour());
        countPieces();

        //Return if a move was made
        return moveMade;
    }

    //Method to make a supplied move and return true if a move was made
    private Boolean move(Move move, GamePiece colour) {

        //Check the move isn't null
        if (move != null) {

            //Send the move to the other player if you are playing a network game
            if (socketGamePiece == getOpposite(colour)) {
                lastMove = move;
                connection.sendString((move.sendMove()));
            }

            //Set the board to its new position
            board[move.getX()][move.getY()].setGamePiece(colour);
            for (int i = 0; i < move.getFlippedPieces().size(); i++) {
                board[move.getFlippedPieces().get(i).getX()][move.getFlippedPieces().get(i).getY()].setGamePiece(colour);
            }

            //Return true that a move was made
            return true;
        }
        else {

            //Send that no move was made if it is a network game and then return false
            if (socketGamePiece == getOpposite(colour)) {
                lastMove = move;
                connection.sendString("move pass");
            }
            return false;
        }
    }

    //Method to set the square clicked
    public void setClicked(int xClicked, int yClicked) {

        blackPlayer.setClicked(xClicked, yClicked);
        whitePlayer.setClicked(xClicked, yClicked);
    }

    //Method to count the pieces of each colour
    private void countPieces() {

        blackPieces = 0;
        whitePieces = 0;

        //Loop through the board and increment the appropriate colour if you find a piece
        for (Square[] row: board) {
            for (Square square: row) {
                switch (square.getGamePiece()) {
                    case BLACK:
                        blackPieces++;
                        break;
                    case WHITE:
                        whitePieces++;
                        break;
                }
            }
        }
    }

    //Method to end the game
    public void gameOver(boolean wasQuit, GamePiece colour) {

        //Close the connection
        if (connection != null) {
            connection.close();
        }

        //If the game was quit pass this to the gui
        if (wasQuit) {
            gui.gameOver(colour, 0, 0);
        }
        else {

            //Count the piece difference and decide the winner before sending this to the gui
            int pieceDifference = blackPieces - whitePieces;

            if (pieceDifference > 0) {
                winner = GamePiece.BLACK;
            } else if (pieceDifference < 0) {
                winner = GamePiece.WHITE;
            } else {
                winner = GamePiece.NONE;
            }

            if (!testMode) {
                gui.gameOver(winner, blackPieces, whitePieces);
            }
        }
    }

    //Method to repaint the gui if the game isn't in test mode
    private void updateGUI() {

        if (!testMode) {
            gui.repaint();
        }
    }

    //Method to return the opposite colour of the current game piece
    private GamePiece getOpposite(GamePiece colour) {

        GamePiece opposite = GamePiece.WHITE;
        if (colour == GamePiece.WHITE) {
            opposite = GamePiece.BLACK;
        }
        return opposite;
    }
}
