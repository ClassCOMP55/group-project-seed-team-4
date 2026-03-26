import java.util.ArrayList;

public class LevelManager {
	private ArrayList<Level> levels;
    private int currentLevelIndex;

    public LevelManager() {
        levels = new ArrayList<>();
        currentLevelIndex = 0;

        levels.add(new Level(1, 100, 1.0)); 
        levels.add(new Level(2, 200, 1.5)); 
        levels.add(new Level(3, 300, 2.0)); 
    }

    public Level getCurrentLevel() {
        return levels.get(currentLevelIndex);
    }

    public void advanceLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
        }
    }

    public void resetLevels() {
        for (Level level : levels) {
            level.resetLevel();
        }
        currentLevelIndex = 0;
    }
}

