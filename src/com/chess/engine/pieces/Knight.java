package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * The Knight Class Extending Off Of Piece Class
 */
public class Knight extends Piece{

    final public static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    /**
     * Constructor For The Knight Used When First Created As isFirstMove Will Always Be True Then
     * @param piecePosition The Coordinate Of The Piece
     * @param pieceAlliance The Alliance Of The Piece
     */
    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, true);
    }

    /**
     * Constructor For The Knight
     * @param piecePosition The Coordinate Of The Piece
     * @param pieceAlliance The Alliance Of The Piece
     * @param isFirstMove If The Piece Has Moved Yet
     */
    public Knight(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
    }

    /**
     * Calculates All Possible Moves For The Knight
     *
     * Create A List possibleMoves To Store The Possible Moves
     *
     * For Each Candidate Vector Coordinate
     * First It Offsets The Destination Coordinate By The Current Candidate Offset
     * If The Candidate Destination Is A Valid Tile And Not A Column Exclusion Get The Tile
     * If That Tile Is Not Occupied Add A New Major Move To possibleMoves
     * Else If The Tile Is Occupied Check The Alliance Of The Piece Currently On That Tile
     * If That Piece Is Of Opposite Alliance Add A New Attack Move To possibleMoves
     *
     * @param board The Board To Evaluate On
     * @return A Copy Of The List possibleMoves
     */
    @Override
    public List<Move> calculatePossibleMoves(final Board board) {

        int candidateDestinationCoordinate;

        final List<Move> possibleMoves = new ArrayList<>();

        //For Each Candidate Move
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){

            //Offset The Destination Coordinate By The Candidate Offset
            candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            //If The Move Is Within Bounds
            if(BoardUtils.isValidTile(candidateDestinationCoordinate)){

                //And The Move Is Not A Column Exclusion
                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }

                //Get The Tile We Want To Move To
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                //If The Tile Is Empty Add A New Major Move, Else Add An Attack Move If The Pieces Have Different Alliances
                if(!candidateDestinationTile.isTileOccupied()){
                    possibleMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance != pieceAlliance){
                        possibleMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(possibleMoves);
    }

    /**
     * Moves The Piece
     * @param move The Move To Do
     * @return A New Knight With That Moves New Location
     */
    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
    }

    /**
     * Returns The String Format Of The Knight
     * @return The Knights Piece Name
     */
    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    /**
     * Checks If Making A Possible Move Would Be Illegal
     * @param currentPosition The Current Piece Position
     * @param candidatePosition The Move Offset
     * @return If The Possible Move Is Illegal
     */
    public static boolean isFirstColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidatePosition == -17)  || (candidatePosition == -10) ||
                (candidatePosition == 6) || (candidatePosition == 15));
    }

    /**
     * Checks If Making A Possible Move Would Be Illegal
     * @param currentPosition The Current Piece Position
     * @param candidatePosition The Move Offset
     * @return If The Possible Move Is Illegal
     */
    public static boolean isSecondColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.SECOND_COLUMN[currentPosition] && ((candidatePosition == -10) || (candidatePosition == 6));
    }

    /**
     * Checks If Making A Possible Move Would Be Illegal
     * @param currentPosition The Current Piece Position
     * @param candidatePosition The Move Offset
     * @return If The Possible Move Is Illegal
     */
    public static boolean isSeventhColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && ((candidatePosition == -6)  || (candidatePosition == 10));
    }

    /**
     * Checks If Making A Possible Move Would Be Illegal
     * @param currentPosition The Current Piece Position
     * @param candidatePosition The Move Offset
     * @return If The Possible Move Is Illegal
     */
    public static boolean isEighthColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidatePosition == -15)  || (candidatePosition == -6) ||
                (candidatePosition == 10) || (candidatePosition == 17));
    }


}
