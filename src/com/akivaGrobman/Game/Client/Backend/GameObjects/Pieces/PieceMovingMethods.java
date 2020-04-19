package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;

import java.awt.*;

import static com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker.hasEnemyPiece;
import static com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker.isVacantPosition;

abstract class PieceMovingMethods {

    protected Board board;

    protected boolean canMoveThere(Point tempDestination, PieceColor playersColor) {
        return isVacantPosition(tempDestination, board) || hasEnemyPiece(playersColor, tempDestination, board);
    }

    protected boolean hasPieceInPathToDestination(Point destination, Point tempDestination) {
        return !destination.equals(tempDestination) && !isVacantPosition(tempDestination, board);
    }


}
