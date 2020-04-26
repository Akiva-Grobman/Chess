package com.akivaGrobman.Game.Client.Backend.GameObjects.Board;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.*;

import java.awt.*;
import java.util.List;
import java.util.NoSuchElementException;

public class Board {

    protected Tile[][] board;
    private King whiteKing;
    private King blackKing;

    // todo split board into two classes (one board and one chessBoard)
    public Board() {
        board = BoardBuilder.newBoard();
        try {
            whiteKing = (King) board[7][4].getPiece();
            blackKing = (King) board[0][4].getPiece();
        } catch (NoPieceFoundException e) {
            e.printStackTrace();
        }
    }

    private Board(Tile[][] costumeBoard) {
        this.board = costumeBoard;
        setKings(Board.this);
    }

    public static Board getConsumeBoard(List<Piece> piecesChanged, List<Point> emptyTiles) {
        Board board = new Board(BoardBuilder.costumeBoard(piecesChanged, emptyTiles));
        setKings(board);
        return board;
    }

    public static Board getClone(Board board) {
        Board clone = new Board(BoardBuilder.clone(board));
        setKings(clone);
        return clone;
    }

    private static void setKings(Board board) {
        try {
            Point whiteKingPosition = getKingPosition(PieceColor.WHITE, board.board);
            board.whiteKing = (King) board.getPiece(whiteKingPosition);
        } catch (NoSuchElementException | NoPieceFoundException e) {
            board.whiteKing = null;
        }
        try {
            Point blackKingPosition = getKingPosition(PieceColor.BLACK, board.board);
            board.blackKing = (King) board.getPiece(blackKingPosition);
        } catch (NoSuchElementException | NoPieceFoundException e) {
            board.blackKing = null;
        }
    }

    public boolean isLegalMove(Point origin, Point destination, int depth) throws IllegalMoveException, NoPieceFoundException {
        if(origin.equals(destination)) throw new IllegalMoveException("can not move piece to original position");
        Piece piece = board[origin.y][origin.x].getPiece();
        piece.move(destination, this);
        // todo handle enpassant
        boolean isInCheck = isInCheck(piece, origin, depth);
        return !isInCheck;
    }

    private boolean isInCheck(Piece piece, Point origin, int depth) {
        boolean isInCheck;
        Point destination = piece.getPiecePosition();
        Piece oldPiece;
        try {
            oldPiece = board[destination.y][destination.x].getPiece();
        } catch (NoPieceFoundException e) {
            oldPiece = null;
        }
        board[destination.y][destination.x].setPiece(piece);
        board[origin.y][origin.x].setPiece(null);
        isInCheck = getKing(piece.getPieceColor()).isInCheck(this, depth);
        piece.reversMove();
        board[origin.y][origin.x].setPiece(piece);
        board[destination.y][destination.x].setPiece(oldPiece);
        return isInCheck;
    }

    public boolean hasPieceInThisPosition(Point position) {
        try {
            board[position.y][position.x].getPiece();
            return true;
        } catch (NoPieceFoundException ignore) {
            return false;
        }

    }

    public Piece getPiece(Point position) throws NoPieceFoundException {
        return board[position.y][position.x].getPiece();
    }

    public King getKing(PieceColor pieceColor) {
        return (pieceColor == PieceColor.BLACK)? blackKing: whiteKing;
    }

    public void updateTile(Point position, Piece piece) {
        board[position.y][position.x].setPiece(piece);
    }

    private static Point getKingPosition(PieceColor kingColor, Tile[][] board) {
        for (Tile[] row: board) {
            for (Tile column: row) {
                if(column.hasPiece()) {
                    try {
                        Piece piece = column.getPiece();
                        if(piece.getPieceType() == PieceType.KING) {
                            if(piece.getPieceColor() == kingColor) {
                                return piece.getPiecePosition();
                            }
                        }
                    } catch (NoPieceFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new NoSuchElementException("no king of color " + kingColor + " found");
    }

    public Tile[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        StringBuilder bo = new StringBuilder();
        String line = "\n----------------------------------------------------------------------------\n";
        bo.append(line);
        for (Tile[] tiles: board) {
            bo.append("|");
            for (Tile tile: tiles) {
                bo.append(tile.toString());
            }
            bo.append(line);
        }
        return bo.toString();
    }

}
