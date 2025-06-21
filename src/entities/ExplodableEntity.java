package entities;

import utils.*;
import entities.interfaces.*;

public abstract class ExplodableEntity extends Entity {
    private int health;
    private double explosionStart;
    private double explosionEnd;

    public ExplodableEntity(Coordinate coordinate, Coordinate velocity, States state, double radius, int health) {
        super(coordinate, velocity, state, radius);
        this.health = health;
        this.explosionStart = 0;
        this.explosionEnd = 0;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void takeDamage(double damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.setState(States.EXPLODING);
        }
    }

    public double getExplosionStart() {
        return explosionStart;
    }

    public void setExplosionStart(double explosionStart) {
        this.explosionStart = explosionStart;
    }

    public double getExplosionEnd() {
        return explosionEnd;
    }

    public void setExplosionEnd(double explosionEnd) {
        this.explosionEnd = explosionEnd;
    }

    public boolean isExploding(long currentTime) {
        return this.getState() == States.EXPLODING && 
               currentTime >= explosionStart && 
               currentTime <= explosionEnd;
    }

    public boolean hasFinishedExploding(long currentTime) {
        return this.getState() == States.EXPLODING && 
               currentTime > explosionEnd;
    }
      public void explosion(long currentTime)
    {
        setState(States.EXPLODING);
        setExplosionStart(currentTime);
        setExplosionEnd(currentTime + 500);
    }

}