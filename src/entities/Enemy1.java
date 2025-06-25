package entities;

import java.util.List;
import utils.*;
import static utils.States.*;

public class Enemy1 extends Enemy {

    private static final double SCREEN_BOTTOM_PADDING = 10.0;
    private static final double SHOOTING_HEIGHT_RATIO = 0.5;

    private static final int MAX_HEALTH = 1;
    private static final double MAX_RADIUS = 9.0;
    
    //ATRIBUTOS DE VELOCIDADE
    private static final double MAX_VX = 0.00;
    private static final double MAX_VY =  0.20 + Math.random() * 0.15;


    public Enemy1(Coordinate coordinate) {
        super(coordinate, new Coordinate(MAX_VX, MAX_VY), ACTIVE, MAX_RADIUS, MAX_HEALTH);
    }

    public static int getMaxHealth() {
        return MAX_HEALTH;
    }

    public static double getMaxRadius() {
        return MAX_RADIUS;
    }

    @Override
    public boolean isOnScreen() {
        if (getState() != ACTIVE) return false;
        return getY() <= GameLib.HEIGHT + SCREEN_BOTTOM_PADDING;
    }

    @Override
    public boolean shouldShoot() {
        if (getState() != ACTIVE) return false;

        double middleStart = GameLib.HEIGHT * (1 - SHOOTING_HEIGHT_RATIO) / 2;
        double middleEnd = GameLib.HEIGHT - middleStart;
        return getY() > middleStart && getY() < middleEnd;
    }

    @Override
    public void update(long delta) {
        if (getState() == EXPLODING) {
            if (System.currentTimeMillis() > getExplosionEnd())
                setState(INACTIVE);
        }

        if (getState() == ACTIVE) {
            setX(getX() + getVx() * delta);
            setY(getY() + getVy() * delta);

            if (getY() > GameLib.HEIGHT + SCREEN_BOTTOM_PADDING) {
                setState(INACTIVE);
            }
        }
    }

    public boolean shouldSpawn(long currentTime) {
        return false;
    }

    public void shoot(long currentTime, List<Projectiles> enemyProjectiles)
    {
        if (shouldShoot()) {
            if (isShotCooldownOver(currentTime)) {
                resetShotCooldown(currentTime);
                enemyProjectiles.add(new Projectiles(
                    new Coordinate(getX(), getY()),
                    new Coordinate(0.0, 0.45),
                    2.0,
                    Projectiles.ENEMY_PROJECTILE,
                    1
                ));
                        
            }
        }
    }
}

