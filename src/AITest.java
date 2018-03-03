import java.util.Arrays;

/**
 * Class to run multiple games between different AI for testing purposes
 */
public class AITest {
    //Integer representing the number of games to be played between each pair of AI players
    private final int testNumber = 1000;

    //Fields called in later methods
    private Player blackPlayer;
    private Player whitePlayer ;
    private Game game;
    private int blackWins;
    private int whiteWins;
    private int draws;
    double highestPercentage = 0;
    private int bestEdgeWeighting;
    private int bestInsideEdgeWeighting;
    private String constantsTestAI;

    //Main method to run AI tests without required a GUI
    public static void main(String[] args) {

        //Initialising an instance of an AITest
        AITest test = new AITest();

        //Only one of the methods below needs to be called at a time,
        //depending on the testing required

        //Method to compare AI classes without changing the positional weighting constants
        //test.run();

        //The String argument specifies which AI win-rate is being tested,
        //and the integer argument specifies the range of the constants to be tested
        //Recommended test number: 100
        test.run("Move reducer", 5);
    }

    //Method to compare AI classes without changing the positional weighting constants
    public void run() {

        //Each of the AI classes is in this array of players, but the ones not currently required in testing can be commented out
        Player[] players = {
//                new AI1(GamePiece.WHITE, false),
//                new AI2(GamePiece.WHITE, false),
//                new AI3(GamePiece.WHITE, false),
// AI4 currently causes exceptions when run
//                new AI4(GamePiece.WHITE, false),
                new AI5(GamePiece.WHITE, false),
//                new AI6(GamePiece.WHITE, false),
//                new AI7(GamePiece.WHITE, false),
//                new AI8(GamePiece.WHITE, false),
//                new AI9(GamePiece.WHITE, false),
                new AI10(GamePiece.WHITE, false),
                new AI11(GamePiece.WHITE, false),
        };

        //A pair of nested 'for' loops to play each player in the array against each other once as black, once as white
        //Loops through each player in the players array
        for (Player black: players) {
            //The current player's colour is set to black
            black.setColour(GamePiece.BLACK);
            //Loops through each player in the players array

            for (Player white: players) {
                //Checks that the player is not the same player as in the 'for' loop above

                if (!black.equals(white)) {
                    //If the players are different AI classes, the second player's colour is set to white
                    white.setColour(GamePiece.WHITE);

                    //The player fields at the start of the method are set to the current players
                    blackPlayer = black;
                    whitePlayer = white;

                    //The playGame method is called with the correct players
                    playGame();
                }
            }
        }

        //Sorts players based on total wins into descending order
        Arrays.sort(players);

        //For each player in the sorted array, test statistics are printed to console
        for (Player player: players) {
            System.out.println(player.getName());
            System.out.println("Wins: " + player.getWins());
            System.out.println("Losses: " + player.getLosses());
            System.out.println("Draws: " + player.getDraws());
            System.out.println("Win percentage: " + player.getWinPercentage());
            System.out.println();
        }

        //At the end of the output, player win percentages are printed for easy reference
        for (Player player: players) {
            System.out.println(player.getName() + ": " + player.getWinPercentage() + "%");
        }
    }

