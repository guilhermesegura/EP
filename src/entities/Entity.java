package entities;
import utils.*;

public class Entity {
    private Coordinate coordinates;
    private Coordinate velocity;
    private States state;
    private double radius;
    private double explosion_start;
    private double explosion_end;

    public Entity(Coordinate coordinate, Coordinate velocity, States state, double radius) {
        this.coordinates = coordinate;
        this.velocity = velocity;
        this.state = state;
        this.radius = radius;
        this.explosion_start = 0.00;
        this.explosion_end = 0.00;
    }

    // Getters and Setters for explosion_start and explosion_end
    public double getExplosionStart() {
        return explosion_start;
    }

    public void setExplosionStart(double explosion_start) {
        this.explosion_start = explosion_start;
    }

    public double getExplosionEnd() {
        return explosion_end;
    }

    public void setExplosionEnd(double explosisting methods (getters/setters for coordinates, velocity, etc.)
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
}