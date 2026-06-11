package com.example.stoper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    private LinearLayout layoutStopwatch, layoutTimer;
    private TextView tvStopwatchTime, tvTimerTime;
    private EditText etTimerMinutes, etTimerSeconds;
    private Button btnStartStopwatch, btnStartTimer;

    // Stopwatch variables
    private long stopwatchStartTime = 0L;
    private long stopwatchPausedTime = 0L;
    private final Handler stopwatchHandler = new Handler(Looper.getMainLooper());
    private boolean isStopwatchRunning = false;

    // Timer variables
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timerTimeLeftInMillis = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        layoutStopwatch = findViewById(R.id.layoutStopwatch);
        layoutTimer = findViewById(R.id.layoutTimer);
        tvStopwatchTime = findViewById(R.id.tvStopwatchTime);
        tvTimerTime = findViewById(R.id.tvTimerTime);
        etTimerMinutes = findViewById(R.id.etTimerMinutes);
        etTimerSeconds = findViewById(R.id.etTimerSeconds);
        Button btnModeStopwatch = findViewById(R.id.btnModeStopwatch);
        Button btnModeTimer = findViewById(R.id.btnModeTimer);
        btnStartStopwatch = findViewById(R.id.btnStartStopwatch);
        Button btnResetStopwatch = findViewById(R.id.btnResetStopwatch);
        btnStartTimer = findViewById(R.id.btnStartTimer);
        Button btnResetTimer = findViewById(R.id.btnResetTimer);

        // Mode Switching
        btnModeStopwatch.setOnClickListener(v -> {
            layoutStopwatch.setVisibility(View.VISIBLE);
            layoutTimer.setVisibility(View.GONE);
        });

        btnModeTimer.setOnClickListener(v -> {
            layoutStopwatch.setVisibility(View.GONE);
            layoutTimer.setVisibility(View.VISIBLE);
        });

        // Stopwatch Logic
        btnStartStopwatch.setOnClickListener(v -> {
            if (isStopwatchRunning) {
                pauseStopwatch();
            } else {
                startStopwatch();
            }
        });

        btnResetStopwatch.setOnClickListener(v -> resetStopwatch());

        // Timer Logic
        btnStartTimer.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        btnResetTimer.setOnClickListener(v -> resetTimer());

        if (savedInstanceState != null) {
            stopwatchStartTime = savedInstanceState.getLong("stopwatchStartTime");
            stopwatchPausedTime = savedInstanceState.getLong("stopwatchPausedTime");
            isStopwatchRunning = savedInstanceState.getBoolean("isStopwatchRunning");
            timerTimeLeftInMillis = savedInstanceState.getLong("timerTimeLeftInMillis");
            isTimerRunning = savedInstanceState.getBoolean("isTimerRunning");

            if (isStopwatchRunning) {
                startStopwatch();
            } else {
                updateStopwatchText(stopwatchPausedTime);
                if (stopwatchPausedTime > 0) {
                    btnStartStopwatch.setText(R.string.resume);
                }
            }

            if (isTimerRunning) {
                startTimer();
            } else {
                updateTimerText();
                if (timerTimeLeftInMillis > 0) {
                    btnStartTimer.setText(R.string.resume);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("stopwatchStartTime", stopwatchStartTime);
        outState.putLong("stopwatchPausedTime", isStopwatchRunning ? System.currentTimeMillis() - stopwatchStartTime : stopwatchPausedTime);
        outState.putBoolean("isStopwatchRunning", isStopwatchRunning);
        outState.putLong("timerTimeLeftInMillis", timerTimeLeftInMillis);
        outState.putBoolean("isTimerRunning", isTimerRunning);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopwatchHandler.removeCallbacks(stopwatchRunnable);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // --- Stopwatch Methods ---
    private void startStopwatch() {
        stopwatchStartTime = System.currentTimeMillis() - stopwatchPausedTime;
        stopwatchHandler.postDelayed(stopwatchRunnable, 0);
        btnStartStopwatch.setText(R.string.pause);
        isStopwatchRunning = true;
    }

    private void pauseStopwatch() {
        stopwatchPausedTime = System.currentTimeMillis() - stopwatchStartTime;
        stopwatchHandler.removeCallbacks(stopwatchRunnable);
        btnStartStopwatch.setText(R.string.resume);
        isStopwatchRunning = false;
    }

    private void resetStopwatch() {
        stopwatchStartTime = 0L;
        stopwatchPausedTime = 0L;
        stopwatchHandler.removeCallbacks(stopwatchRunnable);
        tvStopwatchTime.setText("00:00:00.0");
        btnStartStopwatch.setText(R.string.start);
        isStopwatchRunning = false;
    }

    private final Runnable stopwatchRunnable = new Runnable() {
        @Override
        public void run() {
            long currentElapsed = System.currentTimeMillis() - stopwatchStartTime;
            updateStopwatchText(currentElapsed);
            stopwatchHandler.postDelayed(this, 100);
        }
    };

    private void updateStopwatchText(long timeInMillis) {
        int secs = (int) (timeInMillis / 1000);
        int mins = secs / 60;
        int hrs = mins / 60;
        secs = secs % 60;
        mins = mins % 60;
        int milliseconds = (int) (timeInMillis % 1000 / 100);
        tvStopwatchTime.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d.%d", hrs, mins, secs, milliseconds));
    }

    // --- Timer Methods ---
    private void startTimer() {
        if (!isTimerRunning && timerTimeLeftInMillis == 0) {
            String minInput = etTimerMinutes.getText().toString();
            String secInput = etTimerSeconds.getText().toString();

            if (minInput.isEmpty() && secInput.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_enter_time), Toast.LENGTH_SHORT).show();
                return;
            }

            long minutes = minInput.isEmpty() ? 0 : Long.parseLong(minInput);
            long seconds = secInput.isEmpty() ? 0 : Long.parseLong(secInput);

            timerTimeLeftInMillis = (minutes * 60 + seconds) * 1000;

            if (timerTimeLeftInMillis == 0) {
                Toast.makeText(this, getString(R.string.toast_enter_time), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        updateTimerText();

        countDownTimer = new CountDownTimer(timerTimeLeftInMillis, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTimeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                btnStartTimer.setText(R.string.start);
                timerTimeLeftInMillis = 0;
                tvTimerTime.setText("00:00");
                Toast.makeText(MainActivity.this, getString(R.string.toast_time_up), Toast.LENGTH_LONG).show();
            }
        }.start();

        btnStartTimer.setText(R.string.pause);
        isTimerRunning = true;
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        btnStartTimer.setText(R.string.resume);
        isTimerRunning = false;
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerTimeLeftInMillis = 0;
        isTimerRunning = false;
        btnStartTimer.setText(R.string.start);
        tvTimerTime.setText("00:00");
        etTimerMinutes.setText("");
        etTimerSeconds.setText("");
    }

    private void updateTimerText() {
        int minutes = (int) (timerTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (timerTimeLeftInMillis / 1000) % 60;
        tvTimerTime.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }
}
