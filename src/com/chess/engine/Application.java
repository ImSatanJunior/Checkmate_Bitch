package com.chess.engine;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.Player;

import java.util.*;

public class Application {

    private static Scanner input;

    private Player userPlayer;
    private Player enginePlayer;
    private Player otherEnginePlayer;

    public Application() {
        input = new Scanner(System.in);
    }

    private void runApp(){
        runMenu();
    }

    private void runEngine(){
        setUCI();
    }

    private void testNumMoves() {
        Board board = Board.createStandardBoard();



        //1 Depth
        long startTime = System.currentTimeMillis();
        System.out.print("Number Of Valid Moves At Depth 1: " + moveGenerationTest(1));
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println(" And Took "+ totalTime + "ms");

        //2 Depth
        startTime = System.currentTimeMillis();
        System.out.print("Number Of Valid Moves At Depth 2: " + moveGenerationTest(2));
        totalTime = System.currentTimeMillis() - startTime;
        System.out.println(" And Took "+ totalTime + "ms");

        //3 Depth
        startTime = System.currentTimeMillis();
        System.out.print("Number Of Valid Moves At Depth 3: " + moveGenerationTest(3));
        totalTime = System.currentTimeMillis() - startTime;
        System.out.println(" And Took "+ totalTime + "ms");

        //4 Depth
        startTime = System.currentTimeMillis();
        System.out.print("Number Of Valid Moves At Depth 4: " + moveGenerationTest(4));
        totalTime = System.currentTimeMillis() - startTime;
        System.out.println(" And Took "+ totalTime + "ms");

        //5 Depth
        startTime = System.currentTimeMillis();
        System.out.print("Number Of Valid Moves At Depth 5: " + moveGenerationTest(5));
        totalTime = System.currentTimeMillis() - startTime;
        System.out.println(" And Took "+ totalTime + "ms");

        //6 Depth
        /* startTime = System.currentTimeMillis();
        System.out.print("Number Of Valid Moves At Depth 6: " + moveGenerationTest(6));
        totalTime = System.currentTimeMillis() - startTime;
        System.out.println(" And Took "+ totalTime + "ms"); */
    }

    Board board = Board.createStandardBoard();

    private int moveGenerationTest(int depth) {
        if(depth == 0){
            return 1;
        }

        int numPositions = 0;

        for(final Move move : board.currentPlayer().getPossibleMoves()){

            if(board.currentPlayer().isInCheckMate()){
                return 1;
            }
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(depth == 5){
                System.out.println(move + "," + numPositions);
            }

            if(moveTransition.getMoveStatus().isDone()){
                board = moveTransition.getToBoard();
                numPositions += moveGenerationTest(depth - 1);
            }


            board = moveTransition.getFromBoard();
        }
        return numPositions;
    }

    /**
     * Waits For Input To Set The Engine Up In UCI Mode
     */
    private void setUCI() {
        //Wait For The GUI To Initialise The Engine
        String uciInitialisation = input.nextLine().toLowerCase();
        if (uciInitialisation.startsWith("uci")) {
            runUCIEngine();
        }
    }

    private void runUCIEngine(){
        //Set The Engines Name And Author And Tell The GUI Everything Is Ready
        System.out.println("id name Checkmate Bitch -- This Is The Name");
        System.out.println("id author Astra H");
        System.out.println("uciok");
        String readyCommand = input.nextLine().toLowerCase();
        if(readyCommand.startsWith("isready")){
            //Wait For All The Setup To Finish
            System.out.println("readyok");
        }

        Board board = null;

        while(true){

            //Get The Input From The GUI
            String guiInput = input.nextLine().toLowerCase();


            if(guiInput.equals("ucinewgame")){
                 board = Board.createStandardBoard();
            } else if(guiInput.startsWith("position startpos moves")){
                //Decode The Last Given Move And Make That Move On The Engine Side Board
                int finalLetter = guiInput.length();
                String lastMove = guiInput.substring(finalLetter - 4, finalLetter);
                System.out.println(lastMove);

                int moveInitial = BoardUtils.getCoordinateAtPosition(lastMove.substring(0, 2));
                int moveDestination = BoardUtils.getCoordinateAtPosition(lastMove.substring(2, 4));

                final Move move = Move.MoveFactory.createMove(board, moveInitial, moveDestination);
                final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
                board = moveTransition.getToBoard();
            } else if(guiInput.startsWith("go")){
                Move bestMove = getRandomMove(board.currentPlayer().getPossibleMoves());

                String outputMove = BoardUtils.getPositionAtCoordinate(bestMove.getCurrentCoordinate()) +
                        BoardUtils.getPositionAtCoordinate(bestMove.getDestinationCoordinate());
                //Tells The GUI The Move We Want To Make
                System.out.println("bestmove " + outputMove);

                //Make The Move On The Internal Board
                final MoveTransition moveTransition = board.currentPlayer().makeMove(bestMove);
                board = moveTransition.getToBoard();
            }


            }
        }

