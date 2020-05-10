package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    private Rook rook;
    private Board board;

    @Test
    void legalMoveWorks() throws Exception {
         rook = new Rook(new Point(0, 4), PieceColor.BLACK);
         board = Board.getConsumeBoard(Collections.singletonList(rook), Collections.singletonList(new Point(0, 1)));
         boolean isLegal;

         isLegal = rook.isLegalMove(new Point(0, 4), new Point(4, 4), board);

         assertTrue(isLegal);
    }

    @Test
    void willNotJumpOverPieces() throws Exception {
        Rook secondRook = new Rook(new Point(4, 4), PieceColor.WHITE);
        Pawn blocking = new Pawn(new Point(5, 4), PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(secondRook, blocking), new ArrayList<>());
        rook = (Rook) board.getPiece(new Point(0, 0));
        boolean firstRookMoveIsLegal;
        boolean secondRookMoveIsLegal;

        firstRookMoveIsLegal = rook.isLegalMove(new Point(0, 0), new Point(0, 3), board);
        secondRookMoveIsLegal = secondRook.isLegalMove(new Point(4, 4), new Point(6, 4), board);

        assertFalse(firstRookMoveIsLegal);
        assertFalse(secondRookMoveIsLegal);
    }

    @Test
    void willNotKillSameColorPiece() throws Exception {
        board = new Board();
        rook = (Rook) board.getPiece(new Point(0,0));
        boolean isLegal;

        isLegal = rook.isLegalMove(new Point(0,0), new Point(0, 1), board);

        assertFalse(isLegal);
    }

    @Test
    void willNotMoveDiagonal() throws Exception {
        board = new Board();
        rook = (Rook) board.getPiece(new Point(0,0));
        boolean isLegal;

        isLegal = rook.isLegalMove(new Point(0,0), new Point(4,4), board);

        assertFalse(isLegal);
    }

}