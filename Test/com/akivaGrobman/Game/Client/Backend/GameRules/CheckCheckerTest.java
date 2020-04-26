package com.akivaGrobman.Game.Client.Backend.GameRules;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.King;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Rook;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CheckCheckerTest {

    private Board board;

    @Test
    void willReturnTrueIfIsLegalMove() throws Exception {
        Rook enemyRook = new Rook(new Point(5, 1), PieceColor.WHITE);
        board = Board.getConsumeBoard(Collections.singletonList(enemyRook), Collections.singletonList(new Point(3, 1)));
        King king = (King) board.getPiece(new Point(4, 0));
        boolean isLegal;

        isLegal = board.isLegalMove(king.getPiecePosition(), new Point(3, 1), 1);
        assertTrue(isLegal);
    }

    @Test
    void willReturnFalseIfMovePuttsKingInCheck() {
        Rook enemyRook = new Rook(new Point(4, 5), PieceColor.WHITE);
        board = Board.getConsumeBoard(List.of(enemyRook), List.of(new Point(4,1)));
        boolean isInCheck;

        isInCheck = CheckChecker.kingIsInCheck(PieceColor.BLACK, board, 1);

        assertTrue(isInCheck);
    }

}