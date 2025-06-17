import entities.*;
import game.Loader;
import graphics.BackGroundGraphics;
import graphics.EnemyGraphics;
import graphics.PlayerGraphics;
import graphics.ProjectileGraphics;
import utils.GameLib;
import utils.States;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class Game {
    //private static final int MAX_PROJECTILES = 20;
    //private static int ACTIVE_PROJECTILES = 0;
    public static void main(String[] args) {
        // Game loop control
        boolean running = true;
        long currentTime = System.currentTimeMillis();
        long delta;

        // Player setup
        Player player = new Player(States.ACTIVE, GameLib.WIDTH / 2.0, GameLib.HEIGHT * 0.90, 0.25, 0.25, 12.0, currentTime);

        // Projectile lists
        List<Projectiles> playerProjectiles = new ArrayList<>();
        List<Projectiles> enemyProjectiles = new ArrayList<>();

        // Enemy lists
        List<Enemy> enemies = new ArrayList<>();

        // Background graphics
        BackGroundGraphics background1 = new BackGroundGraphics(0.070, 20);
        BackGroundGraphics background2 = new BackGroundGraphics(0.045, 50);

        // Timers for enemy spawning
        long nextEnemy1 = currentTime + 2000;
        long nextEnemy2 = currentTime + 7000;
        double enemy2_spawnX = GameLib.WIDTH * 0.20;
        int enemy2_count = 0;

        // Initialize graphics
        GameLib.initGraphics();

        // Main game loop
        while (running) {
            delta = System.currentTimeMillis() - currentTime;
            currentTime = System.currentTimeMillis();

            // Check for collisions
            // Player projectiles with enemies
            for (Projectiles projectile : playerProjectiles) {
                for (Enemy enemy : enemies) {
                    if (projectile.collision(enemy)) {
                        enemy.setState(States.EXPLODING);
                        enemy.setExplosionStart(currentTime);
                        enemy.setExplosionEnd(currentTime + 500);
                        projectile.setState(States.INACTIVE);
                    }
                }
            }

            // Enemy projectiles with player
            for (Projectiles projectile : enemyProjectiles) {
                if (projectile.collision(player)) {
                    player.explosion(currentTime);
                }
            }
            // Enemies with player


            // Update states
            player.move(delta);
            player.exploded(currentTime);


            if (GameLib.iskeyPressed(GameLib.KEY_CONTROL) && currentTime > player.getNextShot()) {
                playerProjectiles.add(new Projectiles(new utils.Coordinate(player.getX(), player.getY() - 2 * player.getRadius()), new utils.Coordinate(0.0, -1.0), 2.0, Projectiles.PLAYER_PROJECTILE, 1));
                player.shoot(currentTime);
            }


            // Update player projectiles
            for (Projectiles projectile : playerProjectiles) {
                projectile.update(delta);
            }

            // Update enemy projectiles
            for (Projectiles projectile : enemyProjectiles) {
                projectile.update(delta);
            }

            // Update enemies
            for (Enemy enemy : enemies) {
                if (enemy.getState() == States.EXPLODING) {
                    if (currentTime > enemy.getExplosionEnd()) {
                        enemy.setState(States.INACTIVE);
                    }
                } else if (enemy.getState() == States.ACTIVE) {
                    if (enemy instanceof Enemy1) {
                        ((Enemy1) enemy).move(delta);
                    } else if (enemy instanceof Enemy2) {
                        ((Enemy2) enemy).move(delta);
                    }
                }
            }

            // Handle enemy shooting
            for (Enemy enemy : enemies) {
                if (enemy.getState() == States.ACTIVE) {
                    // Logic for Enemy 1 shooting
                    if (enemy instanceof Enemy1) {
                        Enemy1 enemy1 = (Enemy1) enemy;
                        if (enemy1.shouldShoot() && currentTime > enemy1.getNextShot()) {
                            double angle = enemy1.getAngle();
                            double vx = Math.cos(angle) * 0.45;
                            double vy = Math.sin(angle) * 0.45 * (-1.0);

                            enemyProjectiles.add(new Projectiles(new utils.Coordinate(enemy1.getX(), enemy1.getY()), new utils.Coordinate(vx, vy), 2.0, Projectiles.ENEMY_PROJECTILE, 1));
                            enemy1.setNextShot((long) (currentTime + 200 + Math.random() * 500));
                        }
                    } 
                    // Logic for Enemy 2 shooting
                    else if (enemy instanceof Enemy2) {
                        Enemy2 enemy2 = (Enemy2) enemy;
                        if (enemy2.shouldShoot()) {
                            double[] angles = { Math.PI / 2 + Math.PI / 8, Math.PI / 2, Math.PI / 2 - Math.PI / 8 };
                            for (double angle : angles) {
                                double a = angle + Math.random() * Math.PI / 12 - Math.PI / 24;
                                double vx = Math.cos(a) * 0.30;
                                double vy = Math.sin(a) * 0.30;
                                enemyProjectiles.add(new Projectiles(new utils.Coordinate(enemy2.getX(), enemy2.getY()), new utils.Coordinate(vx, vy), 2.0, Projectiles.ENEMY_PROJECTILE, 1));
                            }

                            // Prevent continuous shooting
                        }
                    }
                }
            }


            // Spawn new enemies
            if (currentTime > nextEnemy1) {
                enemies.add(new Enemy1(Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2, 0.0, 9.0));
                nextEnemy1 = currentTime + 500;
            }

            if (currentTime > nextEnemy2) {
                enemies.add(new Enemy2(enemy2_spawnX, -10.0, 0.42, 0.0, 12.0, (3 * Math.PI) / 2, 0.0));
                enemy2_count++;
                if (enemy2_count < 10) {
                    nextEnemy2 = currentTime + 120;
                } else {
                    enemy2_count = 0;
                    enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
                    nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000);
                }
            }

            // Exit condition
            if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) {
                running = false;
            }

            // Draw scene
            background2.setColor(Color.DARK_GRAY);
            background2.fillBakcGround(delta);

            background1.setColor(Color.GRAY);
            background1.fillBakcGround(delta);

            PlayerGraphics.player(player, Color.BLUE, currentTime);
            ProjectileGraphics.projectiles(playerProjectiles, Color.GREEN);
            ProjectileGraphics.ballProjectiles(enemyProjectiles, Color.RED, 2.0);
            EnemyGraphics.enemy(currentTime, enemies, Color.CYAN, 9.0);

            GameLib.display();

            // Delay for constant frame rate
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }
}