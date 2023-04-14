package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

/**
 * A Class To Hold Information About Each Move Including Which Piece Is Moved And To Where
 * There Is Many Sub Classes Of Move Used To Distinguish Their Different Move Notations
 */
public abstract class Move {

    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    final static Move NULL_MOVE = new NullMove();

    /**
     * A Constructor For A Move
     * @param board The Board The Move Will Be Made On
     * @param movedPiece The Piece That Is Moved
     * @param destinationCoordinate The Location That The Piece Is Moved To
     */
    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    /**
     * A Constructor For A Move Used By Null Move
     * @param board The Board The Move Is Made On
     * @param destinationCoordinate The Destination Coordinate Of the Move
     */
    private Move(final Board board, final int destinationCoordinate){
        this.board = board;
        this.movedPiece = null;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = false;
    }


    /**
     * Calculates A Hashed Value For The Move
     * @return The Hashed Value
     */
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result * this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result * this.movedPiece.getPiecePosition();
        return result;

    }

    /**
     * Overrides The Default Equals Method To Check If Two Moves Are Equal
     *
     * Evaluates If The Object Is Exactly The Same
     * Else If The Other Object Is An Instance Of Move If All The Properties Are Equal Returns True
     *
     * @param other The Other Object To Compare
     * @return If The Objects Are Equal
     */
    @Override
    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }

        final Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece()) &&
                getCurrentCoordinate() == otherMove.getCurrentCoordinate();
    }

    public Board getBoard(){
        return this.board;
    }

    /**
     * Gets The Destination Coordinate Of The Move
     * @return The Destination Coordinate
     */
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    /**
     * Gets The Current Coordinate Of The Piece To Be Moved
     * @return The Moved Pieces Current Coordinate
     */
    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }

    /**
     * Gets The Moved Piece
     * @return The Moved Piece
     */
    public Piece getMovedPiece() {
        return movedPiece;
    }

    /**
     * Gets Whether The Move Is An Attack
     * @return If The Move Is An Attack As A Boolean
     */
    public boolean isAttack(){
        return false;
    }

    /**
     * Gets Whether The Move Is A Castling Move
     * @return If The Move Is A Castle As A Boolean
     */
    public boolean isCastlingMove(){
        return false;
    }

    /**
     * Gets The Attacked Piece For The Move
     * @return The Attacked Piece Of The Move
     */
    public Piece getAttackedPiece(){
        return null;
    }

    /**
     * Executes The Move
     *
     * First It Iterates Through All The Current Players Pieces That Weren't Moved And Sets Then On The Board
     * Then It Places All The Opponents Pieces
     * Finally It Places The Moved Piece On Its New Tile, Sets The Move Maker And Builds
     *
     * @return The New Board State
     */
    public Board execute() {
        final Board.Builder builder = new Board.Builder();

        //Go Through All The Current Players Piece That Weren't Moved And Set Them
        for(final Piece piece : this.board.currentPlayer().getActivePieces()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }

        //Set All The Opponents Pieces
        for(final  Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        //Move The Moved Piece
        this.movedPiece.pieceHasMoved();
        builder.setPiece(this.movedPiece.movePiece(this));
        //Set The Next Person To Make A Move
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    /**
     * A Sub Class Of Move To Deal With Any Major Moves
     * A Major Move Is A Move Where No Pieces Are Taken
     */
    public static final class MajorMove extends Move {
        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate){
            super(board, movedPiece, destinationCoordinate);
        }

        /**
         * Overriding The Move Equals Method To Instead Check If Other Is An Instance Of Major Move
         * @param other The Other Object To Compare
         * @return If The Objects Are Equal
         */
        @Override
        public boolean equals(Object other){
            if(this == other){
                return true;
            }
            if(other instanceof MajorMove && super.equals(other)){
                return true;
            }
            return false;
        }

        /**
         * Gets The String For The Move In Form Piece, New Location
         * @return Sting For The Move
         */
        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    /**
     * A Sub Class Of Move To Deal With Any Attack Moves Where A Piece Is Taken
     */
    public static class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        /**
         * Takes The Pieces Hash Code And The Supers Move Hash Code To Combine For The Attack Moves Hash Code
         * @return The Hash Code For The Move
         */
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        /**
         * Overriding The Move Equals Method To Instead Check If Other Is An Instance Of Attack Move
         * @param other The Other Object To Compare
         * @return If The Objects Are Equal
         */
        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        /**
         * Returns True That The Move Is An Attack
         * @return True As The Move Is An Attack
         */
        @Override
        public boolean isAttack(){
            return true;
        }

        /**
         * Gets The Attacked Piece On The Square We Are Moving To
         * @return The Attacked Piece
         */
        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
    }

    /**
     * A Sub Class Of Move That Deals With All The Pawn Moves
     */
    public static final class PawnMove extends Move {
        public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        /**
         * Overriding The Move Equals Method To Instead Check If Other Is An Instance Of Pawn Move
         * @param other The Other Object To Compare
         * @return If The Objects Are Equal
         */
        @Override
        public boolean equals(final Object other){
            if(this == other|| other instanceof PawnMove && super.equals(other)){
                return true;
            }
            return false;
        }

        /**
         * Returns The Move As A String
         * @return The String For The Move
         */
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    /**
     * A Sub Class Of Attack Move To Deal With Pawn Attack Moves
     */
    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        /**
         * Overriding The Move Equals Method To Instead Check If Other Is An Instance Of Pawn Attack Move
         * @param other The Other Object To Compare
         * @return If The Objects Are Equal
         */
        @Override
        public boolean equals(final Object other){
            if(this == other || other instanceof  PawnAttackMove && super.equals(other)){
                return true;
            }
            return false;
        }

        /**
         * Returns The Move As A String
         * @return The String For The Move
         */
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1) + "x" +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    /**
     * A Sub Class Of Pawn Attack Move To Deal With En Passant
     */
    public static class PawnEnPassantMove extends PawnAttackMove {

        public PawnEnPassantMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        /**
         * Overriding The Move Equals Method To Instead Check If Other Is An Instance Of Pawn En Passant Move
         * @param other The Other Object To Compare
         * @return If The Objects Are Equal
         */
        @Override
        public boolean equals(final Object other){
            if(this == other || other instanceof  PawnEnPassantMove && super.equals(other)){
                return true;
            }
            return false;
        }

        /**
         * Overriding The Default Execute Function To Execute A New Move
         * Firstly It Sets All The Other Pieces On The Board For The Current Player Then Opponent Player
         * Next It Sets And Moved The Relevant Piece
         * @return The Built Board
         */
        @Override
        public Board execute(){
            final Board.Builder builder = new Board.Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }

            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                if(!piece.equals(this.getAttackedPiece())){
                    builder.setPiece(piece);
                }
            }

            this.movedPiece.pieceHasMoved();
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

    }

    public static class PawnPromotionMove extends Move {

        final Move decoratedMove;
        final Pawn promotedPawn;

        public PawnPromotionMove(final Move decoratedMove){
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }

        @Override
        public int hashCode(){
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        /**
         * Overriding The Move Equals Method To Instead Check If Other Is An Instance Of Pawn PromotionMove Move
         * @param other The Other Object To Compare
         * @return If The Objects Are Equal
         */
        @Override
        public boolean equals(final Object other){
            if(this == other || other instanceof  PawnPromotionMove && super.equals(other)){
                return true;
            }
            return false;
        }


        @Override
        public Board execute(){
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Board.Builder();

            for(final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()){
                if(!this.promotedPawn.equals(piece)){
                    builder.setPiece(piece);
                }
            }

            for(final Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }

            this.promotedPawn.pieceHasMoved();
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        /**
         * Gets The Attacked Piece Of The Move
         * @return The Attacked Piece
         */
        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMove.getAttackedPiece();
        }
    }

    /**
     * A Sub Class Of Move To Deal Pawn Jump Moves
     */
    public static final class PawnJump extends Move {

        public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate){
            super(board, movedPiece, destinationCoordinate);
        }

        /**
         * Overriding The Move Equals Method To Instead Check If Other Is An Instance Of Pawn Jump Move
         * @param other The Other Object To Compare
         * @return If The Objects Are Equal
         */
        @Override
        public boolean equals(final Object other){
            if(this == other || other instanceof  PawnJump && super.equals(other)){
                return true;
            }
            return false;
        }

        /**
         * Overriding The Default Execute Function To Execute A New Move
         * Firstly It Sets All The Other Pieces On The Board For The Current Player Then Opponent Player
         * Next It Casts The Piece To Be A Pawn And Sets And Moves The Piece
         * Finally As The Pawn Has Jumped 2 Squares It Sets The En Passant For The Pawn Then Changing The Current Player And Building The Board
         * @return The Built Board
         */
        @Override
        public Board execute(){
            final Board.Builder builder = new Board.Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }

            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            movedPawn.pieceHasMoved();
            builder.setPiece(movedPawn);
            builder.setEnPassant(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    /**
     * A Sub Class Of Move For Castling Moves
     */
    public static abstract class CastleMove extends Move {
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                          final Rook castleRook, final int castleRookStart, final int castleRookDestination){
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        /**
         * Gets The Rook That Is Being Castled With
         * @return The Castling Rook
         */
        public Rook getCastleRook(){
            return this.castleRook;
        }

        /**
         * Returns If The Move Is A Castling Move, True
         * @return Returns True That The Move Is A Castling Move
         */
        @Override
        public boolean isCastlingMove(){
            return true;
        }

        /**
         * Overriding The Default Execute Function To Execute A New Move
         * Firstly It Sets All The Current Players Pieces That Aren't Involved In The Move Then Sets All The Opponents Pieces
         * Next It Sets The Moved Piece
         * Then Its Creates A New Rook At Its New Post Castled Position
         * Finally It Changes The Current Player And Builds The Board
         * @return The Built Board
         */
        @Override
        public Board execute(){
            final Board.Builder builder = new Board.Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!(this.movedPiece == piece) && !(this.castleRook == piece)){
                    builder.setPiece(piece);
                }
            }

            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }

            this.movedPiece.pieceHasMoved();
            builder.setPiece(this.movedPiece.movePiece(this));
            this.castleRook.pieceHasMoved();
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public int hashCode(){
            int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        /**
         * Overriding The Move Equals Method To Instead Check If Other Is An Instance Of Castle Move
         * @param other The Other Object To Compare
         * @return If The Objects Are Equal
         */
        @Override
        public boolean equals(final Object other){
            if(this == other || other instanceof  CastleMove && super.equals(other)){
                return true;
            }
            return false;
        }

    }

    /**
     * A Sub Class For Castling On The Kingside
     */
    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                                  final Rook castleRook, final int castleRookStart, final int castleRookDestination){
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        /**
         * Overriding The Move Equals Method To Instead Check If Other Is An Instance Of Kingside Castle Move
         * @param other The Other Object To Compare
         * @return If The Objects Are Equal
         */
        @Override
        public boolean equals(final Object other){
            if(this == other || other instanceof  KingSideCastleMove && super.equals(other)){
                return true;
            }
            return false;
        }

        /**
         * Gives The Algebraic Notation For The Kingside Castling Move
         * @return The String Format Of The Move
         */
        @Override
        public String toString(){
            return "O-O";
        }
    }

    /**
     * A Sub Class For Castling On The Queenside
     */
    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                                   final Rook castleRook, final int castleRookStart, final int castleRookDestination){
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        /**
         * Overriding The Move Equals Method To Instead Check If Other Is An Instance Of Queenside Castle Move
         * @param other The Other Object To Compare
         * @return If The Objects Are Equal
         */
        @Override
        public boolean equals(final Object other){
            if(this == other || other instanceof  QueenSideCastleMove && super.equals(other)){
                return true;
            }
            return false;
        }

        /**
         * Gives The Algebraic Notation For The Queen Castling Move
         * @return The String Format Of The Move
         */
        @Override
        public String toString(){
            return "O-O-O";
        }
    }

    /**
     * A Sub Class Of Move For Null Moves (Any Invalid Moves)
     */
    public static final class NullMove extends Move {
        public NullMove(){
            super(null, -65);
        }

        /**
         * Throws A Runtime Exception That The Null Move Cannot Be Executed
         * @return
         */
        @Override
        public Board execute(){
            throw new RuntimeException("Cannot Execute The Null Move");
        }

        @Override
        public int getCurrentCoordinate(){
            return -1;
        }
    }

    /**
     *
     */
    public static class MoveFactory {
        private MoveFactory(){
            throw new RuntimeException("Not Instantiable");
        }

        /**
         * Creates The Move
         * @param board
         * @param currentCoordinate
         * @param destinationCoordinate
         * @return The Move
         */
        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate){
            for(final Move move : board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }

    }


}
