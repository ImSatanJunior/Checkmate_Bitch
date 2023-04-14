package com.chess.engine.player;

/**
 * The Enum For The Move Status, This Is The Status Of The Move ( Done / Illegal Move / Leaves Player In Check)
 */
public enum MoveStatus {
    DONE {
        /**
         * Gets Whether The Move Is Done
         * @return True
         */
        @Override
        public boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE{
        /**
         * Gets Whether The Move Is Done
         * @return False
         */
        @Override
        public boolean isDone() {
            return false;
        }
    }, LEAVES_PLAYER_IN_CHECK {
        /**
         * Gets Whether The Move Is Done
         * @return False
         */
        @Override
        public boolean isDone() {
            return false;
        }
    };

    /**
     * Gets Whether The Move Is Done
     */
    public abstract boolean isDone();
}
