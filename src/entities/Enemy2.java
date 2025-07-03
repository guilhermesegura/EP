package entities;

import java.util.List;
import utils.*;
import static utils.States.*;

public class Enemy2 extends Enemy {

    private static final double SCREEN_PADDING = 10.0;
    private static final double ANGLE_TOLERANCE = 0.05;
    private static final double ACTIVATION_THRESHOLD = 0.30;
    private static final double LEFT_SIDE_RV = 0.003;
    private static final double RIGHT_SIDE_RV = -0.003;
    private static final double SHOOT_ANGLE_LEFT = 0.0;
    private static final double SHOOT_ANGLE_RIGHT = 3 * Math.PI;
    private static final double VELOCITY = 0.42;

    private static final int MAX_HEALTH = 1;
    private static final double MAX_RADIUS = 12.0;

    //ATRIBUTOS DE VELOCIDADE
    private static final double MAX_VX = 0.42;
    private static final double MAX_VY = 0.42;

    private long lastTimeShooted = 0;
    private static final long shootCooldown = 500;
    private boolean readyToShoot;
    private double previousY;
    private boolean passedThreshold;

    public Enemy2(Coordinate coordinate) {
        super(coordinate, new Coordinate(MAX_VX, MAX_VY), ACTIVE, MAX_RADIUS, MAX_HEALTH);
        this.readyToShoot = false;
        this.passedThreshold = false;
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
        return !(getX() < -SCREEN_PADDING || getX() > GameLib.WIDTH + SCREEN_PADDING);
    }

    private void checkThresholdBehavior() {
        if (!passedThreshold) {
            double thresholdY = GameLib.HEIGHT * ACTIVATION_THRESHOLD;
            if (previousY < thresholdY && getY() >= thresholdY) {
                setRotationalVelocity((getX() < GameLib.WIDTH / 2) ? LEFT_SIDE_RV : RIGHT_SIDE_RV);
                passedThreshold = true;
            }
        }
    }

    private void checkShootingConditions() {
        readyToShoot = false;

        if (getRotationalVelocity() > 0 && Math.abs(getAngle() - SHOOT_ANGLE_RIGHT) < ANGLE_TOLERANCE) {
            setRotationalVelocity(0.0);
            setAngle(SHOOT_ANGLE_RIGHT);
            readyToShoot = true;
        } else if (getRotationalVelocity() < 0 && Math.abs(getAngle() - SHOOT_ANGLE_LEFT) < ANGLE_TOLERANCE) {
            setRotationalVelocity(0.0);
            setAngle(SHOOT_ANGLE_LEFT);
            readyToShoot = true;
        }
    }

    @Override
    public boolean shouldShoot() {
        return readyToShoot;
    }

    @Override
    public void update(long delta) {
        if (getState() == EXPLODING) {
            if (System.currentTimeMillis() > getExplosionEnd())
                setState(INACTIVE);
        }

        if (getState() == ACTIVE) {
            previousY = getY();

            double newX = getX() + VELOCITY * Math.cos(getAngle()) * delta;
            double newY = getY() + VELOCITY * Math.sin(getAngle()) * delta * (-1.0);

            setX(newX);
            setY(newY);
            setAngle(getAngle() + getRotationalVelocity() * delta);

            checkThresholdBehavior();
            checkShootingConditions();

            if (!isOnScreen()) {
                setState(INACTIVE);
            }
        }
    }

    public void setReadyToShoot(boolean ready) {
        this.readyToShoot = ready;
    }

    public void shoot(long currentTime, List<Projectiles> enemyProjectiles)
    {
        if (shouldShoot() && (currentTime - lastTimeShooted) > shootCooldown) {
            double[] angles = { Math.PI / 2 + Math.PI / 8, Math.PI / 2, Math.PI / 2 - Math.PI / 8 };
            for (double angle : angles) {
                double a = angle + Math.random() * Math.PI/6 - Math.PI/12;
                double vx = Math.cos(a) * 0.30;
                double vy = Math.sin(a) * 0.30;
                enemyProjectiles.add(new Projectiles(
                    new Coordinate(getX(), getY()),
                    new Coordinate(vx, vy),
                    2.0,
                    Projectiles.ENEMY_PROJECTILE,
                    1
                ));
            }
            lastTimeShooted = currentTime;
        }
    }
}
