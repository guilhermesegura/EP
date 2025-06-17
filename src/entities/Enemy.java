package entities;
import utils.*;

public class Enemy {
    private Entity enemy;
    private static int count;
    private long nextShot;
    private double angle;
    private double RV;  

    public Enemy(double x, double y, double vX, double vY, double radius, double angle, double RV) {
        Coordinate cor = new Coordinate(x, y);
        Coordinate vel = new Coordinate(vX, vY);
        States state = States.ACTIVE;
        nextShot = System.currentTimeMillis();
        this.angle = angle;
        this.RV = RV;
        count++;        
        this.enemy = new Entity(cor, vel, state, radius);
    }

    public States getState() {
        return enemy.getState();
    }
    
    public void setState(States state) {
        enemy.setState(state);
    }
    
    public Coordinate getCoordinates() {
        return enemy.getCoordinates();
    }
    
    public Coordinate getVelocity() {
        return enemy.getVelocity();
    }
    
    public double getRadius() {
        return enemy.getRadius();
    }
    
    public static int getCount() {
        return count;
    }
    
    public static void setCount(int count) {
        Enemy.count = count;
    }
    
    public long getNextShot() {
        return nextShot;
    }
    
    public void setNextShot(long nextShot) {
        this.nextShot = nextShot;
    }
    
    public double getAngle() {
        return angle;
    }
    
    public void setAngle(double angle) {
        this.angle = angle;
    }
    
    public double getRV() {
        return RV;
    }
    
    public void setRV(double RV) {
        this.RV = RV;
    }
    
    public double getX() {
        return enemy.getX();
    }
    
    public double getY() {
        return enemy.getY();
    }
    
    public double getVx() {
        return enemy.getVx();
    }
    
    public double getVy() {
        return enemy.getVy();
    }

    public double getExplosionStart() {
        return enemy.getExplosionStart();
    }

    public double getExplosionEnd() {
        return enemy.getExplosionEnd();
    }

    public void setY(double y){
        enemy.setY(y);
    }

    public void setX(double x){
        enemy.setX(x);
    }

    public void setVy(double vy){
        enemy.setVy(vy);
    }

    public void setVx(double vx){
        enemy.setVx(vx);
    }

    public void setExplosionStart(double explosion_start) {
        enemy.setExplosionStart(explosion_start);
    }

    public void setExplosionEnd(double explosion_end) {
        enemy.setExplosionEnd(explosion_end);
    }

    public void explosion(long currentTime) {
        enemy.setState(States.EXPLODING);
        enemy.setExplosionStart(currentTime);
        enemy.setExplosionEnd(currentTime + 2000);
    }
}