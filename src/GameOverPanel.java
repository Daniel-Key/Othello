import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

/**
 * Class to display the game over screen
 */
public class GameOverPanel extends JPanel implements ActionListener {

    //The return to menu button
    private JButton menu = new JButton("Menu");

    //Required variables
    private GUI gui;
    private String[] gameOverInfo;

    //Constructor to display the game over text
    public GameOverPanel(GamePiece colour, int blackPieces, int whitePieces, GUI gui) {

        //Setting the gui and adding a mouse listener
        this.gui = gui;
        menu.addActionListener(this);

        //Setting the correct text for who the winner was
        String colourText;
        switch (colour) {
            case BLACK:
                colourText = "Black Won!";
                break;
            case WHITE:
                colourText = "White Won!";
                break;
            default:
                colourText = "It Was A Draw!";
                break;
        }

        //Set the background colour
        setBackground(Constants.BOARD_COLOR);

        //If both pieces are zero display the game quit message
        if (blackPieces == whitePieces && blackPieces == 0) {
            gameOverInfo = new String[2];
            gameOverInfo[0] = "Game Quit";
            if (colour != GamePiece.NONE) {
                gameOverInfo[1] = colourText;
            }
            else {
                gameOverInfo[1] = "";
            }
        }
        else {

            //Set the game over info to the winner and the number of each piece
            gameOverInfo = new String[3];
            gameOverInfo[0] = colourText;
            gameOverInfo[1] = "Black Pieces: " + blackPieces;
            gameOverInfo[2] = "White Pieces: " + whitePieces;
        }

        //Set the buttons font
        menu.setFont(Constants.FONT);

        //Add the button to the panel
        add(menu);
    }

    //Override paint component method to draw the game over text
    @Override
    protected void paintComponent(Graphics g) {

        //calling the super class method
        super.paintComponent(g);

        //Set the colour and font
        g.setColor(Color.WHITE);
        g.setFont(Constants.GAME_OVER_FONT);

        //Get the font metrics and the font height
        FontMetrics metrics = g.getFontMetrics();
        int h = metrics.getHeight();

        //Loop through the game over text and draw the text in the appropriate place
        for (int i = 0; i < gameOverInfo.length; i++) {

            //Gets the size of the current line and draw the message in the centre of the panel
            Rectangle2D r = metrics.getStringBounds(gameOverInfo[i], g);
            int x = (this.getWidth() - (int) r.getWidth()) / 2;
            int y = (this.getHeight() - (int) r.getHeight()) / 2;
            g.drawString(gameOverInfo[i], x, y + h * (i + 1));
        }
    }

    //Returning to the menu if the button is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        gui.showMenu();
    }
}
