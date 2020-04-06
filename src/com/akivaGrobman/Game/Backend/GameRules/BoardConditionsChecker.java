package com.akivaGrobman.Game.Backend.GameRules;

import com.akivaGrobman.Game.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Backend.GameObjects.Board;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.ChessGame;

import java.awt.*;

public class BoardConditionsChecker {

    public static boolean isInBounds(Point position) throws IllegalMoveException {
        if(position.x < ChessGame.SUM_OF_COLUMNS && position.x >= 0 && position.y < ChessGame.SUM_OF_ROWS && position.y >= 0) {
            return true;
        }
        throw new IllegalMoveException(String.format("x = %d y = %d out of bounds", position.x, position.y));
    }

    public static boolean isVacantPosition(Point position, Board board) {
        return !board.hasPieceInThisPosition(position);
    }

    public static boolean hasEnemyPiece(PieceColor playersColor, Point destination, Board board) {
        if (!isVacantPosition(destination, board)) {
            try {
                return playersColor != board.getPiece(destination).getPieceColor();
            } catch (NoPieceFoundException e) {
                return false;
            }
        }
        return false;
    }



}
