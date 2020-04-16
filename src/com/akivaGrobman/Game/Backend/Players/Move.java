package com.akivaGrobman.Game.Backend.Players;

import java.awt.*;

public class Move {

    private Point origin;
    private Point destination;

    public Move(Point origin) {
        this.origin = origin;
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

}
