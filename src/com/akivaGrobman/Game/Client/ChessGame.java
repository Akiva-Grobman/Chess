package com.akivaGrobman.Game.Client;

import com.akivaGrobman.Game.Client.Backend.Exceptions.IllegalMoveException;
import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Move;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Piece;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.Backend.Players.Enemy;
import com.akivaGrobman.Game.Client.Backend.Players.Positions;
import com.akivaGrobman.Game.Client.Backend.Players.Player;
import com.akivaGrobman.Game.Client.Frontend.GraphicBoard;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.akivaGrobman.Game.Client.Backend.GameRules.SpecialMoves.*;

public class ChessGame {


    public static final int SUM_OF_ROWS = 8;
    public static final int SUM_OF_COLUMNS = 8;
    private final Board backendBoard;
    private final GraphicBoard onScreenBoard;
    private final List<Move> moves;
    private Player player;
    private Enemy enemy;
    private Player currentPlayer;

    public ChessGame(PieceColor playersColor, Enemy enemy) {
        backendBoard = new Board();
        moves = new ArrayList<>();
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

    public void move(Positions positions, Player player) {
        System.out.println(player.getClass().getSimpleName() + " is making a move " + positions);
        if(player.equals(enemy) || isLegalMove(positions)) {
            System.out.println("is legal");
            addMove(positions);
            Piece piece = getPiece(positions.getOrigin());
            assert piece != null; // if piece is null then the move shouldn't be legal
            piece.move(positions.getDestination());
            backendBoard.updateTile(positions.getOrigin(), null);
            backendBoard.updateTile(positions.getDestination(), piece);
            onScreenBoard.updateTile(positions.getDestination(), piece.getPieceType(), piece.getPieceColor());
            onScreenBoard.updateTile(positions.getOrigin(), null, null);
            if(wasEnpassant(backendBoard, moves)) {
                System.out.println("enpassant");
                backendBoard.updateTile(new Point(positions.getDestination().x, positions.getOrigin().y), null);
                onScreenBoard.updateTile(new Point(positions.getDestination().x, positions.getOrigin().y), null, null);
            } // todo
//             else if(wasCastling(backendBoard, moves)) {
//                //done
//            } else if(wasPromotion(backendBoard, positions.getDestination())) {
//                //done
//            }
            if(player.equals(this.player)) {
                System.out.println("sending move");
                enemy.sendMove(positions);
            }
            changeCurrentPlayer();
            if(player.equals(this.player)) {
                System.out.println("making enemy move");
                makeEnemyMove();
            }
        }
    }

    private void addMove(Positions positions) {
        Piece piece;
        try {
            piece = backendBoard.getPiece(positions.getDestination());
        } catch (NoPieceFoundException e) {
            piece = null;
        }
        moves.add(new Move(positions, piece));
    }

    public void makeEnemyMove() {
        move(enemy.getMove(), enemy);
    }

    private boolean isLegalMove(Positions positions) {
        try {
            int STARTING_DEPTH = 1;
            return backendBoard.isLegalMove(positions.getOrigin(), positions.getDestination(), STARTING_DEPTH);
        } catch (IllegalMoveException | NoPieceFoundException e) {
            String msg = e.getMessage();
            System.out.println(msg);
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
