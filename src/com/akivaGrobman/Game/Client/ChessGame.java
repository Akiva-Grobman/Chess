package com.akivaGrobman.Game.Client;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Piece;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.Players.Enemy;
import com.akivaGrobman.Game.Client.Backend.Players.Move;
import com.akivaGrobman.Game.Client.Backend.Players.Player;
import com.akivaGrobman.Game.Client.Frontend.GraphicBoard;

import java.awt.*;

public class ChessGame {

    public static final int SUM_OF_ROWS = 8;
    public static final int SUM_OF_COLUMNS = 8;

    private Player player;
    private Enemy enemy;
    private Player currentPlayer;
    private final Board backendBoard;
    private final GraphicBoard onScreenBoard;

    public ChessGame(PieceColor playersColor, Enemy enemy) {
        backendBoard = new Board();
        setPlayers(playersColor, enemy);
        onScreenBoard = new GraphicBoard(backendBoard, player);
        if(player.getPlayersColor() == PieceColor.BLACK) {
            makeEnemyMove();
        }
    }

    private void setPlayers(PieceColor playersColor, Enemy enemy) {
        this.enemy = enemy;
        player = new Player(playersColor);
        player.setContext(this);
        this.enemy.setContext(this);
        if(player.getPlayersColor() == PieceColor.WHITE) {
            currentPlayer = player;
        } else {
            currentPlayer = this.enemy;
        }
    }

    public void move(Move move, Player player) {
        if(currentPlayer.equals(player)) {
            Piece oldPiece = getPiece(move.getDestination());
            move(move);
            if(wasLegalMove(move, oldPiece)) {
                Point destination = move.getDestination();
                Piece piece = getPiece(destination);
                onScreenBoard.updateTile(destination, piece.getPieceType(), currentPlayer.getPlayersColor());
                onScreenBoard.updateTile(move.getOrigin(), null, null);
                if(move.getPlayersColor() == this.player.getPlayersColor()) {
                    enemy.sendMove(move);
                }
                changeCurrentPlayer();
                if (player.equals(this.player)) {
                    makeEnemyMove();
                }
            }
        }
    }

    public void makeEnemyMove() {
        move(enemy.getMove(), enemy);
    }

    private boolean wasLegalMove(Move move, Piece oldPiece) {
        return getPiece(move.getDestination()) != null && oldPiece != getPiece(move.getDestination()) && !backendBoard.hasPieceInThisPosition(move.getOrigin());
    }

    private void move(Move currentMove) {
        try {
            int STARTING_DEPTH = 1;
            backendBoard.move(currentMove.getOrigin(), currentMove.getDestination(), STARTING_DEPTH);
        } catch (IllegalMoveException | NoPieceFoundException e) {
            String msg = e.getMessage();
            if (msg.contains("can not move piece to original position")) {
                System.out.println("can not move piece to original position");
            } else if (msg.contains("no piece found in position x = " + currentMove.getOrigin().x + " y = " + currentMove.getOrigin().y)) {
                System.out.println("no piece found in position x = " + currentMove.getOrigin().x + " y = " + currentMove.getOrigin().y);
            }
        }
    }

    private Piece getPiece(Point position) {
        try {
            return backendBoard.getPiece(position);
        } catch (NoPieceFoundException e) {
            return null;
        }
    }

    private void changeCurrentPlayer() {
        if(currentPlayer == player) {
            currentPlayer = enemy;
        } else {
            currentPlayer = player;
        }
    }

}
