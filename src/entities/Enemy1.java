package entities;
import utils.*;
import static utils.States.*;

public class Enemy1 extends Enemy {
    private static final double SCREEN_BOTTOM_PADDING = 10.0;

    public Enemy1(double x, double y, double velocity, double angle, double rv, double radius) {
        super(x, y, 
              velocity * Math.cos(angle),  
              velocity * Math.sin(angle) * -1.0,  
              radius, angle, rv);
    }

    public boolean isOnScreen() {
        if (getState() != ACTIVE) return false;
        return getY() <= GameLib.HEIGHT + SCREEN_BOTTOM_PADDING;
    }

    public void move(long delta) {
        if (getState() != ACTIVE) return;
        
        // The enemy now moves in a straight line based on its initial angle.
        double newX = getX() + getVx() * delta;
        double newY = getY() + getVy() * delta;
        
        setX(newX);
        setY(newY);
        
        // Remove the turning logic
        // checkThreshold(); 
        
        if (getY() > GameLib.HEIGHT + SCREEN_BOTTOM_PADDING) {
            setState(INACTIVE);
        }
    }

    // This method needs adjustment since the enemy no longer turns to a specific angle.
    // Let's make it shoot when it's on screen.
    public boolean shouldShoot() {
        return getY() > 0 && getY() < GameLib.HEIGHT;
    }
}