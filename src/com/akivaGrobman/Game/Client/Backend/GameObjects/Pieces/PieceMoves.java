package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import java.awt.*;
import java.util.List;

interface PieceMoves {

    List<Point> getLegalMoves(Board board, Point piecePosition);

}
