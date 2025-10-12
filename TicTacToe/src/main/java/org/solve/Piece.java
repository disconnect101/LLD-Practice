package org.solve;

public class Piece {

    private PieceType pieceType;

    public Piece(PieceType pieceType) {
        this.pieceType = pieceType;
    }
    public PieceType getPieceType() {
        return this.pieceType;
    }
}
