package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {

    private Bishop bishop;
    private Board board;

    @Test
    void legalMoveWorks() throws Exception {
        bishop = new Bishop(new Point(2,2), PieceColor.BLACK);
        board = Board.getConsumeBoard(Collections.singletonList(bishop), new ArrayList<>());
        boolean isLegal;

        isLegal = bishop.isLegalMove(new Point(2,2), new Point(4,4), board);

        assertTrue(isLegal);
    }

    @Test
    void willNotJumpOverPieces() throws Exception {
        Bishop secondBishop = new Bishop(new Point(2,2), PieceColor.WHITE);
        Pawn blocking = new Pawn(new Point(3,3), PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(secondBishop, blocking), new ArrayList<>());
        bishop = (Bishop) board.getPiece(new Point(2,0));
        boolean firstMoveIsLegal;
        boolean secondMoveIsLegal;

        firstMoveIsLegal = bishop.isLegalMove(new Point(2,0), new Point(0, 3), board);
        secondMoveIsLegal = secondBishop.isLegalMove(new Point(2,2), new Point(4,4), board);

        assertFalse(firstMoveIsLegal);
        assertFalse(secondMoveIsLegal);
    }

    @Test
    void willNotKillSameColorPiece() throws Exception {
        board = new Board();
        bishop = (Bishop) board.getPiece(new Point(2,0));
        boolean isLegal;

        isLegal = bishop.isLegalMove(new Point(2,0), new Point(1, 1), board);

        assertFalse(isLegal);
    }

    @Test
    void willNotMoveStraight() throws Exception {
        board = new Board();
        bishop = (Bishop) board.getPiece(new Point(2,0));
        boolean isLegal;

        isLegal = bishop.isLegalMove(new Point(2,0), new Point(2,1), board);

        assertFalse(isLegal);
    }

}