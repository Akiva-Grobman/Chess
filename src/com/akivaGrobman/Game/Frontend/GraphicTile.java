package com.akivaGrobman.Game.Frontend;

import com.akivaGrobman.Game.Backend.GameObjects.Pieces.PieceColor;
import com.akivaGrobman.Game.Backend.GameObjects.Pieces.PieceType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GraphicTile extends JPanel implements MouseListener {

    private BufferedImage image;
    private Point tilePosition;
    private Color tileColor;
    private PieceType pieceType;
    private PieceColor pieceColor;
    private GraphicBoard board;

    public GraphicTile(Point position, Color color, GraphicBoard graphicBoard) {
        tilePosition = position;
        tileColor = color;
        board = graphicBoard;
        image = null;
        panelSetUp();
    }

    public void update(PieceType pieceType, PieceColor pieceColor) {
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
        image = getPieceImage();
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(tileColor);
        if(hasPiece())
            g.drawImage(image, 10, 10, GraphicBoard.TILE_SIZE - 20, GraphicBoard.TILE_SIZE - 20, this);
    }

    private void panelSetUp() {
        this.setPreferredSize(new Dimension(GraphicBoard.TILE_SIZE, GraphicBoard.TILE_SIZE));
        this.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        this.addMouseListener(this);
    }

    private boolean hasPiece() {
        return image != null;
    }

    private BufferedImage getPieceImage() {
        try {
            return  getImageFromFileName("Images/" + pieceColor.toString().toLowerCase() + "_" + pieceType.toString().toLowerCase() + ".png");
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }

    private BufferedImage getImageFromFileName(String fileName) throws IOException {
        return ImageIO.read(getClass().getResource(fileName));
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        board.tileClicked(tilePosition);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

}
