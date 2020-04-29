package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker.isInBounds;

public class Knight extends Piece implements PieceMoves{

    private final List<Point> possibleDirections;

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
    public boolean isLegalMove(Point destination, Board board) throws IllegalMoveException {
        this.board = board;
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
        return false;
    }

    private boolean isLegalDistance(Point destination) {
        return (Math.abs(destination.x - getPiecePosition().x) == 1 && Math.abs(destination.y - getPiecePosition().y) == 2) || (Math.abs(destination.x - getPiecePosition().x) == 2 && Math.abs(destination.y - getPiecePosition().y) == 1);
    }

    private List<Point> getPossibleDirections() {
        List<Point> possiblePositions = new ArrayList<>();
        int [] half1 = {1,-1};
        int [] half2 = {2,-2};
        for (int i : half1) {
            for (int j : half2) {
                possiblePositions.add(new Point(j, i));
                possiblePositions.add(new Point(i, j));
            }
        }
        return possiblePositions;
    }

    @Override
    public List<Point> getLegalMoves() {
        return null;
    }

}
