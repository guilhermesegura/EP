package game;


public class LevelLoader {
    private Loader gameLoader;
    private int currentLevelIndex;
    private long levelStartTime;
    private boolean levelCompleted;
    private boolean gameWon; 
    
    public LevelLoader(Loader gameLoader) {
        this.gameLoader = gameLoader;
        this.currentLevelIndex = 0;
        this.levelCompleted = false;
        this.gameWon = false;  
    }

    public void startLevel(int levelIndex, long currentTime) {
        this.currentLevelIndex = levelIndex;
        this.levelStartTime = currentTime;
        this.levelCompleted = false;
    }

    
    // Métodos para controle de vitória
    public boolean isGameWon() {
        return gameWon;
    }

    public void checkVictoryCondition() {
        if (isLevelCompleted() && !hasMoreLevels()) {
            gameWon = true;
        }
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.levelCompleted = levelCompleted;
        if (levelCompleted) {
            checkVictoryCondition();
        }
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

     public long getLevelStartTime() {
        return levelStartTime;
    }

    public boolean hasMoreLevels() {
        return currentLevelIndex < gameLoader.getLevels().size() - 1;
    }

    public void nextLevel() {
        if (hasMoreLevels()) {
            currentLevelIndex++;
        }
    }
}