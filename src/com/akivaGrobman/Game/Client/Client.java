package com.akivaGrobman.Game.Client;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.Players.Enemy;
import com.akivaGrobman.Game.Client.Backend.Players.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket;
        PieceColor playersColor;
        Enemy enemy;
        socket = new Socket("127.0.0.1", 9090);
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        playersColor = PieceColor.valueOf((String) inputStream.readObject());
        enemy = new Enemy(getEnemyColor(playersColor), inputStream, outputStream);
        new ChessGame(playersColor, enemy);
    }

    private static PieceColor getEnemyColor(PieceColor playersColor) {
        return (playersColor == PieceColor.WHITE)? PieceColor.BLACK: PieceColor.WHITE;
    }

}
