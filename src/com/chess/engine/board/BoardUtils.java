package com.chess.engine.board;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * A Class That Hold Information About The Board
 */
public class BoardUtils {

    public static boolean[] FIRST_COLUMN = initColumn(0);
    public static boolean[] SECOND_COLUMN = initColumn(1);
    public static boolean[] SEVENTH_COLUMN = initColumn(6);
    public static boolean[] EIGHTH_COLUMN = initColumn(7);

    public static boolean[] FIRST_ROW = initRow(0);
    public static boolean[] EIGHT_ROW = initRow(56);

    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

    public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinate();

    /**
     * The Default Constructor For Board Utils Which Shouldn't Be Able To Be Initialised
     */
    private BoardUtils(){
        throw new RuntimeException("Can't Be Instantiated");
    }

    /**
     * Initlalizes The Algerbraic Notation For The Coordinates Of Each Tile
     * @return An Array Of String Of Algebraic Notation
     */
    private static String[] initializeAlgebraicNotation() {
        return new String[]{
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"};
    }

    /**
     * Creates A Hashmap For Each Coordinate To Have Its Corresponding Algebraic Notation As Its Key
     * @return A Hash Map Of String Keys (Algebraic Notation) And Integer (Coordinates)
     */
    private static Map<String, Integer> initializePositionToCoordinate() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();

        for(int i = 0; i < NUM_TILES; i++){
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }

        return ImmutableMap.copyOf(positionToCoordinate);
    }

    /**
     * Initialises Each Colum Array To Contain A Truth Value Where The Value Is In That Column
     * @param columnNumber The Column To Initialise
     * @return A Boolean Array For That Column
     */
    private static boolean[] initColumn(int columnNumber){
        final boolean[] column = new boolean[NUM_TILES];
        do{
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while (columnNumber < NUM_TILES);
        return column;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while(rowNumber % NUM_TILES_PER_ROW != 0);
        return row;
    }

    /**
     * Gets Whether A Tile Is Valid (Within The Coordinate Range)
     * @param coordinate The Coordinate Of The Tile To Check
     * @return If The Tile Is Valid
     */
    public static boolean isValidTile(int coordinate){
        if(coordinate >= 0 && coordinate < NUM_TILES){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets Tne Algebraic Notation For A Given Coordinate
     * @param coordinate The Coordinate To Get
     * @return
     */
    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION[coordinate];
    }

    /**
     * Gets The Coordinate Given An Algebraic Notation String
     * @param position The Algebraic Notation
     * @return
     */
    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }
}
