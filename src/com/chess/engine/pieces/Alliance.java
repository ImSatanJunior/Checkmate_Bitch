package com.chess.engine.pieces;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

/**
 * The Enum For The Alliance, This Is The Alliance Of The Pieces / Players ( White / Black)
 */
public enum Alliance {
    WHITE {
        /**
         * Returns The Direction Of Whites Pieces
         * @return
         */
        @Override
        public int getDirection() {
            return -1;
        }

        /**
         * Always Returns The Current Player Is The White Player For White
         * @param whitePlayer
         * @param blackPlayer
         * @return The White Player
         */
        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer){
            return whitePlayer;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_ROW[position];
        }
    },
    BLACK {
        /**
         * Returns The Direction Of Blacks Pieces
         * @return
         */
        @Override
        public int getDirection() {
            return 1;
        }

        /**
         * Always Returns The Current Player Is The Black Player For White
         * @param whitePlayer
         * @param blackPlayer
         * @return The Black Player
         */
        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer){
            return blackPlayer;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGHT_ROW[position];
        }
    };

    /**
     * Gets The Direction For That Alliance
     */
    public abstract int getDirection();

    /**
     * Choose The Player For That Alliance
     * @param whitePlayer The White Player
     * @param blackPlayer The Black Player
     */
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);

    public abstract boolean isPawnPromotionSquare(int position);
}
