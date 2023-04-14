package com.chess.engine.board;

import com.chess.engine.pieces.Alliance;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * A Class For Each Individual Tile On The Board
 */
public abstract class Tile {

    //Is This Tiles Tile Coordinate, Protect (Only Accessible By Subclasses)
    protected final int tileCoordinate;

    private static final Map<Integer, emptyTile> EMPTY_TILE_CACHE = createAllEmptyTiles();

    /**
     * Creates A Hash Map Of All The Possible Tiles
     * @return A Copy Of The Hash Map
     */
    private static Map<Integer, emptyTile> createAllEmptyTiles(){
        final Map<Integer, emptyTile> emptyTileMap = new HashMap<>();
        for(int i = 0; i < 64; i++ ){
            emptyTileMap.put(i, new emptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    /**
     * Creates A Tile Whenever Needed Creating A New Occupied Tile If There Is A Piece On That Tile Or Gets On Of The
     * Cached Tiles From The Hash Map Of Empty Tiles
     * @param tileCoordinate The Coordinate Of The Tile
     * @param piece The Optional Piece Input
     * @return The Tile
     */
    public static Tile createTile(final int tileCoordinate, final Piece piece){
        if(piece != null){
            return new occupiedTile(tileCoordinate, piece);
        } else {
            return EMPTY_TILE_CACHE.get(tileCoordinate);
        }
    }

    /**
     * The Constructor For The Tile Class Initialising The Integer Tiles Coordinate
     * @param tileCoordinates
     */
    public Tile(int tileCoordinates){
        this.tileCoordinate = tileCoordinates;
    }

    /**
     * Gets The Tiles Coordinate
     * @return The Tiles Coordinate
     */
    public int getTileCoordinate(){
        return this.tileCoordinate;
    }

    /**
     * Whether The Tiles Is Occupied
     * @return Boolean If The Tile Is Occupied
     */
    public abstract boolean isTileOccupied();

    /**
     * Gets The Piece On The Tile
     * @return The Piece On The Tiles
     */
    public abstract Piece getPiece();

    /**
     * Sub Class Of Tile For Empty Tiles (Tiles Without Pieces On)
     */
    public static final class emptyTile extends Tile{

        emptyTile(final int tileCoordinates){
            super(tileCoordinates);
        }

        /**
         * The String Format Of The Empty Tile
         * @return The String For The Tile
         */
        @Override
        public String toString(){
            return "-";
        }

        /**
         * Overrides The Default Function To Always Return False As The Tile Is Never Occupied
         * @return False As The Tile Isn't Occupied
         */
        @Override
        public boolean isTileOccupied(){
            return false;
        }

        /**
         * Overrides The Default Get Piece Function To Always Return Null As The Tile Is Empty
         * @return False As The Tile Is Always Empty
         */
        @Override
        public Piece getPiece(){
            return null;
        }
    }

    /**
     * A Sub Class Of Tile For Occupied Tiles (Tiles With Pieces On)
     */
    public static final class occupiedTile extends Tile{
        //Is The Piece On The Tile
        private final Piece pieceOnTile;

        //Creates An Occupied Tile Setting The Coordinates And The Piece On That Tile
        occupiedTile(final int tileCoordinates, Piece pieceOnTile){
            super(tileCoordinates);
            this.pieceOnTile = pieceOnTile;
        }

        /**
         * Overrides The Default To String Function To Return The Piece Capitalised Depended On The Alliance
         * @return
         */
        @Override
        public String toString(){
            return getPiece().getPieceAlliance() == Alliance.BLACK ? getPiece().toString().toLowerCase() :
                    getPiece().toString();
        }

        /**
         * Returns True As The Tile Is Always Occupied
         * @return True As The Tile Is Occupied
         */
        @Override
        public boolean isTileOccupied(){
            return true;
        }

        /**
         * Gets The Piece On The Tile
         * @return The Piece On The Tile
         */
        @Override
        public Piece getPiece(){
            return this.pieceOnTile;
        }
    }
}
