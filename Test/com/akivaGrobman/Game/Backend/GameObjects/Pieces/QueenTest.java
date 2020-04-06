package com.akivaGrobman.Game.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Backend.GameObjects.Board;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

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
        Queen newPosition =  new Queen(new Point(7, 2), PieceColor.WHITE);

        queenStraight.move(new Point(7, 2), board);
        queenDiagonal.move(new Point(7, 2), board);

        assertEquals(newPosition, queenDiagonal);
    }

    @Test
    void willNotJumpOverPieces() throws Exception {
        queenDiagonal = new Queen(new Point(3,3), PieceColor.WHITE);
        Pawn diagonalBlock = new Pawn(new Point(4,4), PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(queenDiagonal, diagonalBlock), new ArrayList<>());
        queenStraight = (Queen) board.getPiece(new Point(4,0));

        IllegalMoveException straight = assertThrows(
                IllegalMoveException.class,
                () -> queenStraight.move(new Point(4,2), board),
                "Queen can not move from 4,0 to 4,2"
        );
        IllegalMoveException diagonal = assertThrows(
                IllegalMoveException.class,
                () -> queenDiagonal.move(new Point(5,5), board),
                "Queen can not move from 3,3 to 5,5"
        );

        assertTrue(straight.getMessage().contains("Queen can not move from 4,0 to 4,2"));
        assertTrue(diagonal.getMessage().contains("Queen can not move from 3,3 to 5,5"));
    }

    @Test
    void willNotKillSameColorPiece() throws Exception {
        queenDiagonal = new Queen(new Point(0, 3), PieceColor.BLACK);
        board = Board.getConsumeBoard(Arrays.asList(queenDiagonal), new ArrayList<>());
        queenStraight = (Queen) board.getPiece(new Point(4,0));

        IllegalMoveException straight = assertThrows(
                IllegalMoveException.class,
                () -> queenStraight.move(new Point(4,1), board),
                "Queen can not move from 4,0 to 4,1"
        );
        IllegalMoveException diagonal = assertThrows(
                IllegalMoveException.class,
                () -> queenDiagonal.move(new Point(2, 1), board),
                "Queen can not move from 0,3 to 2,1"
        );

        assertTrue(straight.getMessage().contains("Queen can not move from 4,0 to 4,1"));
        assertTrue(diagonal.getMessage().contains("Queen can not move from 0,3 to 2,1"));

    }

}