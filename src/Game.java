import entities.*;
import game.*;
import graphics.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.Coordinate;
import utils.GameLib;
import utils.States;

public class Game {
    private static boolean gameOver = false;
    private static Loader gameLoader;
    private static LevelLoader levelLoader;

    public static void main(String[] args) {
        boolean running = true;
        long currentTime = System.currentTimeMillis();
        long delta;

        try {
            gameLoader = new Loader("EP/src/game/game_config.txt");
            levelLoader = new LevelLoader(gameLoader);
        } catch (IOException e) {
            System.err.println("Erro ao carregar configuração do jogo: " + e.getMessage());
            return;
        }

        Player player = new Player(
            new Coordinate(GameLib.WIDTH / 2.0, GameLib.HEIGHT * 0.90),
            new Coordinate(0.25, 0.25),
            gameLoader.getPlayerLife()        
        );

        List<Projectiles> playerProjectiles = new ArrayList<>();
        List<Projectiles> enemyProjectiles = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        List<PowerUp> powerUps = new ArrayList<>();
        
        Boss boss = null;

        BackGroundGraphics background1 = new BackGroundGraphics(0.070, 20);
        BackGroundGraphics background2 = new BackGroundGraphics(0.045, 50);

        GameLib.initGraphics();
        levelLoader.startLevel(levelLoader.getCurrentLevelIndex(), currentTime);

        while (running) {
            delta = System.currentTimeMillis() - currentTime;
            currentTime = System.currentTimeMillis();

            if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) {
                running = false;
            }
            
            if (gameOver) {
                if (GameLib.iskeyPressed(GameLib.KEY_ENTER)) {
                    resetGame(player, playerProjectiles, enemyProjectiles, enemies, powerUps);
                }
                
                GameOverGraphics.drawGameOver();
                GameLib.display();
                continue;
            }
            
            if (levelLoader.isGameWon()) {
                if (GameLib.iskeyPressed(GameLib.KEY_ENTER)) {
                    resetGame(player, playerProjectiles, enemyProjectiles, enemies, powerUps);
                }
                
                VictoryGraphics.drawVictory();
                GameLib.display();
                continue;
            }

            if (levelLoader.isLevelCompleted()) {
                if (levelLoader.hasMoreLevels()) {
                    levelLoader.nextLevel();
                    levelLoader.startLevel(levelLoader.getCurrentLevelIndex(), currentTime);
                    playerProjectiles.clear();
                    enemyProjectiles.clear();
                    enemies.clear();
                    powerUps.clear();
                    boss = null;
                }
            }

            player.update(delta);
            player.shoot(currentTime, playerProjectiles);
            
            if (player.getHealth() <= 0) {
                gameOver = true;
                player.setState(States.INACTIVE);
                continue;
            }

            for (Projectiles projectile : playerProjectiles) projectile.update(delta);
            for (Projectiles projectile : enemyProjectiles) projectile.update(delta);
            for (PowerUp pw : powerUps) pw.update(delta);
            
            for (Enemy enemy : enemies) {
                enemy.update(delta);
                enemy.shoot(currentTime, enemyProjectiles);
            }

            if (boss != null) {
                boss.update(delta);
                boss.shoot(currentTime, enemyProjectiles);
                
                if (boss.getHealth() <= 0) {
                    boss.explosion(currentTime);
                    levelLoader.setLevelCompleted(true);
                }
            }

            Boss newBoss = levelLoader.processSpawnEvents(currentTime, enemies, player);
            if (newBoss != null) {
                boss = newBoss;
            }

            levelLoader.handleEnemy2SquadSpawning(currentTime, enemies);

            for (Projectiles p : playerProjectiles) {
                for (Enemy e : enemies) {
                    if (Collision.VerifyColision(p, e)) {
                        e.explosion(currentTime);
                        PowerUp pw = e.maybeSpawnPowerUp(e.getX(), e.getY());
                        if (pw != null) {
                            powerUps.add(pw);
                        }
                        p.setState(States.INACTIVE);
                    }
                }
                
                if (boss != null && boss.getState() == States.ACTIVE && Collision.VerifyColision(p, boss)) {
                    boss.takeDamage(p.getDamage());
                    p.setState(States.INACTIVE);
                }
            }

            for (PowerUp pw : powerUps) {
                if (pw instanceof ShrinkPowerUp) {
                    ((ShrinkPowerUp) pw).update(player);
                }
                if (Collision.VerifyColision(pw, player)) {
                    pw.onCollected(player);
                }
            }

            if (player.getState() == States.ACTIVE) {
                for (Projectiles p : enemyProjectiles) {
                    if (Collision.VerifyColision(p, player)) {
                        player.takeDamage(1);
                        p.setState(States.INACTIVE);
                    }
                }
                for (Enemy e : enemies) {
                    if (Collision.VerifyColision(e, player)) {
                        player.takeDamage(1);
                    }
                }
                if (boss != null && boss.getState() == States.ACTIVE && Collision.VerifyColision(boss, player)) {
                    player.takeDamage(1);
                }
            }

            playerProjectiles.removeIf(p -> p.getState() == States.INACTIVE);
            enemyProjectiles.removeIf(p -> p.getState() == States.INACTIVE);
            enemies.removeIf(e -> e.getState() == States.INACTIVE);
            powerUps.removeIf(e -> e.getState() == States.INACTIVE);

            background2.setColor(Color.DARK_GRAY);
            background2.fillBakcGround(delta);
            background1.setColor(Color.GRAY);
            background1.fillBakcGround(delta);

            PlayerGraphics.draw(player, Color.BLUE, currentTime);
            
            ProjectileGraphics.projectiles(playerProjectiles, Color.GREEN);
            ProjectileGraphics.ballProjectiles(enemyProjectiles, Color.RED, 2.0);
            EnemyGraphics.enemy(currentTime, enemies, Color.CYAN, 9.0);
            PowerUpGraphics.drawPowerUps(currentTime, powerUps, 5.0);
            
            if (boss != null) {
                if (boss instanceof Boss2) {
                    Boss2Graphics.drawBoss2((Boss2) boss, currentTime);
                } else {
                    BossGraphics.boss(boss, currentTime);
                }
            }

            GameLib.display();

            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    private static void resetGame(Player player, List<Projectiles> playerProjectiles, 
                                List<Projectiles> enemyProjectiles, List<Enemy> enemies, 
                                List<PowerUp> powerUps) {
        gameOver = false;
        levelLoader.resetGame();
        player.reset(
            new Coordinate(GameLib.WIDTH / 2.0, GameLib.HEIGHT * 0.90),
            new Coordinate(0.25, 0.25),
            gameLoader.getPlayerLife()
        );
        playerProjectiles.clear();
        enemyProjectiles.clear();
        enemies.clear();
        powerUps.clear();
        levelLoader.startLevel(levelLoader.getCurrentLevelIndex(), System.currentTimeMillis());
    }
}