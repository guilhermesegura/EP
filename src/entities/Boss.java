package entities;
import utils.Coordinate;
import utils.GameLib;
import utils.States;
import entities.interfaces.*;

public class Boss extends Enemy {

    private long lastAttackTime;
    private long attackCooldown = 2000; // 2 seconds
    private int attackPattern = 0;
    private int maxHealth;
    private int currentHealth;
    private int size;

    public Boss(Coordinate coordinate, Coordinate velocity, int health, int size) {
        super(coordinate, velocity, States.ACTIVE, 30.0, health); // Larger radius for the boss
        this.maxHealth = health;
        this.currentHealth = health;
        this.lastAttackTime = System.currentTimeMillis();
        this.size = size;
    }

    @Override
    public void update(long delta) {
        if (getState() == States.EXPLODING) {
            if (System.currentTimeMillis() > getExplosionEnd()) {
                setState(States.INACTIVE);
            }
            return;
        }

        if (getState() == States.ACTIVE) {
            // Movement pattern: Move horizontally and slightly vertically
            setX(getX() + getVx() * delta);
            if (getX() < getRadius() || getX() > GameLib.WIDTH - getRadius()) {
                setVx(getVx() * -1); // Reverse horizontal direction
            }
            setY(getY() + getVy() * delta);
            if (getY() > GameLib.HEIGHT * 0.3) {
                setVy(0); // Stop moving down
            }
        }
    }

    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime > attackCooldown;
    }

    public void performAttack(java.util.List<Projectiles> enemyProjectiles) {
        lastAttackTime = System.currentTimeMillis();
        switch (attackPattern) {
            case 0:
                // Shoots a spread of projectiles
                for (int i = -2; i <= 2; i++) {
                    double angle = Math.PI / 2 + i * (Math.PI / 8);
                    double vx = Math.cos(angle) * 0.3;
                    double vy = Math.sin(angle) * 0.3;
                    enemyProjectiles.add(new Projectiles(new Coordinate(getX(), getY()), new Coordinate(vx, vy), 5.0, Projectiles.ENEMY_PROJECTILE, 1));
                }
                break;
            case 1:
                // Shoots a powerful, single projectile
                enemyProjectiles.add(new Projectiles(new Coordinate(getX(), getY()), new Coordinate(0, 0.5), 10.0, Projectiles.ENEMY_PROJECTILE, 3));
                break;
        }
        attackPattern = (attackPattern + 1) % 2;
    }

    public boolean isOnScreen() {
        return getX() > -getRadius() && getX() < GameLib.WIDTH + getRadius() &&
               getY() > -getRadius() && getY() < GameLib.HEIGHT + getRadius();
    }


    public boolean shouldShoot() {
        return canAttack();
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getSize() {
        return size;
    }

    public void takeDamage(int damage) {
        this.currentHealth -= damage;
        if (this.currentHealth <= 0) {
            this.currentHealth = 0;
            setState(States.INACTIVE);
        }
    }
}