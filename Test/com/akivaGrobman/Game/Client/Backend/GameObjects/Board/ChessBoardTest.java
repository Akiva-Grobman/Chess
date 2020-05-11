package com.akivaGrobman.Game.Client.Backend.GameObjects.Board;

import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.King;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ChessBoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void moveWillReturnTrueIfLegalMove() throws Exception {
        board = new Board();
        Point pawnOrigin = new Point(2, 1);
        Point destination = new Point(2, 3);
        boolean isLegal;

        isLegal = board.isLegalMove(pawnOrigin, destination, 1);

        assertTrue(isLegal);
    }

    @Test
    void willNotAllowIllegalMove() {
        NoPieceFoundException thrown = assertThrows(
                NoPieceFoundException.class,
                () -> board.isLegalMove(new Point(4,4), new Point(3,3), 1),
                "no piece found in position x = " + 4 + " y = " + 4
        );

        assertTrue(thrown.getMessage().contains("no piece found in position x = " + 4 + " y = " + 4));
    }

    @Test
    void willNotAllowMoveToSamePosition() throws Exception {
        boolean isLegal;

        isLegal = board.isLegalMove(new Point(4,4), new Point(4, 4), 1);

        assertFalse(isLegal);
    }

    @Test
    void hasPieceInThisPositionTest() {
        boolean hasPiece;
        boolean doesNotHavePiece;

        hasPiece = board.hasPieceInThisPosition(new Point(1, 1));
        doesNotHavePiece = board.hasPieceInThisPosition(new Point(4,4));

        assertTrue(hasPiece);
        assertFalse(doesNotHavePiece);
    }

    @Test
    void willReturnTrueIfIsLegalMove() throws Exception {
        board = Board.getConsumeBoard(new ArrayList<>(), new ArrayList<>(), Collections.singletonList(new Point(3, 1)));
        King king = (King) board.getPiece(new Point(4, 0));
        boolean isLegal;

        isLegal = board.isLegalMove(new Point(4, 0), new Point(3, 1), 1);

        assertTrue(isLegal);
    }

}