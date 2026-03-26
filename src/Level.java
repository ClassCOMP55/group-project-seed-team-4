
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
}
