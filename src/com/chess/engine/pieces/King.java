package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * The King Class Extending Off Of Piece Class
 */
public class King extends Piece{

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    /**
     * Constructor For The King Used When First Created As isFirstMove Will Always Be True Then
     * @param piecePosition The Coordinate Of The Piece
     * @param pieceAlliance The Alliance Of The Piece
     */
    public King(final int piecePosition, final Alliance pieceAlliance){
        super(PieceType.KING, piecePosition, pieceAlliance, true);
    }

    /**
     * Constructor For The King
     * @param piecePosition The Coordinate Of The Piece
     * @param pieceAlliance The Alliance Of The Piece
     * @param isFirstMove If The Piece Has Moved Yet
     */
    public King(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove){
        super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
    }

    /**
     * Calculates All The Possible Moves For The King
     *
     * Create A List possibleMoves To Store The Possible Moves
     *
     * For Each Candidate Vector Coordinate
     * First It Offsets The Destination Coordinate By The Current Candidate Offset
     * If The Candidate Destination Is A Valid Tile And Not A Column Exclusion Get The Tile
     *
     * If That Tile Is Not Occupied Add A New Major Move To possibleMoves
     * Else If The Tile Is Occupied Check The Alliance Of The Piece Currently On That Tile
     * If That Piece Is Of Opposite Alliance Add A New Attack Move To possibleMoves
     *
     * @param board The Board To Evaluate On
     * @return A Copy Of The List possibleMoves
     */
    @Override
    public List<Move> calculatePossibleMoves(Board board) {

        final List<Move> possibleMoves = new ArrayList<>();

        int candidateDestinationCoordinate;

        //Loop Through Each Vector
        for(final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){

            //Offset The Destination Coordinate By The Candidate Offset
            candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            //If The Move Is Within Bounds
            if(BoardUtils.isValidTile(candidateDestinationCoordinate)){


                if(isFirstColumnExclusion(this.piecePosition, candidateDestinationCoordinate) ||
                        isEightColumnExclusion(this.piecePosition, candidateDestinationCoordinate)){
                    continue;
                }

                //Get The Tile
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                //If The Tile Is Empty Add A New Major Move, Else Add An Attack Move If The Pieces Have Different Alliances
                if(!candidateDestinationTile.isTileOccupied()){
                    possibleMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    if(this.pieceAlliance != pieceAtDestination.getPieceAlliance()){
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
     * @return A New King With That Moves New Location
     */
    @Override
    public King movePiece(Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
    }

    /**
     * Returns The String Format Of The King
     * @return The Kings Piece Name
     */
    @Override
    public String toString(){
        return PieceType.KING.toString();
    }

    /**
     * Checks If Making A Possible Move Would Be Illegal
     * @param currentPosition The Current Piece Position
     * @param candidatePosition The Move Offset
     * @return If The Possible Move Is Illegal
     */
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidatePosition == -9) || (candidatePosition == -1) ||
                (candidatePosition == 7));
    }

    /**
     * Checks If Making A Possible Move Would Be Illegal
     * @param currentPosition The Current Piece Position
     * @param candidatePosition The Move Offset
     * @return If The Possible Move Is Illegal
     */
    private static boolean isEightColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidatePosition == -7) || (candidatePosition == 1) ||
                (candidatePosition == 9));
    }
}
