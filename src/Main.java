import java.awt.*;

/**
 * Main class - run this for a game using a GUI
 */
public class Main {

    public static void main(String[] args) {
        //Start the event dispatch thread and initialise the GUI
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI();
                gui.setUpGUI();
            }
        });
    }
}
