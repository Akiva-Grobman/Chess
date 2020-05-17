package com.akivaGrobman.Game.Client.GameManagers;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.*;
import com.akivaGrobman.Game.Client.Backend.Players.AI;
import com.akivaGrobman.Game.Client.Backend.Players.Player;
import com.akivaGrobman.Game.Client.Backend.Players.Positions;
import com.akivaGrobman.Game.Client.Frontend.GraphicBoard;
import com.akivaGrobman.Game.Client.Frontend.PawnPromotionWindow;
import java.awt.*;
import java.util.ArrayList;

import static com.akivaGrobman.Game.Client.Backend.GameRules.SpecialMoves.*;

public class SinglePlayerChessGame extends ChessGame {

    private final AI ai;

    public SinglePlayerChessGame(PieceColor playersColor) {
        backendBoard = new Board();
        moves = new ArrayList<>();
        player = new Player(playersColor);
        player.setContext(this);
        ai = new AI(getOtherColor(playersColor), this);
        if(playersColor == PieceColor.WHITE) {
            currentPlayer = player;
        } else {
            currentPlayer = ai;
        }
        onScreenBoard = new GraphicBoard(backendBoard, this);
        if(currentPlayer.equals(ai)) {
            ai.makeAMove(backendBoard);
        }
    }

    @Override
    public void tileClicked(Point tilePosition) {
        if(currentPlayer.equals(player)) {
            super.tileClicked(tilePosition);
        }
    }

    @Override
    public void move(Positions positions, Player player) {
        assert currentPlayer.equals(player);
        addMoveToMoveList(positions);
        updateBoards(positions);
        handleSpecialMoves(positions);
        changeCurrentPlayer();
        if(enemyKingIsInCheck(positions.getDestination())) {
            putKingInCheck(currentPlayer);
        }
        if(currentPlayer.equals(ai)) {
            ai.makeAMove(backendBoard);
        }
    }

    private void changeCurrentPlayer() {
        if(currentPlayer == player) {
            currentPlayer = ai;
        } else {
            currentPlayer = player;
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
            backendBoard.updateTile(promotionPosition, piece);
            onScreenBoard.updateTile(promotionPosition, piece.getPieceType(), piece.getPieceColor());
        }
    }

    private PieceColor getOtherColor(PieceColor playersColor) {
        return (playersColor == PieceColor.BLACK)? PieceColor.WHITE: PieceColor.BLACK;
    }

}
