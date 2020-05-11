package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueenTest {

    private Queen queenStraight;
    private Queen queenDiagonal;
    private Board board;

    @Test
    void legalMoveWorks() throws Exception {
        queenStraight = new Queen(PieceColor.BLACK);
        queenDiagonal = new Queen(PieceColor.WHITE);
        board = Board.getConsumeBoard(Arrays.asList(queenDiagonal, queenStraight), List.of(new Point(5, 4), new Point(2, 2)), new ArrayList<>());
        boolean straightIsLegal;
        boolean diagonalIsLegal;

        straightIsLegal = queenStraight.isLegalMove(new Point(2,2), new Point(7, 2), board);
        diagonalIsLegal = queenDiagonal.isLegalMove(new Point(5,4), new Point(6, 3), board);

        assertTrue(straightIsLegal);
        assertTrue(diagonalIsLegal);
    }

    @Test
    void willNotJumpOverPieces() throws Exception {
        queenDiagonal = new Queen(PieceColor.WHITE);
        Pawn diagonalBlock = new Pawn(PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(queenDiagonal, diagonalBlock), List.of(new Point(3, 3), new Point(4, 4)), new ArrayList<>());
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
        queenDiagonal = new Queen(PieceColor.BLACK);
        board = Board.getConsumeBoard(Collections.singletonList(queenDiagonal), Collections.singletonList(new Point(0, 3)), new ArrayList<>());
        queenStraight = (Queen) board.getPiece(new Point(3,0));
        boolean straightIsLegal;
        boolean diagonalIsLegal;

        straightIsLegal = queenStraight.isLegalMove(new Point(3,0), new Point(3, 1), board);
        diagonalIsLegal = queenDiagonal.isLegalMove(new Point(0, 3), new Point(2, 1), board);

        assertFalse(straightIsLegal);
        assertFalse(diagonalIsLegal);
    }

}