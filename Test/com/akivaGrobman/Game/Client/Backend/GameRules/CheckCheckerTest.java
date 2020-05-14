package com.akivaGrobman.Game.Client.Backend.GameRules;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Rook;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CheckCheckerTest {

    @Test
    void willReturnTrueIfMovePutsKingInCheck() {
        Rook enemyRook = new Rook(PieceColor.BLACK);
        Board board = Board.getConsumeBoard(Collections.singletonList(enemyRook), Collections.singletonList(new Point(4, 6)), Collections.singletonList(new Point(4, 6)));
        CheckChecker checkChecker = new CheckChecker(1, PieceColor.WHITE, board);
        boolean isInCheck;

        isInCheck = checkChecker.isInCheck();

        assertTrue(isInCheck);
    }

}