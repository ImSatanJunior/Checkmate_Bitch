package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Alliance;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player{

    /**
     * Constructor For The White Player
     * @param board The Current Board
     * @param whiteStandardLegalMoves The Legal Moves For White
     * @param blackStandardLegalMoves The Legal Moves Black
     */
    public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    /**
     * Gets All Of White's Active Pieces
     * @return Whites Pieces On The Board
     */
    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    /**
     * Gets White's Alliance
     * @return WHITE
     */
    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    /**
     * Gets White's Opponent
     * @return blackPlayer
     */
    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    /**
     * Calculates The Possible King Castling Moves
     *
     * Creates A List Of kingCastles Moves
     *
     * Firstly Checks It's The Kings First Move And The King Isn't In Check
     * If Both The Tiles Between The King And That Sides Castle Are Not Occupied
     * Get The Tile The Rook Is On, If That Tile Is Occupied And Its Piece Is On Its First Move
     * Calculate The Attacks On Tiles The King Is Moving Over / To
     * If There Isn't Any Add The Relevant Castling Move Fpr That Side
     *
     * @param playerLegals The Current Players Legal Moves
     * @param opponentLegals The Opponents Legal Moves
     * @return A Copy Of The List kingCastles
     */
    @Override
    public Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !(this.isInCheck())) {
            //Whites Kingside Castle
            if (!(this.board.getTile(61).isTileOccupied()) &&
                    !(this.board.getTile(62).isTileOccupied())) {
                final Tile rookTile = this.board.getTile(63);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentLegals).isEmpty()) {
                        kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 62,
                                (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
                    }
                }
            }
        }

            //Whites Queenside Castle
            if(!(this.board.getTile(59).isTileOccupied()) &&
                    !(this.board.getTile(58).isTileOccupied()) &&
                    !(this.board.getTile(57).isTileOccupied())){
                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(58, opponentLegals).isEmpty()){
                        kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 58,
                                (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                    }
                }
            }

        return ImmutableList.copyOf(kingCastles);
    }

    @Override
    public String toString() {
        return "White";
    }
}
