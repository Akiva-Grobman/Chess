package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;

import java.awt.*;
import java.util.List;

import static com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker.*;

public class Rook extends Piece implements PieceMoves{

    public Rook(Point position, PieceColor color) {
        super(position, PieceType.ROOK, color);
    }

    @Override
    public Piece getClone() {
        return new Rook((Point) getPiecePosition().clone(), getPieceColor());
    }

    @Override
    public String getPieceInString() {
        return " " + getPieceType() + " ";
    }

    @Override
    public boolean isLegalMove(Point destinationsPosition, Board board) throws IllegalMoveException {
        if(destinationsPosition.x != getPiecePosition().x && destinationsPosition.y != getPiecePosition().y) throw new IllegalMoveException(getClass().getSimpleName(), getPiecePosition(), destinationsPosition);
        this.board = board;
        Point tempDestination = new Point(getPiecePosition());
        int direction;
        if(destinationsPosition.x == getPiecePosition().x) {
            direction = getDirection(getPiecePosition().y, destinationsPosition.y);
            while (tempDestination.y != destinationsPosition.y) {
                tempDestination.y += direction;
                if(!isInBounds(tempDestination)) {
                    break;
                }
                if(hasPieceInPathToDestination(destinationsPosition, tempDestination)) {
                    break;
                }
                if(destinationsPosition.equals(tempDestination) && canMoveThere(tempDestination, getPieceColor())){
                    return true;
                }
            }
        }
        if(destinationsPosition.y == getPiecePosition().y) {
            direction = getDirection(getPiecePosition().x, destinationsPosition.x);
            while (tempDestination.x != destinationsPosition.x) {
                tempDestination.x += direction;
                try {
                    if(!isInBounds(tempDestination)) {
                        break;
                    }
                } catch (IllegalMoveException ime) {
                    if(isOutOfBounds(tempDestination, ime)) break;
                    throw ime;
                }
                if(hasPieceInPathToDestination(destinationsPosition, tempDestination)) {
                    break;
                }
                if(destinationsPosition.equals(tempDestination) && (canMoveThere(tempDestination, getPieceColor()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOutOfBounds(Point tempDestination, IllegalMoveException ime) {
        return ime.getMessage().contains(String.format("x = %d y = %d out of bounds", tempDestination.x, tempDestination.y));
    }

    private int getDirection(int origin, int destination) {
        return (Math.min(origin, destination) ==  origin)? 1: -1;
    }

    @Override
    public List<Point> getLegalMoves() {
        return null;
    }
}
