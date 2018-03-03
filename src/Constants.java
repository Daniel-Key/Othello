import java.awt.*;

/**
 * Class containing any final constants required throughout the program
 */
public class Constants {

    //Constants required for the GUI
    public static final int BOARD_SIZE = 8;
    public static final int WINDOW_SIZE = 800;
    public static final int MENU_HEIGHT = 200;
    public static final int MENU_IMAGE_HEIGHT = WINDOW_SIZE - MENU_HEIGHT;

    //Board colour constant
    public static final Color BOARD_COLOR = new Color(10, 156, 10);

    //Font constants
    public static final Font FONT = new Font("Arial", Font.BOLD, 14);
    public static final Font GAME_OVER_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 36);

    //Board square values
    public static final int CORNER_WEIGHTING = 10;
    public static final int EDGE_WEIGHTING = 6;
    public static final int A_SQUARE_WEIGHTING = 5;
    public static final int B_SQUARE_WEIGHTING = 7;
    public static final int MIDDLE_WEIGHTING = 6;
    public static final int X_SQUARE_WEIGHTING = 0;
    public static final int C_SQUARE_WEIGHTING = 1;
    public static final int INSIDE_EDGE_WEIGHTING = 2;
}
