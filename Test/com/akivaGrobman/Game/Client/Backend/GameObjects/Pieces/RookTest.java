package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    private Rook rook;
    private Board board;

    @Test
    void legalMoveWorks() throws Exception {
         rook = new Rook(new Point(0, 4), PieceColor.BLACK);
         Rook newPosition = new Rook(new Point(4, 4), PieceColor.BLACK);
         board = Board.getConsumeBoard(Arrays.asList(rook), new ArrayList<>());

         rook.move(newPosition.getPiecePosition(), board);

         assertEquals(newPosition, rook);
    }

    @Test
    void willNotJumpOverPieces() throws Exception {
        Rook secondRook = new Rook(new Point(4,4), PieceColor.WHITE);
        Pawn blocking = new Pawn(new Point(5,4), PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(secondRook, blocking), new ArrayList<>());
        rook = (Rook) board.getPiece(new Point(0, 0));

        IllegalMoveException one = assertThrows(
                IllegalMoveException.class,
                () -> rook.move(new Point(0, 3), board),
                "Rook can not move from 0,0 to 0,3"
        );

        IllegalMoveException two = assertThrows(
                IllegalMoveException.class,
                () -> secondRook.move(new Point(6, 4), board),
                "Rook can not move from 4,4 to 6,4"
        );

        assertTrue(one.getMessage().contains("Rook can not move from 0,0 to 0,3"));
        assertTrue(two.getMessage().contains("Rook can not move from 4,4 to 6,4"));
    }

    @Test
    void willNotKillSameColorPiece() throws Exception {
        board = new Board();
        rook = (Rook) board.getPiece(new Point(0,0));

        IllegalMoveException thrown = assertThrows(
                IllegalMoveException.class,
                () -> rook.move(new Point(0, 1), board),
                "Rook can not move from 0,0 to 0,1"
        );

        assertTrue(thrown.getMessage().contains("Rook can not move from 0,0 to 0,1"));
    }

    @Test
    void willNotMoveDiagonal() throws Exception {
        board = new Board();
        rook = (Rook) board.getPiece(new Point(0,0));

        IllegalMoveException thrown = assertThrows(
                IllegalMoveException.class,
                () -> rook.move(new Point(4,4), board),
                String.format("%s can not move from 0,0 to 4,4", rook.getClass().getSimpleName())
        );

        assertTrue(thrown.getMessage().contains(String.format("%s can not move from 0,0 to 4,4", rook.getClass().getSimpleName())));
    }
}