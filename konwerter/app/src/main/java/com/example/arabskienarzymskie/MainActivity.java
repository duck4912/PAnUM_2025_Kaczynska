package com.example.arabskienarzymskie;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etInput;
    private TextView tvOutput;
    private GridLayout keyboardArabic;
    private GridLayout keyboardRoman;
    private Button btnSwitchMode;

    private RomanConverter converter;
    private boolean isArabicToRoman = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInput = findViewById(R.id.etInput);
        etInput.setShowSoftInputOnFocus(false);
        tvOutput = findViewById(R.id.tvOutput);
        keyboardArabic = findViewById(R.id.keyboardArabic);
        keyboardRoman = findViewById(R.id.keyboardRoman);
        btnSwitchMode = findViewById(R.id.btnSwitchMode);

        converter = new RomanConverter();

        setupKeyboardButtons(keyboardArabic);
        setupKeyboardButtons(keyboardRoman);

        findViewById(R.id.btnBackArabic).setOnClickListener(v -> handleBackspace());
        findViewById(R.id.btnBackRoman).setOnClickListener(v -> handleBackspace());

        btnSwitchMode.setOnClickListener(v -> switchMode());

        updateUI();
    }

    private void setupKeyboardButtons(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof Button) {
                Button b = (Button) v;
                String text = b.getText().toString();
                if (!text.equals("DEL")) {
                    b.setOnClickListener(view -> handleInput(text));
                }
            }
        }
    }

    private void handleInput(String character) {
        String current = etInput.getText().toString();

        if (isArabicToRoman) {
            if (current.isEmpty() && character.equals("0")) return;
            
            String nextValueStr = current + character;
            try {
                long val = Long.parseLong(nextValueStr);
                if (val > 3999) {
                    tvOutput.setText("Zakres: 1-3999");
                    return; 
                } else {
                    etInput.setText(nextValueStr);
                    tvOutput.setText(converter.arabicToRoman((int) val));
                }
            } catch (NumberFormatException e) {
            }
        } else {
            String nextValue = current + character;
            if (converter.isValidRoman(nextValue)) {
                etInput.setText(nextValue);
                tvOutput.setText(String.valueOf(converter.romanToArabic(nextValue)));
            } else {
                tvOutput.setText("Błąd zapisu");
            }
        }
    }

    private void handleBackspace() {
        String current = etInput.getText().toString();
        if (!current.isEmpty()) {
            String next = current.substring(0, current.length() - 1);
            etInput.setText(next);
            
            if (next.isEmpty()) {
                tvOutput.setText("Wynik");
            } else {
                if (isArabicToRoman) {
                    tvOutput.setText(converter.arabicToRoman(Integer.parseInt(next)));
                } else {
                    if (converter.isValidRoman(next)) {
                        tvOutput.setText(String.valueOf(converter.romanToArabic(next)));
                    } else {
                        tvOutput.setText("Błąd zapisu");
                    }
                }
            }
        }
    }

    private void switchMode() {
        isArabicToRoman = !isArabicToRoman;
        etInput.setText("");
        tvOutput.setText("Wynik");
        updateUI();
    }

    private void updateUI() {
        if (isArabicToRoman) {
            keyboardArabic.setVisibility(View.VISIBLE);
            keyboardRoman.setVisibility(View.GONE);
            btnSwitchMode.setText("TRYB: ARABSKIE -> RZYMSKIE (KLIKNIJ BY ZMIENIĆ)");
            etInput.setHint("Wpisz liczbę arabską");
        } else {
            keyboardArabic.setVisibility(View.GONE);
            keyboardRoman.setVisibility(View.VISIBLE);
            btnSwitchMode.setText("TRYB: RZYMSKIE -> ARABSKIE (KLIKNIJ BY ZMIENIĆ)");
            etInput.setHint("Wpisz liczbę rzymską");
        }
    }
}
