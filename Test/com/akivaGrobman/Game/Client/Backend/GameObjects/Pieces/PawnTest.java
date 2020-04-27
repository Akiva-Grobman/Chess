package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.Players.Enemy;
import com.akivaGrobman.Game.Client.ChessGame;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    private Pawn pawn1;
    private Pawn pawn2;
    private Pawn expectedPawn1;
    private Pawn expectedPawn2;
    private Board board;

    @Test
    void legalMoveWithOutKillingWorks() throws Exception {
        board = new Board();
        pawn1 = (Pawn) board.getPiece(new Point(1,1));
        pawn2 = new Pawn(new Point(1,1), PieceColor.BLACK);
        expectedPawn1 = new Pawn(new Point(1, 2),PieceColor.BLACK);
        expectedPawn2 = new Pawn(new Point(1, 3),PieceColor.BLACK);

        pawn1.move(new Point(1, 2), board);
        pawn2.move(new Point(1,3), board);

        assertEquals(pawn1, expectedPawn1);
        assertEquals(pawn2, expectedPawn2);
    }

    @Test
    void LegalMoveWithKillingWorks() throws Exception {
        pawn1 = new Pawn(new Point(2, 5), PieceColor.BLACK);
        pawn2 = new Pawn(new Point(6, 5), PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(pawn1, pawn2), new ArrayList<>());
        expectedPawn1 = new Pawn(new Point(3, 6), PieceColor.BLACK);
        expectedPawn2 = new Pawn(new Point(5, 6), PieceColor.BLACK);

        pawn1.move(new Point(3, 6), board);
        pawn2.move(new Point(5, 6), board);

        assertEquals(pawn1, expectedPawn1);
        assertEquals(pawn2, expectedPawn2);
    }

    @Test
    void illegalMoveWillNotWork() {
        board = new Board();
        pawn1 = new Pawn(new Point(1,1), PieceColor.BLACK);
         IllegalMoveException thrown = assertThrows(
                IllegalMoveException.class,
                () -> pawn1.move(new Point(4,4), board),
                 String.format("%s can not move from 1,1 to 4,4", pawn1.getClass().getSimpleName())
         );

         assertTrue(thrown.getMessage().contains(String.format("%s can not move from 1,1 to 4,4", pawn1.getClass().getSimpleName())));
    }

    @Test
    void willAllowEnpassant() throws Exception{
        pawn1 = new Pawn(new Point(2,3), PieceColor.WHITE);
        pawn2 = new Pawn(new Point(1, 3), PieceColor.BLACK, true);
        board = Board.getConsumeBoard(List.of(pawn1, pawn2), new ArrayList<>());
        boolean isLegal;

        isLegal = board.isLegalMove(pawn1.getPiecePosition(), new Point(1,2), 1);

        assertTrue(isLegal);
    }

}