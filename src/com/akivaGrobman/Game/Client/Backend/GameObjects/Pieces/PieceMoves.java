package com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces;

import java.awt.*;
import java.util.List;

interface PieceMoves {

    void move(Point destinationsPosition);

    List<Point> getLegalMoves();

}
