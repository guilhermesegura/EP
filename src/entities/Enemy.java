package entities;
import java.util.List;

import utils.*;

public abstract class Enemy extends ExplodableEntity{
    protected static final long DEFAULT_SHOOT_COOLDOWN = 1000; 
    
    protected long nextShotTime;
    protected double angle;
    protected double rotationalVelocity;  

    public Enemy(Coordinate coordinate, Coordinate velocity, States state, double radius, int health)  {
        super(coordinate, velocity, state, radius, health);
        this.nextShotTime = System.currentTimeMillis() + DEFAULT_SHOOT_COOLDOWN;
        this.angle = (3 * Math.PI) / 2.0;
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

    public boolean isShotCooldownOver(long currentTime) {
        return currentTime > nextShotTime;
    }
    
    public void resetShotCooldown(long currentTime) {
        this.nextShotTime = currentTime + DEFAULT_SHOOT_COOLDOWN;
    }

    /*Serão implementadas por Enemy1 e Enemy2*/
    public abstract boolean isOnScreen();

    public abstract boolean shouldShoot();

    public PowerUp maybeSpawnPowerUp(double x, double y) {
        if (Math.random() < 0.15) {  // 15% de chance de spawn

            double speedY = 0.1;
            Coordinate coord = new Coordinate(x, y);
            Coordinate velocity = new Coordinate(0, speedY);

            // Decide entre os dois tipos de power up
            if (Math.random() < 0.5) {
                return new IncreaseLifePowerUp(coord, velocity, 10.0);
            } else {
                return new ShrinkPowerUp(coord, velocity, 10.0);
            }
        }
        return null;
    }

    public abstract void shoot(long currentTime, List<Projectiles> playerProjectiles);

}