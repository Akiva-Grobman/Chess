package com.akivaGrobman.Game.Backend.GameRules;

import com.akivaGrobman.Game.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Backend.GameObjects.Board;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.*;
import com.akivaGrobman.Game.ChessGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CheckChecker {

    private static Board board;
    private static List<Piece> enemyPieces;
    private static PieceColor playersColor;
    
    public static boolean kingIsInCheck(PieceColor playersColor, Board board, int depth) {
        try {
            CheckChecker.board = Board.getClone(board);
        } catch (NoSuchElementException ignored) {}
        CheckChecker.playersColor = playersColor;
        enemyPieces = getEnemyPieces();
        return isInCheck(depth);
    }

    private static boolean isInCheck(int depth) {
        Board testingBoard;
        King playersKing = getKing(playersColor);
        King enemiesKing = null;
        try {
             enemiesKing = getKing(getEnemiesColor());
        } catch (NoSuchElementException ignored) {}
        for (Piece enemyPiece: enemyPieces) {
            try {
                if(depth < 3) {
                    testingBoard = Board.getClone(board);
                    testingBoard.move(enemyPiece.getPiecePosition(), playersKing.getPiecePosition());
                    if(enemiesKing.isInCheck(testingBoard, depth + 1)) {
                        continue;
                    }
                }
                board.move(enemyPiece.getPiecePosition(), playersKing.getPiecePosition());
                return true;
            } catch (IllegalMoveException | NoPieceFoundException ignore) {}
        }
        return false;
    }

    private static PieceColor getEnemiesColor() {
        return (playersColor == PieceColor.BLACK)? PieceColor.WHITE : PieceColor.BLACK;
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
