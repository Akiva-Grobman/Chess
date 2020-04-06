package com.akivaGrobman.Game.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Backend.GameObjects.Board;
import java.awt.*;
import java.util.Objects;

public abstract class Piece extends PieceMovingMethods implements PieceMoves{

    private Point position;
    private PieceType type;
    private PieceColor color;

    public Piece(Point position, PieceType type, PieceColor color) {
        this.position = position;
        this.type = type;
        this.color = color;
    }

    @Override
    public void move(Point destinationsPosition, Board board) throws IllegalMoveException {
        setPiecePosition(destinationsPosition);
    }

    public Point getPiecePosition() {
        return position;
    }

    private void setPiecePosition(Point position) {
        this.position = position;
    }

    public PieceType getPieceType() {
        return type;
    }

    public PieceColor getPieceColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return position.equals(piece.position) &&
                type == piece.type &&
                color == piece.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, type, color);
    }

    @Override
    public String toString() {
        return "Piece{" +
                "position=" + position +
                ", type=" + type +
                ", color=" + color +
                '}';
    }

    public abstract Piece getClone();

    public abstract String getPieceInString();
}
