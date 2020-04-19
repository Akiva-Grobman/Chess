package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;

import java.awt.*;
import java.util.List;

interface PieceMoves {

    void move(Point destinationsPosition, Board board) throws IllegalMoveException;

    List<Point> getLegalMoves();

}
