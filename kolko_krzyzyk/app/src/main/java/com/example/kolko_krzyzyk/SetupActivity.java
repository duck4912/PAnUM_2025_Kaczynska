package com.example.kolko_krzyzyk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        EditText editNumGames = findViewById(R.id.edit_num_games);
        Button btnStart = findViewById(R.id.btn_start);

        btnStart.setOnClickListener(v -> {
            String input = editNumGames.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Podaj liczbę gier", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int numGames = Integer.parseInt(input);
                if (numGames <= 0) {
                    Toast.makeText(this, "Liczba gier musi być większa od 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("TOTAL_GAMES", numGames);
                startActivity(intent);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Niepoprawna liczba", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
