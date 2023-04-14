package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

/**
 * The Move Transition Class
 */
public class MoveTransition {

    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    /**
     * Constructor For Move Transition
     * @param board The Board To Transition On
     * @param move The Move We Are Transitioning
     * @param moveStatus The Moves Status
     */
    public MoveTransition(final Board board, final Move move, final MoveStatus moveStatus){
        this.transitionBoard = board;
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

    /**
     * Get The Transition Board
     * @return The Transition Board
     */
    public Board getTransitionBoard() {
        return this.transitionBoard;
    }
}
