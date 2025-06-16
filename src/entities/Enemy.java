package entities;
import utils.*;

public class Enemy {
    private Entity enemy;
    private static int count;

    public Enemy(double x, double y, double vX, double vY, double radius) {
        Coordinate cor = new Coordinate(x, y);
        Coordinate vel = new Coordinate(vX, vY);
        States state = States.ACTIVE;
        this.enemy = new Entity(cor, vel, state, radius);
        count++;
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
}