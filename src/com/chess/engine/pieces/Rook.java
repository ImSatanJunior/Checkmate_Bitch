package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.google.common.collect.ImmutableList;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * The Rook Class Extending Off Of Piece Class
 */
public class Rook extends Piece{

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8, -1, 1, 8};

    /**
     * Constructor For The Rook Used When First Created As isFirstMove Will Always Be True Then
     * @param piecePosition The Coordinate Of The Piece
     * @param pieceAlliance The Alliance Of The Piece
     */
    public Rook(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.ROOK, piecePosition, pieceAlliance, true);
    }

    /**
     * Constructor For The Bishop
     * @param piecePosition The Coordinate Of The Piece
     * @param pieceAlliance The Alliance Of The Piece
     * @param isFirstMove If The Piece Has Moved Yet
     */
    public Rook(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.ROOK, piecePosition, pieceAlliance, isFirstMove);
    }

    /**
     * Calculates All The Possible Moves For The Bishop
     *
     * Create A List possibleMoves To Store The Possible Moves
     *
     * For Each Candidate Vector Coordinate
     * Whilst The Candidate Destination Is Valid (Tile To Move To Is In The Valid Range) And Not A Column Exclusion
     * First It Offsets The Destination Coordinate By The Current Candidate Offset
     * If The New Candidate Destination Is Valid Get The Tile
     * If That Tile Is Empty Add A New Major Move To possibleMoves
     *
     * Else If The Tile Is Occupied Check If The Piece On That Tile Is Of Opposite Alliance
     * If Its Opposite Add A New Attack Move To possibleMoves
     * Break The While Loop As An Occupied Tile Means There No Additional Legal Moves With That Vector
     *
     * @param board The Board To Evaluate On
     * @return A Copy Of The List possibleMoves
     */
    @Override
    public List<Move> calculatePossibleMoves(Board board) {

        final List<Move> possibleMoves = new ArrayList<>();

        //Loop Though Each Vector
        for(final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){

            //Set Initial Destination To Piece Position
            int candidateDestinationCoordinate = this.piecePosition;

            //Whilst The Possible Move Is Valid
            while(BoardUtils.isValidTile(candidateDestinationCoordinate)){

                //And Not A Column Exclusion
                if(isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset) ||
                        isEightColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset)){
                    break;
                }

                //Offset The Destination Coordinate By The Candidate Offset
                candidateDestinationCoordinate += currentCandidateOffset;

                //If Still A Valid Tile
                if(BoardUtils.isValidTile(candidateDestinationCoordinate)){

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
                        break;
                    }
                }
            }

        }

        return ImmutableList.copyOf(possibleMoves);
    }

    /**
     * Moves The Piece
     * @param move The Move To Do
     * @return A New Rook With That Moves New Location
     */
    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
    }

    /**
     * Returns The String Format Of The Rook
     * @return The Rooks Piece Name
     */
    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }

    /**
     * Checks If Making A Possible Move Would Be Illegal
     * @param currentPosition The Current Piece Position
     * @param candidatePosition The Move Offset
     * @return If The Possible Move Is Illegal
     */
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidatePosition == -1);
    }

    /**
     * Checks If Making A Possible Move Would Be Illegal
     * @param currentPosition The Current Piece Position
     * @param candidatePosition The Move Offset
     * @return If The Possible Move Is Illegal
     */
    private static boolean isEightColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidatePosition == 1);
    }

}
