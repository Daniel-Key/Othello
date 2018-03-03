import javax.swing.*;
import java.awt.*;

/**
 * GUI class which controls the gui
 */
public class GUI {

    private JFrame gui;

    //Method to set up the gui initially and then display the menu
    public void setUpGUI() {

        //Initialise the JFrame and make it visible
        gui = new JFrame();
        gui.setVisible(true);

        //Set the minimum window size and make it not resizable
        gui.setResizable(false);
        gui.pack();
        gui.setMinimumSize(new Dimension(Constants.WINDOW_SIZE, Constants.WINDOW_SIZE));

        //Display the menu
        showMenu();

        //Set the JFrame's location to the centre of the screen and give it a title and make it exit on close
        gui.setTitle("Othello");
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setLocationRelativeTo(null);
    }

    //Method to start a local game
    public void startGame(PlayerType blackPlayerType, PlayerType whitePlayerType) {

        //Initialise the board GUI with the appropriate players
        BoardGUI board = new BoardGUI(blackPlayerType, whitePlayerType, this);

        //Display the board
        setUpBoard(board);
    }

    //Method to start a java sockets game
    public void startGame(Player player, IConnection connection) {

        //Initialise the board GUI with a local player and a server or client
        BoardGUI board = new BoardGUI(player, connection, this);

        //Display the board
        setUpBoard(board);
    }

    //Method to display the board
    private void setUpBoard(BoardGUI board) {

        //Remove existing components from the GUI
        gui.getContentPane().removeAll();

        //Enable the board and add it to the GUI
        board.setEnabled(true);
        gui.add(board);
        gui.pack();

        //Put the board in focus and draw it
        board.requestFocusInWindow();
        board.repaint();
    }

    //Method to display the menu
    public void showMenu() {

        //Initialise new split pane
        JSplitPane menuSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new MenuImagePanel(), new Menu(this));
        menuSplitPane.setEnabled(false);

        //Remove existing components from the GUI
        gui.getContentPane().removeAll();

        //Add the menu to the split pane
        gui.add(menuSplitPane);
        gui.pack();
    }

    //Method to display the game over screen
    public void gameOver(GamePiece colour, int blackPieces, int whitePieces) {

        //Initialise the game over screen
        GameOverPanel gameOverPanel = new GameOverPanel(colour, blackPieces, whitePieces, this);
        gameOverPanel.setEnabled(true);

        //Remove existing components from the GUI
        gui.getContentPane().removeAll();

        //Add the game over screen to the GUI
        gui.add(gameOverPanel);
        gui.pack();

        //Focus the game over screen and display it
        gameOverPanel.requestFocusInWindow();
        gameOverPanel.repaint();
    }

    //Method to redisplay the GUI
    public void repaint(){
        gui.getContentPane().revalidate();
        gui.getContentPane().repaint();
    }
}
