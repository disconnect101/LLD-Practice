package org.solve;

public class Player {

    private String name;
    private Piece piece;

    public Player(String name, Piece piece) {
        this.name = name;
        this.piece = piece;
    }

    Piece getPiece() {
        return this.piece;
    }

    String getName() {
        return this.name;
    }

}
