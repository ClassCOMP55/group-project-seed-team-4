
public class Level {
	private int levelNumber;
    private int targetScore;
    private double spawnRate; 
    private boolean isComplete;
    
    public Level(int levelNumber, int targetScore, double spawnRate) {
    	this.levelNumber = levelNumber;
    	this.targetScore = targetScore;
    	this.spawnRate = spawnRate;
    	this.isComplete = false;
    }
    
    public int getLevelNumbers() {
    	return levelNumber;
    }
    
    public int getTargetScore() {
    	return targetScore;
    }
    
    public double getSpawnRate() {
    	return spawnRate;
    }
    
    public boolean getIsComplete() {
    	return isComplete;
    }
    
    public void checkCompletion(int currentScore) {
    	if (currentScore >= targetScore) {
    		isComplete = true;
    	}
    }
}
