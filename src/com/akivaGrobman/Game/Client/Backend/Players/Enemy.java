package com.akivaGrobman.Game.Client.Backend.Players;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Enemy extends Player {

    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    public Enemy(PieceColor playersPieceColor, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        super(playersPieceColor);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void sendMove(Move move) {
        try {
            outputStream.writeObject(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Move getMove() {
        try {
            Object input = inputStream.readObject();
            if(input instanceof Move) {
                return (Move)input;
            } throw new Error("wrong cast type " + input.getClass().getSimpleName());
        } catch (IOException | ClassNotFoundException e) {
            throw new Error(e.getMessage());
        }
    }

}
