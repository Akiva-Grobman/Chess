package com.akivaGrobman.Game.Client.Backend.Players;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import java.awt.*;
import java.io.Serializable;

public class Move implements Serializable {

    private Point origin;
    private Point destination;
    private PieceColor playersColor;

    public Move(Point origin, PieceColor playersColor) {
        this.origin = origin;
        this.playersColor = playersColor;
    }

    void setDestination(Point destination) {
        this.destination = destination;
    }

    public Point getOrigin() {
        return origin;
    }

    public Point getDestination() {
        return destination;
    }

    public boolean isReadyToBeUsed() {
        return origin != null && destination != null;
    }

    public PieceColor getPlayersColor() {
        return playersColor;
    }

    @Override
    public String toString() {
        return origin.toString() + destination.toString();
    }
}
