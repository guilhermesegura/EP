import entities.*;
import graphics.BackGroundGraphics;
import graphics.EnemyGraphics;
import graphics.PlayerGraphics;
import graphics.ProjectileGraphics;
import utils.Coordinate;
import utils.GameLib;
import utils.States;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Game {
    public static void main(String[] args) {
        // Controle do loop do jogo
        boolean running = true;
        long currentTime = System.currentTimeMillis();
        long delta;

        // Configuração do jogador
        Player player = new Player(
            new Coordinate(GameLib.WIDTH / 2.0, GameLib.HEIGHT * 0.90),
            new Coordinate(0.25, 0.25), // Velocidade de movimento
            12.0 // Raio
        );

        // Listas de projéteis e inimigos
        List<Projectiles> playerProjectiles = new ArrayList<>();
        List<Projectiles> enemyProjectiles = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();

        // Componentes gráficos de fundo
        BackGroundGraphics background1 = new BackGroundGraphics(0.070, 20);
        BackGroundGraphics background2 = new BackGroundGraphics(0.045, 50);

        // Temporizadores para o surgimento de inimigos (lógica hard-coded)
        long nextEnemy1 = currentTime + 2000;
        long nextEnemy2 = currentTime + 7000;
        double enemy2_spawnX = GameLib.WIDTH * 0.20;
        int enemy2_count = 0;

        // Inicializa a interface gráfica
        GameLib.initGraphics();

        // Loop principal do jogo
        while (running) {
            delta = System.currentTimeMillis() - currentTime;
            currentTime = System.currentTimeMillis();

            // --- 1. Processamento de Entrada do Usuário ---
            if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) {
                running = false;
            }
            // O método update do jogador já lida com o movimento baseado na entrada
            player.update(delta);

            if (GameLib.iskeyPressed(GameLib.KEY_CONTROL) && currentTime > player.getNextShot()) {
                // Cria um novo projétil do jogador
                playerProjectiles.add(new Projectiles(
                    new Coordinate(player.getX(), player.getY() - 2 * player.getRadius()),
                    new Coordinate(0.0, -1.0), // Velocidade do projétil
                    2.0, // Raio do projétil
                    Projectiles.PLAYER_PROJECTILE,
                    1 // Dano
                ));
                player.shoot(currentTime); // Atualiza o tempo do próximo tiro do jogador
            }

            // --- 2. Atualização de Estado das Entidades (Polimorfismo) ---
            for (Projectiles projectile : playerProjectiles) projectile.update(delta);
            for (Projectiles projectile : enemyProjectiles) projectile.update(delta);

            for (Enemy enemy : enemies) {
                enemy.update(delta); // Cada tipo de inimigo (1 ou 2) executa seu próprio update

                // Lógica de tiro específica para cada inimigo
                if (enemy.shouldShoot() && enemy.isShotCooldownOver(currentTime)) {
                     enemy.resetShotCooldown(currentTime);
                     if (enemy instanceof Enemy1) {
                        double angle = Math.PI / 2; // Atira para baixo
                        enemyProjectiles.add(new Projectiles(new Coordinate(enemy.getX(), enemy.getY()), new Coordinate(Math.cos(angle), Math.sin(angle)), 2.0, Projectiles.ENEMY_PROJECTILE, 1));
                    } else if (enemy instanceof Enemy2) {
                        Enemy2 e2 = (Enemy2) enemy;
                        double[] angles = { Math.PI / 2 + Math.PI / 8, Math.PI / 2, Math.PI / 2 - Math.PI / 8 };
                        for (double angle : angles) {
                            double vx = Math.cos(angle) * 0.30;
                            double vy = Math.sin(angle) * 0.30;
                            enemyProjectiles.add(new Projectiles(new Coordinate(e2.getX(), e2.getY()), new Coordinate(vx, vy), 2.0, Projectiles.ENEMY_PROJECTILE, 1));
                        }
                        //e2.setReadyToShoot(false); // Impede disparos contínuos
                    }
                }
            }

            // --- 3. Surgimento de Inimigos (Hard-Coded) ---
            if (currentTime > nextEnemy1) {
                enemies.add(new Enemy1(
                    new Coordinate(Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0),
                    new Coordinate(0.0, 0.20 + Math.random() * 0.15), 
                    States.ACTIVE,
                    9.0
                ));
                nextEnemy1 = currentTime + 1000;
            }

            if (currentTime > nextEnemy2) {
                 enemies.add(new Enemy2(
                    new Coordinate(enemy2_spawnX, -10.0),
                    new Coordinate(0.42, 0.42),
                    States.ACTIVE,
                    12.0
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

            // --- 4. Verificação de Colisões ---
            // Projéteis do jogador com inimigos
            for (Projectiles p : playerProjectiles) {
                for (Enemy e : enemies) {
                    if (Collision.VerifyColision(p, e)) {
                        e.explosion(currentTime);
                        p.setState(States.INACTIVE);
                    }
                }
            }

            // Projéteis inimigos com jogador e Inimigos com jogador
            if (player.getState() == States.ACTIVE) {
                for (Projectiles p : enemyProjectiles) {
                    if (Collision.VerifyColision(p, player)) {
                        player.explosion(currentTime);
                        p.setState(States.INACTIVE);
                    }
                }
                for (Enemy e : enemies) {
                    if (Collision.VerifyColision(e, player)) {
                        player.explosion(currentTime);
                    }
                }
            }

            // --- 5. Limpeza de Entidades Inativas ---
            playerProjectiles.removeIf(p -> p.getState() == States.INACTIVE);
            enemyProjectiles.removeIf(p -> p.getState() == States.INACTIVE);
            enemies.removeIf(e -> e.getState() == States.INACTIVE);

            // --- 6. Desenho da Cena ---
            background2.setColor(Color.DARK_GRAY);
            background2.fillBakcGround(delta);
            background1.setColor(Color.GRAY);
            background1.fillBakcGround(delta);

            PlayerGraphics.player(player, Color.BLUE, currentTime);
            ProjectileGraphics.projectiles(playerProjectiles, Color.GREEN);
            ProjectileGraphics.ballProjectiles(enemyProjectiles, Color.RED, 2.0);
            EnemyGraphics.enemy(currentTime, enemies, Color.CYAN, 9.0);

            GameLib.display();

            // Pausa para manter uma taxa de quadros constante
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }
}