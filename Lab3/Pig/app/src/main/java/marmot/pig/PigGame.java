package marmot.pig;

/**
 * Created by marmot on 6/27/2017.
 */

public class PigGame {
    // Game Information
    private String player1Name;
    private String player2Name;
    private int player1Score;
    private int player2Score;
    private int currentTurn;
    private int currentScore;
    private boolean finalTurn;

    // Default Constants
    private final int[] WINNING_SCORES = {50, 100, 200, 500};

    // Preferences
    private int winningScore = 1;
    private boolean useAI = false;

    public PigGame() {
        player1Name = "";
        player2Name = "";

        player1Score = 0;
        player2Score = 0;

        currentTurn = 1;
        currentScore = 0;

        finalTurn = false;
    }

    public int rollDie() {
        int roll = (int)(Math.random() * 6 + 1);

        if (roll == 1) {
            currentScore = 0;
        } else {
            currentScore += roll;
        }

        return roll;
    }

    public int endTurn() {
        int endingTurn = currentTurn;

        if (currentTurn == 1) {
            player1Score += currentScore;
            if (player1Score >= WINNING_SCORES[winningScore]) {
                finalTurn = true;
            }
        } else {
            player2Score += currentScore;
            if (player2Score >= WINNING_SCORES[winningScore]) {
                finalTurn = true;
            }
        }

        currentScore = 0;

        currentTurn %= 2;
        currentTurn += 1;

        return endingTurn;
    }

    public void newGame() {
        player1Score = 0;
        player2Score = 0;
        currentScore = 0;
        currentTurn = 1;
        finalTurn = false;
    }

    // Getters and Setters

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public boolean isFinalTurn() {
        return finalTurn;
    }

    public void setFinalTurn(boolean finalTurn) {
        this.finalTurn = finalTurn;
    }

    public int getWinningScore() {
        return winningScore;
    }

    public void setWinningScore(int winningScore) {
        this.winningScore = winningScore;
    }

    public boolean isUseAI() {
        return useAI;
    }

    public void setUseAI(boolean useAI) {
        this.useAI = useAI;
    }
}