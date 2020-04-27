package com.akivaGrobman.Game.Client.Backend.GameObjects.Board;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.King;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ChessBoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void moveWillMoveAPieceCorrectly() throws Exception {
        board = new Board();
        Point pawnOrigin = new Point(2, 1);
        Point destination = new Point(2, 3);
        boolean isLegal;
        String boardAfterMove = "\n----------------------------------------------------------------------------\n" +
                "||  ROOK  || KNIGHT || BISHOP ||  QUEEN ||  KING  || BISHOP || KNIGHT ||  ROOK  |\n" +
                "----------------------------------------------------------------------------\n" +
                "||  PAWN  ||  PAWN  ||        ||  PAWN  ||  PAWN  ||  PAWN  ||  PAWN  ||  PAWN  |\n" +
                "----------------------------------------------------------------------------\n" +
                "||        ||        ||        ||        ||        ||        ||        ||        |\n" +
                "----------------------------------------------------------------------------\n" +
                "||        ||        ||  PAWN  ||        ||        ||        ||        ||        |\n" +
                "----------------------------------------------------------------------------\n" +
                "||        ||        ||        ||        ||        ||        ||        ||        |\n" +
                "----------------------------------------------------------------------------\n" +
                "||        ||        ||        ||        ||        ||        ||        ||        |\n" +
                "----------------------------------------------------------------------------\n" +
                "||  PAWN  ||  PAWN  ||  PAWN  ||  PAWN  ||  PAWN  ||  PAWN  ||  PAWN  ||  PAWN  |\n" +
                "----------------------------------------------------------------------------\n" +
                "||  ROOK  || KNIGHT || BISHOP ||  QUEEN ||  KING  || BISHOP || KNIGHT ||  ROOK  |\n" +
                "----------------------------------------------------------------------------\n";

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
    void willNotAllowMoveToSamePosition() {
        IllegalMoveException thrown = assertThrows(
                IllegalMoveException.class,
                () -> board.isLegalMove(new Point(0,0), new Point(0,0), 1),
                "can not move piece to original position"
        );

        assertTrue(thrown.getMessage().contains("can not move piece to original position"));
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
        Rook enemyRook = new Rook(new Point(5, 1), PieceColor.WHITE);
        board = Board.getConsumeBoard(Collections.singletonList(enemyRook), Collections.singletonList(new Point(3, 1)));
        King king = (King) board.getPiece(new Point(4, 0));
        boolean isLegal;

        isLegal = board.isLegalMove(king.getPiecePosition(), new Point(3, 1), 1);
        assertTrue(isLegal);
    }

}