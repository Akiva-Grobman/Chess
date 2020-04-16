package com.akivaGrobman.Game.Client.Backend.Players;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Enemy extends Player {

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public Enemy(PieceColor playersPieceColor, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        super(playersPieceColor);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }



}
