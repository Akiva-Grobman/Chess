package com.akivaGrobman.Game.Backend.GameRules;

import com.akivaGrobman.Game.Backend.Exceptions.MoveWillPutKingInCheck;
import com.akivaGrobman.Game.Backend.GameObjects.Board;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.King;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.Pawn;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.Rook;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckCheckerTest {

    private Board board;

    @Test
    void willReturnTrueIfIsLegalMove() throws Exception {
        Rook enemyRook = new Rook(new Point(4, 1), PieceColor.WHITE);
        board = Board.getConsumeBoard(Collections.singletonList(enemyRook), new ArrayList<>());
        King king = (King) board.getPiece(new Point(3, 0));
        boolean isInCheck;

        board.move(king.getPiecePosition(), enemyRook.getPiecePosition());
        isInCheck = CheckChecker.kingIsInCheck(king.getPieceColor(), board, 1);

        assertFalse(isInCheck);
    }

    @Test
    void willReturnFalseIfIsIllegalMove() throws Exception {
        Rook enemyRook = new Rook(new Point(3, 5), PieceColor.WHITE);
        Pawn enemyPawn = new Pawn(new Point(4,2), PieceColor.WHITE);
        board = Board.getConsumeBoard(List.of(enemyPawn, enemyRook), new ArrayList<>());
        Pawn pawn = (Pawn) board.getPiece(new Point(3, 1));
        boolean isInCheck;

        board.move(pawn.getPiecePosition(), enemyPawn.getPiecePosition());
        isInCheck = CheckChecker.kingIsInCheck(pawn.getPieceColor(), board, 1);

        assertTrue(isInCheck);
//        MoveWillPutKingInCheck thrown = assertThrows(
//                MoveWillPutKingInCheck.class,
//                () -> pawn.move(destination, board),
//                String.format("can not move form 3,1 to %d,%d %s in position 4,5 will put king in check",
//                        destination.x, destination.y, enemyRook.getClass().getSimpleName())
//        );
//
//        assertTrue(thrown.getMessage().contains(String.format("can not move form 3,1 to %d,%d %s in position 4,5 will put king in check",
//                destination.x, destination.y, enemyRook.getClass().getSimpleName())));
    }

}