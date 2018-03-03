import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class to display the menu options for the game
 */
public class Menu extends JPanel implements ActionListener{

    //Start button
    private JButton startButton = new JButton("Start");

    //The label and buttons for deciding the black player's type in a local game
    private JLabel blackPlayerLabel = new JLabel("Black Player");
    private JRadioButton blackPlayerHuman = new JRadioButton("Human", true);
    private JRadioButton blackPlayerAI = new JRadioButton("AI", false);
    private ButtonGroup blackButtonGroup = new ButtonGroup();

    //The label and buttons for deciding the white player's type in a local game
    private JLabel whitePlayerLabel = new JLabel("White Player");
    private JRadioButton whitePlayerHuman = new JRadioButton("Human", true);
    private JRadioButton whitePlayerAI = new JRadioButton("AI", false);
    private ButtonGroup whiteButtonGroup = new ButtonGroup();

    //The label and buttons for deciding the type of game that gets played
    private JLabel gameTypeLabel = new JLabel("Game Type");
    private JRadioButton localGame = new JRadioButton("Local Game", true);
    private JRadioButton serverGame = new JRadioButton("Game as Server", false);
    private JRadioButton clientGame = new JRadioButton("Game as Client", false);
    private ButtonGroup gameTypeGroup = new ButtonGroup();

    //The possible AIs for a server or client game
    private Player AI1 = new AI1(GamePiece.WHITE, true);
    private Player AI2 = new AI5(GamePiece.WHITE, true);
    private Player AI3 = new AI9(GamePiece.WHITE, true);
    private Player AI4 = new AI11(GamePiece.WHITE, true);

    //The label and buttons for deciding which AI is used
    private JLabel playerTypeLabel = new JLabel("Player Type");
    private JRadioButton human = new JRadioButton("Human", true);
    private JRadioButton AI1Button = new JRadioButton(AI1.getName(), false);
    private JRadioButton AI2Button = new JRadioButton(AI2.getName(), false);
    private JRadioButton AI3Button = new JRadioButton(AI3.getName(), false);
    private JRadioButton AI4Button = new JRadioButton(AI4.getName(), false);
    private ButtonGroup playerTypeGroup = new ButtonGroup();

    //The label and text area for providing the host name
    private JLabel hostNameLabel = new JLabel("Host Name");
    private JTextArea hostName = new JTextArea();

    //Required variables
    private GUI gui;
    private boolean local;

    //Contstructor that takes a gui object as a parameter
    public Menu(GUI gui) {

        this.gui = gui;

        //Setting the background, preferred size and layout of the menu
        setBackground(Color.white);
        setPreferredSize(new Dimension(Constants.WINDOW_SIZE, Constants.MENU_HEIGHT));
        setLayout(new GridBagLayout());

        //Adding action listeners to all the buttons
        startButton.addActionListener(this);
        blackPlayerHuman.addActionListener(this);
        blackPlayerAI.addActionListener(this);
        whitePlayerHuman.addActionListener(this);
        whitePlayerAI.addActionListener(this);
        localGame.addActionListener(this);
        serverGame.addActionListener(this);
        clientGame.addActionListener(this);
        human.addActionListener(this);
        AI1Button.addActionListener(this);
        AI2Button.addActionListener(this);
        AI3Button.addActionListener(this);
        AI4Button.addActionListener(this);

        //Setting the fonts for all the components
        setFont();

        //Adding radio buttons to their corresponding groups
        gameTypeGroup.add(localGame);
        gameTypeGroup.add(serverGame);
        gameTypeGroup.add(clientGame);
        blackButtonGroup.add(blackPlayerHuman);
        blackButtonGroup.add(blackPlayerAI);
        whiteButtonGroup.add(whitePlayerHuman);
        whiteButtonGroup.add(whitePlayerAI);
        playerTypeGroup.add(human);
        playerTypeGroup.add(AI1Button);
        playerTypeGroup.add(AI2Button);
        playerTypeGroup.add(AI3Button);
        playerTypeGroup.add(AI4Button);

        //Making a border for the hostname box and applying it
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        hostName.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        //Setting that the game is local by default
        local = true;

        //calling a method to add the components to the menu
        addComponents();
    }

    //Method to set the font for the components
    public void setFont() {

        startButton.setFont(Constants.FONT);
        blackPlayerLabel.setFont(Constants.FONT);
        blackPlayerHuman.setFont(Constants.FONT);
        blackPlayerAI.setFont(Constants.FONT);
        whitePlayerLabel.setFont(Constants.FONT);
        whitePlayerHuman.setFont(Constants.FONT);
        whitePlayerAI.setFont(Constants.FONT);
        gameTypeLabel.setFont(Constants.FONT);
        localGame.setFont(Constants.FONT);
        serverGame.setFont(Constants.FONT);
        clientGame.setFont(Constants.FONT);
        playerTypeLabel.setFont(Constants.FONT);
        human.setFont(Constants.FONT);
        AI1Button.setFont(Constants.FONT);
        AI2Button.setFont(Constants.FONT);
        AI3Button.setFont(Constants.FONT);
        AI4Button.setFont(Constants.FONT);
        hostNameLabel.setFont(Constants.FONT);
        hostName.setFont(Constants.FONT);
    }

