package com.akivaGrobman.Game.Client.Backend.Players;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import java.io.IOException;
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

    public void sendMove(Move move) {
        try {
            System.out.println("sending " + move);
            outputStream.writeObject(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Move getMove() {
        try {
            System.out.println("getting move");
            Object input = inputStream.readObject();
            if(input instanceof Move) {
                return (Move)input;
            } throw new Error("wrong cast type " + input.getClass().getSimpleName());
        } catch (IOException | ClassNotFoundException e) {
            throw new Error(e.getMessage());
        }
    }

}
