package entities;
import utils.Coordinate;
import utils.GameLib;
import utils.States;

public class Boss extends Enemy {

    private long lastAttackTime;
    long attackCooldown = 2000; // 2 seconds
    private int attackPattern = 0;
    private static final int MAX_HEALTH = 100;
    private static final double MAX_RADIUS = 30.0;
    private static boolean shouldSpawn = true;

    public Boss(Coordinate coordinate, Coordinate velocity, double r) {
        super(coordinate, velocity, States.ACTIVE, r, MAX_HEALTH);
        this.lastAttackTime = System.currentTimeMillis();
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
            // Movimento horizontal com reversão ao tocar a borda
            setX(getX() + getVx() * delta);
            if (getX() < getRadius() || getX() > GameLib.WIDTH - getRadius()) {
                setVx(getVx() * -1);
            }

            // Movimento vertical até 30% da tela
            setY(getY() + getVy() * delta);
            if (getY() > GameLib.HEIGHT * 0.3) {
                setVy(0);
            }
        }
    }

    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime > attackCooldown && getState() == States.ACTIVE;
    }

    public void performAttack(java.util.List<Projectiles> enemyProjectiles) {
        lastAttackTime = System.currentTimeMillis();
        switch (attackPattern) {
            case 0:
                // Disparo em leque
                for (int i = -2; i <= 2; i++) {
                    double angle = Math.PI / 2 + i * (Math.PI / 8);
                    double vx = Math.cos(angle) * 0.3;
                    double vy = Math.sin(angle) * 0.3;
                    enemyProjectiles.add(new Projectiles(
                        new Coordinate(getX(), getY()),
                        new Coordinate(vx, vy),
                        5.0,
                        Projectiles.ENEMY_PROJECTILE,
                        1
                    ));
                }
                break;

            case 1:
                // Disparo único forte
                enemyProjectiles.add(new Projectiles(
                    new Coordinate(getX(), getY()),
                    new Coordinate(0, 0.5),
                    10.0,
                    Projectiles.ENEMY_PROJECTILE,
                    3
                ));
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
        return MAX_HEALTH;
    }

    public double getMaxRadius() {
        return MAX_RADIUS;
    }

    @Override
    public void takeDamage(int damage) {
        if (getState() != States.ACTIVE) return;

        setHealth(getHealth() - damage);
        if (getHealth() <= 0) {
            setHealth(0);
            explosion(System.currentTimeMillis());
            setState(States.INACTIVE);
            shouldSpawn = false;
        }
    }

    public void setAttackCooldown(long l)
    {
        attackCooldown = l;
    }
}
