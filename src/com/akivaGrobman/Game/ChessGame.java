package com.akivaGrobman.Game;

import com.akivaGrobman.Game.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Backend.GameObjects.Board;
import com.akivaGrobman.Game.Backend.GameObjects.BoardBuilder;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.Piece;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Backend.Players.Move;
import com.akivaGrobman.Game.Backend.Players.Player;
import com.akivaGrobman.Game.Frontend.GraphicBoard;

import java.awt.*;

public class ChessGame {

    private final int STARTING_DEPTH = 1;
    public static final int SUM_OF_ROWS = 8;
    public static final int SUM_OF_COLUMNS = 8;

    private Player player;
    private Player enemy;
    private Player currentPlayer;
    private Board backendBoard;
    private GraphicBoard ouScreenBoard;

    public ChessGame() {
        backendBoard = new Board();
        ouScreenBoard = new GraphicBoard(BoardBuilder.getGraphicsBoard(backendBoard));
        setPlayers();
    }

    private void setPlayers() {
        // todo get this from server
        player = new Player(PieceColor.WHITE, this);
        enemy = new Player(PieceColor.BLACK, this);
        if(player.getPlayersColor() == PieceColor.WHITE) {
            currentPlayer = player;
        } else {
            currentPlayer = enemy;
        }
    }

    public synchronized void move(Move move, Player player) {
        if(currentPlayer.equals(player)) {
            move(move);
            if(wasLegalMove(move.getOrigin())) {
                Point destination = move.getDestination();
                Piece piece = getPiece(destination);
                ouScreenBoard.updateTile(destination, piece.getPieceType(),currentPlayer.getPlayersColor());
                changeCurrentPlayer();
            }
        }
    }

    private void move(Move currentMove) {
        try {
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

    private boolean wasLegalMove(Point origin) {
        return !backendBoard.hasPieceInThisPosition(origin);
    }

    private Piece getPiece(Point position) {
        try {
            return backendBoard.getPiece(position);
        } catch (NoPieceFoundException e) {
            throw new Error("the original position is empty yet no piece found in position " + position);
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
