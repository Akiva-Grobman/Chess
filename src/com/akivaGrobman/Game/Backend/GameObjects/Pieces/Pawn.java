package com.akivaGrobman.Game.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Backend.Exceptions.IllegalMoveException;
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
    private boolean hasEnpassantMove;
    private boolean isOnStartingLine;

    public Pawn(Point position, PieceColor color) {
        super(position, PieceType.PAWN, color);
        board = null;
        setDirection();
        hasEnpassantMove = false;
        isOnStartingLine = true;
    }

    @Override
    public void move(Point destinationsPosition, Board board) throws IllegalMoveException {
        this.board = board;
        if (isLegalMove(destinationsPosition)) {
            super.move(destinationsPosition, board);
            isOnStartingLine = false;
        } else {
            throw new IllegalMoveException(getClass().getSimpleName(), getPiecePosition(), destinationsPosition);
        }
    }

    @Override
    public Piece getClone() {
        Pawn pawn = new Pawn((Point) getPiecePosition().clone(), getPieceColor());
        pawn.direction = direction;
        pawn.hasEnpassantMove = hasEnpassantMove;
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
                return isInBounds(tempDestination) && isVacantPosition(tempDestination, board) && isVacantPosition(new Point(tempDestination.x, oldY), board);
            }
        }
        // todo add enpassant
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