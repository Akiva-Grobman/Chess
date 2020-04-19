package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {

    private Bishop bishop;
    private Board board;

    @Test
    void legalMoveWorks() throws Exception {
        bishop = new Bishop(new Point(2,2), PieceColor.BLACK);
        Bishop newPosition = new Bishop(new Point(4,4), PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(bishop), new ArrayList<>());

        bishop.move(new Point(4,4), board);

        assertEquals(newPosition, bishop);
    }

    @Test
    void willNotJumpOverPieces() throws Exception {
        Bishop secondBishop = new Bishop(new Point(2,2), PieceColor.WHITE);
        Pawn blocking = new Pawn(new Point(3,3), PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(secondBishop, blocking), new ArrayList<>());
        bishop = (Bishop) board.getPiece(new Point(2,0));

        IllegalMoveException thrown1 = assertThrows(
                IllegalMoveException.class,
                () -> bishop.move(new Point(0, 3), board),
                ""
        );

        IllegalMoveException thrown2 = assertThrows(
                IllegalMoveException.class,
                () -> secondBishop.move(new Point(4,4), board),
                ""
        );

        assertTrue(thrown1.getMessage().contains("Bishop can not move from 2,0 to 0,3"));
        assertTrue(thrown2.getMessage().contains("Bishop can not move from 2,2 to 4,4"));
    }

    @Test
    void willNotKillSameColorPiece() throws Exception {
        board = new Board();
        bishop = (Bishop) board.getPiece(new Point(2,0));

        IllegalMoveException thrown = assertThrows(
                IllegalMoveException.class,
                () -> bishop.move(new Point(1, 1), board),
                "Bishop can not move from 2,0 to 1,1"
        );

        assertTrue(thrown.getMessage().contains("Bishop can not move from 2,0 to 1,1"));
    }

    @Test
    void willNotMoveStraight() throws Exception {
        board = new Board();
        bishop = (Bishop) board.getPiece(new Point(2,0));

        IllegalMoveException thrown = assertThrows (
                IllegalMoveException.class,
                () -> bishop.move(new Point(2,1), board),
                String.format("%s can not move from 2,0 t0 2,1", bishop.getClass().getSimpleName())
        );
        System.out.println(thrown.getMessage());
        System.out.println(String.format("%s can not move from 2,0 t0 2,1", bishop.getClass().getSimpleName()));
        assertTrue(thrown.getMessage().contains(String.format("%s can not move from 2,0 to 2,1", bishop.getClass().getSimpleName())));
    }
}