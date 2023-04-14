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

/**
 * The Black Player Class Extending Off Player
 */
public class BlackPlayer extends Player{

    /**
     * Constructor For The Black Player
     * @param board The Current Board
     * @param whiteStandardLegalMoves The Legal Moves For White
     * @param blackStandardLegalMoves The Legal Moves Black
     */
    public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    /**
     * Gets All Of Black's Active Pieces
     * @return Blacks Pieces On The Board
     */
    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    /**
     * Gets Black's Alliance
     * @return BLACK
     */
    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    /**
     * Gets Black's Opponent
     * @return whitePlayer
     */
    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
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
            if (!(this.board.getTile(5).isTileOccupied()) &&
                    !(this.board.getTile(6).isTileOccupied())) {
                final Tile rookTile = this.board.getTile(7);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegals).isEmpty()) {
                        kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 6,
                                (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
                    }
                }
            }
        }

            //Whites Queenside Castle
            if(!(this.board.getTile(3).isTileOccupied()) &&
                    !(this.board.getTile(2).isTileOccupied()) &&
                    !(this.board.getTile(1).isTileOccupied())){
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(2, opponentLegals).isEmpty()){
                        kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 2,
                                (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
                    }
                }
            }

            return ImmutableList.copyOf(kingCastles);
    }

    @Override
    public String toString() {
        return "Black";
    }
}
