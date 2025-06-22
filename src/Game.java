import entities.*;
import game.*;
import graphics.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import utils.Coordinate;
import utils.GameLib;
import utils.States;

public class Game {
    private static boolean gameOver = false;
    private static boolean levelCompleted = false;
    private static int currentLevelIndex = 0;
    private static long levelStartTime;
    private static Loader gameLoader;

    // --- Início: Variáveis de estado para o spawn do Inimigo 2 ---
    private static boolean isSpawningEnemy2Squad = false;
    private static int enemy2SquadCount = 0;
    private static long nextEnemy2SquadSpawnTime = 0;
    private static double enemy2SquadSpawnX = 0;
    // --- Fim: Variáveis de estado ---
    
    public static void main(String[] args) {
        boolean running = true;
        long currentTime = System.currentTimeMillis();
        long delta;

        try {
            // O caminho do arquivo foi ajustado para refletir a estrutura do projeto
            gameLoader = new Loader("EP/src/game/game_config.txt");
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
        startLevel(currentLevelIndex, currentTime);

        while (running) {
            delta = System.currentTimeMillis() - currentTime;
            currentTime = System.currentTimeMillis();

            if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) {
                running = false;
            }
            
            if (gameOver) {
                if (GameLib.iskeyPressed(GameLib.KEY_ENTER)) {
                    gameOver = false;
                    player = new Player(
                        new Coordinate(GameLib.WIDTH / 2.0, GameLib.HEIGHT * 0.90),
                        new Coordinate(0.25, 0.25),
                        gameLoader.getPlayerLife()        
                    );
                    playerProjectiles.clear();
                    enemyProjectiles.clear();
                    enemies.clear();
                    powerUps.clear();
                    boss = null;
                    currentLevelIndex = 0;
                    startLevel(currentLevelIndex, currentTime);
                }
                
                GameOverGraphics.drawGameOver();
                GameLib.display();
                continue;
            }
            
            if (levelCompleted) {
                currentLevelIndex++;
                if (currentLevelIndex < gameLoader.getLevels().size()) {
                    startLevel(currentLevelIndex, currentTime);
                    playerProjectiles.clear();
                    enemyProjectiles.clear();
                    enemies.clear();
                    powerUps.clear();
                    boss = null;
                    levelCompleted = false;
                } else {
                    GameLib.display();
                    continue;
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
                    levelCompleted = true;
                }
            }

            Boss newBoss = processSpawnEvents(currentTime, enemies, player);
            if (newBoss != null) {
                boss = newBoss;
            }

            // --- Adicionado: Chamada para o novo método de spawn ---
            handleEnemy2SquadSpawning(currentTime, enemies);
            // --- Fim da adição ---

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

    private static void startLevel(int levelIndex, long currentTime) {
        levelStartTime = currentTime;
        levelCompleted = false;

        // Reseta o estado do esquadrão ao iniciar um novo nível
        isSpawningEnemy2Squad = false;
        enemy2SquadCount = 0;

        Level currentLevel = gameLoader.getLevels().get(levelIndex);
        
        long messageStartTime = System.currentTimeMillis();
        long displayDuration = 2000;

        while (System.currentTimeMillis() - messageStartTime < displayDuration) {
            GameLib.clear();
            GameLib.setColor(Color.WHITE);
            GameLib.drawText("Iniciando Fase " + (levelIndex + 1), GameLib.WIDTH / 2 - 50, GameLib.HEIGHT / 2);
            GameLib.display();
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static Boss processSpawnEvents(long currentTime, List<Enemy> enemies, Player player) {
        long levelTime = currentTime - levelStartTime;
        Level currentLevel = gameLoader.getLevels().get(currentLevelIndex);
        
        Iterator<SpawnEvent> iterator = currentLevel.getEvents().iterator();
        while (iterator.hasNext()) {
            SpawnEvent event = iterator.next();
            
            if (levelTime >= event.getTime()) {
                if (event.getType() == SpawnEvent.Type.INIMIGO) {
                    if (event.getEntityType() == 1) {
                        enemies.add(new Enemy1(
                            new Coordinate(event.getX(), event.getY()),
                            new Coordinate(0.0, 0.20 + Math.random() * 0.15)
                        ));
                    } else if (event.getEntityType() == 2) {
                        // --- Modificado: Apenas inicia o processo de spawn do esquadrão ---
                        if (!isSpawningEnemy2Squad) { // Evita reativar se já estiver em andamento
                            isSpawningEnemy2Squad = true;
                            enemy2SquadCount = 0;
                            enemy2SquadSpawnX = event.getX();
                            nextEnemy2SquadSpawnTime = currentTime;
                        }
                    }
                } else if (event.getType() == SpawnEvent.Type.CHEFE) {
                    Boss boss;
                    if (event.getEntityType() == 1) {
                        boss = new Boss(
                            new Coordinate(event.getX(), event.getY()),
                            new Coordinate(0.1, 0.05),
                            30
                            
                        );
                    } else {
                        boss = new Boss2(
                            new Coordinate(event.getX(), event.getY()),
                            new Coordinate(0.1, 0.05),
                            20,
                            player
                        );
                    }
                    iterator.remove();
                    return boss;
                }
                iterator.remove();
            }
        }
        return null;
    }

    // --- Início: Novo método para controlar o spawn do esquadrão Inimigo 2 ---
    private static void handleEnemy2SquadSpawning(long currentTime, List<Enemy> enemies) {
        if (isSpawningEnemy2Squad && currentTime > nextEnemy2SquadSpawnTime) {
            if (enemy2SquadCount < 10) {
                enemies.add(new Enemy2(
                    new Coordinate(enemy2SquadSpawnX, -10.0),
                    new Coordinate(0.42, 0.42)
                ));
                enemy2SquadCount++;
                nextEnemy2SquadSpawnTime = currentTime + 120; // Próximo inimigo em 120ms
            } else {
                // Esquadrão completo, reseta o estado.
                isSpawningEnemy2Squad = false;
                enemy2SquadCount = 0;
                // A posição do *próximo* esquadrão será definida pelo próximo evento do arquivo.
            }
        }
    }
    // --- Fim: Novo método ---
}