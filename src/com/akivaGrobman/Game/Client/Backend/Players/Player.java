package com.akivaGrobman.Game.Client.Backend.Players;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.ChessGame;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private final List<Move> playersMoves;
    private final PieceColor playersPieceColor;
    private Move currentMove;
    protected ChessGame game;

    public Player(PieceColor playersPieceColor) {
        this.playersPieceColor = playersPieceColor;
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
