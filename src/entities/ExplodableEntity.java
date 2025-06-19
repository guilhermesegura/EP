package entities;

import utils.Coordinate;
import utils.States;

public abstract class ExplodableEntity extends Entity implements IExplodable{

    private double explosion_start;
    private double explosion_end;

    public ExplodableEntity(Coordinate coordinate, Coordinate velocity, States state, double radius)
    {
        super(coordinate, velocity, state,radius);
        explosion_end = 0.0;
        explosion_start = 0.0;
    }
    
    public double getExplosionStart() {
        return explosion_start;
    }

    public void setExplosionStart(double explosion_start) {
        this.explosion_start = explosion_start;
    }

    public double getExplosionEnd() {
        return explosion_end;
    }

    public void setExplosionEnd(double explosion_end) {
        this.explosion_end = explosion_end;
    }

    public void explosion(long currentTime)
    {
        setState(States.EXPLODING);
        setExplosionStart(currentTime);
        setExplosionEnd(currentTime + 2000);
    }

    public void TakeDamage()
    {
        return;
    }

}
