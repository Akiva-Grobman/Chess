package com.akivaGrobman.Game.Client.Frontend;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceType;
import com.akivaGrobman.Game.Client.GameManagers.Parent;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

import static com.akivaGrobman.Game.Client.Frontend.BoardBuilder.*;

public class GraphicBoard {

    private JFrame frame;
    private GraphicTile[][] board;
    private final Parent game;
    static final int TILE_SIZE = 90;

    public GraphicBoard(Board board, Parent game) {
        this.game = game;
        boardSetUp(board);
        frame.setVisible(true);
    }

    public void updateTile(Point tilePosition, PieceType pieceType, PieceColor pieceColor) {
        board[tilePosition.y][tilePosition.x].update(pieceType, pieceColor);
        board[tilePosition.y][tilePosition.x].repaint();
    }

    public void drawLegalTiles(List<Point> legalMoves) {
        for (Point position : legalMoves) {
            board[position.y][position.x].drawAsLegalTile();
        }
    }

    public void resetTilesColor() {
        for (GraphicTile[] row: board) {
            for (GraphicTile column: row) {
                column.resetColor();
            }
        }
    }

    private void boardSetUp(Board board) {
        URL iconUrl = getClass().getResource("Images/icon.png");
        frame = getChessBoardFrame(iconUrl);
        this.board = getStartingBoard(board, game);
        addTilesToFrame(game.getPlayersColor(), frame, this.board);
    }

    public JFrame getFrame() {
        return frame;
    }

    @Override
    public String toString() {
        StringBuilder bo = new StringBuilder();
        String line = "\n----------------------------------------------------------------------------\n";
        bo.append(line);
        for (GraphicTile[] tiles : board) {
            bo.append("|");
            for (GraphicTile tile : tiles) {
                bo.append(tile.toString());
            }
            bo.append(line);
        }
        return bo.toString();
    }

}
