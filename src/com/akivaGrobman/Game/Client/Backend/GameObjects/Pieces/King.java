package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameRules.CheckChecker;
import com.akivaGrobman.Game.Client.ChessGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static com.akivaGrobman.Game.Client.Backend.GameRules.BoardConditionsChecker.*;

public class King extends Piece implements PieceMoves {

    private final int STARTING_COLUMN;
    private boolean wasInCheck;
    private boolean moved;

    public King(Point position, PieceColor color) {
        super(position, PieceType.KING, color);
        wasInCheck = false;
        moved = false;
        STARTING_COLUMN = position.x;
    }

    @Override
    public Piece getClone() {
        King king = new King((Point) getPiecePosition().clone(), getPieceColor());
        king.wasInCheck = this.wasInCheck;
        return king;
    }

    @Override
    public String getPieceInString() {
        return " " + getPieceType() + " ";
    }

    public boolean isInCheck(Board board, int depth) {
        return CheckChecker.kingIsInCheck(getPieceColor(), board, depth);
    }

    @Override
    public boolean isLegalMove(Point destination, Board board) throws IllegalMoveException {
        this.board = board;
        Point tempDestination = new Point(getPiecePosition());
        Point direction = getDirection(destination);
        tempDestination.x += direction.x;
        tempDestination.y += direction.y;
        if(!isInBounds(tempDestination)) {
            return false;
        }
        return (destination.equals(tempDestination) && canMoveThere(tempDestination, getPieceColor())) || isCastlingMove(destination);
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

    private boolean isCastlingMove(Point destination) {
        if (moved || wasInCheck || getPiecePosition().x != STARTING_COLUMN || destination.x != 6 && destination.x != 1) return false;
        int rookX;
        if(destination.x < getPiecePosition().x) {
            rookX = 0;
        } else {
            rookX = 7;
        }
        try {
            Piece piece = board.getPiece(new Point(rookX, getPiecePosition().y));
            if(piece instanceof Rook) {
                if (piece.getPieceColor() != getPieceColor() || ((Rook) piece).getHasMoved()) {
                    return false;
                }
            } else {
                return false;
            }
        } catch (NoPieceFoundException e) {
            return false;
        }
        Board isInCheckTesterBoard;
        switch (destination.x) {
            case 1:
                for (int x = getPiecePosition().x; x > 0; x--) {
                    move(new Point(x, getPiecePosition().y));
                    isInCheckTesterBoard = Board.getConsumeBoard(List.of(this), List.of(getPreviousPosition()));
                    if(board.hasPieceInThisPosition(new Point(x, getPiecePosition().y)) || isInCheck(isInCheckTesterBoard, 1)) {
                        reversMove();
                        return false;
                    }
                    reversMove();
                }
                break;
            case 6:
                for (int x = getPiecePosition().x; x < ChessGame.SUM_OF_COLUMNS; x++) {
                    move(new Point(x, getPiecePosition().y));
                    isInCheckTesterBoard = Board.getConsumeBoard(List.of(this), List.of(getPreviousPosition()));
                    if(board.hasPieceInThisPosition(new Point(x, getPiecePosition().y)) || isInCheck(isInCheckTesterBoard, 1)) {
                        reversMove();
                        return false;
                    }
                    reversMove();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public List<Point> getLegalMoves(Board board) {
        this.board = board;
        List<Point> legalMoves = new ArrayList<>();
        Point temp;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if(y == 0 && x == 0) continue;// king original position
                temp = new Point(getPiecePosition().x + x, getPiecePosition().y + y);
                if(shouldAddPositionToLegalMovesList(getPiecePosition(), temp)) {
                    legalMoves.add(temp);
                }
            }
        }
        // todo add castling
        return legalMoves;
    }

    public void moved() {
        moved = true;
    }

    public void setToIsInCheck() {
        wasInCheck = true;
    }

}

