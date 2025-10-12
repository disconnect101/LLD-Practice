package org.solve;

public class Board {

    Piece [][] grid;
    int size;

    public Board(int size) {
        grid = new Piece[size][size];
        this.size = size;
    }

    boolean update(int x, int y, Piece piece) {
        if(!validate(x, y)) return false;
        grid[size-x][y-1] = piece;
        return true;
    }

    boolean checkWin(int x, int y) {
        PieceType pieceType;
        if (grid[size-x][y-1] == null) return false;
        pieceType = grid[size-x][y-1].getPieceType();

        return checkRowCol(x, y, pieceType) || checkDiagnols(pieceType);
    }

    private boolean checkRowCol(int x, int y, PieceType pieceType) {
        int i, j;
        boolean result = true;
        for (j=0 ; j<size ; j++) if (grid[size-x][j]==null || grid[size-x][j].getPieceType() != pieceType) {
            result = false;
            break;
        }
        if (result) return true;

        for (i=0, result = true ; i<size ; i++) if (grid[i][y-1]==null || grid[i][y-1].getPieceType() != pieceType) {
            result = false;
            break;
        }
        return result;
    }

    boolean checkDiagnols(PieceType pieceType) {
        int i, j;

        boolean result = true;

        for (i=0, j=0 ; i<size&&j<size ; i++,j++) {
            if (grid[i][j]==null || grid[i][j].getPieceType() != pieceType) {
                result = false;
                break;
            }
        }
        if (result) return true;

        for (i=size-1, j=0, result = true ; i>=0&&j<size ; i--,j++) {
            if (grid[i][j]==null || grid[i][j].getPieceType() != pieceType) {
                result = false;
                break;
            }
        }
        return result;
    }

    private boolean validate(int x, int y) {
        return x <= size && y <= size && x >= 1 && y >= 1 && grid[size - x][y - 1] == null;
    }

    public void printBoard() {
        int i, j;

        for (i=0 ; i<size ; i++) {
            for (j=0 ; j<size ; j++) {
                if (grid[i][j]==null) System.out.print("  |");
                else System.out.print(grid[i][j].getPieceType() + " |");
            }
            System.out.println();
        }
    }
}
