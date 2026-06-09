package com.example.kolko_krzyzyk;

import java.io.Serializable;

public class MatchStats implements Serializable {
    private int totalGamesToPlay;
    private int gamesPlayed;
    private int winsX;
    private int winsO;
    private int draws;
    private double pointsX;
    private double pointsO;

    public MatchStats(int totalGames) {
        this.totalGamesToPlay = totalGames;
        this.gamesPlayed = 0;
        this.winsX = 0;
        this.winsO = 0;
        this.draws = 0;
        this.pointsX = 0;
        this.pointsO = 0;
    }

    public void recordWinX() {
        winsX++;
        pointsX += 1.0;
        gamesPlayed++;
    }

    public void recordWinO() {
        winsO++;
        pointsO += 1.0;
        gamesPlayed++;
    }

    public void recordDraw() {
        draws++;
        pointsX += 0.5;
        pointsO += 0.5;
        gamesPlayed++;
    }

    public int getTotalGamesToPlay() { return totalGamesToPlay; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getGamesRemaining() { return totalGamesToPlay - gamesPlayed; }
    public int getWinsX() { return winsX; }
    public int getWinsO() { return winsO; }
    public int getDraws() { return draws; }
    public double getPointsX() { return pointsX; }
    public double getPointsO() { return pointsO; }
    public boolean isMatchOver() { return gamesPlayed >= totalGamesToPlay; }
}
