package com.akivaGrobman.Game.Client.Backend.GameObjects;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardConditionsCheckerTest {

    private Board board;

    @Test
    void isInBoundsTest() throws Exception {
        Point positionInBounds = new Point(3,3);
        boolean result;

        result = BoardConditionsChecker.isInBounds(positionInBounds);

        assertTrue(result);
    }

    @Test
    void throwsErrorWithCorrectMessageIfOutOfBounds() throws Exception {
        IllegalMoveException thrown = assertThrows(
                IllegalMoveException.class,
                () -> BoardConditionsChecker.isInBounds(new Point(10, -1)),
                String.format("x = %d y = %d out of bounds", 10, -1)
        );
    }

    @Test
    void isVacantPositionTest() throws Exception {
        board = new Board();
        boolean hasPiece;
        boolean doesNotHavePiece;

        hasPiece = BoardConditionsChecker.isVacantPosition(new Point(1,1), board);
        doesNotHavePiece = BoardConditionsChecker.isVacantPosition(new Point(4,4), board);

        assertTrue(doesNotHavePiece);
        assertFalse(hasPiece);
    }

    @Test
    void hasEnemyPieceTest() throws Exception{
        board = new Board();
        boolean hasEnemyPiece;
        boolean hasPlayersPiece;
        boolean isEmpty;

        hasEnemyPiece = BoardConditionsChecker.hasEnemyPiece(PieceColor.BLACK, new Point(7, 7), board);
        hasPlayersPiece = BoardConditionsChecker.hasEnemyPiece(PieceColor.BLACK, new Point(2, 0), board);
        isEmpty = BoardConditionsChecker.hasEnemyPiece(PieceColor.BLACK, new Point(3, 3), board);

        assertTrue(hasEnemyPiece);
        assertFalse(hasPlayersPiece);
        assertFalse(isEmpty);
    }

}