    //Detecting any action events and giving the required response
    @Override
    public void actionPerformed(ActionEvent e) {

        //Checking for the three events a response is required for
        if (e.getSource() == startButton) {

            //calling a method to start the game
            startGame();
        }
        else if (e.getSource() == localGame && localGame.isSelected()) {

            //Setting the game to local and adding the required components
            local = true;
            addComponents();
        }
        else if ((e.getSource() == serverGame && serverGame.isSelected()) || (e.getSource() == clientGame && clientGame.isSelected())) {

            //Setting the game to not be local and adding the required components
            local = false;
            addComponents();
        }
    }

    //Method to start the game with the supplied parameters
    private void startGame() {

        //Check to see if the game is local
        if (local) {

            //Setting both players to be AI before checking if they should be human
            PlayerType blackPlayerType = PlayerType.AI;
            PlayerType whitePlayerType = PlayerType.AI;

            if (blackPlayerHuman.isSelected()) {
                blackPlayerType = PlayerType.HUMAN;
            }
            if (whitePlayerHuman.isSelected()) {
                whitePlayerType = PlayerType.HUMAN;
            }

            //Initialise a game with the correct player types
            gui.startGame(blackPlayerType, whitePlayerType);
        }
        else {

            //Initialise a connection of the required type (Client or Server)
            IConnection connection;
            boolean server;
            if (serverGame.isSelected()) {
                connection = new Server();
                server = true;
            }
            else {
                connection = new Client(hostName.getText());
                server = false;
            }

            //Only continue if a connection is made
            if (connection.connect()) {

                //Initialise a local player with the correct colour of gamepiece
                Player player = initialisePlayer(server);

                //Initialising a game with the supplied player and connection
                gui.startGame(player, connection);

            }
        }
    }

    //Method to initialise a local player for a server or client game
    private Player initialisePlayer(boolean server) {

        //Set the correct gamepiece
        GamePiece gamePiece;
        if (server) {
            gamePiece = GamePiece.WHITE;
        }
        else {
            gamePiece = GamePiece.BLACK;
        }

        //Declare a player and set it to the supplied preference
        Player player;
        if (human.isSelected()) {
            player = new Human(gamePiece);
        }
        else if (AI1Button.isSelected()) {
            player = AI1;
        }
        else if (AI2Button.isSelected()) {
            player = AI2;
        }
        else if (AI3Button.isSelected()) {
            player = AI3;
        }
        else {
            player = AI4;
        }

        //Set the players gamepiece and returnm the player
        player.setColour(gamePiece);
        return player;
    }

    //Method to add components to the menu
    private void addComponents() {

        //Remove all previous components
        removeAll();

        //Initialise a new set of constraints
        GridBagConstraints c = new GridBagConstraints();

        //Set the default constraint settings
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;

        //Add the start button
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        add(startButton, c);
        c.gridwidth = 1;

        //Add the network game options
        c.gridy = 1;
        add(gameTypeLabel, c);
        c.gridy = 2;
        add(localGame, c);
        c.gridy = 3;
        add(serverGame, c);
        c.gridy = 4;
        add(clientGame, c);

        //Check if the game is local
        if (local) {

            //Initialise an array of the black and white player type components
            Component[] components = new Component[] {
                    blackPlayerLabel,
                    blackPlayerHuman,
                    blackPlayerAI,
                    whitePlayerLabel,
                    whitePlayerHuman,
                    whitePlayerAI
            };

            //Add these to the menu
            addExtraComponents(components, c);
        }
        else {

            //Initialise an array of the player type components for a network game
            Component[] components = new Component[] {
                    playerTypeLabel,
                    human,
                    AI1Button,
                    AI2Button,
                    AI3Button,
                    AI4Button,
            };

            //Add these to the menu
            addExtraComponents(components, c);

            //If its a client game, add the hostname text area
            if (clientGame.isSelected()) {
                c.gridx = 1;
                c.gridy = 4;
                add(hostNameLabel, c);
                c.gridx = 2;
                add(hostName, c);
            }
        }

        //Repaint the GUI
        gui.repaint();
    }

    //Method to add the second and third columns to the menu
    private void addExtraComponents(Component[] components, GridBagConstraints c) {

        //Add column 2
        c.gridx = 1;
        c.gridy = 1;
        add(components[0], c);
        c.gridy = 2;
        add(components[1], c);
        c.gridy = 3;
        add(components[2], c);

        //Add column 3
        c.gridx = 2;
        c.gridy = 1;
        add(components[3], c);
        c.gridy = 2;
        add(components[4], c);
        c.gridy = 3;
        add(components[5], c);
    }
}