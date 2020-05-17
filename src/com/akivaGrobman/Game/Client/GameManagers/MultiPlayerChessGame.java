package com.akivaGrobman.Game.Client.GameManagers;

import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.*;
import com.akivaGrobman.Game.Client.Backend.GameObjects.PromotionMessage;
import com.akivaGrobman.Game.Client.Backend.Players.Enemy;
import com.akivaGrobman.Game.Client.Backend.Players.Player;
import com.akivaGrobman.Game.Client.Backend.Players.Positions;
import com.akivaGrobman.Game.Client.Frontend.GraphicBoard;
import com.akivaGrobman.Game.Client.Frontend.PawnPromotionWindow;
import java.awt.*;
import java.util.ArrayList;

import static com.akivaGrobman.Game.Client.Backend.GameRules.SpecialMoves.*;

public class MultiPlayerChessGame extends ChessGame {

    private final Enemy enemy;

    public MultiPlayerChessGame(PieceColor playersColor, Enemy enemy) {
        backendBoard = new Board();
        moves = new ArrayList<>();
        this.enemy = enemy;
        setPlayers(playersColor);
        onScreenBoard = new GraphicBoard(backendBoard, this);
        if (player.getPlayersColor() == PieceColor.BLACK) {
            makeEnemyMove();
        }
    }

    private void setPlayers(PieceColor playersColor) {
        player = new Player(playersColor);
        this.enemy.setContext(this);
        player.setContext(this);
        if(player.getPlayersColor() == PieceColor.WHITE) {
            currentPlayer = player;
        } else {
            currentPlayer = this.enemy;
        }
    }

    @Override
    public void tileClicked(Point tilePosition) {
        if(isLocalPlayer(currentPlayer)) {
            super.tileClicked(tilePosition);
        }
    }

    @Override
    public void move(Positions positions, Player player) {
        if(!isLocalPlayer(player) || isLegalMove(positions)) {
            if(gameIsWon(positions.getDestination())) {
                Thread sendGameOver = new Thread(() -> enemy.sendGameOver(player.getPlayersColor()));
                gameOver(positions.getPlayersColor());
            }
            addMoveToMoveList(positions);
            updateBoards(positions);
            handleSpecialMoves(positions);
            if(isLocalPlayer(player) && !isPromoting) { // if it was a promoting move the message is sent in handleSpacialMoves
                enemy.sendMove(positions);
            }
            changeCurrentPlayer();
            if(enemyKingIsInCheck(positions.getDestination())) {
                putKingInCheck(currentPlayer);
            }
            if(isLocalPlayer(player)) {
                Thread messageSender = new Thread(this::makeEnemyMove);
                messageSender.start();
            }
        }
    }

    private void handleSpecialMoves(Positions positions) {
        updateEnpassantData(positions);
        isPromoting = false;
        if(wasEnpassant(backendBoard, moves)) {
            backendBoard.updateTile(new Point(positions.getDestination().x, positions.getOrigin().y), null);
            onScreenBoard.updateTile(new Point(positions.getDestination().x, positions.getOrigin().y), null, null);
        } else if(wasCastling(backendBoard, positions)) {
            int y = positions.getDestination().y;
            Piece rook;
            int originalX;
            int newX;
            if(positions.getDestination().x == 1) {
                originalX = 0;
                newX = 2;
            } else {
                originalX = 7;
                newX = 5;
            }
            rook = getPiece(new Point(originalX, positions.getDestination().y));
            assert rook != null; // will not be a castling move if piece is null
            backendBoard.updateTile(new Point(newX, y), rook);
            backendBoard.updateTile(new Point(originalX, y), null);
            onScreenBoard.updateTile(new Point(newX, y), PieceType.ROOK, positions.getPlayersColor());
            onScreenBoard.updateTile(new Point(originalX, y), null, null);
        } else if(wasPromotion(backendBoard, positions.getDestination())) {
            Piece piece;
            Point promotionPosition;
            if(isLocalPlayer(currentPlayer)) {
                promotedPieceType = null;
                promotionPosition = positions.getDestination();
                new PawnPromotionWindow(this, positions.getPlayersColor(), onScreenBoard.getFrame());
                assert promotedPieceType != null; // will be set by the popup window
                switch (promotedPieceType) {
                    case ROOK:
                        piece = new Rook(positions.getPlayersColor());
                        break;
                    case QUEEN:
                        piece = new Queen(positions.getPlayersColor());
                        break;
                    case BISHOP:
                        piece = new Bishop(positions.getPlayersColor());
                        break;
                    case KNIGHT:
                        piece = new Knight(positions.getPlayersColor());
                        break;
                    default:
                        throw new Error("wrong type " + promotedPieceType);
                }
                enemy.sendPromotionMessage(piece, promotionPosition);
                enemy.sendMove(positions);
                isPromoting = true;
            } else {
                PromotionMessage msg = enemy.getPromotion();
                piece = msg.getPiece();
                promotionPosition = msg.getPosition();
            }
            assert piece != null;
            backendBoard.updateTile(promotionPosition, piece);
            onScreenBoard.updateTile(promotionPosition, piece.getPieceType(), piece.getPieceColor());
        }
    }

    private void makeEnemyMove() {
        move(enemy.getMove(), enemy);
    }

    private boolean isLocalPlayer(Player player) {
        return this.player.equals(player);
    }

    private void changeCurrentPlayer() {
        if(currentPlayer == player) {
            currentPlayer = enemy;
        } else {
            currentPlayer = player;
        }
    }

}
