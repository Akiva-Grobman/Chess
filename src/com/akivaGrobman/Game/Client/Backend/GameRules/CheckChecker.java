package com.akivaGrobman.Game.Client.Backend.GameRules;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.*;
import com.akivaGrobman.Game.Client.ChessGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class CheckChecker {

    private static final int MAX_DEPTH = 3;
    private static Board board;
    private static List<Piece> enemyPieces;
    private static PieceColor playersColor;
    
    public static boolean kingIsInCheck(PieceColor playersColor, Board board, int depth) {
        CheckChecker.board = Board.getClone(board);
        CheckChecker.playersColor = playersColor;
        enemyPieces = getEnemyPieces();
        return isInCheck(depth);
    }

    private static boolean isInCheck(int depth) {
        // in this method we ignore the NoSuchElementException because when depth == 2.  because one of the players doesn't have a king
        King playersKing = getKing(playersColor);
        for (Piece enemyPiece: enemyPieces) {
            if(enemyPiece instanceof King)
                continue;
            try {
                if(depth < MAX_DEPTH) {
                    if(board.isLegalMove(enemyPiece.getPiecePosition(), playersKing.getPiecePosition(), depth)){
                        return true;
                    }
                }
            } catch (IllegalMoveException | NoPieceFoundException ignore) {}
        }
        return false;
    }

    private static King getKing(PieceColor playersColor) {
        for (int y = 0; y < ChessGame.SUM_OF_ROWS; y++) {
            for (int x = 0; x < ChessGame.SUM_OF_COLUMNS; x++) {
                try {
                    Piece piece = board.getPiece(new Point(x, y));
                    if (piece.getPieceType() == PieceType.KING) {
                        if (piece.getPieceColor() == playersColor) {
                            return (King) piece;
                        }
                    }
                } catch (NoPieceFoundException ignored) {}
            }
        }
        throw new NoSuchElementException(playersColor.toString());
    }

    private static List<Piece> getEnemyPieces() {
        List<Piece> enemyPieces = new ArrayList<>();
        for (int y = 0; y < ChessGame.SUM_OF_ROWS; y++) {
            for (int x = 0; x < ChessGame.SUM_OF_COLUMNS; x++) {
                try {
                    Piece piece = board.getPiece(new Point(x,y));
                    if(piece.getPieceColor() != playersColor) {
                        enemyPieces.add(piece);
                    }
                } catch (NoPieceFoundException ignore) {}
            }
        }
        return enemyPieces;
    }

}
