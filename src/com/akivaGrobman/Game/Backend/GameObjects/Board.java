package com.akivaGrobman.Game.Backend.GameObjects;

import com.akivaGrobman.Game.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.*;

import java.awt.*;
import java.util.List;
import java.util.NoSuchElementException;

public class Board {

    private final int STARTING_DEPTH = 1;
    protected Tile[][] board;
    private King whiteKing;
    private King blackKing;

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
            throw new NoSuchElementException();
        }
        return clone;
    }

    public void move(Point pieceOriginalPosition, Point destination) throws IllegalMoveException, NoPieceFoundException {
        if(pieceOriginalPosition.equals(destination)) throw new IllegalMoveException("can not move piece to original position");
        Piece piece = board[pieceOriginalPosition.y][pieceOriginalPosition.x].getPiece();
        Piece pieceAtDestination;
        try {
            pieceAtDestination = board[destination.y][destination.x].getPiece();
        } catch (NoPieceFoundException e) {
            pieceAtDestination = null;
        }
        piece.move(destination, this);
        board[piece.getPiecePosition().y][piece.getPiecePosition().x].setPiece(piece);
        board[pieceOriginalPosition.y][pieceOriginalPosition.x].setPiece(null);
        if(getKing(piece.getPieceColor()).isInCheck(this, STARTING_DEPTH)){
            piece.moveBack();
            board[pieceOriginalPosition.y][pieceOriginalPosition.x].setPiece(piece);
            board[destination.y][destination.x].setPiece(pieceAtDestination);
            if(piece instanceof Pawn) {
                Pawn pawn = (Pawn) piece;
                pawn.resetEnpassant();
                pawn.resetFirsLine();
            }
        }
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
        throw new NoSuchElementException("no king of color " + kingColor + " found");
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
