package com.akivaGrobman.Game.Client.Backend.GameObjects.Board;

import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.*;
import com.akivaGrobman.Game.Client.ChessGame;
import com.akivaGrobman.Game.Client.Frontend.GraphicTile;

import java.awt.*;
import java.util.List;

public class BoardBuilder {

    private static Tile[][] board;

    public static Tile[][] newBoard() {
        initializeNewBoard();
        return board;
    }

    public static Tile[][] costumeBoard(List<Piece> newPiecePositions, List<Point> emptyTiles) {
        setBoard();
        initializeCostumeBoard(emptyTiles, newPiecePositions);
        return board;
    }

    public static Tile[][] clone(Board oldBoard) {
        board = oldBoard.board;
        board = getClone(board);
        return board;
    }

    private static Tile[][] getClone(Tile[][] board) {
        Tile[][] tempBoard = new Tile[ChessGame.SUM_OF_ROWS][ChessGame.SUM_OF_COLUMNS];
        for (int y = 0; y < ChessGame.SUM_OF_ROWS; y++) {
            for (int x = 0; x < ChessGame.SUM_OF_COLUMNS; x++) {
                tempBoard[y][x] = board[y][x].getClone();
            }
        }
        return tempBoard;
    }

    private static void initializeNewBoard() {
        Tile tile;
        Point position;
        setBoard();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                position = new Point(x, y);
                tile = new Tile(position);
                if(isAddingAPieceToNewBoard(y)) {
                    tile.setPiece(getPieceForNewBoard(position, getPieceColorForNewBoard(y)));
                }
                board[y][x] = tile;
            }
        }
    }

    private static void initializeCostumeBoard(List<Point> emptyTiles, List<Piece> newPiecePositions) {
        initializeNewBoard();
        removeEmptyTiles(emptyTiles);
        addNewPiece(newPiecePositions);
    }

    private static void removeEmptyTiles(List<Point> emptyTiles) {
        for (Tile[] tiles: board) {
            for(Tile tile: tiles) {
                for (Point emptyTile : emptyTiles) {
                    if (tile.getPosition().equals(emptyTile)) {
                        tile.setPiece(null);
                    }
                }
            }
        }
    }

    private static void addNewPiece(List<Piece> pieces) {
        for (Piece piece: pieces) {
            board[piece.getPiecePosition().y][piece.getPiecePosition().x].setPiece(piece);
        }
    }

    private static void setBoard() {
        board = new Tile[ChessGame.SUM_OF_ROWS][ChessGame.SUM_OF_COLUMNS];
    }

    private static boolean isAddingAPieceToNewBoard(int row) {
        return row != 2 && row != 3 && row != 4 && row != 5;
    }

    private static PieceColor getPieceColorForNewBoard(int row) {
        return (row == 0 || row == 1) ? PieceColor.BLACK : PieceColor.WHITE;
    }

    private static Piece getPieceForNewBoard(Point position, PieceColor color) {
        if(position.y == 1 || position.y == 6) {
            return new Pawn(position, color);
        } else {
            switch (position.x) {
                case 0:
                case 7:
                    return new Rook(position, color);
                case 1:
                case 6:
                    return new Knight(position, color);
                case 2:
                case 5:
                    return new Bishop(position, color);
                case 3:
                    return new Queen(position, color);
                case 4:
                    return new King(position, color);
                default:
                    throw new Error("no piece matches row " + position.y + " column " + position.x);
            }
        }
    }

    public static void updateGraphicsBoard(Board board, GraphicTile[][] graphicTiles) {
        for (int y = 0; y < ChessGame.SUM_OF_ROWS; y++) {
            for (int x = 0; x < ChessGame.SUM_OF_COLUMNS; x++) {
                try {
                    Piece piece = board.getPiece(new Point(x,y));
                    graphicTiles[y][x].update(piece.getPieceType(), piece.getPieceColor());
                } catch (NoPieceFoundException ignore) {}
            }
        }
    }
}
