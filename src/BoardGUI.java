import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class to display the game board
 */
public class BoardGUI extends JPanel implements MouseListener, KeyListener {

    //Required variables
    private int squareWidth;
    private Game game;

    //Constructor for a local game
    public BoardGUI(PlayerType blackPlayerType, PlayerType whitePlayerType, GUI gui) {

        //Call a method to set the boards initial settings
        initialSetUp();

        //Initialise a new local game and run it
        game = new Game(blackPlayerType, whitePlayerType, gui);
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    public BoardGUI(Player player, IConnection connection, GUI gui) {

        //Call a method to set the boards initial settings
        initialSetUp();

        //Initialise a new network game and run it
        game = new Game(player, connection, gui);
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    //Method to set the boards initial settings and add listeners
    private void initialSetUp() {

        //Set the background, preferred size and layout of the board and make it focusable
        setBackground(Constants.BOARD_COLOR);
        setPreferredSize(new Dimension(Constants.WINDOW_SIZE, Constants.WINDOW_SIZE));
        setLayout(new GridBagLayout());
        setFocusable(true);

        //Add mouse and key listeners and set the square width
        addMouseListener(this);
        addKeyListener(this);
        squareWidth = Constants.WINDOW_SIZE/Constants.BOARD_SIZE;
    }

    //Overriding the paint component method to draw the board
    @Override
    protected void paintComponent(Graphics g) {

        //Calling the super method
        super.paintComponent(g);

        //Set the draw colour and draw the gridlines
        g.setColor(Color.white);
        for (int i = squareWidth; i <= Constants.WINDOW_SIZE; i += squareWidth) {
            g.drawLine(i, 0, i, Constants.WINDOW_SIZE);
        }
        for (int i = squareWidth; i <= Constants.WINDOW_SIZE; i += squareWidth) {
            g.drawLine(0, i, Constants.WINDOW_SIZE, i);
        }

        //Iterate through all the squares in the board and if they have a gamepiece, call a method to draw it
        for (int i = 0; i < game.getBoard().length; i++) {
            for (int j = 0; j < game.getBoard().length; j++) {
                GamePiece gamePiece = game.getBoard()[i][j].getGamePiece();
                if (gamePiece != GamePiece.NONE) {
                    drawGamePiece(gamePiece, g, i, j);
                }
            }
        }

        //Display the valid moves if the current player is human
        if (game.getValidMoves() != null && game.getCurrentPlayerType().equals(PlayerType.HUMAN)) {
            for (Move move : game.getValidMoves()) {
                drawLegalMove(g, move.getX(), move.getY());
            }
        }
    }

    //Method to draw a gamepiece
    private void drawGamePiece(GamePiece gamePiece, Graphics g, int x, int y) {

        //Set the colour to the correct gamepiece
        switch (gamePiece) {
            case WHITE:
                g.setColor(Color.white);
                break;
            case BLACK:
                g.setColor(Color.black);
                break;
        }

        //Draw the gamepiece inside the square
        g.fillOval(x * squareWidth + 1, y * squareWidth + 1, squareWidth - 2, squareWidth - 2);
    }

    //Method to draw the position of a legal move
    private void drawLegalMove(Graphics g, int x, int y) {

        //Setting the colour to red and drawing the circle
        g.setColor(Color.red);
        g.fillOval(x * squareWidth + squareWidth / 4, y * squareWidth + squareWidth / 4, squareWidth / 2, squareWidth / 2);
    }

    //Method to set the square that was clicked
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        game.setClicked(mouseEvent.getX() / squareWidth, mouseEvent.getY() / squareWidth);
    }

    //Unused MouseListener methods
    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    //Method to exit the game if escape is pressed
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            game.gameOver(true, GamePiece.NONE);
        }
    }

    //Unused KeyListener methods
    @Override
    public void keyPressed(KeyEvent e) {
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
}
