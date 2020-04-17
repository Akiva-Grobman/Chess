package com.akivaGrobman.Game.Client.Backend.Players;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.ChessGame;

import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private PieceColor playersPieceColor;
    private boolean isPlayersTurn;
    private List<Move> playersMoves;
    private Move currentMove;
    protected ChessGame game;

    public Player(PieceColor playersPieceColor) {
        this.playersPieceColor = playersPieceColor;
        isPlayersTurn = playersPieceColor == PieceColor.WHITE;
        playersMoves = new ArrayList<>();
    }

    public void addPositionToMove(Point position) {
       if(currentMove == null) {
           currentMove = new Move(position, playersPieceColor);
       } else if(!currentMove.isReadyToBeUsed()) {
           currentMove.setDestination(position);
           playersMoves.add(currentMove);
           game.move(currentMove, this);
       } else {
           currentMove = new Move(position, playersPieceColor);
       }
    }

    public Move getMove() {
        if (currentMove == null || !currentMove.isReadyToBeUsed()) throw new Error("Move data is not complete and ready for usage");
        playersMoves.add(currentMove);
        return currentMove;
    }

    public boolean isPlayersTurn() {
        return isPlayersTurn;
    }

    public void updateTurn() {
        isPlayersTurn = !isPlayersTurn;
    }

    public PieceColor getPlayersColor() {
        return playersPieceColor;
    }

    public void setContext(ChessGame game) {
        if(this.game == null) {
            this.game = game;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Player)) return false;
        Player player = (Player) obj;
        return this.playersPieceColor == player.playersPieceColor;
    }
}
