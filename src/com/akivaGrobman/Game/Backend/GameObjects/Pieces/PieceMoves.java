package com.akivaGrobman.Game.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Backend.GameObjects.Board;

import java.awt.*;
import java.util.List;

interface PieceMoves {

    void move(Point destinationsPosition, Board board) throws IllegalMoveException;

    List<Point> getLegalMoves();

}
