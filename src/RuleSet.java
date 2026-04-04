public class RuleSet {

    // Base health
    private int baseHealth;
    private int maxBaseHealth;

    // Score tracking
    private int score;
    private int badPacketsStopped;
    private int goodPacketsBlocked;
    private int goodPacketsPassed;
    private int badPacketsPassed;

    // Win / lose settings
    private int scoreToWin;
    private boolean gameOver;
    private boolean playerWon;

    // Packet values
    private int stopBadPacketPoints;
    private int blockGoodPacketPenalty;
    private int badPacketDamage;

    public RuleSet() {
        maxBaseHealth = 5;
        baseHealth = maxBaseHealth;

        score = 0;
        badPacketsStopped = 0;
        goodPacketsBlocked = 0;
        goodPacketsPassed = 0;
        badPacketsPassed = 0;

        scoreToWin = 100;
        gameOver = false;
        playerWon = false;

        stopBadPacketPoints = 10;
        blockGoodPacketPenalty = 5;
        badPacketDamage = 1;
    }

    // Called when player correctly destroys a bad packet
    public void handleBadPacketStopped() {
        if (gameOver) return;

        badPacketsStopped++;
        score += stopBadPacketPoints;
        checkWinCondition();
    }

    // Called when a bad packet reaches the home base
    public void handleBadPacketReachedBase() {
        if (gameOver) return;

        badPacketsPassed++;
        baseHealth -= badPacketDamage;

        if (baseHealth <= 0) {
            baseHealth = 0;
            gameOver = true;
            playerWon = false;
        }
    }

    // Called when player incorrectly destroys a good packet
    public void handleGoodPacketBlocked() {
        if (gameOver) return;

        goodPacketsBlocked++;
        score -= blockGoodPacketPenalty;

        // optional: do not let score go below 0
        if (score < 0) {
            score = 0;
        }
    }

    // Called when a good packet safely passes
    public void handleGoodPacketPassed() {
        if (gameOver) return;

        goodPacketsPassed++;
    }

    // Check if player has won
    private void checkWinCondition() {
        if (score >= scoreToWin) {
            gameOver = true;
            playerWon = true;
        }
    }

    // Reset game values
    public void resetGame() {
        baseHealth = maxBaseHealth;
        score = 0;
        badPacketsStopped = 0;
        goodPacketsBlocked = 0;
        goodPacketsPassed = 0;
        badPacketsPassed = 0;
        gameOver = false;
        playerWon = false;
    }

    // Getters
    public int getBaseHealth() {
        return baseHealth;
    }

    public int getMaxBaseHealth() {
        return maxBaseHealth;
    }

    public int getScore() {
        return score;
    }

    public int getBadPacketsStopped() {
        return badPacketsStopped;
    }

    public int getGoodPacketsBlocked() {
        return goodPacketsBlocked;
    }

    public int getGoodPacketsPassed() {
        return goodPacketsPassed;
    }

    public int getBadPacketsPassed() {
        return badPacketsPassed;
    }

    public int getScoreToWin() {
        return scoreToWin;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean hasPlayerWon() {
        return playerWon;
    }

    // Setters if you want to customize difficulty
    public void setScoreToWin(int scoreToWin) {
        this.scoreToWin = scoreToWin;
    }

    public void setMaxBaseHealth(int maxBaseHealth) {
        this.maxBaseHealth = maxBaseHealth;
        this.baseHealth = maxBaseHealth;
    }

    public void setStopBadPacketPoints(int stopBadPacketPoints) {
        this.stopBadPacketPoints = stopBadPacketPoints;
    }

    public void setBlockGoodPacketPenalty(int blockGoodPacketPenalty) {
        this.blockGoodPacketPenalty = blockGoodPacketPenalty;
    }

    public void setBadPacketDamage(int badPacketDamage) {
        this.badPacketDamage = badPacketDamage;
    }

	public Object classify(Packet packet) {
		// TODO Auto-generated method stub
		return null;
	}
}
