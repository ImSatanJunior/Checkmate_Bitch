package com.chess.engine.board;

import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

/**
 * The Board Class Used To Hold All The Information About The Board Including All Tiles, All Pieces And The Players Including The Current Player
 */
public final class Board {


    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final Collection<Piece> whiteTakenPieces;
    private final Collection<Piece> blackTakenPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;

    /**
     * Constructor For The Board
     * @param builder
     */
    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);

        this.enPassantPawn = builder.enPassantPawn;

        Collection<Move> whiteStandardLegalMoves = calculatePossibleMoves(this.whitePieces);
        Collection<Move> blackStandardLegalMoves = calculatePossibleMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);

        whiteTakenPieces = null;
        blackTakenPieces = null;
    }

    /**
     * The To String Function For The Board Overriding The Default
     *
     * Using A String Builder It Appends Each Tiles String Format To The String Builder
     * If At The End Of A Row Of Tiles Appends A New Line
     *
     * @return The Builder To A String
     */
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();

        for(int i = 0; i < BoardUtils.NUM_TILES; i ++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0){
                builder.append("\n");
            }
        }
        return  builder.toString();
    }

    /**
     * Gets The White Player
     * @return The White Player
     */
    public Player whitePlayer(){
        return this.whitePlayer;
    }

    /**
     * Gets The Black Player
     * @return The Black Player
     */
    public Player blackPlayer(){
        return this.blackPlayer;
    }

    /**
     * Gets The Player Who's Turn It Is
     * @return The Current Player
     */
    public Player currentPlayer(){
        return this.currentPlayer;
    }

    /**
     * Gets The Collection Of All Of Black's Pieces
     * @return Black's Pieces
     */
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    /**
     * Gets The Collection Of All Of White's Pieces
     * @return White's Pieces
     */
    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    /**
     * Gets The En Passant Pawn
     * @return The En Passant Pawn
     */
    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }

    /**
     * Calculates All The Possible Legal Moves For The Current Board State
     *
     * Iterates Through The Current Pieces Where If The Move Is Legal(Doesn't Leave Your King In Check) Is Added To The List
     *
     * @param pieces All The Active Pieces On The Current Board
     * @return A Copy Of The List legalMoves
     */
    private Collection<Move> calculatePossibleMoves(Collection<Piece> pieces){

        final List<Move> legalMoves = new ArrayList<>();

        //This Creates A List With All The Possible Moves However This Could Result In A Invalid Game State
        for(final Piece piece : pieces){
            legalMoves.addAll(piece.calculatePossibleMoves(this));
        }

        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Calculates All The Active Pieces On The Board For An Alliance
     *
     * Iterates Over All The Tiles And Checks If The Tile Has A Piece On It
     * Then It Checks If Those Pieces Match The Given Alliance
     *
     * @param gameBoard The Tiles On The Board To Evaluate
     * @param alliance The Alliance Who's Pieces You Want To Get
     * @return All The Pieces On The Board With Matching Alliance
     */
    private Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance){
        final List<Piece> activePieces = new ArrayList<>();

        for(final Tile tile : gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    /**
     * Gets The Tile At The Given Coordinate
     * @param tileCoordinates The Coordinate Of Thw Tile
     * @return The Tile On The Board
     */
    public Tile getTile(final int tileCoordinates){
        return gameBoard.get(tileCoordinates);
    }

    /**
     * Given The Builder It Returns A List Of All The Tiles In The Board Ordered By Coordinate
     * @param builder The Builder
     * @return A List Of All The Tiles
     */
    private static List<Tile> createGameBoard(final Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    /**
     * Creates A Standard Board With All The Pieces On Their Correct Tiles
     * @return A Board With All The Pieces On Correct Tiles
     */
    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        //Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));

        //White Layout
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE ));
        builder.setPiece(new Rook(63, Alliance.WHITE));

        //White Always Move's First
        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();

    }




    /**
     * Gets All The Legal Moves On The Board
     * @return An Iterable Of All The Legal Moves On The Board
     */
    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getPossibleMoves(), this.blackPlayer.getPossibleMoves()));
    }

    public Collection<Piece> getAllPieces() {
        List<Piece> pieces = new ArrayList<>();
        pieces.addAll(whitePieces);
        pieces.addAll(blackPieces);
        return ImmutableList.copyOf(pieces);
    }

    /**
     * A Nested Class Within Board
     */
    public static class Builder{

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;

        /**
         * The Default Constructor Of Builder Initializing A Hash Map
         */
        public Builder(){
            this.boardConfig = new HashMap<>();
        }

        /**
         * Sets A Piece On The Board
         * @param piece The Piece To Be Places
         * @return The Builder
         */
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        /**
         * Sets The Next Player To Make A Move
         * @param nextMoveMaker The Alliance Of The Next Player To Make A Move
         * @return The Builder
         */
        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        /**
         * Builds A Board
         * @return The Board
         */
        public Board build(){
            return new Board(this);
        }

        public void setEnPassant(Pawn enPassantPawn){
            this.enPassantPawn = enPassantPawn;
        }

    }
}
