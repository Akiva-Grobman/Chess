package com.akivaGrobman.Game.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Backend.GameObjects.Board;
import java.awt.*;
import java.util.List;
import static com.akivaGrobman.Game.Backend.GameRules.BoardConditionsChecker.*;

public class Pawn extends Piece implements PieceMoves {

    private enum Direction {
        UP,
        DOWN
    }

    private Direction direction;
    private boolean isInEnpassantPosition;
    private boolean isOnStartingLine;
    private boolean previousEnpassantStatus;
    private boolean previousStatingLineState;

    public Pawn(Point position, PieceColor color) {
        super(position, PieceType.PAWN, color);
        board = null;
        setDirection();
        isInEnpassantPosition = false;
        isOnStartingLine = true;
    }

    @Override
    public void move(Point destinationsPosition, Board board) throws IllegalMoveException {
        this.board = board;
        if (isLegalMove(destinationsPosition)) {
            super.move(destinationsPosition, board);
            previousStatingLineState = isOnStartingLine;
            isOnStartingLine = false;
            if(previousEnpassantStatus) {
                isInEnpassantPosition = false;
            }
            previousEnpassantStatus = isInEnpassantPosition;
        } else {
            throw new IllegalMoveException(getClass().getSimpleName(), getPiecePosition(), destinationsPosition);
        }
    }

    @Override
    public Piece getClone() {
        Pawn pawn = new Pawn((Point) getPiecePosition().clone(), getPieceColor());
        pawn.direction = direction;
        pawn.isInEnpassantPosition = isInEnpassantPosition;
        pawn.isOnStartingLine = isOnStartingLine;
        return pawn;
    }

    @Override
    public String getPieceInString() {
        return " " + getPieceType() + " ";
    }

    @Override
    public List<Point> getLegalMoves() {
        return null;
    }

    public void resetEnpassant() {
        isInEnpassantPosition = previousEnpassantStatus;
    }

    private void setDirection() {
        if (getPieceColor() == PieceColor.BLACK) {
            direction = Direction.UP;
        } else {
            direction = Direction.DOWN;
        }
    }

    private boolean isLegalMove(Point destination) throws IllegalMoveException {
        int direction = getDirection();
        Point tempDestination = new Point(getPiecePosition());
        tempDestination.y += direction;
        // the tile in front
        if (tempDestination.equals(destination)) {
            return isInBounds(tempDestination) && isVacantPosition(tempDestination, board);
        }
        // then one to the left
        tempDestination.x -= 1;
        if (tempDestination.equals(destination)) {
            return isInBounds(tempDestination) && hasEnemyPiece(getPieceColor(), tempDestination, board);
        }
        // then the one on the right
        tempDestination.x += 2;
        if (tempDestination.equals(destination)) {
            return isInBounds(tempDestination) && hasEnemyPiece(getPieceColor(), tempDestination, board);
        }
        if (isOnStartingLine) {
            int oldY = tempDestination.y;
            tempDestination.y += direction;
            tempDestination.x = getPiecePosition().x;
            // then the tile two to the front
            if (tempDestination.equals(destination)) {
                boolean isLegal = isInBounds(tempDestination) && isVacantPosition(tempDestination, board) && isVacantPosition(new Point(tempDestination.x, oldY), board);
                isInEnpassantPosition = isLegal;
                return isLegal;
            }
        }
        tempDestination = new Point(getPiecePosition());
        if(Math.abs(tempDestination.x - destination.x) == 1 && tempDestination.y + direction == destination.y) {
            try {
                tempDestination.x += 1;
                if (isEnpassant(tempDestination)) {
                    return true;
                }
                tempDestination.x -= 2;
                if (isEnpassant(tempDestination)) {
                    return true;
                }
            } catch (NoPieceFoundException e) {
                return false;
            }
        }
        return false;
    }

    private boolean isEnpassant(Point tempDestination) throws IllegalMoveException, NoPieceFoundException {
        if (isInBounds(tempDestination)) {
            if (board.getPiece(tempDestination) instanceof Pawn) {
                Pawn pawn = ((Pawn) board.getPiece(tempDestination));
                return pawn.isInEnpassantPosition;
            }
        }
        return false;
    }

    private int getDirection() {
        if (this.direction == Direction.UP) {
            return 1;
        } else {
            return -1;
        }
    }

    public void resetFirsLine() {
        isOnStartingLine = previousStatingLineState;
    }

}