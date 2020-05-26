package com.akivaGrobman.Game;

import com.akivaGrobman.Game.Client.Client;
import com.akivaGrobman.Game.Server.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // todo opening window to chose multiplayer of single
        boolean isMultiPlayerGame = true;
        if(isMultiPlayerGame) {
            Client.startMultiplayerGame();
        } else {
            Client.startSinglePlayerGame();
        }
    }

}
