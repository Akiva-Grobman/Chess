package com.akivaGrobman.Game.Client.Backend.GameObjects.Board;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.King;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Pawn;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardBuilderTest {

    @Test
    void initializationOfNewBoardReturnsCorrectBoard() {
        Tile[][] board;           //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 /
        String expectedBoard =  "[[|  ROOK  |, | KNIGHT |, | BISHOP |, |  QUEEN |, |  KING  |, | BISHOP |, | KNIGHT |, |  ROOK  |], [|  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |], [|  ROOK  |, | KNIGHT |, | BISHOP |, |  QUEEN |, |  KING  |, | BISHOP |, | KNIGHT |, |  ROOK  |]]";

        board = BoardBuilder.newBoard();

        assertEquals(expectedBoard, Arrays.deepToString(board));
    }

    @Test
    void initializationOfCostumeBoardReturnsCorrectBoard() {
        Tile[][] board;
        String expectedBoard = "[[|  ROOK  |, | KNIGHT |, | BISHOP |, |  QUEEN |, |  KING  |, |        |, | KNIGHT |, |  ROOK  |], [|  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|        |, |        |, |        |, |  KING  |, |        |, |        |, |        |, |        |], [|        |, |        |, |        |, |        |, |  PAWN  |, |        |, |        |, |        |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|  PAWN  |, |  PAWN  |, |        |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |], [|  ROOK  |, | KNIGHT |, | BISHOP |, |  QUEEN |, |  KING  |, | BISHOP |, | KNIGHT |, |  ROOK  |]]";
        Point erase1 = new Point(5, 0);
        Point erase2 = new Point(2, 6);
        Pawn piece1 = new Pawn(PieceColor.BLACK);
        King piece2 = new King(3, PieceColor.WHITE);

        board = BoardBuilder.costumeBoard(List.of(piece1, piece2), List.of(new Point(4, 4), new Point(3, 3)), List.of(erase1, erase2));

        assertEquals(expectedBoard, Arrays.deepToString(board));
    }
}