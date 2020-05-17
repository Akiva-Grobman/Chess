package com.akivaGrobman.Game.Client;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.Players.Enemy;
import com.akivaGrobman.Game.Client.GameManagers.MultiPlayerChessGame;
import com.akivaGrobman.Game.Client.GameManagers.Parent;
import com.akivaGrobman.Game.Client.GameManagers.SinglePlayerChessGame;
import com.akivaGrobman.Game.Server.Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class Client {

    public static void startSinglePlayerGame() {
        Random random = new Random();
        new SinglePlayerChessGame(PieceColor.values()[random.nextInt(PieceColor.values().length)]);
    }

    public static void startMultiplayerGame() throws IOException, ClassNotFoundException {
        Socket socket;
        PieceColor playersColor;
        Enemy enemy;
        socket = new Socket("127.0.0.1", Server.PORT);
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        playersColor = PieceColor.valueOf((String) inputStream.readObject());
        enemy = new Enemy(getEnemyColor(playersColor), inputStream, outputStream);
        new MultiPlayerChessGame(playersColor, enemy);
    }

    private static PieceColor getEnemyColor(PieceColor playersColor) {
        return (playersColor == PieceColor.WHITE)? PieceColor.BLACK: PieceColor.WHITE;
    }

}
