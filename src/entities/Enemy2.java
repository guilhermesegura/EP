package entities;
import utils.*;
import static utils.States.*;

public class Enemy2 extends Enemy {
    private static final double SCREEN_PADDING = 10.0;
    private static final double ANGLE_TOLERANCE = 0.05;
    private static final double ACTIVATION_THRESHOLD = 0.30;
    
    private boolean shootNow;
    private double previousY;
    private boolean passedThreshold;

    public Enemy2(double x, double y, double vX, double vY, double radius, double angle, double RV) {
        super(x, y, vX, vY, radius, angle, RV);
        this.shootNow = false;
        this.previousY = y;
        this.passedThreshold = false;
    }

    public boolean isOnScreen() {
        if (getState() != ACTIVE) return false;
        return !(getX() < -SCREEN_PADDING || getX() > GameLib.WIDTH + SCREEN_PADDING);
    }

    public void move(long delta) {
        if (getState() != ACTIVE) return;
        
        previousY = getY();
        
        double velocityMagnitude = Math.sqrt(getVx() * getVx() + getVy() * getVy());
        
        double newX = getX() + velocityMagnitude * Math.cos(getAngle()) * delta;
        double newY = getY() + velocityMagnitude * Math.sin(getAngle()) * delta * (-1.0);
        double newAngle = getAngle() + getRV() * delta;
        
        setX(newX);
        setY(newY);
        setAngle(newAngle);
        
        checkThresholdBehavior();
        checkShootingConditions();

        if (!isOnScreen()) {
            setState(INACTIVE);
        }
    }

    private void checkThresholdBehavior() {
        if (!passedThreshold) {
            double thresholdY = GameLib.HEIGHT * ACTIVATION_THRESHOLD;
            if (previousY < thresholdY && getY() >= thresholdY) {
                setRV((getX() < GameLib.WIDTH / 2) ? 0.003 : -0.003);
                passedThreshold = true;
            }
        }
    }

    private void checkShootingConditions() {
        shootNow = false;
        
        if (getRV() > 0 && Math.abs(getAngle() - 3 * Math.PI) < ANGLE_TOLERANCE) {
            setRV(0.0);
            setAngle(3 * Math.PI);
            shootNow = true;
        }
        else if (getRV() < 0 && Math.abs(getAngle()) < ANGLE_TOLERANCE) {
            setRV(0.0);
            setAngle(0.0);
            shootNow = true;
        }
    }

    public boolean shouldShoot() {
        return shootNow;
    }
}