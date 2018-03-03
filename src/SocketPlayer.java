import java.io.IOException;
import java.util.ArrayList;

/**
 * Class to take commands from a network player
 */
public class SocketPlayer extends Player {

    //Variable to count the number of invalid commands sent in a row
    private int invalidCommands;

    //Constructor to set the players colour, type and set initialise the number of invalid commands
    public SocketPlayer(GamePiece colour) {
        super(colour);
        playerType = PlayerType.SOCKET;
        invalidCommands = 0;
    }

    //Method to move the player
    @Override
    protected Move move(ArrayList<Move> validMoves) {

        //Check if the invalid commands is 2 and if it is, quit the game
        if (invalidCommands == 2) {
            game.getConnection().sendString("quit");
        }

        try {

            //Set the input to null and keep requesting an input until you get a non null input
            String input = null;
            while (input == null) {
                try {
                    input = game.getConnection().readInput();
                }
                catch (NullPointerException e) {
                    input = null;
                }
            }

            //Check if they have sent a move or something else
            if (!input.split(" ")[0].equals("move")) {

                //Check if any of the specified statements have been sent
                switch (input) {

                    //If I win is received, end the game with the opposition winning
                    case "I win":
                        game.gameOver(false, oppositionColour);
                        break;
                    //If you win is received, end the game with you winning
                    case "you win":
                        game.gameOver(false, colour);
                        break;
                    //If we draw is received, end the game as a draw
                    case "we draw":
                        game.gameOver(false, GamePiece.NONE);
                        break;
                    //If bye is received, quit the game
                    case "bye":
                        game.gameOver(true, GamePiece.NONE);
                        break;
                    //If resend is received, resend the last move
                    case "resend":
                        if (game.getLastMove() != null) {
                            game.getConnection().sendString(game.getLastMove().sendMove());
                        }
                        else {
                            game.getConnection().sendString("move pass");
                        }
                        break;
                    //If quit is received, quit the game with the opposition winning
                    case "quit":
                        game.gameOver(true, oppositionColour);
                        break;
                    //If none of these are picked up, ask the opponent to resend their move
                    default:
                        game.getConnection().sendString("resend");
                        invalidCommands++;
                        return move(validMoves);
                }
            }
            else {

                //If the first word is move but not pass, check if its a valid move
                if (!input.split(" ")[1].equals("pass")) {

                    //Get the move co-ordinate and take away the appropriate amount to convert the chars to the correct ints
                    String moveCoord = input.split(" ")[1];
                    int x = (int) (moveCoord.charAt(0)) - 97;
                    int y = (int) (moveCoord.charAt(1)) - 49;

                    //Loop through the valid moves and return the move if a valid one has been sent
                    for (Move move : validMoves) {
                        if (move.getX() == x && move.getY() == y) {
                            return move;
                        }
                    }
                    //If an invalid move was sent, end the game and send I win
                    game.getConnection().sendString("I win");
                    game.gameOver(true, oppositionColour);
                }
                else {

                    //Return null if they passed
                    return null;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        //Return null if nothing else is returned
        return null;
    }
}
