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

    public Application(){
        input = new Scanner(System.in);
    }

    private void runApp(){
        runMenu();
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
        System.out.println("Q - Quit The Program");
    }

    private void playGame(Board board){

        do {
            //Print Out The Board And The Current Player, Before Every Move
            System.out.println("The Current Player Is " + board.currentPlayer().toString());
            System.out.println(board);


            if(board.currentPlayer().equals(userPlayer)){
                //Allow The User To Select And Make A Move
                Move move = selectMove(board);
                MoveTransition moveTransition = board.currentPlayer().makeMove(move);

                board = moveTransition.getTransitionBoard();
                userPlayer = board.currentPlayer().getOpponent();
                enginePlayer = board.currentPlayer();

            } else if (board.currentPlayer().equals(enginePlayer)){
                //Allow The Engine To Select And Make A Move

                //Gets A Random Move From The Collection
                Move move = getRandomMove(board.currentPlayer().getLegalMoves());

                MoveTransition moveTransition = board.currentPlayer().makeMove(move);

                board = moveTransition.getTransitionBoard();
                userPlayer = board.currentPlayer();
                enginePlayer = board.currentPlayer().getOpponent();
            } else {
                //User Is Playing Against Themselves
                //Allow The User To Select And Make A Move
                Move move = selectMove(board);
                MoveTransition moveTransition = board.currentPlayer().makeMove(move);

                board = moveTransition.getTransitionBoard();
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
        if(moveTransition.getMoveStatus().isDone() && board.currentPlayer().getLegalMoves().contains(move)){
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


    public static void main(String[] args){

        Application app = new Application();

        app.runApp();
        


    }
}
