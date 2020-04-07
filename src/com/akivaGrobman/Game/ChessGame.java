package com.akivaGrobman.Game;

import com.akivaGrobman.Game.Backend.GameObjects.Board;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Frontend.GraphicBoard;

public class ChessGame {

    public static final int SUM_OF_ROWS = 8;
    public static final int SUM_OF_COLUMNS = 8;

    private PieceColor currentPlayersColor;
    private Board board;

    public ChessGame() {
        currentPlayersColor = PieceColor.WHITE;
        board = new Board();
        new GraphicBoard(board);
    }

}
