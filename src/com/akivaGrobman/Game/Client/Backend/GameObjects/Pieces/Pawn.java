package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import java.awt.*;
import java.util.List;
import static com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker.*;

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

    @Override
    public void moveBack() {
        super.moveBack();
        isInEnpassantPosition = previousEnpassantStatus;
        isOnStartingLine = previousStatingLineState;
    }

    private void setDirection() {
        if (getPieceColor() == PieceColor.BLACK) {
            direction = Direction.UP;
        } else {
            direction = Direction.DOWN;
        }
    }

    @Override
    protected boolean isLegalMove(Point destination) throws IllegalMoveException {
        boolean isLegal = false;
        int direction = getDirection();
        Point tempDestination = new Point(getPiecePosition());
        tempDestination.y += direction;
        // the tile in front
        if (tempDestination.equals(destination)) {
            isLegal = isInBounds(tempDestination) && isVacantPosition(tempDestination, board);
        }
        // then one to the left
        if(!isLegal) {
            tempDestination.x -= 1;
            if (tempDestination.equals(destination)) {
                isLegal = isInBounds(tempDestination) && hasEnemyPiece(getPieceColor(), tempDestination, board);
            }
        }
        // then the one on the right
        if(!isLegal) {
            tempDestination.x += 2;
            if (tempDestination.equals(destination)) {
                isLegal = isInBounds(tempDestination) && hasEnemyPiece(getPieceColor(), tempDestination, board);
            }
        }
        if(!isLegal) {
            if (isOnStartingLine) {
                int oldY = tempDestination.y;
                tempDestination.y += direction;
                tempDestination.x = getPiecePosition().x;
                // then the tile two to the front
                if (tempDestination.equals(destination)) {
                    isLegal = isInBounds(tempDestination) && isVacantPosition(tempDestination, board) && isVacantPosition(new Point(tempDestination.x, oldY), board);
                    isInEnpassantPosition = isLegal;
                }
            }
        }
        if(!isLegal) {
            tempDestination = new Point(getPiecePosition());
            tempDestination.y += direction;
            tempDestination.x += 1;
            if(tempDestination.equals(destination)) {
                isLegal = isEnpassant(tempDestination);
            }
            if(!isLegal) {
                tempDestination.x -= 2;
                tempDestination.y -= direction;
                if(tempDestination.equals(destination)) {
                    isLegal = isEnpassant(tempDestination);
                }
            }
        }
        if(isLegal) {
            previousStatingLineState = isOnStartingLine;
            isOnStartingLine = false;
            if(previousEnpassantStatus) {
                isInEnpassantPosition = false;
            }
            previousEnpassantStatus = isInEnpassantPosition;
        }
        return isLegal;
    }

    private boolean isEnpassant(Point tempDestination) {
        try {
            if (isInBounds(tempDestination)) {
                try {
                    if (board.getPiece(tempDestination) instanceof Pawn) {
                        Pawn pawn = ((Pawn) board.getPiece(tempDestination));
                        return pawn.isInEnpassantPosition;
                    }
                } catch (NoPieceFoundException e) {
                    return false;
                }
            }
        } catch (IllegalMoveException e) {
            return false;
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

}