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
    private static List<Point> enemyPiecePositions;
    private static PieceColor playersColor;
    
    public static boolean kingIsInCheck(PieceColor playersColor, Board board, int depth) {
        CheckChecker.board = Board.getClone(board);
        CheckChecker.playersColor = playersColor;
        enemyPiecePositions = getEnemyPiecePositions();
        return isInCheck(depth);
    }

    private static boolean isInCheck(int depth) {
        // in this method we ignore the NoSuchElementException because when depth == 2.  because one of the players doesn't have a king
        Point kingPosition = getKingPosition(playersColor);
        for (Point enemyPiecePosition: enemyPiecePositions) {
            try {
                Piece piece = board.getPiece(enemyPiecePosition);
                assert piece != null;
                if(piece instanceof King) continue;
                if(depth < MAX_DEPTH) {
                    return board.isLegalMove(enemyPiecePosition, kingPosition, depth);
                }
            } catch (NoPieceFoundException | IllegalMoveException ignored) {}
        }
        return false;
    }

    private static Point getKingPosition(PieceColor kingColor) {
        for (int y = 0; y < ChessGame.SUM_OF_ROWS; y++) {
            for (int x = 0; x < ChessGame.SUM_OF_COLUMNS; x++) {
                try {
                    Piece piece = board.getPiece(new Point(x, y));
                    if(piece instanceof King && piece.getPieceColor() == kingColor) {
                        return new Point(x, y);
                    }
                } catch (NoPieceFoundException ignored) {}
            }
        }
        throw new NoSuchElementException(playersColor.toString());
    }

    private static List<Point> getEnemyPiecePositions() {
        List<Point> positions = new ArrayList<>();
        for (int y = 0; y < ChessGame.SUM_OF_ROWS; y++) {
            for (int x = 0; x < ChessGame.SUM_OF_COLUMNS; x++) {
                try {
                    Piece piece = board.getPiece(new Point(x, y));
                    if(piece.getPieceColor() != playersColor) {
                        positions.add(new Point(x, y));
                    }
                } catch (NoPieceFoundException ignored) {}
            }
        }
        return positions;
    }

}
