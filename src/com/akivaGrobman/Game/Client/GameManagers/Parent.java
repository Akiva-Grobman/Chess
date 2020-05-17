package com.akivaGrobman.Game.Client.GameManagers;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Move;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.*;
import com.akivaGrobman.Game.Client.Backend.Players.Positions;
import com.akivaGrobman.Game.Client.Backend.Players.Player;
import com.akivaGrobman.Game.Client.Frontend.GraphicBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Parent {

    public static final int SUM_OF_ROWS = 8;
    public static final int SUM_OF_COLUMNS = 8;
    protected Board backendBoard;
    protected GraphicBoard onScreenBoard;
    protected List<Move> moves;
    protected Player player;
    protected Player currentPlayer;
    protected PieceType promotedPieceType;
    protected boolean isPromoting;

    public abstract void move(Positions positions, Player player);

    public void tileClicked(Point tilePosition) {
        player.addPositionToMove(tilePosition);
        try {
            if(backendBoard.getPiece(tilePosition).getPieceColor() == currentPlayer.getPlayersColor()) {
                onScreenBoard.resetTilesColor();
                onScreenBoard.drawLegalTiles(backendBoard.getPiece(tilePosition).getLegalMoves(backendBoard, tilePosition));
            } else {
                onScreenBoard.resetTilesColor();
            }
        } catch (NoPieceFoundException e) {
            onScreenBoard.resetTilesColor();
        }
    }

    protected boolean enemyKingIsInCheck(Point movedPiecePosition) {
        try {
            return backendBoard.isLegalMove(movedPiecePosition, backendBoard.getKingPosition(backendBoard.getPiece(movedPiecePosition).getPieceColor()), 1);
        } catch (IllegalMoveException | NoPieceFoundException e) {
            return false;
        }
    }

    protected void putKingInCheck(Player currentPlayer) {
        King playersKing = backendBoard.getKing(currentPlayer.getPlayersColor());
        playersKing.setToIsInCheck();
    }

    protected void addMoveToMoveList(Positions positions) {
        Piece piece;
        try {
            piece = backendBoard.getPiece(positions.getDestination());
        } catch (NoPieceFoundException e) {
            piece = null;
        }
        moves.add(new Move(positions, piece));
    }

    protected void updateBoards(Positions positions) {
        Piece piece = getPiece(positions.getOrigin());
        assert piece != null; // if piece is null then the move shouldn't be legal
        // for castling
        if(piece instanceof King) {
            ((King) piece).moved();
        } else if(piece instanceof Rook) {
            ((Rook) piece).moved();
        }
        backendBoard.updateTile(positions.getOrigin(), null);
        backendBoard.updateTile(positions.getDestination(), piece);
        onScreenBoard.updateTile(positions.getDestination(), piece.getPieceType(), piece.getPieceColor());
        onScreenBoard.updateTile(positions.getOrigin(), null, null);
    }

    protected void updateEnpassantData(Positions positions) {
        try {
            Piece piece = backendBoard.getPiece(positions.getDestination());
            if(piece instanceof Pawn) {
                if(Math.abs(positions.getOrigin().y - positions.getDestination().y) == 2) {
                    backendBoard.setEnpassant(piece.getPieceColor(), positions.getDestination().x);
                }
            }
            backendBoard.resetOpponentsEnpassant(piece.getPieceColor());
        } catch (NoPieceFoundException e) {
            e.printStackTrace();
        }
    }

    protected boolean isLegalMove(Positions positions) {
        try {
            if(player.getPlayersColor() != backendBoard.getPiece(positions.getOrigin()).getPieceColor()) return false;
            int STARTING_DEPTH = 1;
            return backendBoard.isLegalMove(positions.getOrigin(), positions.getDestination(), STARTING_DEPTH);
        } catch (IllegalMoveException | NoPieceFoundException e) {
            String msg = e.getMessage();
            return false;
        }
    }

    public Piece getPiece(Point position) {
        try {
            return backendBoard.getPiece(position);
        } catch (NoPieceFoundException e) {
            return null;
        }
    }

    public PieceColor getPlayersColor() {
        return player.getPlayersColor();
    }

    public void promotionClick(PieceType pieceType) {
        promotedPieceType = pieceType;
    }

}

