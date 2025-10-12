package org.solve;

import java.util.Scanner;

public class TicTacToeGame {

    private Player [] players;
    private int cursor;
    private Board board;

    public TicTacToeGame(int size, Player []players) {
        this.players = players;
        this.board = new Board(size);
        this.cursor = 0;
    }

    public Player startGame() {
        boolean live = true;
        Player currentPlayer = null;

        while (live) {
            currentPlayer = players[cursor];
            System.out.println(currentPlayer.getName() + "'s turn");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Coordinates: ");
            String move = scanner.nextLine();

            int x, y;
            try {
                String[] arr = move.split(",");
                x = Integer.parseInt(arr[0]);
                y = Integer.parseInt(arr[1]);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Invalid move, re-enter");
                continue;
            }


            if (!board.update(x, y, currentPlayer.getPiece())) {
                System.out.println("invalid Move, try again");
                continue;
            }

            if (board.checkWin(x, y)) {
                board.printBoard();
                break;
            }

            cursor = (cursor + 1) % 2;

            board.printBoard();
        }

        return currentPlayer;
    }


}
