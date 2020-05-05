package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;

import java.awt.*;
import java.util.List;

import static com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker.*;

public class Queen extends Piece implements PieceMoves {

    public Queen(Point position, PieceColor color) {
        super(position, PieceType.QUEEN, color);
    }

    @Override
    public Piece getClone() {
        return new Queen((Point) getPiecePosition().clone(), getPieceColor());
    }

    @Override
    public String getPieceInString() {
        return " " + getPieceType();
    }

    @Override
    public boolean isLegalMove(Point destination, Board board) throws IllegalMoveException {
        this.board = board;
        Point tempDestination = new Point(getPiecePosition());
        Point direction = getDirection(destination);
        while (!tempDestination.equals(destination)) {
            tempDestination.x += direction.x;
            tempDestination.y += direction.y;
            if(!isInBounds(tempDestination)) {
                break;
            }
            if(hasPieceInPathToDestination(destination, tempDestination)) {
                break;
            }
            if(destination.equals(tempDestination) && canMoveThere(tempDestination, getPieceColor())) {
                return true;
            }
        }
        return false;
    }

    private Point getDirection(Point destination) {
        Point direction = new Point();
        if(getPiecePosition().x == destination.x) {
            direction.x = 0;
        } else {
            direction.x = (Math.min(getPiecePosition().x, destination.x) == getPiecePosition().x)? 1 : -1;
        }
        if (getPiecePosition().y == destination.y) {
            direction.y = 0;
        } else {
            direction.y = (Math.min(getPiecePosition().y, destination.y) == getPiecePosition().y)? 1 : -1;
        }
        return direction;
    }

    @Override
    public List<Point> getLegalMoves(Board board) {
        return null;
    }
}
