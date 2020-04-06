package com.akivaGrobman.Game.Backend.GameObjects;

import com.akivaGrobman.Game.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.King;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.Piece;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.PieceType;

import java.awt.*;
import java.util.List;

public class Board {

    // hey
    protected Tile[][] board;
    private King whiteKing;
    private King blackKing;

    public Board() {
        board = BoardBuilder.newBoard();
        try {
            whiteKing = (King) board[0][3].getPiece();
            blackKing = (King) board[7][3].getPiece();
        } catch (NoPieceFoundException e) {
            e.printStackTrace();
        }
    }

    private Board(Tile[][] costumeBoard) {
        this.board = costumeBoard;
        try {
            Point whiteKingPosition = getKingPosition(PieceColor.WHITE, board);
            Point blackKingPosition = getKingPosition(PieceColor.BLACK, board);
            whiteKing = (King) getPiece(whiteKingPosition);
            blackKing = (King) getPiece(blackKingPosition);
        } catch (NoPieceFoundException e) {
            e.printStackTrace();
        }
    }

    public static Board getConsumeBoard(List<Piece> piecesChanged, List<Point> emptyTiles) {
        Board board = new Board(BoardBuilder.costumeBoard(piecesChanged, emptyTiles));
        try {
            Point whiteKingPosition = getKingPosition(PieceColor.WHITE, board.board);
            Point blackKingPosition = getKingPosition(PieceColor.BLACK, board.board);
            board.whiteKing = (King) board.getPiece(whiteKingPosition);
            board.blackKing = (King) board.getPiece(blackKingPosition);
        } catch (NoPieceFoundException e) {
            e.printStackTrace();
        }
        return board;
    }

    public static Board getClone(Board board) {
        Board clone = new Board(BoardBuilder.clone(board));
        try {
            Point whiteKingPosition = getKingPosition(PieceColor.WHITE, clone.board);
            Point blackKingPosition = getKingPosition(PieceColor.BLACK, clone.board);
            clone.whiteKing = (King) clone.getPiece(whiteKingPosition);
            clone.blackKing = (King) clone.getPiece(blackKingPosition);
        } catch (NoPieceFoundException e) {
            e.printStackTrace();
        }
        return clone;
    }

    public void move(Point pieceOriginalPosition, Point destination) throws IllegalMoveException, NoPieceFoundException {
        if(pieceOriginalPosition.equals(destination)) throw new IllegalMoveException("can not move piece to original position");
        Piece piece = board[pieceOriginalPosition.y][pieceOriginalPosition.x].getPiece();
        piece.move(destination, this);
        board[piece.getPiecePosition().y][piece.getPiecePosition().x].setPiece(piece);
        board[pieceOriginalPosition.y][pieceOriginalPosition.x].setPiece(null);
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
        throw new Error("no king of color " + kingColor + " found");
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
