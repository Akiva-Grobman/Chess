package com.akivaGrobman.Game.Client.Frontend;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceType;
import com.akivaGrobman.Game.Client.ChessGame;

import javax.swing.*;
import java.awt.*;

import static com.akivaGrobman.Game.Client.Frontend.BoardBuilder.*;

public class GraphicBoard {

    private JFrame frame;
    private GraphicTile [][] board;
    private final ChessGame game;
    static final int TILE_SIZE = 90;

    public GraphicBoard(Board board, ChessGame game) {
        this.game = game;
        boardSetUp(board);
        frame.setVisible(true);
    }

    public void updateTile(Point tilePosition, PieceType pieceType, PieceColor pieceColor) {
        board[tilePosition.y][tilePosition.x].update(pieceType, pieceColor);
        board[tilePosition.y][tilePosition.x].repaint();
    }

    private void boardSetUp(Board board) {
        frame = getChessBoardFrame(getClass().getResource("Images/icon.png"));
        this.board = getStartingBoard(board, game);
        addTilesToFrame(game.getPlayersColor(), frame, this.board);
    }

    @Override
    public String toString() {
        StringBuilder bo = new StringBuilder();
        String line = "\n----------------------------------------------------------------------------\n";
        bo.append(line);
        for (GraphicTile[] tiles: board) {
            bo.append("|");
            for (GraphicTile tile: tiles) {
                bo.append(tile.toString());
            }
            bo.append(line);
        }
        return bo.toString();
    }
}
