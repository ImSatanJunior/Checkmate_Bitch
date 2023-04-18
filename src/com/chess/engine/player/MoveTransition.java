package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

/**
 * The Move Transition Class
 */
public class MoveTransition {

    private final Board fromBoard;
    private final Board toBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    /**
     * Constructor For Move Transition
     * @param fromBoard The Board Before Executing The Move
     * @param toBoard The Board After Executing The Move
     * @param move The Move We Are Transitioning
     * @param moveStatus The Moves Status
     */
    public MoveTransition(final Board fromBoard, final Board toBoard, final Move move, final MoveStatus moveStatus){
        this.fromBoard = fromBoard;
        this.toBoard = toBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    /**
     * Gets The Moves Status
     * @return
     */
    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }


    public Board getFromBoard() {
        return this.fromBoard;
    }

    public Board getToBoard(){
        return this.toBoard;
    }


}
