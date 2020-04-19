package com.akivaGrobman.Game.Client.Backend.GameObjects;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void cloneIsToFullDepth() throws Exception {
        Board clone = Board.getClone(board);

        assertEquals(board.toString(), clone.toString());
        assertEquals(board.getPiece(new Point(1,1)).getPiecePosition(), clone.getPiece(new Point(1,1)).getPiecePosition());
        assertEquals(board.getPiece(new Point(1,1)), clone.getPiece(new Point(1,1)));
        assertNotEquals(board.hashCode(), clone.hashCode());
        assertNotEquals(Arrays.deepHashCode(board.getBoard()), Arrays.deepHashCode(clone.getBoard()));
    }

    @Test
    void moveWillMoveAPieceCorrectly() throws Exception {
        board = new Board();
        Point pawnOrigin = new Point(2, 1);
        Point destination = new Point(2, 3);
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

        board.move(pawnOrigin, destination, 1);

        assertEquals(boardAfterMove, board.toString());
    }

    @Test
    void willNotAllowIllegalMove() {
        NoPieceFoundException thrown = assertThrows(
                NoPieceFoundException.class,
                () -> board.move(new Point(4,4), new Point(3,3), 1),
                "no piece found in position x = " + 4 + " y = " + 4
        );

        assertTrue(thrown.getMessage().contains("no piece found in position x = " + 4 + " y = " + 4));
    }

    @Test
    void willNotAllowMoveToSamePosition() {
        IllegalMoveException thrown = assertThrows(
            IllegalMoveException.class,
            () -> board.move(new Point(0,0), new Point(0,0), 1),
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

    //todo will not put piece in check

}