package com.akivaGrobman.Game.Client.Backend.GameObjects;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.King;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Pawn;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardBuilderTest {

    @Test
    void initializationOfNewBoardReturnsCorrectBoard() throws Exception{
        Tile[][] board;           //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 /
        String expectedBoard =  "[[|  ROOK  |, | KNIGHT |, | BISHOP |, |  QUEEN |, |  KING  |, | BISHOP |, | KNIGHT |, |  ROOK  |], [|  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |], [|  ROOK  |, | KNIGHT |, | BISHOP |, |  QUEEN |, |  KING  |, | BISHOP |, | KNIGHT |, |  ROOK  |]]";

        board = BoardBuilder.newBoard();

        assertEquals(expectedBoard, Arrays.deepToString(board));
    }

    @Test
    void initializationOfCostumeBoardReturnsCorrectBoard() throws Exception {
        Tile[][] board;
        String expectedBoard = "[[|  ROOK  |, | KNIGHT |, | BISHOP |, |  QUEEN |, |  KING  |, |        |, | KNIGHT |, |  ROOK  |], [|  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|        |, |        |, |        |, |  KING  |, |        |, |        |, |        |, |        |], [|        |, |        |, |        |, |        |, |  PAWN  |, |        |, |        |, |        |], [|        |, |        |, |        |, |        |, |        |, |        |, |        |, |        |], [|  PAWN  |, |  PAWN  |, |        |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |, |  PAWN  |], [|  ROOK  |, | KNIGHT |, | BISHOP |, |  QUEEN |, |  KING  |, | BISHOP |, | KNIGHT |, |  ROOK  |]]";
        Point erase1 = new Point(5, 0);
        Point erase2 = new Point(2, 6);
        Pawn piece1 = new Pawn(new Point(4,4), PieceColor.BLACK);
        King piece2 = new King(new Point(3,3), PieceColor.WHITE);

        board = BoardBuilder.costumeBoard(Arrays.asList(piece1, piece2), Arrays.asList(erase1, erase2));

        assertEquals(expectedBoard, Arrays.deepToString(board));
    }
}