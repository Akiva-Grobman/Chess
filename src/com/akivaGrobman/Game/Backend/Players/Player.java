package com.akivaGrobman.Game.Backend.Players;

import com.akivaGrobman.Game.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.ChessGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private PieceColor playersPieceColor;
    private boolean isPlayersTurn;
    private List<Move> playersMoves;
    private Move currentMove;
    private ChessGame game;

    public Player(PieceColor playersPieceColor, ChessGame game) {
        this.playersPieceColor = playersPieceColor;
        this.game = game;
        isPlayersTurn = playersPieceColor == PieceColor.WHITE;
        playersMoves = new ArrayList<>();
    }

    public void addPositionToMove(Point position) {
       if(currentMove == null) {
           currentMove = new Move(position);
       } else if(!currentMove.isReadyToBeUsed()) {
           currentMove.setDestination(position);
           playersMoves.add(currentMove);
           game.move(currentMove, this);
       } else {
           currentMove = new Move(position);
       }
    }

    public Move getMove() {
        if (!currentMove.isReadyToBeUsed()) throw new Error("Move data is not complete and ready for usage");
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
}
