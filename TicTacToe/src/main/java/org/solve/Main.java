package org.solve;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Player []players = new Player[2];
        players[0] = new Player("Aditya", new Piece(PieceType.X));
        players[1] = new Player("Vaibhav", new Piece(PieceType.O));

        TicTacToeGame game = new TicTacToeGame(3, players);
        Player winner = game.startGame();

        System.out.println("The winner is " + winner.getName());
    }
}