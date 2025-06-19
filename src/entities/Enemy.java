package entities;
import utils.*;

public abstract class Enemy extends ExplodableEntity {
    protected static final long DEFAULT_SHOOT_COOLDOWN = 1000; 
    
    protected long nextShotTime;
    protected double angle;
    protected double rotationalVelocity;  
    
    public Enemy(Coordinate coordinate, Coordinate velocity, States state, double radius) {
        super(coordinate, velocity, state, radius);
        this.nextShotTime = System.currentTimeMillis() + DEFAULT_SHOOT_COOLDOWN;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRotationalVelocity() {
        return rotationalVelocity;
    }

    public void setRotationalVelocity(double rotationalVelocity) {
        this.rotationalVelocity = rotationalVelocity;
    }

    protected boolean isShotCooldownOver(long currentTime) {
        return currentTime > nextShotTime;
    }
    
    protected void resetShotCooldown(long currentTime) {
        this.nextShotTime = currentTime + DEFAULT_SHOOT_COOLDOWN;
    }

    /*Serão implementadas por Enemy1 e Enemy2*/
    public abstract boolean isOnScreen();

    public abstract boolean shouldShoot();
    
}