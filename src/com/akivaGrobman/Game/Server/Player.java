package com.akivaGrobman.Game.Server;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.Players.Positions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class Player {

    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    public Player(Socket socket, PieceColor playersColor) throws IOException {
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream.writeObject(playersColor.toString());
    }

    public Positions getMove() throws IOException, ClassNotFoundException {
        Object input = inputStream.readObject();
        if(input instanceof Positions) {
            return (Positions) input;
        }
        throw new Error("wrong cast Type "+ input.getClass().getSimpleName());
    }

    public void sendMove(Positions positions) throws IOException {
        outputStream.writeObject(positions);
    }

}
