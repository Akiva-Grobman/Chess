package com.akivaGrobman.Game.Server;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.GameObjects.PromotionMessage;
import com.akivaGrobman.Game.Client.Backend.Players.Positions;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 9090;

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
        otherPlayer = players[1];
        do {
            Object msg = currentPlayer.getMove();
            if(msg instanceof PromotionMessage) { // when promotion a pawn we will receive the promotion message first and then the move and we will send it to the client in the reverse order
                otherPlayer.sendMove(currentPlayer.getMove());
            }
            otherPlayer.sendMove(msg);
            updatePlayers();
        } while (run);
    }

    private static void updatePlayers() {
        Player temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;
    }

    private void stop() {
        run = false;
    }

}
