package com.akivaGrobman.Game.Client.Backend.GameObjects.Board;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.King;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Piece;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceType;
import java.awt.*;
import java.util.NoSuchElementException;

public abstract class ChessBoard {

    protected Tile[][] board;
    protected King whiteKing;
    protected King blackKing;
    private Board backendBoard;

    public boolean isLegalMove(Point origin, Point destination, int depth) throws IllegalMoveException, NoPieceFoundException {
        if(origin.equals(destination)) throw new IllegalMoveException("can not move piece to original position");
        Piece piece = board[origin.y][origin.x].getPiece();
        if(!piece.isLegalMove(destination, backendBoard)) {
            throw new IllegalMoveException(getClass().getSimpleName(), origin, destination);
        }
        boolean isInCheck = isInCheck(piece, destination, depth);
        return !isInCheck;
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

    King getKing(PieceColor pieceColor) {
        return (pieceColor == PieceColor.BLACK)? blackKing: whiteKing;
    }

    protected static void setKings(Board board) {
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

    protected boolean isInCheck(Piece piece, Point destination, int depth) {
        boolean isInCheck;
        Point origin = piece.getPiecePosition();
        Piece oldPiece;
        try {
            oldPiece = board[destination.y][destination.x].getPiece();
        } catch (NoPieceFoundException e) {
            oldPiece = null;
        }
        board[destination.y][destination.x].setPiece(piece);
        board[origin.y][origin.x].setPiece(null);
        isInCheck = getKing(piece.getPieceColor()).isInCheck(backendBoard, depth);
        piece.reversMove();
        board[origin.y][origin.x].setPiece(piece);
        board[destination.y][destination.x].setPiece(oldPiece);
        return isInCheck;
    }

    protected static Point getKingPosition(PieceColor kingColor, Tile[][] board) {
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

    protected void setContext(Board backendBoard) {
        this.backendBoard = backendBoard;
    }

}