    /**
     * Runs The Menu
     */
    private void runMenu(){
        String response;
        do {
            //Prints Out The Menu And Allows The User To Select An Option
            printMenu();
            System.out.println("Select Your Option:");
            response = input.nextLine().toUpperCase();

            Board board = Board.createStandardBoard();

            switch(response){
                case "1":
                    //Chooses A Random Colour For The User
                    Random random = new Random();
                    boolean userIsWhite = random.nextBoolean();

                    //Set The User And Engine Player Based On The Random Boolean
                    if(userIsWhite){
                        userPlayer = board.whitePlayer();
                        enginePlayer = board.blackPlayer();
                    } else {
                        userPlayer = board.blackPlayer();
                        enginePlayer = board.whitePlayer();
                    }

                    playGame(board);

                    break;
                case "2":
                    //Sets The User To White And Engine To Black
                    userPlayer = board.whitePlayer();
                    enginePlayer = board.blackPlayer();

                    playGame(board);

                    break;
                case "3":
                    //Sets The User To Black And Engine To White
                    userPlayer = board.blackPlayer();
                    enginePlayer = board.whitePlayer();

                    playGame(board);

                    break;
                case "4":
                    //No Need To Set Players As User Takes Both Sides Moves
                    playGame(board);

                    break;
                case "5":
                    //Sets Two Engine Sides
                    otherEnginePlayer = board.blackPlayer();
                    enginePlayer = board.whitePlayer();

                    playGame(board);
                    break;
                case "Q":
                    System.out.println("Thanks For Using The Program, Goodbye");
                    break;
                default:
                    System.err.println(response + " Is Not A Valid Option, Please Try Again");
                    break;
            }
        } while (!response.equals("Q"));
    }

    /**
     * Prints Out All The Possible Menu Options
     */
    private void printMenu(){
        System.out.println("1 - Play As A Random Colour Vs Engine");
        System.out.println("2 - Play As A White Vs Engine");
        System.out.println("3 - Play As Black Vs Engine");
        System.out.println("4 - Play As Both Sides");
        System.out.println("5 - Play An Engine Vs Engine Game");
        System.out.println("Q - Quit The Program");
    }

    private void playGame(Board board){

        do {
            //Print Out The Board And The Current Player, Before Every Move
            System.out.println("The Current Player Is " + board.currentPlayer().toString());
            System.out.println(board);
            System.out.println(board.currentPlayer().getPossibleMoves());


            if(board.currentPlayer().equals(userPlayer)){
                //Allow The User To Select And Make A Move
                Move move = selectMove(board);
                MoveTransition moveTransition = board.currentPlayer().makeMove(move);

                board = moveTransition.getToBoard();
                userPlayer = board.currentPlayer().getOpponent();
                enginePlayer = board.currentPlayer();

            } else if (board.currentPlayer().equals(enginePlayer)){
                //Allow The Engine To Select And Make A Move

                //Gets A Random Move From The Collection
                Move move = getRandomMove(board.currentPlayer().getPossibleMoves());

                MoveTransition moveTransition = board.currentPlayer().makeMove(move);

                board = moveTransition.getToBoard();
                if(otherEnginePlayer != null){
                    otherEnginePlayer = board.currentPlayer();
                } else {
                    userPlayer = board.currentPlayer();
                }
                enginePlayer = board.currentPlayer().getOpponent();
            }  else if (board.currentPlayer().equals(otherEnginePlayer)){
                //Allow The Engine To Select And Make A Move

                //Gets A Random Move From The Collection
                Move move = getRandomMove(board.currentPlayer().getPossibleMoves());

                MoveTransition moveTransition = board.currentPlayer().makeMove(move);

                board = moveTransition.getToBoard();
                enginePlayer = board.currentPlayer();
                otherEnginePlayer = board.currentPlayer().getOpponent();
            } else {
                //User Is Playing Against Themselves
                //Allow The User To Select And Make A Move
                Move move = selectMove(board);
                MoveTransition moveTransition = board.currentPlayer().makeMove(move);

                board = moveTransition.getToBoard();
            }

        } while (!board.currentPlayer().isInCheckMate() && !board.currentPlayer().isInStalemate());

        if(board.currentPlayer().isInCheckMate()){
            System.out.println(board.currentPlayer().getOpponent().toString() + " Wins By Checkmate!");
        } else {
            System.out.println("The Game Has Ended In A Draw");
        }

        System.out.println(board);

    }

    private Move selectMove(Board board){
        System.out.println("Enter The Pieces Current Location:");
        String currentLocation = input.nextLine();
        System.out.println("Enter The Pieces Destination Location:");
        String destinationLocation = input.nextLine();

        final Move move = Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition(currentLocation),
                                                        BoardUtils.getCoordinateAtPosition(destinationLocation));
        final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

        //If The Move Is Done And A Valid Move
        if(moveTransition.getMoveStatus().isDone() && board.currentPlayer().getPossibleMoves().contains(move)){
            return move;
        } else {
            //The Move Is Not Valid
            System.err.println("The Given Move Is Not Valid, Please Select Another");
            selectMove(board);
        }
        return move;
    }

    private Move getRandomMove(Collection<Move> from) {
        Random rnd = new Random();
        int i = rnd.nextInt(from.size());
        return (Move) from.toArray()[i];
    }


    public static void main(String[] args) {
        Application app = new Application();

        app.runApp();
        //app.runEngine();
        app.testNumMoves();
    }
}
