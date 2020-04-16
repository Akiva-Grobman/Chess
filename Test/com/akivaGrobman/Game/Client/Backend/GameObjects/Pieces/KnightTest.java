package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;

class KnightTest {

    private Knight xUpYDownWhite;
    private Knight xDownYUpBlack;
    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        xUpYDownWhite = (Knight) board.getPiece(new Point(1, 7));
        xDownYUpBlack = (Knight) board.getPiece(new Point(6,0));
    }

    @Test
    void legalMoveWillWork() throws Exception {
        Point newPositionBlack = new Point(5, 2);
        Point newPositionWhite = new Point(2,5);

        xDownYUpBlack.move(newPositionBlack, board);
        xUpYDownWhite.move(newPositionWhite, board);

        assertEquals(newPositionBlack, xDownYUpBlack.getPiecePosition());
        assertEquals(newPositionWhite, xUpYDownWhite.getPiecePosition());
    }

    @Test
    void willNotKillPieceOfSameColor() throws Exception {
        IllegalMoveException whiteThrown = assertThrows(
                IllegalMoveException.class,
                () -> xUpYDownWhite.move(new Point(3,6), board),
                "Knight can not move from 1,7 to 3,6"
        );

        IllegalMoveException blackThrown = assertThrows(
                IllegalMoveException.class,
                () -> xDownYUpBlack.move(new Point(4,1), board),
                "Knight can not move from 6,0 to 4,1"
        );

        assertTrue(whiteThrown.getMessage().contains("Knight can not move from 1,7 to 3,6"));
        assertTrue(blackThrown.getMessage().contains("Knight can not move from 6,0 to 4,1"));
    }

    @Test
    void illegalMoveWillNotWork() throws Exception {
        Point newPositionBlack = new Point(4, 2);
        Point newPositionWhite = new Point(2,4);

        IllegalMoveException thrownBlack = assertThrows(
            IllegalMoveException.class,
            () -> xDownYUpBlack.move(newPositionBlack, board),
            String.format("%s can not move from 6,0 to 4,2", xDownYUpBlack.getClass().getSimpleName())
        );

        IllegalMoveException thrownWhite = assertThrows(
                IllegalMoveException.class,
                () -> xUpYDownWhite.move(newPositionWhite, board),
                String.format("%s can not move from 1,7 to 2,4", xDownYUpBlack.getClass().getSimpleName())
        );

        assertTrue(thrownBlack.getMessage().contains(String.format("%s can not move from 6,0 to 4,2", xDownYUpBlack.getClass().getSimpleName())));
        assertTrue(thrownWhite.getMessage().contains(String.format("%s can not move from 1,7 to 2,4", xDownYUpBlack.getClass().getSimpleName())));
    }

}