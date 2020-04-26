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
        onScreenBoard = new GraphicBoard(backendBoard, this);
        if(player.getPlayersColor() == PieceColor.BLACK) {
            makeEnemyMove();
        }
    }

    private void setPlayers(PieceColor playersColor, Enemy enemy) {
        enemy.setContext(this);
        this.enemy = enemy;
        player = new Player(playersColor);
        player.setContext(this);
        if(player.getPlayersColor() == PieceColor.WHITE) {
            currentPlayer = player;
        } else {
            currentPlayer = this.enemy;
        }
    }

    public void move(Move move, Player player) {
        if(isLegalMove(move)) {
            Piece piece = getPiece(move.getOrigin());
            backendBoard.updateTile(move.getOrigin(), null);
            backendBoard.updateTile(move.getDestination(), piece);
            assert piece != null; // because it wouldn't be legal if it was
            onScreenBoard.updateTile(move.getDestination(), piece.getPieceType(), piece.getPieceColor());
            onScreenBoard.updateTile(move.getOrigin(), null, null);
            if(player.equals(this.player)) {
                enemy.sendMove(move);
            }
            changeCurrentPlayer();
            if(player.equals(this.player)) {
                makeEnemyMove();
            }
        }
    }

    public void makeEnemyMove() {
        move(enemy.getMove(), enemy);
    }

    private boolean isLegalMove(Move move) {
        try {
            int STARTING_DEPTH = 1;
            return backendBoard.isLegalMove(move.getOrigin(), move.getDestination(), STARTING_DEPTH);
        } catch (IllegalMoveException | NoPieceFoundException e) {
            String msg = e.getMessage();
            if (msg.contains("can not move piece to original position")) {
                System.out.println("can not move piece to original position");
            } else if (msg.contains("no piece found in position x = " + move.getOrigin().x + " y = " + move.getOrigin().y)) {
                System.out.println("no piece found in position x = " + move.getOrigin().x + " y = " + move.getOrigin().y);
            }
            return false;
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

    public void tileClicked(Point tilePosition) {
        if(currentPlayer.equals(player)) {
            player.addPositionToMove(tilePosition);
        }
    }

    public PieceColor getPlayersColor() {
        return player.getPlayersColor();
    }
}
