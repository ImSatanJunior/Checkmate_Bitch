package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Alliance;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The Player Class
 */
public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> possibleMoves;
    private final boolean isInCheck;

    /**
     * Constructor For The Player
     * @param board The Current Board
     * @param legalMoves The Legal Moves For That Player
     * @param opponentMoves The Legal Moves For The Opponent
     */
    public Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        this.possibleMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    /**
     * Gets The Players King
     * @return The Players King
     */
    public King getPlayerKing(){
        return this.playerKing;
    }

    /**
     * Gets All The Legal Moves For The Player
     * @return The List Of Legal Moves For The Player
     */
    public Collection<Move> getPossibleMoves(){
        return this.possibleMoves;
    }

    /**
     * Calculates All The Possible Attacks On A Given Tile
     *
     * Creates A List attackMoves
     * For Each Move In opponentMoves
     * If That Moves Destination Coordinate Is Equal To The Piece Position Add That Move To Attack Moves
     *
     * @param piecePosition The Tile Position To Check
     * @param opponentMoves List Of Opponent Moves
     * @return A Copy Of The List Of attackMoves
     */
    public static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> opponentMoves){
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move : opponentMoves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    /**
     * Establishes The King
     * For Each Piece In The Players Active Pieces
     * If The Pieces PieceType Is King Then Return That Piece Cast To A King
     *
     * Throws A Runtime Exception If There Is No King Found
     * @return
     */
    private King establishKing() {
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType() == Piece.PieceType.KING){
                return(King) piece;
            }
        }

        throw new RuntimeException("Should Not Reach Here! This Is Not A Valid Board!");
    }

    /**
     * Checks Whether A Move Is Lega
     * @param move The Move To Check
     * @return If The Move Is Contained With legalMoves
     */
    public boolean isMovePossible(final Move move){
        return this.possibleMoves.contains(move);
    }

    /**
     * If The Player Is Currently In Check
     * @return If The Player Is In Check
     */
    public boolean isInCheck(){
        return this.isInCheck;
    }

    /**
     * If The Player Is In Checkmate
     * @return If The Player Is In Check And Cannot Escape
     */
    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }

    /**
     * If The Player Is In Stalemate
     * @return If The Player Is Not In Check And Cannot Move
     */
    public boolean isInStalemate(){
        return !this.isInCheck && !hasEscapeMoves();
    }

    /**
     * Calculates Whether The Player Has Any Possible Escape Moves
     *
     * For Each Move In legalMoves
     * Make The Move And If The Move Status Is Done The Move Is Legal, Return True That There Is Escape Moves
     * Else Return False If No Moves Are Possible
     * @return If The Player Has Any Possible Moves
     */
    protected boolean hasEscapeMoves(){
        for(final Move move : this.possibleMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }

    /**
     * Get Whether The Player Has Castled
     * @return False
     */
    public boolean isCastled(){
        return false;
    }

    /**
     * Makes The Given Move Returning A New Move Transition With That Given Moves Status
     *
     * First If The Move Is Not Legal Return A New Move Transition With Status Illegal Move
     *
     * Afterwards Execute The Given Move
     * Calculate Any King Attacks After The Move Is Made
     * If kingAttacks Is Not Empty Return A New Move Transition With Status Leaves Player In Check
     * Else Return A New Move Transition With Status Done
     *
     * @param move The Move To Attempt
     * @return The New Move Transition
     */
    public MoveTransition makeMove(final Move move){

        //If The Move Is Not Legal Return A New Move Transition With Status Illegal Move
        if(!isMovePossible(move)){
                return new MoveTransition(this.board, this.board , move ,MoveStatus.ILLEGAL_MOVE);
        }

        //Execute The Move
        final Board transitionBoard = move.execute();

        //Calculate Any New kingAttacks
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getPossibleMoves());

        //If kingAttacks Is Not Empty Return A New Move Transition With Status Leaves Player In Check
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        //Return A New Move Transition With Status Done
        return new MoveTransition(this.board, transitionBoard, move, MoveStatus.DONE);
    }

    public MoveTransition unMakeMove(final Move move){
        return new MoveTransition(this.board, move.undo(), move, MoveStatus.DONE);
    }

    /**
     * Gets All The Active Pieces For The Player
     * @return All The Active Pieces
     */
    public abstract Collection<Piece> getActivePieces();

    /**
     * Get The Alliance Of The Player
     * @return The Players Alliance
     */
    public abstract Alliance getAlliance();

    /**
     * Get The Opponent Of The Player
     * @return The Players Opponent
     */
    public abstract Player getOpponent();

    /**
     * Calculate The Possible Castling Moves
     * @param playerLegals The Current Players Legal Moves
     * @param opponentLegals The Opponents Legal Moves
     * @return A Collection Of Possible King Castling Moves
     */
    public abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);
}
