import entities.*;
import graphics.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.Coordinate;
import utils.GameLib;
import utils.States;
import game.*;

public class Game {
    private static boolean gameOver = false;
    private static boolean levelCompleted = false;
    private static int currentLevelIndex = 0;
    private static long levelStartTime;
    private static Loader gameLoader;
    
    public static void main(String[] args) {
        // Game loop control
        boolean running = true;
        long currentTime = System.currentTimeMillis();
        long delta;

        try {
            // Carrega a configuração do jogo
            gameLoader = new Loader("EP/src/game/game_config.txt");
        } catch (IOException e) {
            System.err.println("Erro ao carregar configuração do jogo: " + e.getMessage());
            return;
        }

        // Player setup
        Player player = new Player(
            new Coordinate(GameLib.WIDTH / 2.0, GameLib.HEIGHT * 0.90),
            new Coordinate(0.25, 0.25)        
        );
        player.setHealth(gameLoader.getPlayerLife());

        // Entity lists
        List<Projectiles> playerProjectiles = new ArrayList<>();
        List<Projectiles> enemyProjectiles = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        List<PowerUp> powerUps = new ArrayList<>();
        
        // Boss object
        Boss boss = null;

        // Background graphics
        BackGroundGraphics background1 = new BackGroundGraphics(0.070, 20);
        BackGroundGraphics background2 = new BackGroundGraphics(0.045, 50);

        // Initialize graphics
        GameLib.initGraphics();

        // Inicia a primeira fase
        startLevel(currentLevelIndex, currentTime);

        // Main game loop
        while (running) {
            delta = System.currentTimeMillis() - currentTime;
            currentTime = System.currentTimeMillis();

            // 1. Handle user input
            if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) {
                running = false;
            }
            
            // Lógica de Game Over
            if (gameOver) {
                // Verifica se o jogador quer reiniciar
                if (GameLib.iskeyPressed(GameLib.KEY_ENTER)) {
                    gameOver = false;
                    // Reinicia o jogo
                    player = new Player(
                        new Coordinate(GameLib.WIDTH / 2.0, GameLib.HEIGHT * 0.90),
                        new Coordinate(0.25, 0.25)        
                    );
                    player.setHealth(gameLoader.getPlayerLife());
                    playerProjectiles.clear();
                    enemyProjectiles.clear();
                    enemies.clear();
                    powerUps.clear();
                    boss = null;
                    currentLevelIndex = 0;
                    startLevel(currentLevelIndex, currentTime);
                }
                
                // Mostra tela de Game Over
                GameOverGraphics.drawGameOver();
                GameLib.display();
                continue; // Pula o resto do loop
            }
            
            // Verifica se o nível foi completado
            if (levelCompleted) {
                currentLevelIndex++;
                if (currentLevelIndex < gameLoader.getLevels().size()) {
                    // Próxima fase
                    startLevel(currentLevelIndex, currentTime);
                    playerProjectiles.clear();
                    enemyProjectiles.clear();
                    enemies.clear();
                    powerUps.clear();
                    boss = null;
                    levelCompleted = false;
                } else {
                    // Fim do jogo (vitória)
                    GameLib.display();
                    continue;
                }
            }

            player.update(delta);
            player.shoot(currentTime, playerProjectiles);
            
            // Verifica se o jogador morreu
            if (player.getHealth() <= 0) {
                gameOver = true;
                player.setState(States.INACTIVE);
                continue;
            }

            // 2. Update entities
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
                
                // Verifica se o chefe foi derrotado
                if (boss.getHealth() <= 0) {
                    boss.explosion(currentTime);
                    levelCompleted = true;
                }
            }

            // 3. Processar eventos de spawn da fase atual
            processSpawnEvents(currentTime, enemies, boss, player);

            // 4. Check collisions
            for (Projectiles p : playerProjectiles) {
                for (Enemy e : enemies) {
                    if (Collision.VerifyColision(p, e)) {
                        e.explosion(currentTime);
                        PowerUp pw = e.maybeSpawnPowerUp(e.getX(), e.getY());
                        if(pw != null) {
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

            for(PowerUp pw : powerUps) {
                if(pw instanceof ShrinkPowerUp) {
                    ShrinkPowerUp pw1 = (ShrinkPowerUp) pw;
                    pw1.update(player);
                }
                if(Collision.VerifyColision(pw, player)) pw.onCollected(player);
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

            // Renderização
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

        // Pega o nível atual
        Level currentLevel = gameLoader.getLevels().get(levelIndex);

        // Reseta o status de todos os eventos de spawn desse nível
        for (SpawnEvent event : currentLevel.getEvents()) {
            event.setSpawned(false);
        }

        // Mostra uma tela de "Início de Fase"
        long messageStartTime = System.currentTimeMillis();
        long displayDuration = 2000;  // Exibir por 2 segundos

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

    private static void processSpawnEvents(long currentTime, List<Enemy> enemies, Boss boss, Player player) {
        long levelTime = currentTime - levelStartTime;
        Level currentLevel = gameLoader.getLevels().get(currentLevelIndex);
        
        for (SpawnEvent event : currentLevel.getEvents()) {
            // Verifica se é hora de spawnar este evento e se ainda não foi spawnado
            if (levelTime >= event.getTime() && !event.isSpawned()) {
                if (event.getType() == SpawnEvent.Type.INIMIGO) {
                    // Spawn de inimigo comum
                    Enemy enemy;
                    if (event.getEntityType() == 1) {
                        enemy = new Enemy1(
                            new Coordinate(event.getX(), event.getY()),
                            new Coordinate(0.0, 0.20 + Math.random() * 0.15)
                        );
                    } else { // tipo 2
                        enemy = new Enemy2(
                            new Coordinate(event.getX(), event.getY()),
                            new Coordinate(0.42, 0.42)
                        );
                    }
                    enemies.add(enemy);
                } else if (event.getType() == SpawnEvent.Type.CHEFE) {
                    // Spawn de chefe
                    if (event.getEntityType() == 1) {
                        boss = new Boss(
                            new Coordinate(event.getX(), event.getY()),
                            new Coordinate(0.1, 0.05),
                            event.getLife()
                        );
                    } else { // tipo 2
                        boss = new Boss2(
                            new Coordinate(event.getX(), event.getY()),
                            new Coordinate(0.1, 0.05),
                            event.getLife(),
                            player
                        );
                    }
                }
                event.setSpawned(true);
            }
        }
    }
}