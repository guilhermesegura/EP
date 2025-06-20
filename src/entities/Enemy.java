package entities;
import utils.*;

public abstract class Enemy extends ExplodableEntity implements Spawnable{
    protected static final long DEFAULT_SHOOT_COOLDOWN = 1000; 
    protected static final long DEFAULT_SPAWN_COOLDOWN = 2000;
    
    protected long nextShotTime;
    protected double angle;
    protected double rotationalVelocity;  
    protected int spawnCount = 0;
    
    public Enemy(Coordinate coordinate, Coordinate velocity, States state, double radius)  {
        super(coordinate, velocity, state, radius);
        this.nextShotTime = System.currentTimeMillis() + DEFAULT_SHOOT_COOLDOWN;
        this.nextSpawnTime = System.currentTimeMillis() + DEFAULT_SPAWN_COOLDOWN;
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

    public boolean shouldSpawn(long currentTime){
        return currentTime > nextSpawnTime;
    }

    public void updateSpawnTimer(long currentTime) {
        this.nextSpawnTime = currentTime + 
    }

    public 
}