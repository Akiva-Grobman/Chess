package com.akivaGrobman.Game.Client.Backend.GameRules;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Move;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.King;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Pawn;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.Players.Positions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.akivaGrobman.Game.Client.Backend.GameRules.SpecialMoves.*;
import static org.junit.jupiter.api.Assertions.*;

class SpecialMovesTest {

    @Test
    void willDetectAnEnpassantMove() {
        boolean wasEnpassant;
        Board board;
        List<Move> moves = new ArrayList<>();
        Pawn pawn1 = new Pawn(new Point(1, 2), PieceColor.WHITE);
        Pawn pawn2 = new Pawn(new Point(1, 3), PieceColor.BLACK);
        board = Board.getConsumeBoard(List.of(pawn1, pawn2), new ArrayList<>());
        Positions positions = new Positions(new Point(2, 3), PieceColor.WHITE);
        positions.setDestination(new Point(1, 2));
        moves.add(new Move(positions, null));

        wasEnpassant = wasEnpassant(board, moves);

        assertTrue(wasEnpassant);
    }

    @Test
    void willDetectACastlingMove() {
        King king = new King(new Point(1, 0), PieceColor.BLACK);
        Board board = Board.getConsumeBoard(List.of(king), List.of(new Point(4, 0), new Point(3, 0), new Point(2, 0)));
        List<Move> moves = new ArrayList<>();
        Positions positions = new Positions(new Point(4, 0), PieceColor.BLACK);
        positions.setDestination(new Point(1, 0));
        moves.add(new Move(positions, king));
        boolean wasCastlingMove;

        wasCastlingMove = wasCastling(board, moves);

        assertTrue(wasCastlingMove);
    }

    @Test
    void willDetectAPawnPromotion() {
        boolean wasPromotionForPawnOne;
        boolean wasPromotionForPawnTwo;
        Pawn promotedPawn = new Pawn(new Point(2, 0), PieceColor.WHITE);
        Pawn misplacedPawn = new Pawn(new Point(1, 0), PieceColor.BLACK);
        Board board = Board.getConsumeBoard(List.of(promotedPawn, misplacedPawn), List.of(new Point(2,1), new Point(2, 6)));

        wasPromotionForPawnOne = wasPromotion(board, new Point(2, 0));
        wasPromotionForPawnTwo = wasPromotion(board, new Point(1, 0));

        assertTrue(wasPromotionForPawnOne);
        assertFalse(wasPromotionForPawnTwo);
    }

}