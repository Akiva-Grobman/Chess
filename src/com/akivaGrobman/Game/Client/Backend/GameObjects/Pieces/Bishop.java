package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;

import java.awt.*;
import java.util.List;

import static com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker.*;

public class Bishop extends Piece implements PieceMoves {

    public Bishop(Point position, PieceColor color) {
        super(position, PieceType.BISHOP, color);
    }

    @Override
    public Piece getClone() {
        return new Bishop((Point) getPiecePosition().clone(), getPieceColor());
    }

    @Override
    public String getPieceInString() {
        return getPieceType().toString();
    }

    @Override
    public boolean isLegalMove(Point destinationsPosition, Board board) throws IllegalMoveException {
        if(movingInStraightLine(destinationsPosition)) throw new IllegalMoveException(getClass().getSimpleName(), getPiecePosition(), destinationsPosition);
        this.board = board;
        Point tempDestination = new Point(getPiecePosition());
        Point direction = getDirection(destinationsPosition);
        while (!tempDestination.equals(destinationsPosition)) {
            tempDestination.x += direction.x;
            tempDestination.y += direction.y;
            if(!isInBounds(tempDestination)) {
                return false;
            }
            if(hasPieceInPathToDestination(destinationsPosition, tempDestination)) {
                return false;
            }
            if(destinationsPosition.equals(tempDestination) && canMoveThere(tempDestination, getPieceColor())) {
                return true;
            }
        }
        return false;
    }

    private boolean movingInStraightLine(Point destination) {
        return getPiecePosition().x == destination.x || getPiecePosition().y == destination.y;
    }

    private Point getDirection(Point destination) {
        Point direction = new Point();
        direction.x = (Math.min(getPiecePosition().x, destination.x) == getPiecePosition().x)? 1 : -1;
        direction.y = (Math.min(getPiecePosition().y, destination.y) == getPiecePosition().y)? 1 : -1;
        return direction;
    }

    @Override
    public List<Point> getLegalMoves() {
        return null;
    }
}
