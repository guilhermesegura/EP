package entities;
import utils.*;
import static utils.States.*;

public class Enemy1 extends Enemy {
    private static final double SCREEN_BOTTOM_PADDING = 10.0;
    private static final double SHOOTING_HEIGHT_RATIO = 0.5; 

    public Enemy1(Coordinate coordinate, Coordinate velocity, States state, double radius) {
        super(coordinate, velocity, state, radius);
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


    public void update(long delta) {
        if (getState() == States.EXPLODING) {
            if (System.currentTimeMillis() > getExplosionEnd()) 
                setState(States.INACTIVE);
        } 
        if (getState() == States.ACTIVE) {
       
            setX(getX() + getVx() * delta);
            setY(getY() + getVy() * delta);
        
            if (getY() > GameLib.HEIGHT + SCREEN_BOTTOM_PADDING) {
                setState(INACTIVE);
            }
        }
    }

    public boolean shouldSpawn(long currentTime){
        return false;
    }
}