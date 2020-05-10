package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.ChessGame;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker.*;

public class Rook extends Piece implements PieceMoves{

    private boolean moved;

    public Rook(Point position, PieceColor color) {
        super(position, PieceType.ROOK, color);
        moved = false;
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
    public boolean isLegalMove(Point origin, Point destinationsPosition, Board board) throws IllegalMoveException {
        if(destinationsPosition.x != origin.x && destinationsPosition.y != origin.y) return false;
        this.board = board;
        Point tempDestination = new Point(origin);
        int direction;
        if(destinationsPosition.x == origin.x) {
            direction = getDirection(origin.y, destinationsPosition.y);
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
        if(destinationsPosition.y == origin.y) {
            direction = getDirection(origin.x, destinationsPosition.x);
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
    public List<Point> getLegalMoves(Board board, Point piecePosition) {
        this.board = board;
        List<Point> legalMoves = new ArrayList<>();
        Point temp;
        for (int x = piecePosition.x + 1; x < ChessGame.SUM_OF_COLUMNS; x++) {
            temp = new Point(x, piecePosition.y);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int x = piecePosition.x - 1; x >= 0; x--) {
            temp = new Point(x, piecePosition.y);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int y = piecePosition.y + 1; y < ChessGame.SUM_OF_ROWS; y++) {
            temp = new Point(piecePosition.x, y);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int y = piecePosition.y - 1; y >= 0; y--) {
            temp = new Point(piecePosition.x, y);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        return legalMoves;
    }

    public boolean getHasMoved() {
        return moved;
    }

    public void moved() {
        moved = true;
    }
}
