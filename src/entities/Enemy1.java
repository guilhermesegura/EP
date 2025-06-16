package entities;
import utils.*;

public class Enemy1 extends Enemy {
    
    public Enemy1(double x, double y, double vX, double vY, double radius, double angle, double RV) {
        super(x, y, vX, vY, radius, angle, RV);
    }

    public boolean isOnScreen() {
        if(super.getState() == States.ACTIVE) {
            if(super.getY() > 0 || super.getY() <= GameLib.HEIGHT || super.getX() > 0 || super.getX() > GameLib.WIDTH) {
                return true;
            }
        }
        super.setState(States.INACTIVE);
        return false;
    }

    public void move(long delta) {
        if(isOnScreen()) {
           double newX = super.getX() + (super.getVx() * Math.cos(super.getAngle()) * delta);
           super.setX(newX);
           double newY = super.getY() + (super.getVy() * Math.sin(super.getAngle()) * delta * (-1.0));
           super.setY(newY);
           double newAngle = super.getAngle() + (super.getRV() * delta);
           super.setAngle(newAngle);
        }
    }

    

}