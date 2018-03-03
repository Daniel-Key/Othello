import javax.swing.*;
import java.awt.*;

/**
 * Class to show the main image for the menu
 */
public class MenuImagePanel extends JPanel {

    //Required variable
    private Image menuImage;

    //constructor to set the correct dimensions of the component and load images
    public MenuImagePanel() {
        setPreferredSize(new Dimension(Constants.WINDOW_SIZE, Constants.MENU_IMAGE_HEIGHT));
        loadImages();
    }

    //Override method to paint the image to the JPanel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(menuImage, 1, 1, this);
    }

    //Method to load all required images
    private void loadImages() {

        ImageIcon menuIcon = new ImageIcon("resources/othelloMenuImage.png");
        menuImage = menuIcon.getImage();
    }
}
