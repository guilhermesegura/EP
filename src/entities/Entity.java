package entities;
import utils.*;
import entities.interfaces.*;

public abstract class Entity implements IEntity {

    private Coordinate coordinates;
    private Coordinate velocity;
    private States state;
    private double radius;


    public Entity(Coordinate coordinate, Coordinate velocity, States state, double radius) {
        this.coordinates = coordinate;
        this.velocity = velocity;
        this.state = state;
        this.radius = radius;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinate getVelocity() {
        return velocity;
    }

    public void setVelocity(Coordinate velocity) {
        this.velocity = velocity;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getX() {
        return this.coordinates.getX();
    }

    public void setX(double x) {
        this.coordinates.setX(x);
    }

    public double getY() {
        return this.coordinates.getY();
    }

    public void setY(double y) {
        this.coordinates.setY(y);
    }

    public double getVx() {
        return this.velocity.getX();
    }

    public void setVx(double vx) {
        this.velocity.setX(vx);
    }

    public double getVy() {
        return this.velocity.getY();
    }

    public void setVy(double vy) {
        this.velocity.setY(vy);
    }

	public Coordinate getCoordinate() {
        return this.coordinates;
	}

}