package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * The Pawn Class Extending Off Of Piece Class
 */
public class Pawn extends Piece{

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {7, 8, 9, 16};

    /**
     * Constructor For The Pawn Used When First Created As isFirstMove Will Always Be True Then
     * @param piecePosition The Coordinate Of The Piece
     * @param pieceAlliance The Alliance Of The Piece
     */
    public Pawn(final int piecePosition, final Alliance pieceAlliance){
        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }

    /**
     * Constructor For The Pawn
     * @param piecePosition The Coordinate Of The Piece
     * @param pieceAlliance The Alliance Of The Piece
     * @param isFirstMove If The Piece Has Moved Yet
     */
    public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove){
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }

    /**
     * Calculates All The Possible Moves For The Pawn
     *
     * Create A List possibleMoves To Store The Possible Moves
     *
     * For Each Candidate Vector Coordinate
     * First It Offsets The Destination Coordinate By The Current Candidate Offset
     * If That Is A Valid Tile It Gets The Tile
     * Then It Checks Each Possible Move Case
     *
     * @param board The Board To Evaluate On
     * @return A Copy Of The List possibleMoves
     */
    @Override
    public List<Move> calculatePossibleMoves(Board board){

        final List<Move> possibleMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){

            //Offset The Destination Coordinate By The Candidate Offset
            int candidateDestinationCoordinate = this.piecePosition + (currentCandidateOffset * this.pieceAlliance.getDirection());

            //If Tile To Move To Is Not Valid Continue
            if(!BoardUtils.isValidTile(candidateDestinationCoordinate)){
                continue;
            }

            //Get The Tile
            final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

            //If Tile To In Front Is Not Occupied, Add New Legal Move
            if(currentCandidateOffset == 8 && !candidateDestinationTile.isTileOccupied()){
                if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                     possibleMoves.add(new Move.PawnPromotionMove(new Move.PawnMove(board, this, candidateDestinationCoordinate)));
                } else {
                    possibleMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));
                }
            } else if(currentCandidateOffset == 16 && this.isFirstMove() && !candidateDestinationTile.isTileOccupied()){
                if(!board.getTile(this.piecePosition + (8 * this.pieceAlliance.getDirection())).isTileOccupied()){
                    possibleMoves.add(new Move.PawnJump(board, this, candidateDestinationCoordinate));
                }
            } else if((currentCandidateOffset == 7) &&
                    !isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) &&
                    !isEightColumnExclusion(this.piecePosition, currentCandidateOffset)){
                if(candidateDestinationTile.isTileOccupied()){
                    final Piece pieceOnCandidate = candidateDestinationTile.getPiece();
                    if(pieceOnCandidate.getPieceAlliance() != this.pieceAlliance){
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                            possibleMoves.add(new Move.PawnPromotionMove(new Move.PawnMove(board, this, candidateDestinationCoordinate)));
                        } else {
                            possibleMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                } else if(board.getEnPassantPawn() != null){
                    if(board.getEnPassantPawn().getPiecePosition() == this.getPiecePosition() + (this.pieceAlliance.getDirection() * -1)){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            possibleMoves.add(new Move.PawnEnPassantMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            } else if((currentCandidateOffset == 9) &&
                    !isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) &&
                    !isEightColumnExclusion(this.piecePosition, currentCandidateOffset)){
                if(candidateDestinationTile.isTileOccupied()) {
                    final Piece pieceOnCandidate = candidateDestinationTile.getPiece();
                    if (pieceOnCandidate.getPieceAlliance() != this.pieceAlliance) {
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                            possibleMoves.add(new Move.PawnPromotionMove(new Move.PawnMove(board, this, candidateDestinationCoordinate)));
                        } else {
                            possibleMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }

                    }
                } else if(board.getEnPassantPawn() != null){
                    if(board.getEnPassantPawn().getPiecePosition() == this.getPiecePosition() + this.pieceAlliance.getDirection()){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            possibleMoves.add(new Move.PawnEnPassantMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
        }

        return ImmutableList.copyOf(possibleMoves);
    }

    /**
     * Moves The Piece
     * @param move The Move To Do
     * @return A New Pawn With That Moves New Location
     */
    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
    }

    /**
     * Returns The String Format Of The Pawn
     * @return The Pawns Piece Name
     */
    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece() {
        return new Queen(this.piecePosition, this.pieceAlliance, false);
    }

    /**
     * Checks If Making A Possible Move Would Be Illegal
     * @param currentPosition The Current Piece Position
     * @param candidatePosition The Move Offset
     * @return If The Possible Move Is Illegal
     */
    private boolean isFirstColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidatePosition == 7 && this.pieceAlliance.equals(Alliance.BLACK)) ||
                                                            (candidatePosition == 9 && this.pieceAlliance.equals(Alliance.WHITE)));
    }

    /**
     * Checks If Making A Possible Move Would Be Illegal
     * @param currentPosition The Current Piece Position
     * @param candidatePosition The Move Offset
     * @return If The Possible Move Is Illegal
     */
    private boolean isEightColumnExclusion(final int currentPosition, final int candidatePosition){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidatePosition == 7 && this.pieceAlliance.equals(Alliance.WHITE)) ||
                                                            (candidatePosition == 9 && this.pieceAlliance.equals(Alliance.BLACK)));
    }
}
