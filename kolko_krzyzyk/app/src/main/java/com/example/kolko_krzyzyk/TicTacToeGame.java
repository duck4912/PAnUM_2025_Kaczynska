package com.example.kolko_krzyzyk;

import java.io.Serializable;

public class TicTacToeGame implements Serializable {
    public enum Player { X, O, NONE }
    public enum GameResult { X_WINS, O_WINS, DRAW, ONGOING }

    private Player[][] board;
    private Player currentPlayer;
    private int movesCount;

    public TicTacToeGame() {
        board = new Player[3][3];
        reset();
    }

    public void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = Player.NONE;
            }
        }
        currentPlayer = Player.X;
        movesCount = 0;
    }

    public boolean makeMove(int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != Player.NONE) {
            return false;
        }
        board[row][col] = currentPlayer;
        movesCount++;
        return true;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getPlayerAt(int row, int col) {
        return board[row][col];
    }

    public GameResult checkResult() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != Player.NONE && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return (board[i][0] == Player.X) ? GameResult.X_WINS : GameResult.O_WINS;
            }
        }
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != Player.NONE && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return (board[0][j] == Player.X) ? GameResult.X_WINS : GameResult.O_WINS;
            }
        }
        // Check diagonals
        if (board[0][0] != Player.NONE && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return (board[0][0] == Player.X) ? GameResult.X_WINS : GameResult.O_WINS;
        }
        if (board[0][2] != Player.NONE && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return (board[0][2] == Player.X) ? GameResult.X_WINS : GameResult.O_WINS;
        }

        if (movesCount == 9) {
            return GameResult.DRAW;
        }

        return GameResult.ONGOING;
    }
}
