package com.akivaGrobman.Game.Client.Frontend;

import com.akivaGrobman.Game.Client.GameManagers.Parent;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MouseListener implements java.awt.event.MouseListener {
    
    private final Parent game;
    private final Point position;

    public MouseListener(Parent game, Point position) {
        this.game = game;
        this.position = position;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        game.tileClicked(position);
    }   

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
