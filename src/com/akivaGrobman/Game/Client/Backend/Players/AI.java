package com.akivaGrobman.Game.Client.Backend.Players;

import com.akivaGrobman.Game.Client.Backend.Exceptions.NoPieceFoundException;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Board.Board;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.King;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.Piece;
import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Client.GameManagers.ChessGame;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.akivaGrobman.Game.Client.GameManagers.ChessGame.SUM_OF_COLUMNS;
import static com.akivaGrobman.Game.Client.GameManagers.ChessGame.SUM_OF_ROWS;

public class AI extends Player {

    private final int MAX_DEPTH;

    public AI(PieceColor color, ChessGame chessGame) {
        super(color);
        MAX_DEPTH = 4;
        setContext(chessGame);
    }

    public void makeAMove(Board board) {
        game.move(getMove(board), this);
    }

    private Positions getMove(Board board) {
        return getBestMoveForAi(Board.getClone(board));
    }

    private Positions getBestMoveForAi(Board board) {
        List<Point> piecePositions = new ArrayList<>();
        List<Piece> pieces = getPieces(board, getPlayersColor(), piecePositions);
        int highestScore = Integer.MIN_VALUE;
        Point origin = null;
        Point destination = null;
        for (int i = 0; i < pieces.size(); i++) {
            Point tempOrigin = piecePositions.get(i);
            for (Point tempDestination: pieces.get(i).getLegalMoves(board, tempOrigin)) {
                Piece pieceAtDestination = getPiece(board, tempDestination);
                if(isEnemyKing(pieceAtDestination)) {
                    Positions bestMove = new Positions(origin, getPlayersColor());
                    bestMove.setDestination(tempDestination);
                    return bestMove;
                }
                board.updateTile(tempOrigin, null);
                board.updateTile(tempDestination, pieces.get(i));
                int score = getMinMax(board, getPlayersColor(), 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
//                System.out.println(tempOrigin + " -> " + tempDestination + " score:" + score);
                board.updateTile(tempOrigin, pieces.get(i));
                board.updateTile(tempDestination, pieceAtDestination);
                if(highestScore < score) {
                    highestScore = score;
                    origin = new Point(tempOrigin);
                    destination = new Point(tempDestination);
                }
            }
        }
//        System.out.println("---------------------------------------------------");
        Positions bestMove = new Positions(origin, getPlayersColor());
        bestMove.setDestination(destination);
        return bestMove;
    }

    private boolean isEnemyKing(Piece piece) {
        if (piece instanceof King) {
            return piece.getPieceColor() != getPlayersColor();
        }
        return false;
    }

    private int getMinMax(Board board, PieceColor playersColor, int depth, int alpha, int beta) {
        if(depth == MAX_DEPTH) {
            return getBoardScore(board);
        }
        if(playersColor == getPlayersColor()) {
            return getMax(board, getOtherPlayersColor(playersColor), depth, alpha, beta);
        } else {
            return getMin(board, getOtherPlayersColor(playersColor), depth, alpha, beta);
        }
    }

    private int getMin(Board board, PieceColor playersColor, int depth, int alpha, int beta) {
        int min = Integer.MAX_VALUE;
        List<Point> piecesPositions = new ArrayList<>(); // the positions will be added while finding the pieces
        List<Piece> pieces = getPieces(board, playersColor, piecesPositions);
        for (int i = 0; i < pieces.size(); i++) {
            for (Point destination : pieces.get(i).getLegalMoves(board, piecesPositions.get(i))) {
                Piece pieceAtDestination = getPiece(board, destination);
                if(isEnemyKing(pieceAtDestination)) return Integer.MIN_VALUE;
                board.updateTile(piecesPositions.get(i), null);
                board.updateTile(destination, pieces.get(i));
                int score = getMinMax(board, playersColor, depth + 1, alpha, beta);
                min = Integer.min(min, score);
                beta = Integer.min(beta, min);
                if(alpha >= beta) return min;
                board.updateTile(piecesPositions.get(i), pieces.get(i));
                board.updateTile(destination, pieceAtDestination);
            }
        }
        return min;
    }

    private int getMax(Board board, PieceColor playersColor, int depth, int alpha, int beta) {
        int max = Integer.MIN_VALUE;
        List<Point> piecesPositions = new ArrayList<>(); // the positions will be added while finding the pieces
        List<Piece> pieces = getPieces(board, playersColor, piecesPositions);
        for (int i = 0; i < pieces.size(); i++) {
            for (Point destination : pieces.get(i).getLegalMoves(board, piecesPositions.get(i))) {
                Piece pieceAtDestination = getPiece(board, destination);
                if(isEnemyKing(pieceAtDestination)) return Integer.MAX_VALUE;
                board.updateTile(piecesPositions.get(i), null);
                board.updateTile(destination, pieces.get(i));
                int score = getMinMax(board, playersColor, depth + 1, alpha, beta);
                max = Integer.max(max, score);
                alpha = Integer.max(alpha, max);
                if(alpha >= beta) return max;
                board.updateTile(piecesPositions.get(i), pieces.get(i));
                board.updateTile(destination, pieceAtDestination);
            }
        }
        return max;
    }

    private Piece getPiece(Board board, Point position) {
        try {
            return board.getPiece(position);
        } catch (NoPieceFoundException e) {
            return null;
        }
    }

    private int getBoardScore(Board board) {
        List<Point> playersPositions = new ArrayList<>();
        List<Piece> playersPieces = getPieces(board, getOtherPlayersColor(getPlayersColor()), playersPositions);
        List<Point> aiPositions = new ArrayList<>();
        List<Piece> aiPieces = getPieces(board, getPlayersColor(), aiPositions);
        //todo improve scoring method
        return getPiecesScore(playersPieces, aiPieces);
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
                    if(currentPlayersColor == piece.getPieceColor()) {
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

    @Override
    public void addPositionToMove(Point position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Positions getMove() {
        throw new UnsupportedOperationException();
    }

}
