package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker.*;

public class Pawn extends Piece implements PieceMoves {

    private final int STARTING_ROW;
    private final int direction;

    public Pawn(Point position, PieceColor color) {
        super(position, PieceType.PAWN, color);
        STARTING_ROW = position.y;
        board = null;
        direction = getDirection();
    }

    // this is for testing only (that's why it's protected)
    protected Pawn(Point position, PieceColor pieceColor, boolean isInEnpassantPosition) {
        super(position, PieceType.PAWN, pieceColor);
        if(pieceColor == PieceColor.BLACK) {
            STARTING_ROW = 1;
            direction = 1;
        } else {
            STARTING_ROW = 6;
            direction = -1;
        }
        if(isInEnpassantPosition) {
            setPreviousPosition(new Point(getPiecePosition().x, STARTING_ROW));
        }
        board = null;
    }

    @Override
    public Piece getClone() {
        return new Pawn((Point) getPiecePosition().clone(), getPieceColor());
    }

    @Override
    public String getPieceInString() {
        return " " + getPieceType() + " ";
    }

    @Override
    public List<Point> getLegalMoves(Board board, Point piecePosition) {
        this.board = board;
        List<Point> legalMoves = new ArrayList<>();
        Point temp;
        temp = new Point(piecePosition.x, piecePosition.y + direction);
        if(shouldAddPositionToLegalMovesList(piecePosition, temp)) {
            legalMoves.add(temp);
            temp = new Point(piecePosition.x, piecePosition.y + (2 * direction));
            if(shouldAddPositionToLegalMovesList(piecePosition, temp)) {
                legalMoves.add(temp);
            }
        }
        // these two will handle enpassant as well
        temp = new Point(piecePosition.x + 1, piecePosition.y + direction);
        if(shouldAddPositionToLegalMovesList(piecePosition, temp)) {
            legalMoves.add(temp);
        }
        temp = new Point(piecePosition.x - 1, piecePosition.y + direction);
        if(shouldAddPositionToLegalMovesList(piecePosition, temp)) {
            legalMoves.add(temp);
        }
        return legalMoves;
    }

    private int getDirection() {
        if (getPieceColor() == PieceColor.BLACK) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean isLegalMove(Point origin, Point destination, Board board) throws IllegalMoveException {
        this.board = board;
        boolean isLegal = false;
        Point tempDestination = new Point(origin);
        tempDestination.y += direction;
        // the tile in front
        if (tempDestination.equals(destination)) {
            isLegal = isInBounds(tempDestination) && isVacantPosition(tempDestination, this.board);
        }
        // then one to the left
        tempDestination.x -= 1;
        if (tempDestination.equals(destination)) {
            isLegal = isInBounds(tempDestination) && hasEnemyPiece(getPieceColor(), tempDestination, this.board);
            // enpassant to the left
            if(!isLegal) {
                isLegal = isEnpassant(tempDestination);
            }
        }
        // then the one on the right
        tempDestination.x += 2;
        if (tempDestination.equals(destination)) {
            isLegal = isInBounds(tempDestination) && hasEnemyPiece(getPieceColor(), tempDestination, this.board);
            // enpassant to the right
            if(!isLegal) {
                isLegal = isEnpassant(tempDestination);
            }
        }
        // then the tile two to the front
        if (origin.y == STARTING_ROW) {
            int oldY = tempDestination.y;
            tempDestination.y += direction;
            tempDestination.x = origin.x;
            if (tempDestination.equals(destination)) {
                isLegal = isInBounds(tempDestination) && isVacantPosition(tempDestination, this.board) && isVacantPosition(new Point(tempDestination.x, oldY), this.board);
            }
        }
        return isLegal;
    }

    private boolean isEnpassant(Point tempDestination) {
        try {
            if (isInBounds(tempDestination)) {
                try {
                    if (board.getPiece(new Point(tempDestination.x, tempDestination.y - direction)) instanceof Pawn) {
                        Pawn pawn = ((Pawn) board.getPiece(new Point(tempDestination.x, tempDestination.y - direction)));
                        return pawn.getPieceColor() != getPieceColor() && board.getEnpassant(pawn.getPieceColor(), tempDestination.x);
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

}