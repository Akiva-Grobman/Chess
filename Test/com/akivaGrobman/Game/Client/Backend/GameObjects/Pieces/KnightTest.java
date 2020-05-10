package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;

class KnightTest {

    private Knight xUpYDownWhite;
    private Knight xDownYUpBlack;
    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        xUpYDownWhite = (Knight) board.getPiece(new Point(1, 7));
        xDownYUpBlack = (Knight) board.getPiece(new Point(6,0));
    }

    @Test
    void legalMoveWillWork() throws Exception {
        Point newPositionBlack = new Point(5, 2);
        Point newPositionWhite = new Point(2,5);
        boolean upIsLegal;
        boolean downIsLegal;

        upIsLegal = xDownYUpBlack.isLegalMove(new Point(1, 7), newPositionBlack, board);
        downIsLegal = xUpYDownWhite.isLegalMove(new Point(6,0), newPositionWhite, board);

        assertTrue(upIsLegal);
        assertTrue(downIsLegal);
    }

    @Test
    void willNotKillPieceOfSameColor() throws Exception {
        boolean blackKnightMoveIsLegal;
        boolean whiteKnightMoveIsLegal;

        blackKnightMoveIsLegal = xUpYDownWhite.isLegalMove(new Point(1, 7), new Point(3,6), board);
        whiteKnightMoveIsLegal = xDownYUpBlack.isLegalMove(new Point(6,0), new Point(4,1), board);

        assertFalse(blackKnightMoveIsLegal);
        assertFalse(whiteKnightMoveIsLegal);
    }

    @Test
    void illegalMoveWillNotWork() throws Exception {
        Point newPositionBlack = new Point(4, 2);
        Point newPositionWhite = new Point(2,4);
        boolean whiteKnightMoveIsLegal;
        boolean blackKnightMoveIsLegal;

        blackKnightMoveIsLegal = xDownYUpBlack.isLegalMove(new Point(1, 7), newPositionBlack, board);
        whiteKnightMoveIsLegal = xUpYDownWhite.isLegalMove(new Point(6,0), newPositionWhite, board);

        assertFalse(blackKnightMoveIsLegal);
        assertFalse(whiteKnightMoveIsLegal);
    }

}