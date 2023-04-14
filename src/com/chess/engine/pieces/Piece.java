package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.List;

/**
 * The Piece Class Containing All The Information About The Piece
 */
public abstract class  Piece {

    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected boolean isFirstMove;
    private final int cachedHashCode;

    /**
     * The Constructor For The Piece
     * @param pieceType The Type Of Position
     * @param piecePosition The Coordinate / Position Of The Piece
     * @param pieceAlliance The Alliance Of The Piece
     * @param isFirstMove If The Move Is The First Move
     */
    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove){
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    /**
     * Computes The Hash Code For The Piece Dependent On Its Attributes
     * @return
     */
    private int computeHashCode(){
        final int prime = 31;
        int result = pieceType.hashCode();
        result = prime * result + pieceAlliance.hashCode();
        result = prime * result + piecePosition;
        result = prime * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }

        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance()  && isFirstMove == otherPiece.isFirstMove();
    }

    /**
     * Gets The Hash Code Of The Piece
     * @return The Hash Code
     */
    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    /**
     * Gets The Alliance Of The Piece
     * @return The Pieces Alliance
     */
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    /**
     * Whether It Is Current The Pieces First Move
     * @return
     */
    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    /**
     * Gets The Pieces Type
     * @return The Piece Type
     */
    public PieceType getPieceType(){
        return this.pieceType;
    }

    /**
     * Sets Whether the Piece Has Moves
     */
    public void pieceHasMoved(){
        isFirstMove = false;
    }

    /**
     * Gets The Position Of The Piece
     * @return The Pieces Position
     */
    public int getPiecePosition(){
        return this.piecePosition;
    }

    /**
     * Calculates All The Pieces Possible Positions On The Current Board
     * @param board The Board To Evaluate On
     * @return List Of All Legal Moves
     */
    public abstract List<Move> calculatePossibleMoves(final Board board);

    /**
     * Moves The Piece
     * @param move The Move To Do
     * @return
     */
    public abstract Piece movePiece(Move move);

    public enum PieceType {
        PAWN(100, "P"),
        KNIGHT(300, "N"),
        BISHOP(350, "B"),
        ROOK(500, "R"),
        QUEEN(900, "Q"),
        KING(10000, "K");

        private String pieceName;
        private int pieceValue;

        PieceType(final int pieceValue, final String pieceName){
            this.pieceValue = pieceValue;
            this.pieceName = pieceName;
        }

        /**
         * Gets The String Result Of The Piece Type
         * @return The Name Of The Piece Type
         */
        @Override
        public String toString(){
            return this.pieceName;
        }

        /**
         * Gets The Value Of The Piece
         * @return The Piece Value
         */
        public int getPieceValue(){
            return this.pieceValue;
        }
    }

}
