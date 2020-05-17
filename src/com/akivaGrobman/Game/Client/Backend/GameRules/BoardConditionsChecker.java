package com.akivaGrobman.Game.Client.Backend.GameRules;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;

import java.awt.*;

import static com.akivaGrobman.Game.Client.GameManagers.ChessGame.SUM_OF_COLUMNS;
import static com.akivaGrobman.Game.Client.GameManagers.ChessGame.SUM_OF_ROWS;

public abstract class BoardConditionsChecker {

    public static boolean isInBounds(Point position) throws IllegalMoveException {
        if(position.x < SUM_OF_COLUMNS && position.x >= 0 && position.y < SUM_OF_ROWS && position.y >= 0) {
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
