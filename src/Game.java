import entities.*;
import graphics.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import utils.Coordinate;
import utils.GameLib;
import utils.States;

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

        // Listas de projéteis, inimigos e power-ups
        List<Projectiles> playerProjectiles = new ArrayList<>();
        List<Projectiles> enemyProjectiles = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        List<PowerUp> powerUps = new ArrayList<>();
        
        // Objeto do Chefe (Boss)
        Boss boss = null;

        // Componentes gráficos de fundo
        BackGroundGraphics background1 = new BackGroundGraphics(0.070, 20); //
        BackGroundGraphics background2 = new BackGroundGraphics(0.045, 50); //

        // Temporizadores para o surgimento de inimigos (lógica hard-coded)
        long nextEnemy1 = currentTime + 2000; //
        long nextEnemy2 = currentTime + 7000; //
        long nextBoss = currentTime + 20000; // Temporizador para o chefe aparecer
        double enemy2_spawnX = GameLib.WIDTH * 0.20; //
        int enemy2_count = 0; //

        // Inicializa a interface gráfica
        GameLib.initGraphics(); //

        // Loop principal do jogo
        while (running) {
            delta = System.currentTimeMillis() - currentTime;
            currentTime = System.currentTimeMillis();

            // --- 1. Processamento de Entrada do Usuário ---
            if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) { //
                running = false;
            }
            // O método update do jogador já lida com o movimento baseado na entrada
            player.update(delta); //

            if (GameLib.iskeyPressed(GameLib.KEY_CONTROL) && currentTime > player.getNextShot()) { //
                // Cria um novo projétil do jogador
                playerProjectiles.add(new Projectiles(
                    new Coordinate(player.getX(), player.getY() - 2 * player.getRadius()),
                    new Coordinate(0.0, -1.0), // Velocidade do projétil
                    2.0, // Raio do projétil
                    Projectiles.PLAYER_PROJECTILE,
                    1 // Dano
                )); //
                player.shoot(currentTime); //
            }

            // --- 2. Atualização de Estado das Entidades ---
            for (Projectiles projectile : playerProjectiles) projectile.update(delta); //
            for (Projectiles projectile : enemyProjectiles) projectile.update(delta); //
            for (PowerUp pw : powerUps) pw.update(delta); //
            
            for (Enemy enemy : enemies) {
                enemy.update(delta); //

                // Lógica de tiro polimórfica
                if (enemy.shouldShoot()) { //
                    if (enemy instanceof Enemy1) { //
                        if (enemy.isShotCooldownOver(currentTime)) { //
                            enemy.resetShotCooldown(currentTime); //
                            enemyProjectiles.add(new Projectiles(new Coordinate(enemy.getX(), enemy.getY()), new Coordinate(0.0, 0.45), 2.0, Projectiles.ENEMY_PROJECTILE, 1)); //
                        }
                    } else if (enemy instanceof Enemy2) { //
                        // A lógica de tiro do Enemy2 já é tratada internamente pelo seu método update.
                        // Aqui, adicionamos os projéteis quando ele está pronto.
                        double[] angles = { Math.PI / 2 + Math.PI / 8, Math.PI / 2, Math.PI / 2 - Math.PI / 8 }; //
                        for (double angle : angles) { //
                            double a = angle + Math.random() * Math.PI/6 - Math.PI/12; //
                            double vx = Math.cos(a) * 0.30; //
                            double vy = Math.sin(a) * 0.30; //
                            enemyProjectiles.add(new Projectiles(new Coordinate(enemy.getX(), enemy.getY()), new Coordinate(vx, vy), 2.0, Projectiles.ENEMY_PROJECTILE, 1)); //
                        }
                    }
                }
            }
            
            // --- Lógica do Chefe (Boss) ---
            if (boss != null) {
                boss.update(delta);
                if (boss.shouldShoot()) {
                    boss.performAttack(enemyProjectiles);
                }
            }

            // --- 3. Surgimento de Entidades (Hard-Coded) ---
            if (currentTime > nextEnemy1) { //
                enemies.add(new Enemy1(
                    new Coordinate(Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0),
                    new Coordinate(0.0, 0.20 + Math.random() * 0.15), 
                    States.ACTIVE,
                    9.0, 1
                )); //
                nextEnemy1 = currentTime + 1000; //
            }

            if (currentTime > nextEnemy2) { //
                 enemies.add(new Enemy2(
                    new Coordinate(enemy2_spawnX, -10.0),
                    new Coordinate(0.42, 0.42),
                    States.ACTIVE,
                    12.0, 1
                )); //
                enemy2_count++; //
                if (enemy2_count < 10) { //
                    nextEnemy2 = currentTime + 120; //
                } else {
                    enemy2_count = 0; //
                    enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8; //
                    nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000); //
                }
            }
            
            // Spawn do Chefe
            if (currentTime > nextBoss && boss == null) {
                boss = new Boss(
                    new Coordinate(GameLib.WIDTH / 2.0, -50.0), // Posição inicial
                    new Coordinate(0.1, 0.05), // Velocidade
                    10, // Vida
                    32  // Tamanho
                );
                nextBoss = Long.MAX_VALUE; // Evita que outro chefe seja criado
            }


            // --- 4. Verificação de Colisões ---
            // Projéteis do jogador com inimigos e chefe
            for (Projectiles p : playerProjectiles) {
                for (Enemy e : enemies) {
                    if (Collision.VerifyColision(p, e)) { //
                        e.explosion(currentTime); //
                        PowerUp pw = e.maybeSpawnPowerUp(e.getX(), e.getY()); //
                        if(pw != null) { //
                            powerUps.add(pw); //
                        }
                        p.setState(States.INACTIVE); //
                    }
                }
                
                // ... dentro do loop de verificação de colisões ...
                if (boss != null && boss.getState() == States.ACTIVE && Collision.VerifyColision(p, boss)) {
                    // A chamada agora está correta. Java converterá o 'int' de p.getDamage() para 'double' automaticamente.
                    // A lógica de explosão agora está encapsulada dentro do método takeDamage do Boss.
                    boss.takeDamage(p.getDamage());
                    p.setState(States.INACTIVE);
                }
                
                }
            

            for(PowerUp pw: powerUps) { //
                if(pw instanceof ShrinkPowerUp) { //
                    ShrinkPowerUp pw1 = (ShrinkPowerUp) pw; //
                    pw1.update(player); //
                }
                if(Collision.VerifyColision(pw, player)) pw.onCollected(player); //
            }

            // Projéteis inimigos, inimigos e chefe com o jogador
            if (player.getState() == States.ACTIVE) { //
                for (Projectiles p : enemyProjectiles) {
                    if (Collision.VerifyColision(p, player)) { //
                        player.explosion(currentTime); //
                        p.setState(States.INACTIVE); //
                    }
                }
                for (Enemy e : enemies) {
                    if (Collision.VerifyColision(e, player)) { //
                        player.explosion(currentTime); //
                    }
                }
                if (boss != null && boss.getState() == States.ACTIVE && Collision.VerifyColision(boss, player)) {
                    player.explosion(currentTime);
                }
            }

            // --- 5. Limpeza de Entidades Inativas ---
            playerProjectiles.removeIf(p -> p.getState() == States.INACTIVE); //
            enemyProjectiles.removeIf(p -> p.getState() == States.INACTIVE); //
            enemies.removeIf(e -> e.getState() == States.INACTIVE); //
            powerUps.removeIf(e -> e.getState() == States.INACTIVE); //

            // --- 6. Desenho da Cena ---
            background2.setColor(Color.DARK_GRAY); //
            background2.fillBakcGround(delta); //
            background1.setColor(Color.GRAY); //
            background1.fillBakcGround(delta); //

            PlayerGraphics.player(player, Color.BLUE, currentTime); //
            ProjectileGraphics.projectiles(playerProjectiles, Color.GREEN); //
            ProjectileGraphics.ballProjectiles(enemyProjectiles, Color.RED, 2.0); //
            EnemyGraphics.enemy(currentTime, enemies, Color.CYAN, 9.0); //
            PowerUpGraphics.drawPowerUps(currentTime, powerUps, 5.0); //
            
            // Desenha o chefe e sua barra de vida
            if (boss != null) {
                BossGraphics.boss(boss, currentTime);
            }

            GameLib.display(); //

            // Pausa para manter uma taxa de quadros constante
            try {
                Thread.sleep(3); //
            } catch (InterruptedException e) {
                e.printStackTrace(); //
            }
        }
        System.exit(0); //
    }
}