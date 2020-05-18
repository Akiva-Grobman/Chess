package com.akivaGrobman.Game.Client.Backend.GameRules;

import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Move;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.King;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Pawn;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Piece;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.Players.Positions;

import java.awt.*;
import java.util.List;

public abstract class SpecialMoves {

    public static boolean wasEnpassant(Board backendBoard, Point origin, Point destination, Piece pieceAtDestination)  {
        if(pieceAtDestination == null) {
            try {
                if(backendBoard.getPiece(destination) instanceof Pawn && backendBoard.getPiece(new Point(destination.x, origin.y)) instanceof Pawn) {
                    return backendBoard.getPiece(new Point(destination.x, origin.y)).getPieceColor() != backendBoard.getPiece(destination).getPieceColor();
                }
            } catch (NoPieceFoundException | NullPointerException e) {
                return false;
            }
        }

        return false;
    }

    public static boolean wasCastling(Board backendBoard, Positions move) {
        Point destination = move.getDestination();
        Point origin = move.getOrigin();
        try {
            if(backendBoard.getPiece(destination) instanceof King) {
                if(isRightColor(backendBoard.getPiece(destination).getPieceColor(), destination.y)) {
                    return Math.abs(origin.x - destination.x) > 1;
                }
            }
        } catch (NoPieceFoundException e) {
            return false;
        }
        return false;
    }

    private static boolean isRightColor(PieceColor pieceColor, int y) {
        if(y == 0) {
            return pieceColor == PieceColor.BLACK;
        } else if (y == 7) {
            return pieceColor == PieceColor.WHITE;
        } else {
            return false;
        }
    }

    public static boolean wasPromotion(Board backendBoard, Point destination) {
        if(destination.y == 0 || destination.y == 7) {
            try {
                if (backendBoard.getPiece(destination) instanceof Pawn) {
                    int endLine;
                    if(backendBoard.getPiece(destination).getPieceColor() == PieceColor.BLACK) {
                        endLine = 7;
                    } else {
                        endLine = 0;
                    }
                    return destination.y == endLine;
                }
            } catch (NoPieceFoundException e) {
                return false;
            }
        }
        return false;
    }

}
