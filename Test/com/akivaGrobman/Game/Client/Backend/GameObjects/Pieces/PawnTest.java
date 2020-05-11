package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    private Pawn pawn1;
    private Pawn pawn2;
    private Board board;

    @Test
    void legalMoveWithOutKillingWorks() throws Exception {
        board = new Board();
        pawn1 = (Pawn) board.getPiece(new Point(1,1));
        pawn2 = (Pawn) board.getPiece(new Point(2, 1));
        boolean firsMoveIsLegal;
        boolean secondMoveIsLegal;

        firsMoveIsLegal = pawn1.isLegalMove(new Point(1,1), new Point(1, 2), board);
        secondMoveIsLegal = pawn2.isLegalMove(new Point(2, 1), new Point(2, 3), board);

        assertTrue(firsMoveIsLegal);
        assertTrue(secondMoveIsLegal);
    }

    @Test
    void legalMoveWithKillingWorks() throws Exception {
        pawn1 = new Pawn(PieceColor.BLACK);
        pawn2 = new Pawn(PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(pawn1, pawn2), List.of(new Point(2, 5), new Point(6, 5)), new ArrayList<>());
        boolean firstMoveIsLegal;
        boolean secondMoveIsLegal;

        firstMoveIsLegal = pawn1.isLegalMove(new Point(2, 5), new Point(3, 6), board);
        secondMoveIsLegal = pawn2.isLegalMove(new Point(6, 5), new Point(5, 6), board);

        assertTrue(firstMoveIsLegal);
        assertTrue(secondMoveIsLegal);
    }

    @Test
    void illegalPositionWillNotWork() throws Exception {
        board = new Board();
        pawn1 = new Pawn(PieceColor.BLACK);
        boolean isLegal;

        isLegal = pawn1.isLegalMove(new Point(1,1), new Point(4, 4), board);

        assertFalse(isLegal);
    }

    @Test
    void willAllowEnpassant() throws Exception {
        pawn1 = new Pawn(PieceColor.WHITE);
        pawn2 = new Pawn(PieceColor.BLACK);
        board = Board.getConsumeBoard(List.of(pawn1, pawn2), List.of(new Point(2, 3), new Point(new Point(1, 3))), new ArrayList<>());
        boolean isLegal;
        board.setEnpassant(PieceColor.BLACK, 1);

        isLegal = pawn1.isLegalMove(new Point(2,3), new Point(1, 2), board);

        assertTrue(isLegal);
    }

}