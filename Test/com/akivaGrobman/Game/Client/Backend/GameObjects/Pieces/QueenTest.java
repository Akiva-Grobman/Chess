package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class QueenTest {

    private Queen queenStraight;
    private Queen queenDiagonal;
    private Board board;

    @Test
    void legalMoveWorks() throws Exception {
        queenStraight = new Queen(new Point(2,2), PieceColor.BLACK);
        queenDiagonal = new Queen(new Point(5,4), PieceColor.WHITE);
        board = Board.getConsumeBoard(Arrays.asList(queenDiagonal, queenStraight), new ArrayList<>());
        boolean straightIsLegal;
        boolean diagonalIsLegal;

        straightIsLegal = queenStraight.isLegalMove(new Point(2,2), new Point(7, 2), board);
        diagonalIsLegal = queenDiagonal.isLegalMove(new Point(5,4), new Point(6, 3), board);

        assertTrue(straightIsLegal);
        assertTrue(diagonalIsLegal);
    }

    @Test
    void willNotJumpOverPieces() throws Exception {
        queenDiagonal = new Queen(new Point(3,3), PieceColor.WHITE);
        Pawn diagonalBlock = new Pawn(new Point(4,4), PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(queenDiagonal, diagonalBlock), new ArrayList<>());
        queenStraight = (Queen) board.getPiece(new Point(3,0));
        boolean straightIsLegal;
        boolean diagonalIsLegal;

        straightIsLegal = queenStraight.isLegalMove(new Point(3,0), new Point(3, 2), board);
        diagonalIsLegal = queenDiagonal.isLegalMove(new Point(3,3), new Point(5, 5), board);

        assertFalse(straightIsLegal);
        assertFalse(diagonalIsLegal);
    }

    @Test
    void willNotKillSameColorPiece() throws Exception {
        queenDiagonal = new Queen(new Point(0, 3), PieceColor.BLACK);
        board = Board.getConsumeBoard(Collections.singletonList(queenDiagonal), new ArrayList<>());
        queenStraight = (Queen) board.getPiece(new Point(3,0));
        boolean straightIsLegal;
        boolean diagonalIsLegal;

        straightIsLegal = queenStraight.isLegalMove(new Point(3,0), new Point(3, 1), board);
        diagonalIsLegal = queenDiagonal.isLegalMove(new Point(0, 3), new Point(2, 1), board);

        assertFalse(straightIsLegal);
        assertFalse(diagonalIsLegal);
    }

}