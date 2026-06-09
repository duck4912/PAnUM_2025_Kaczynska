package com.example.kolko_krzyzyk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        MatchStats stats = (MatchStats) getIntent().getSerializableExtra("STATS");
        if (stats == null) {
            finish();
            return;
        }

        TextView tvWinsX = findViewById(R.id.tv_summary_wins_x);
        TextView tvWinsO = findViewById(R.id.tv_summary_wins_o);
        TextView tvDraws = findViewById(R.id.tv_summary_draws);
        TextView tvPoints = findViewById(R.id.tv_summary_points);
        TextView tvOverallWinner = findViewById(R.id.tv_overall_winner);
        Button btnNewMatch = findViewById(R.id.btn_new_match);

        tvWinsX.setText(getString(R.string.player_x) + " " + getString(R.string.total_wins, stats.getWinsX()));
        tvWinsO.setText(getString(R.string.player_o) + " " + getString(R.string.total_wins, stats.getWinsO()));
        tvDraws.setText(getString(R.string.total_draws, stats.getDraws()));
        
        tvPoints.setText(String.format(Locale.getDefault(), "Punkty: X: %.1f, O: %.1f", stats.getPointsX(), stats.getPointsO()));

        String winner;
        if (stats.getPointsX() > stats.getPointsO()) {
            winner = getString(R.string.player_x);
        } else if (stats.getPointsO() > stats.getPointsX()) {
            winner = getString(R.string.player_o);
        } else {
            winner = getString(R.string.draw);
        }
        tvOverallWinner.setText(getString(R.string.match_winner, winner));

        btnNewMatch.setOnClickListener(v -> {
            Intent intent = new Intent(this, SetupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
