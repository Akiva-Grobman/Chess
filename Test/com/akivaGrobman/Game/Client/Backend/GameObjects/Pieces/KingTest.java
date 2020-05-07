package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    private King kingStraight;
    private Board board;

    @Test
    void legalMoveWillWork() throws Exception {
        kingStraight = new King(new Point(4,4), PieceColor.WHITE);
        King kingDiagonal = new King(new Point(5, 5), PieceColor.BLACK);
        Pawn cannonFodder = new Pawn(new Point(4,3), PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(kingDiagonal, kingStraight, cannonFodder), List.of(new Point(3,0), new Point(3,7)));
        boolean straightIsLegal;
        boolean diagonalIsLegal;

        straightIsLegal = kingStraight.isLegalMove(new Point(4, 3), board);
        diagonalIsLegal = kingDiagonal.isLegalMove(new Point(4, 6), board);

        assertTrue(straightIsLegal);
        assertTrue(diagonalIsLegal);
    }

    @Test
    void willNotKillSameColorPiece() throws Exception {
        board = new Board();
        kingStraight = (King) board.getPiece(new Point(4, 0));
        boolean isLegal;

        isLegal = kingStraight.isLegalMove(new Point(4, 1), board);

        assertFalse(isLegal);
    }

    @Test
    void castlingIsLegal() throws Exception {
        board = Board.getConsumeBoard(new ArrayList<>(), List.of(new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(5, 0), new Point(6, 0)));
        boolean castlingToTheLeftIsLegal;
        boolean castlingToTheRightIsLegal;

        castlingToTheLeftIsLegal = board.isLegalMove(new Point(4, 0), new Point(1, 0), 1);
        castlingToTheRightIsLegal = board.isLegalMove(new Point(4, 0), new Point(6, 0), 1);

        assertTrue(castlingToTheLeftIsLegal);
        assertTrue(castlingToTheRightIsLegal);
    }

    @Test
    void castlingIsIllegalIfKingHasMoved() throws Exception {
        board = Board.getConsumeBoard(new ArrayList<>(), List.of(new Point(1, 0), new Point(2, 0), new Point(3, 0)));
        boolean castlingIsLegal;
        King king = (King) board.getPiece(new Point(4, 0));
        king.moved();

        castlingIsLegal = board.isLegalMove(new Point(4, 0), new Point(1, 0), 1);

        assertFalse(castlingIsLegal);
    }

    @Test
    void castlingIsIllegalIfRookMoved() throws Exception {
        board = Board.getConsumeBoard(new ArrayList<>(), List.of(new Point(1, 0), new Point(2, 0), new Point(3, 0)));
        boolean castlingIsLegal;
        Rook rook = (Rook) board.getPiece(new Point(0, 0));
        rook.moved();

        castlingIsLegal = board.isLegalMove(new Point(4, 0), new Point(1, 0), 1);

        assertFalse(castlingIsLegal);
    }

    @Test
    void castlingIsIllegalIfKingWasInCheck() throws Exception {
        board = Board.getConsumeBoard(new ArrayList<>(), List.of(new Point(1, 0), new Point(2, 0), new Point(3, 0)));
        boolean castlingIsLegal;
        King king = (King) board.getPiece(new Point(4, 0));
        king.setToIsInCheck();

        castlingIsLegal = board.isLegalMove(new Point(4, 0), new Point(1, 0), 1);

        assertFalse(castlingIsLegal);
    }

    @Test
    void castlingIsIllegalIfKingWillBeInCheckOnTheWay() throws Exception {
        board = Board.getConsumeBoard(List.of(new Rook(new Point(2, 3), PieceColor.WHITE)), List.of(new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(2, 1)));
        boolean castlingIsLegal;

        castlingIsLegal = board.isLegalMove(new Point(4, 0), new Point(1, 0), 1);

        assertFalse(castlingIsLegal);
    }

}