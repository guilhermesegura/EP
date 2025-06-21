import entities.*;
import graphics.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import utils.Coordinate;
import utils.GameLib;
import utils.States;

public class Game {
    private static boolean gameOver = false;
    
    public static void main(String[] args) {
        // Game loop control
        boolean running = true;
        long currentTime = System.currentTimeMillis();
        long delta;

        // Player setup
        Player player = new Player(
            new Coordinate(GameLib.WIDTH / 2.0, GameLib.HEIGHT * 0.90),
            new Coordinate(0.25, 0.25)        
        );

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

        // Enemy spawn timers
        long nextEnemy1 = currentTime + 2000;
        long nextEnemy2 = currentTime + 7000;
        long nextBoss = currentTime + 10000;
        double enemy2_spawnX = GameLib.WIDTH * 0.20;
        int enemy2_count = 0;

        // Initialize graphics
        GameLib.initGraphics();

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
                    playerProjectiles.clear();
                    enemyProjectiles.clear();
                    enemies.clear();
                    powerUps.clear();
                    boss = null;
                    nextEnemy1 = currentTime + 2000;
                    nextEnemy2 = currentTime + 7000;
                    nextBoss = currentTime + 10000;
                    enemy2_count = 0;
                }
                
                // Mostra tela de Game Over
                GameOverGraphics.drawGameOver();
                GameLib.display();
                continue; // Pula o resto do loop
            }
            
            // Atualização do jogador
            player.update(delta);

            // Verifica se o jogador morreu
            if (player.getHealth() <= 0) {
                gameOver = true;
                player.setState(States.INACTIVE);
                continue;
            }

            if (GameLib.iskeyPressed(GameLib.KEY_CONTROL) && currentTime > player.getNextShot()) {
                playerProjectiles.add(new Projectiles(
                    new Coordinate(player.getX(), player.getY() - 2 * player.getRadius()),
                    new Coordinate(0.0, -1.0),
                    2.0,
                    Projectiles.PLAYER_PROJECTILE,
                    1
                ));
                player.shoot(currentTime);
            }

            // 2. Update entities
            for (Projectiles projectile : playerProjectiles) projectile.update(delta);
            for (Projectiles projectile : enemyProjectiles) projectile.update(delta);
            for (PowerUp pw : powerUps) pw.update(delta);
            
            for (Enemy enemy : enemies) {
                enemy.update(delta);

                if (enemy.shouldShoot()) {
                    if (enemy instanceof Enemy1) {
                        if (enemy.isShotCooldownOver(currentTime)) {
                            enemy.resetShotCooldown(currentTime);
                            enemyProjectiles.add(new Projectiles(
                                new Coordinate(enemy.getX(), enemy.getY()),
                                new Coordinate(0.0, 0.45),
                                2.0,
                                Projectiles.ENEMY_PROJECTILE,
                                1
                            ));
                        }
                    } else if (enemy instanceof Enemy2) {
                        double[] angles = { Math.PI / 2 + Math.PI / 8, Math.PI / 2, Math.PI / 2 - Math.PI / 8 };
                        for (double angle : angles) {
                            double a = angle + Math.random() * Math.PI/6 - Math.PI/12;
                            double vx = Math.cos(a) * 0.30;
                            double vy = Math.sin(a) * 0.30;
                            enemyProjectiles.add(new Projectiles(
                                new Coordinate(enemy.getX(), enemy.getY()),
                                new Coordinate(vx, vy),
                                2.0,
                                Projectiles.ENEMY_PROJECTILE,
                                1
                            ));
                        }
                    }
                }
            }
            
            // Boss logic
            if (boss != null) {
                boss.update(delta);
                if (boss.shouldShoot()) {
                    boss.performAttack(enemyProjectiles);
                }
            }

            // 3. Spawn entities
            if (currentTime > nextEnemy1) {
                enemies.add(new Enemy1(
                    new Coordinate(Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0),
                    new Coordinate(0.0, 0.20 + Math.random() * 0.15)                    
                    ));
                nextEnemy1 = currentTime + 1000;
            }

            if (currentTime > nextEnemy2) {
                enemies.add(new Enemy2(
                    new Coordinate(enemy2_spawnX, -10.0),
                    new Coordinate(0.42, 0.42)
                ));
                enemy2_count++;
                if (enemy2_count < 10) {
                    nextEnemy2 = currentTime + 120;
                } else {
                    enemy2_count = 0;
                    enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
                    nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000);
                }
            }
            
            // Spawn boss
            if (currentTime > nextBoss && boss == null) {
                boolean spawnBoss2 = Math.random() < 1;
                
                if (spawnBoss2) {
                    boss = new Boss2(
                        new Coordinate(GameLib.WIDTH / 2.0, -50.0),
                        new Coordinate(0.1, 0.05), 20, player
                    );
                } else {
                    boss = new Boss(
                        new Coordinate(GameLib.WIDTH / 2.0, -50.0),
                        new Coordinate(0.1, 0.05), 30
                    );
                }
                nextBoss = Long.MAX_VALUE;
            }

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

            // 5. Clean up inactive entities
            playerProjectiles.removeIf(p -> p.getState() == States.INACTIVE);
            enemyProjectiles.removeIf(p -> p.getState() == States.INACTIVE);
            enemies.removeIf(e -> e.getState() == States.INACTIVE);
            powerUps.removeIf(e -> e.getState() == States.INACTIVE);

            // 6. Render scene
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
}