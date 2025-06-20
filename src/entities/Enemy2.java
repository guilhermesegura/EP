package entities;
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

    private boolean readyToShoot;
    private double previousY;
    private boolean passedThreshold;

    public Enemy2(Coordinate coordinate, Coordinate velocity, States state, double radius) {
        super(coordinate, velocity, state, radius);
        this.readyToShoot = false;
        this.passedThreshold = false;
    }

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
        }
        else if (getRotationalVelocity() < 0 && Math.abs(getAngle() - SHOOT_ANGLE_LEFT) < ANGLE_TOLERANCE) {
            setRotationalVelocity(0.0);
            setAngle(SHOOT_ANGLE_LEFT);
            readyToShoot = true;
        }
    }

    public boolean shouldShoot() {
        return readyToShoot;
    }

    public void update(long delta){
        if (getState() == States.EXPLODING) {
            if (System.currentTimeMillis() > getExplosionEnd()) 
                setState(States.INACTIVE);
        }    
        if (getState() == States.ACTIVE){
        
            previousY = getY();
    
            double newX = getX() + VELOCITY * Math.cos(getAngle()) * delta;
            double newY = getY() + B * Math.sin(getAngle()) * delta * (-1.0);
        
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

    
}

/*


 */