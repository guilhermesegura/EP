package entities;
import utils.*;
import static utils.States.*;

public class Enemy1 extends Enemy {
    private static final double SCREEN_BOTTOM_PADDING = 10.0;
    private double previousY;
    private boolean passedThreshold;

    public Enemy1(double x, double y, double velocity, double angle, double rv, double radius) {
        super(x, y, 
              velocity * Math.cos(angle),  
              velocity * Math.sin(angle) * -1.0,  
              radius, angle, rv);
        this.previousY = y;
        this.passedThreshold = false;
    }

    public boolean isOnScreen() {
        if (getState() != ACTIVE) return false;
        return getY() <= GameLib.HEIGHT + SCREEN_BOTTOM_PADDING;
    }

    public void move(long delta) {
        if (getState() != ACTIVE) return;
        
        previousY = getY();
        
        double velocity = Math.sqrt(getVx() * getVx() + getVy() * getVy());
        
        double newX = getX() + velocity * Math.cos(getAngle()) * delta;
        double newY = getY() + velocity * Math.sin(getAngle()) * delta * (-1.0);
        double newAngle = getAngle() + getRV() * delta;
        
        setX(newX);
        setY(newY);
        setAngle(newAngle);
        
        checkThreshold();
        
        if (getY() > GameLib.HEIGHT + SCREEN_BOTTOM_PADDING) {
            setState(INACTIVE);
        }
    }

    private void checkThreshold() {
        double threshold = GameLib.HEIGHT * 0.30;
        
        if (!passedThreshold && previousY < threshold && getY() >= threshold) {
            setRV((getX() < GameLib.WIDTH / 2) ? 0.003 : -0.003);
            passedThreshold = true;
        }
    }

    public boolean shouldShoot() {
        if (getRV() > 0 && Math.abs(getAngle() - 3 * Math.PI) < 0.05) {
            setRV(0.0);
            setAngle(3 * Math.PI);
            return true;
        }
        else if (getRV() < 0 && Math.abs(getAngle()) < 0.05) {
            setRV(0.0);
            setAngle(0.0);
            return true;
        }
        return false;
    }
}