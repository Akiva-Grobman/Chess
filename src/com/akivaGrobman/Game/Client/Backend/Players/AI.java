package com.akivaGrobman.Game.Client.Backend.Players;

import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Piece;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.akivaGrobman.Game.Client.GameManagers.Parent.SUM_OF_COLUMNS;
import static com.akivaGrobman.Game.Client.GameManagers.Parent.SUM_OF_ROWS;

public class AI implements ChessPlayer{

    private final PieceColor aiColor;
    private final int MAX_DEPTH;

    public AI(PieceColor color) {
        this.aiColor = color;
        MAX_DEPTH = 4;
    }

    public Positions getMove(Board board) {
        return getBestMoveForAI(Board.getClone(board));
    }

    private Positions getBestMoveForAI(Board board) {
        List<Point> piecePositions = new ArrayList<>();
        List<Piece> pieces = getPieces(board, aiColor, piecePositions);
        int highestScore = Integer.MIN_VALUE;
        Point origin = null;
        Point destination = null;

        for (int i = 0; i < pieces.size(); i++) {
            Point tempOrigin = piecePositions.get(i);
            for (Point tempDestination: pieces.get(i).getLegalMoves(board, tempOrigin)) {
                board.updateTile(tempOrigin, null);
                board.updateTile(tempDestination, pieces.get(i));
                int score = getMinMax(board, aiColor, 1);
                board.updateTile(tempOrigin, pieces.get(i));
                board.updateTile(tempDestination, null);
                if(highestScore < score) {
                    highestScore = score;
                    origin = new Point(tempOrigin);
                    destination = new Point(tempDestination);
                }
            }
        }

        Positions bestMove = new Positions(origin, aiColor);
        bestMove.setDestination(destination);
        return bestMove;
    }

    private int getMinMax(Board board, PieceColor playersColor, int depth) {
        if(depth >= MAX_DEPTH) {
            return getBoardScore(board);
        }
        if(playersColor == aiColor) {
            return getMax(board, getOtherPlayersColor(playersColor), depth);
        } else {
            return getMin(board, getOtherPlayersColor(playersColor), depth);
        }
    }

    private int getMin(Board board, PieceColor playersColor, int depth) {
        int min = Integer.MAX_VALUE;
        List<Point> piecesPositions = new ArrayList<>(); // the positions will be added while finding the pieces
        List<Piece> pieces = getPieces(board, playersColor, piecesPositions);
        for (int i = 0; i < pieces.size(); i++) {
            for (Point destination : pieces.get(i).getLegalMoves(board, piecesPositions.get(i))) {
                board.updateTile(piecesPositions.get(i), null);
                board.updateTile(destination, pieces.get(i));
                int score = getMinMax(board, playersColor, depth + 1);
                min = Integer.min(min, score);
                board.updateTile(piecesPositions.get(i), pieces.get(i));
                board.updateTile(destination, null);
            }
        }
        return min;
    }

    private int getMax(Board board, PieceColor playersColor, int depth) {
        int max = Integer.MIN_VALUE;
        List<Point> piecesPositions = new ArrayList<>(); // the positions will be added while finding the pieces
        List<Piece> pieces = getPieces(board, playersColor, piecesPositions);
        for (int i = 0; i < pieces.size(); i++) {
            for (Point destination : pieces.get(i).getLegalMoves(board, piecesPositions.get(i))) {
                board.updateTile(piecesPositions.get(i), null);
                board.updateTile(destination, pieces.get(i));
                int score = getMinMax(board, playersColor, depth + 1);
                max = Integer.max(max, score);
                board.updateTile(piecesPositions.get(i), pieces.get(i));
                board.updateTile(destination, null);
            }
        }
        return max;
    }

    private int getBoardScore(Board board) {
        List<Point> playersPositions = new ArrayList<>();
        List<Piece> playersPieces = getPieces(board, getOtherPlayersColor(aiColor), playersPositions);
        List<Point> aiPositions = new ArrayList<>();
        List<Piece> aiPieces = getPieces(board, aiColor, aiPositions);
        int score = getPiecesScore(playersPieces, aiPieces);
        //todo add this scoring method
        score += getScoreByPosition();
        return score;
    }

    private int getScoreByPosition() {
        return 0;
    }

    private List<Piece> getPieces(Board board, PieceColor currentPlayersColor, List<Point> positions) {
        final int MAX_PIECES = 16;
        int pieceCount = 0;
        List<Piece> pieces = new ArrayList<>();
        for (int y = 0; y < SUM_OF_ROWS; y++) {
            for (int x = 0; x < SUM_OF_COLUMNS; x++) {
                if(pieceCount >= MAX_PIECES) return pieces;
                try {
                    Piece piece = board.getPiece(new Point(x, y));
                    if(currentPlayersColor != piece.getPieceColor()) {
                        pieceCount++;
                        pieces.add(piece);
                        positions.add(new Point(x, y));

                    }
                } catch (NoPieceFoundException ignored) {}
            }
        }
        return pieces;
    }

    private int getPiecesScore(List<Piece> currentPlayersColor, List<Piece> aiPieces) {
        int score = 0;
        for (Piece piece : aiPieces) {
            switch (piece.getPieceType()) {
                    case PAWN:
                        score += 1;
                        break;
                    case ROOK:
                        score += 5;
                        break;
                    case KNIGHT:
                    case BISHOP:
                        score += 3;
                        break;
                    case KING:
                        score += 100;
                        break;
                    case QUEEN:
                        score += 9;
                        break;
            }
        }
        for (Piece piece : currentPlayersColor) {
            switch (piece.getPieceType()) {
                case PAWN:
                    score -= 1;
                    break;
                case ROOK:
                    score -= 5;
                    break;
                case KNIGHT:
                case BISHOP:
                    score -= 3;
                    break;
                case KING:
                    score -= 100;
                    break;
                case QUEEN:
                    score -= 9;
                    break;
            }
        }
        return score;
    }

    private PieceColor getOtherPlayersColor(PieceColor playersColor) {
        return (playersColor == PieceColor.BLACK)? PieceColor.WHITE: PieceColor.BLACK;
    }

}
