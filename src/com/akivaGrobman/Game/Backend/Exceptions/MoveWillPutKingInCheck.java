package com.akivaGrobman.Game.Backend.Exceptions;

import java.awt.*;

public class MoveWillPutKingInCheck extends Exception {

    public MoveWillPutKingInCheck(String enemyPiece, Point enemyPiecePosition, Point origin, Point destination) {
        super(String.format("can not move form %d,%d to %d,%d %s in position %d,%d will put king in check",
                origin.x, origin.y, destination.x, destination.y, enemyPiece, enemyPiecePosition.x, enemyPiecePosition.y));
    }

}
