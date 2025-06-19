package entities;

import utils.Coordinate;
import utils.States;

public interface IEntity extends ICollidable{

    public Coordinate getCoordinates();
    public void setCoordinates(Coordinate coordinates);
    public Coordinate getVelocity();
    public void setVelocity(Coordinate velocity);
    public States getState();
    public void setState(States state);
    public double getRadius();
    public void setRadius(double radius);
    public double getX();
    public double getY();
    public double getVx();
    public double getVy();
    public void update(long delta);

}
