package com.akivaGrobman.Game.Client.Backend.Players;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class EnemyTest {

    private ServerSocket serverSocket;
    private Enemy enemy;
    private Socket client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    @BeforeEach
    void setUp() throws Exception {
        serverSocket = new ServerSocket(9090);
        Thread thread = new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();
                inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        System.out.println("server waiting");
        System.out.println("starting client");
        client = new Socket("127.0.0.1", 9090);
        enemy = new Enemy(PieceColor.BLACK, new ObjectInputStream(client.getInputStream()), new ObjectOutputStream(client.getOutputStream()));
        System.out.println("client done");

    }

    @Test
    void sendTheMessageWithoutErrors() throws Exception {
        Move move;
        do {
            if(enemy != null) {
                move = new Move(new Point(1,2), enemy.getPlayersColor());
                break;
            }
        } while (true);

        enemy.sendMove(move);

        assertEquals(move, inputStream.readObject());
    }
}