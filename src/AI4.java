import java.util.ArrayList;

/**
 * Basic look-ahead
 *
 * Looks ahead and picks the best move from a certain depth
 * (Very broken)
 *
 * Only method headers commented due to the fact it causes exceptions when run
 */
public class AI4 extends AI {

    //Requires variables
    private int lookAheadDepth = 2;
    private int bestWorstMoveScore;
    private ArrayList<Move> bestMoves;
    private Square[][] board;

    //Constructor to set the name and type of the AI
    public AI4(GamePiece colour, boolean gui) {

        super(colour, gui);
        playerType = PlayerType.AI;
        name = "Basic look ahead";
    }

    //Method to return a move from the list of valid moves
    @Override
    public Move move(ArrayList<Move> validMoves) {

        //Initialise arraylist to store best move
        bestWorstMoveScore = 0;
        bestMoves = new ArrayList<Move>();

        //Check that there is a valid move
        if (validMoves.size() > 0) {

            //Calls the super class method to introduce a time delay if a gui is in use
            super.move(null);

            //Initialise a new board and copy the old board into it
            board = new Square[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    board[i][j] = new Square(game.getBoard()[i][j].getGamePiece());
                }
            }
            //Pick the best move out of the valid moves
            pickBestMove(validMoves, 0);

            //If multiple moves are equal, pick a random one of them
            return pickRandomMove(bestMoves);
        }
        return null;
    }

    //Method to be recursively called, picking the best move
    private int pickBestMove(ArrayList<Move> validMoves, int currentDepth) {

        int currentBestWorstMoveScore = 0;
        if (currentDepth < lookAheadDepth) {
            if (validMoves.size() > 0) {
                for (Move move: validMoves) {
                    makeMove(move, board, colour);

                    MoveChecker.setBoard(board);

                    int score = makeOppositionMove(currentDepth);
                    if (currentDepth == 0) {
                        move.setMoveScore(score);
                    }
                    else if (score < currentBestWorstMoveScore) {
                        currentBestWorstMoveScore = score;
                    }
                }
            }
            else {
                int score = makeOppositionMove(currentDepth);
                if (score < currentBestWorstMoveScore) {
                    currentBestWorstMoveScore = score;
                }
            }
        }
        else if (currentDepth >= lookAheadDepth) {
            if (validMoves.size() > 0) {
                currentBestWorstMoveScore = simulateMove(validMoves);
            }
        }

        if (currentDepth == 0) {
            for (Move move: validMoves) {
                if (move.getMoveScore() > bestWorstMoveScore) {
                    bestMoves = new ArrayList<Move>();
                    bestMoves.add(move);
                }
                else if (move.getMoveScore() == bestWorstMoveScore){
                    bestMoves.add(move);
                }
            }
        }

        return currentBestWorstMoveScore;
    }

    //Method to make an opposition move
    private int makeOppositionMove(int currentDepth) {

        GamePiece oppositionColour = GamePiece.BLACK;
        switch (colour) {
            case BLACK:
                oppositionColour = GamePiece.WHITE;
                break;
            case WHITE:
                oppositionColour = GamePiece.BLACK;
                break;
        }

        ArrayList<Move> oppositionMoves = MoveChecker.checkMove(oppositionColour);
        if (oppositionMoves != null) {
            for (Move oppositionMove : oppositionMoves) {

                board = makeMove(oppositionMove, board, oppositionColour);

                MoveChecker.setBoard(board);

                pickBestMove(MoveChecker.checkMove(colour), currentDepth + 1);
            }
        }

        return pickBestMove(MoveChecker.checkMove(colour), currentDepth + 1);
    }

    //Method to make a move and return the new board
    private Square[][] makeMove(Move move, Square[][] board, GamePiece colour) {

        board[move.getX()][move.getY()].setGamePiece(colour);
        for (int i = 0; i < move.getFlippedPieces().size(); i++) {
            board[move.getFlippedPieces().get(i).getX()][move.getFlippedPieces().get(i).getY()].setGamePiece(colour);
        }

        return board;
    }

    //Method to simulate a set of moves and return the score of the worst one
    private int simulateMove(ArrayList<Move> validMoves) {

        int worstMoveScore = 1000;

        for (Move move: validMoves) {
            scoreMove(move);
            if (move.getMoveScore() < worstMoveScore) {
                board = makeMove(move, board, colour);
                worstMoveScore = move.getMoveScore();
            }
        }

        return worstMoveScore;
    }

    //Method to score a move based on position and the ratio of pieces before and after
    private void scoreMove(Move move) {

        int pieceScore = 0;
        double initialPieceRatio = game.getBlackPieces() / game.getWhitePieces();
        double finalPieceRatio = 0;

        int blackPieces = 0;
        int whitePieces = 0;

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

        if (blackPieces != 0 && whitePieces != 0) {
            switch (colour) {
                case BLACK:
                    finalPieceRatio = blackPieces / whitePieces;
                    break;
                case WHITE:
                    finalPieceRatio = whitePieces / blackPieces;
                    break;
            }
        }

        pieceScore = (int)(finalPieceRatio * 10/ initialPieceRatio);

        int positionScore = game.getBoard()[move.getX()][move.getY()].getWeighting();

        move.setMoveScore(pieceScore + positionScore);
    }
}