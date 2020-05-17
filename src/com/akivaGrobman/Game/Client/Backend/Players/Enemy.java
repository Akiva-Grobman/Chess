package com.akivaGrobman.Game.Client.Backend.Players;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Piece;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.GameObjects.PromotionMessage;

import java.awt.*;
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

    public void sendMove(Positions positions) {
        try {
            outputStream.writeObject(positions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Positions getMove() {
        try {
            Object input = inputStream.readObject();
            if(input instanceof Positions) {
                return (Positions)input;
            } else if(input instanceof String) {
                game.gameOver(PieceColor.valueOf((String) input));
            } throw new Error("wrong cast type " + input.getClass().getSimpleName());
        } catch (IOException | ClassNotFoundException e) {
            throw new Error(e.getMessage());
        }
    }

    public PromotionMessage getPromotion() {
        try {
            Object input = inputStream.readObject();
            if(input instanceof PromotionMessage) {
                return (PromotionMessage) input;
            } throw new Error("wrong cast type " + input.getClass().getSimpleName());
        } catch (IOException | ClassNotFoundException e) {
            throw new Error(e.getMessage());
        }
    }

    public void sendPromotionMessage(Piece piece, Point promotionPosition) {
        try {
            outputStream.writeObject(new PromotionMessage(promotionPosition, piece));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameOver(PieceColor playersColor) {
        try {
            outputStream.writeObject(playersColor.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
