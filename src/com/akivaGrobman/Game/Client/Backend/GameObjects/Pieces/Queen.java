package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.ChessGame;

import java.awt.*;
import java.util.ArrayList;
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
    public boolean isLegalMove(Point origin, Point destination, Board board) throws IllegalMoveException {
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
    public List<Point> getLegalMoves(Board board, Point piecePosition) {
        this.board = board;
        final int SUM_OF_LEGAL_MOVES = 27;
        List<Point> legalMoves = new ArrayList<>();
        Point temp;
        // straight
        for (int x = getPiecePosition().x + 1; x < ChessGame.SUM_OF_COLUMNS; x++) {
            temp = new Point(x, getPiecePosition().y);
            if(!addedDestinationToLegalMovesList(legalMoves, getPiecePosition(), temp)) {
                break;
            }
        }
        for (int x = getPiecePosition().x - 1; x >= 0; x--) {
            temp = new Point(x, getPiecePosition().y);
            if(!addedDestinationToLegalMovesList(legalMoves, getPiecePosition(), temp)) {
                break;
            }
        }
        for (int y = getPiecePosition().y + 1; y < ChessGame.SUM_OF_ROWS; y++) {
            temp = new Point(getPiecePosition().x, y);
            if(!addedDestinationToLegalMovesList(legalMoves, getPiecePosition(), temp)) {
                break;
            }
        }
        for (int y = getPiecePosition().y - 1; y >= 0; y--) {
            temp = new Point(getPiecePosition().x, y);
            if(!addedDestinationToLegalMovesList(legalMoves, getPiecePosition(), temp)) {
                break;
            }
        }
        // diagonal
        for (int i = 1; i <= 4; i++) {
            temp = new Point(getPiecePosition().x + i, getPiecePosition().y + i);
            if(!addedDestinationToLegalMovesList(legalMoves, getPiecePosition(), temp)) {
                break;
            }
        }
        for (int i = 1; i <= 4; i++) {
            temp = new Point(getPiecePosition().x - i, getPiecePosition().y + i);
            if(!addedDestinationToLegalMovesList(legalMoves, getPiecePosition(), temp)) {
                break;
            }
        }
        for (int i = 1; i <= 4; i++) {
            temp = new Point(getPiecePosition().x + i, getPiecePosition().y - i);
            if(!addedDestinationToLegalMovesList(legalMoves, getPiecePosition(), temp)) {
                break;
            }
        }
        for (int i = 1; i <= 4; i++) {
            if (legalMoves.size() >= SUM_OF_LEGAL_MOVES) break;
            temp = new Point(getPiecePosition().x - i, getPiecePosition().y - i);
            if(!addedDestinationToLegalMovesList(legalMoves, getPiecePosition(), temp)) {
                break;
            }
        }
        return legalMoves;
    }
}
