package com.akivaGrobman.Game.Client.Backend.GameObjects;

import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Piece;

import java.awt.*;

class Tile {

    private Piece piece;
    private Point tilePosition;

    Tile(Point tilePosition) {
        this.tilePosition = tilePosition;
        piece = null;
    }

    Piece getPiece() throws NoPieceFoundException {
        if(!hasPiece()) {
            throw new NoPieceFoundException(tilePosition);
        }
        return piece;
    }

    void setPiece(Piece piece) {
        this.piece = piece;
    }

    boolean hasPiece() {
        return piece != null;
    }

    public Point getPosition() {
        return tilePosition;
    }

    Tile getClone() {
        Tile tile = new Tile((Point) tilePosition.clone());
        if(hasPiece()) {
            tile.setPiece(piece.getClone());
        }
        return tile;
    }

    @Override
    public String toString() {
        return (hasPiece()) ? "| " + piece.getPieceInString() + " |" : "|        |";
    }

}