    //Method to compare AI classes with different sets of positional weighting constants
    public void run(String constantsTestAI, int testRange) {

        this.constantsTestAI = constantsTestAI;

        //Values to track the best win percentage and the best value of constants
        double bestWinPercentage = 0;
        int iBestValue = 0;
        int jBestValue = 0;

        for (int i = 0; i <= testRange; i++) {
            for (int j = 0; j < testRange; j++) {

                Player.edgeWeighting = i;
                Player.cornerWeighting = j;

                //Each of the AI classes is in this array of players, but the ones not currently required in testing can be commented out
                Player[] players = {
//                new AI1(GamePiece.WHITE, false),
//                new AI2(GamePiece.WHITE, false),
//                new AI3(GamePiece.WHITE, false),
// AI4 currently causes exceptions when run
//                new AI4(GamePiece.WHITE, false),
                        new AI5(GamePiece.WHITE, false),
//                new AI6(GamePiece.WHITE, false),
//                new AI7(GamePiece.WHITE, false),
//                new AI8(GamePiece.WHITE, false),
//                new AI9(GamePiece.WHITE, false),
                new AI10(GamePiece.WHITE, false),
//                new AI11(GamePiece.WHITE, false),
                };

                //A pair of nested 'for' loops to play each player in the array against each other once as black, once as white
                //Loops through each player in the players array
                for (Player black : players) {
                    //The current player's colour is set to black
                    black.setColour(GamePiece.BLACK);
                    //Loops through each player in the players array

                    for (Player white : players) {
                        //Checks that the player is not the same player as in the 'for' loop above

                        if (!black.equals(white)) {
                            //If the players are different AI classes, the second player's colour is set to white
                            white.setColour(GamePiece.WHITE);

                            //The player fields at the start of the method are set to the current players
                            blackPlayer = black;
                            whitePlayer = white;

                            //The playGame method is called with the correct players
                            playGame();
                        }
                    }
                }
                //Sorts players based on total wins into descending order
                Arrays.sort(players);

                //For each player in the sorted array, test statistics are printed to console
                for (Player player : players) {
                    System.out.println(player.getName());
                    System.out.println("Wins: " + player.getWins());
                    System.out.println("Losses: " + player.getLosses());
                    System.out.println("Draws: " + player.getDraws());
                    System.out.println("Win percentage: " + player.getWinPercentage());
                    System.out.println();
                }

                //At the end of the output, player win percentages are printed for easy reference
                for (Player player : players) {
                    System.out.println(player.getName() + ": " + player.getWinPercentage() + "%");
                }

                //Checks if the current player's win percentage is higher than the current best win percentage
                if (players[1].getWinPercentage() > bestWinPercentage) {
                    //If so it is set to the best win percentage
                    bestWinPercentage = players[1].getWinPercentage();
                    //The best values for the constants are set here
                    iBestValue = i;
                    jBestValue = j;
                }
            }
        }
        //After each combination of the constants is tested, the results are printed to console
        System.out.println();
        System.out.println("Highest win percentage: " + highestPercentage + "%");
        System.out.println("Edge weighting: " + bestEdgeWeighting);
        System.out.println("Corner weighting: " + bestInsideEdgeWeighting);
        System.out.println("Best win percentage = " + bestWinPercentage + "% at i = " + iBestValue + " and j = " + jBestValue);
    }

    //Method to increment the win count each time an AI wins a game
    private void addWinner(GamePiece colour) {

        //Switch statement for player colour
        //Increments wins and draws correctly
        switch (colour) {
            case BLACK:
                blackWins++;
                break;
            case WHITE:
                whiteWins++;
                break;
            default:
                draws++;
                break;
        }
    }

    //Method to play the specified number of games for each test
    private void playGame() {

        blackWins = 0;
        whiteWins = 0;
        draws = 0;

        //Iterates through the required number of tests and plays a game for each
        for (int i = 0; i < testNumber; i++) {
            //Sets up the game with the two AI players
            game = new Game(blackPlayer, whitePlayer);
            //Runs the game, calling the method in the Game class
            game.run();
            //Calls the addWinner method to increment the win count for the winning AI
            addWinner(game.getWinner());
        }

        //Prints out statistics for the match-up to console
        System.out.println(blackPlayer.getName() + " vs " + whitePlayer.getName());
        System.out.println(blackPlayer.getName() + ": " + blackWins);
        System.out.println(whitePlayer.getName() + ": " + whiteWins);
        System.out.println("Draw: " + draws);
        System.out.println("Win percentage: " + (double)blackWins * 100 / testNumber);
        System.out.println();

        //Sets the number of wins, draws and losses for each player
        blackPlayer.setWins(blackPlayer.getWins() + blackWins);
        blackPlayer.setLosses(blackPlayer.getLosses() + whiteWins);
        blackPlayer.setDraws(blackPlayer.getDraws() + draws);
        whitePlayer.setWins(whitePlayer.getWins() + whiteWins);
        whitePlayer.setLosses(whitePlayer.getLosses() + blackWins);
        whitePlayer.setDraws(whitePlayer.getDraws() + draws);

        //Sets the highest win percentage and constants weightings for the player being tested in the second run method
        if (blackPlayer.getName().equals(constantsTestAI)) {
            if (blackPlayer.getWinPercentage() > highestPercentage) {
                highestPercentage = ((double) blackWins * 100 / testNumber);
                bestEdgeWeighting = blackPlayer.edgeWeighting;
                bestInsideEdgeWeighting = blackPlayer.insideEdgeWeighting;
            }
        }
        if (whitePlayer.getName().equals(constantsTestAI)) {
            if (whitePlayer.getWinPercentage() > highestPercentage) {
                highestPercentage = ((double) whiteWins * 100 / testNumber);
                bestEdgeWeighting = whitePlayer.edgeWeighting;
                bestInsideEdgeWeighting = whitePlayer.insideEdgeWeighting;
            }
        }
    }
}
