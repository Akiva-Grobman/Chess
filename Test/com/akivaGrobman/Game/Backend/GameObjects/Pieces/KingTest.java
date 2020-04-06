package com.akivaGrobman.Game.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Backend.GameObjects.Board;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    private King kingStraight;
    private King kingDiagonal;
    private Board board;

    @Test
    void legalMoveWillWork() throws Exception {
        kingStraight = new King(new Point(4,4), PieceColor.WHITE);
        kingDiagonal = new King(new Point(5,5), PieceColor.BLACK);
        Pawn cannonFodder = new Pawn(new Point(4,3), PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(kingDiagonal, kingStraight, cannonFodder), List.of(new Point(3,0), new Point(3,7)));
        kingStraight.move(new Point(4,3), board);
        kingDiagonal.move(new Point(4,6), board);
        assertEquals(new Point(4,3), kingStraight.getPiecePosition());
        assertEquals(new Point(4,6), kingDiagonal.getPiecePosition());
    }

    @Test
    void willNotKillSameColorPiece() throws Exception {
        board = new Board();
        kingStraight = (King) board.getPiece(new Point(3, 0));

        IllegalMoveException thrown = assertThrows(
                IllegalMoveException.class,
                () -> kingStraight.move(new Point(3, 1), board),
                ""
        );

        assertTrue(thrown.getMessage().contains("King can not move from 3,0 to 3,1"));
    }

    //todo add willNotPutItselfInCheck

}