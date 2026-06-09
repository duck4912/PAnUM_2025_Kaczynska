package com.example.kolko_krzyzyk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private TicTacToeGame game;
    private MatchStats stats;
    private Button[][] boardButtons = new Button[3][3];
    private TextView tvMatchStats, tvScores, tvTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvMatchStats = findViewById(R.id.tv_match_stats);
        tvScores = findViewById(R.id.tv_scores);
        tvTurn = findViewById(R.id.tv_turn);

        initBoard();

        if (savedInstanceState != null) {
            game = (TicTacToeGame) savedInstanceState.getSerializable("game");
            stats = (MatchStats) savedInstanceState.getSerializable("stats");
        } else {
            int totalGames = getIntent().getIntExtra("TOTAL_GAMES", 5);
            game = new TicTacToeGame();
            stats = new MatchStats(totalGames);
        }

        updateUI();

        findViewById(R.id.btn_reset_match).setOnClickListener(v -> {
            finish(); // Back to setup
        });
    }

    private void initBoard() {
        GridLayout grid = findViewById(R.id.grid_board);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "btn_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                boardButtons[i][j] = findViewById(resID);
                final int row = i;
                final int col = j;
                boardButtons[i][j].setOnClickListener(v -> handleMove(row, col));
            }
        }
    }

    private void handleMove(int row, int col) {
        if (game.makeMove(row, col)) {
            boardButtons[row][col].setText(game.getCurrentPlayer().toString());
            checkGameEnd();
        }
    }

    private void checkGameEnd() {
        TicTacToeGame.GameResult result = game.checkResult();
        if (result != TicTacToeGame.GameResult.ONGOING) {
            if (result == TicTacToeGame.GameResult.X_WINS) {
                stats.recordWinX();
                Toast.makeText(this, R.string.winner_x, Toast.LENGTH_SHORT).show();
            } else if (result == TicTacToeGame.GameResult.O_WINS) {
                stats.recordWinO();
                Toast.makeText(this, R.string.winner_o, Toast.LENGTH_SHORT).show();
            } else {
                stats.recordDraw();
                Toast.makeText(this, R.string.draw, Toast.LENGTH_SHORT).show();
            }

            if (stats.isMatchOver()) {
                Intent intent = new Intent(this, SummaryActivity.class);
                intent.putExtra("STATS", stats);
                startActivity(intent);
                finish();
            } else {
                game.reset();
                updateUI();
            }
        } else {
            game.switchPlayer();
            updateUI();
        }
    }

    private void updateUI() {
        tvMatchStats.setText(getString(R.string.games_played, stats.getGamesPlayed()) + " " +
                getString(R.string.games_remaining, stats.getGamesRemaining()));
        tvScores.setText(getString(R.string.player_x) + ": " + stats.getPointsX() + " | " +
                getString(R.string.player_o) + ": " + stats.getPointsO());
        tvTurn.setText("Kolej: Gracz " + game.getCurrentPlayer().toString());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TicTacToeGame.Player p = game.getPlayerAt(i, j);
                boardButtons[i][j].setText(p == TicTacToeGame.Player.NONE ? "" : p.toString());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", game);
        outState.putSerializable("stats", stats);
    }
}
