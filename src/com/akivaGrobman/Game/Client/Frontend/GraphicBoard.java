package com.akivaGrobman.Game.Client.Frontend;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.BoardBuilder;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceType;
import com.akivaGrobman.Game.Client.Backend.Players.Player;
import com.akivaGrobman.Game.Client.ChessGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GraphicBoard extends JFrame {

    private GraphicTile [][] board;
    private Color White = new Color(242, 218, 182);
    private Color Black = new Color(181, 137, 102);
    private Player player;
    static final int TILE_SIZE = 90;

    public GraphicBoard(Board board, Player player) {
        this.player = player;
        boardSetUp(board);
        showBoard();
    }

    public void showBoard() {
        for (int y = 0; y < ChessGame.SUM_OF_ROWS; y++) {
            for (int x = 0; x < ChessGame.SUM_OF_COLUMNS; x++) {
                board[y][x].repaint();
            }
        }
        this.setVisible(true);
    }

    public void updateTile(Point tilePosition, PieceType pieceType, PieceColor pieceColor) {
        board[tilePosition.y][tilePosition.x].update(pieceType, pieceColor);
        board[tilePosition.y][tilePosition.x].repaint();
    }

    private void boardSetUp(Board board) {
        int windowWidth = TILE_SIZE * ChessGame.SUM_OF_ROWS;;
        int windowHeight = TILE_SIZE * ChessGame.SUM_OF_ROWS;
        this.setLayout(new GridLayout(ChessGame.SUM_OF_ROWS, ChessGame.SUM_OF_COLUMNS));
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        this.setSize(windowWidth, windowHeight);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Akiva's Awesome Chess");
        setFrameIcon();
        this.board = new GraphicTile[ChessGame.SUM_OF_ROWS][ChessGame.SUM_OF_COLUMNS];
        addTiles(board);
    }

    private void addTiles(Board board) {
        initializeTiles(board);
        if(player.getPlayersColor() == PieceColor.WHITE) {
            // will show white pieces on the bottom
            for (int y = 0; y < ChessGame.SUM_OF_ROWS; y++) {
                for (int x = 0; x < ChessGame.SUM_OF_COLUMNS; x++) {
                    add(this.board[y][x]);
                }
            }
        } else {
            // will show black pieces on the bottom
            for (int y = ChessGame.SUM_OF_ROWS - 1; y >= 0; y--) {
                for (int x = ChessGame.SUM_OF_COLUMNS - 1; x >= 0; x--) {
                    add(this.board[y][x]);
                }
            }
        }
    }

    private void initializeTiles(Board board) {
        GraphicTile tile;
        Color color;
        for (int y = 0; y < ChessGame.SUM_OF_ROWS; y++) {
            for (int x = 0; x < ChessGame.SUM_OF_COLUMNS; x++) {
                if((x + y) % 2 == 0){
                    color = White;
                } else {
                    color = Black;
                }
                tile = new GraphicTile(new Point(x,y), color, this);
                this.board[y][x] = tile;
            }
        }
        BoardBuilder.updateGraphicsBoard(board, this.board);
    }

    private void setFrameIcon() {
        Image icon = null;
        try {
            icon = ImageIO.read(getClass().getResource("Images/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setIconImage(icon);
    }

    public void tileClicked(Point tilePosition) {
        player.addPositionToMove(tilePosition);
    }

}
