package game;

import entities.*;
import java.util.Iterator;
import java.util.List;
import utils.Coordinate;

public class LevelLoader {
    private Loader gameLoader;
    private int currentLevelIndex;
    private long levelStartTime;
    private boolean levelCompleted;
    private boolean gameWon;  // Novo campo para controle de vitória
    
    // Variáveis para controle do esquadrão de inimigos
    private boolean isSpawningEnemy2Squad;
    private int enemy2SquadCount;
    private long nextEnemy2SquadSpawnTime;
    private double enemy2SquadSpawnX;

    public LevelLoader(Loader gameLoader) {
        this.gameLoader = gameLoader;
        this.currentLevelIndex = 0;
        this.levelCompleted = false;
        this.gameWon = false;  // Inicializa como false
        resetSquadState();
    }

    public void startLevel(int levelIndex, long currentTime) {
        this.currentLevelIndex = levelIndex;
        this.levelStartTime = currentTime;
        this.levelCompleted = false;
        resetSquadState();
    }

    private void resetSquadState() {
        isSpawningEnemy2Squad = false;
        enemy2SquadCount = 0;
        nextEnemy2SquadSpawnTime = 0;
        enemy2SquadSpawnX = 0;
    }

    public Boss processSpawnEvents(long currentTime, List<Enemy> enemies, Player player) {
        long levelTime = currentTime - levelStartTime;
        Level currentLevel = gameLoader.getLevels().get(currentLevelIndex);
        
        Iterator<SpawnEvent> iterator = currentLevel.getEvents().iterator();
        while (iterator.hasNext()) {
            SpawnEvent event = iterator.next();
            
            if (levelTime >= event.getTime()) {
                if (event.getType() == SpawnEvent.Type.INIMIGO) {
                    handleEnemySpawn(event, currentTime, enemies);
                } else if (event.getType() == SpawnEvent.Type.CHEFE) {
                    iterator.remove();
                    return createBoss(event, player);
                }
                iterator.remove();
            }
        }
        return null;
    }
    
    private void handleEnemySpawn(SpawnEvent event, long currentTime, List<Enemy> enemies) {
        if (event.getEntityType() == 1) {
            enemies.add(new Enemy1(
                new Coordinate(event.getX(), event.getY())
            ));
        } else if (event.getEntityType() == 2 && !isSpawningEnemy2Squad) {
            isSpawningEnemy2Squad = true;
            enemy2SquadCount = 0;
            enemy2SquadSpawnX = event.getX();
            nextEnemy2SquadSpawnTime = currentTime;
        }
    }

    private Boss createBoss(SpawnEvent event, Player player) {
        if (event.getEntityType() == 1) {
            return new Boss(
                new Coordinate(event.getX(), event.getY()),
                event.getHealth()
            );
        } else {
            return new Boss2(
                new Coordinate(event.getX(), event.getY()),
                event.getHealth(),
                player
            );
        }
    }

    public void handleEnemy2SquadSpawning(long currentTime, List<Enemy> enemies) {
        if (isSpawningEnemy2Squad && currentTime > nextEnemy2SquadSpawnTime) {
            if (enemy2SquadCount < 10) {
                enemies.add(new Enemy2(
                    new Coordinate(enemy2SquadSpawnX, -10.0)
                ));
                enemy2SquadCount++;
                nextEnemy2SquadSpawnTime = currentTime + 120;
            } else {
                isSpawningEnemy2Squad = false;
                enemy2SquadCount = 0;
            }
        }
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

    public void resetGame() {
        currentLevelIndex = 0;
        levelCompleted = false;
        gameWon = false;
        resetSquadState();
    }

    // Métodos existentes
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

    public boolean hasMoreLevels() {
        return currentLevelIndex < gameLoader.getLevels().size() - 1;
    }

    public void nextLevel() {
        if (hasMoreLevels()) {
            currentLevelIndex++;
        }
    }
}