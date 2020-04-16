package com.akivaGrobman.Game.Server;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.Players.Move;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 9090;

    private static Player[] players;
    private static Player currentPlayer;
    private static Player otherPlayer;
    private static boolean run;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket;
        Socket clientSocket;
        serverSocket = new ServerSocket(PORT);
        players = new Player[2];
        clientSocket = serverSocket.accept();
        players[0] = new Player(clientSocket, PieceColor.WHITE);
        clientSocket = serverSocket.accept();
        players[1] = new Player(clientSocket, PieceColor.BLACK);
        serverSocket.close();
        runGame();
    }

    private static void runGame() throws IOException, ClassNotFoundException {
        run = true;
        currentPlayer = players[0];
        do {
            Move move = currentPlayer.getMove();
            otherPlayer.sendMove(move);
            updatePlayers();
        } while (run);
    }

    private static void updatePlayers() {
        if(currentPlayer == players[0]) {
            currentPlayer = players[1];
            otherPlayer = players[0];
        } else {
            currentPlayer = players[0];
            otherPlayer = players[1];
        }
    }

    private void stop() {
        run = false;
    }

}
