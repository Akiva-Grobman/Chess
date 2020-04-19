package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker.isInBounds;

public class Knight extends Piece implements PieceMoves{

    private List<Point> possibleDirections;

    public Knight(Point position, PieceColor color) {
        super(position, PieceType.KNIGHT, color);
        possibleDirections = getPossibleDirections();
    }

    @Override
    public Piece getClone() {
        return new Knight((Point) getPiecePosition().clone(), getPieceColor());
    }

    @Override
    public String getPieceInString() {
        return getPieceType().toString();
    }

    @Override
    protected boolean isLegalMove(Point destination) throws IllegalMoveException {
//        Point tempDestination = null;
//        for (Point direction: allPossibleDirections) {
//            tempDestination = new Point(getPiecePosition().x + direction.x, getPiecePosition().y + direction.y);
//            if(isInBounds(tempDestination)) {
//
//            }
//        }
//
        Point tempDestination;
        if(isLegalDistance(destination)) {
            for (Point direction: possibleDirections) {
                tempDestination = new Point(getPiecePosition());
                tempDestination.x += direction.x;
                tempDestination.y += direction.y;
                try {
                    if (isInBounds(tempDestination) && destination.equals(tempDestination)) {
                        return canMoveThere(tempDestination, getPieceColor());
                    }
                } catch (IllegalMoveException ime) {
                    if (!ime.getMessage().contains(String.format("x = %d y = %d out of bounds", tempDestination.x, tempDestination.y))) {
                        throw ime;
                    }
                }
            }
        }
        throw new IllegalMoveException(getClass().getSimpleName(), getPiecePosition(), destination);
    }

    private boolean isLegalDistance(Point destination) {
        return (Math.abs(destination.x - getPiecePosition().x) == 1 && Math.abs(destination.y - getPiecePosition().y) == 2) || (Math.abs(destination.x - getPiecePosition().x) == 2 && Math.abs(destination.y - getPiecePosition().y) == 1);
    }

    private List<Point> getPossibleDirections() {
        List<Point> possiblePositions = new ArrayList<>();
        int [] half1 = {1,-1};
        int [] half2 = {2,-2};
        for (int i = 0; i < half1.length; i++) {
            for (int j = 0; j < half2.length; j++) {
                possiblePositions.add(new Point(half1[i], half2[j]));
                possiblePositions.add(new Point(half2[j], half1[i]));
            }
        }
        return possiblePositions;
    }

    @Override
    public List<Point> getLegalMoves() {
        return null;
    }

}
