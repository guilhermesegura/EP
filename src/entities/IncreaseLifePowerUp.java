package entities;
import utils.*;

public class IncreaseLifePowerUp extends PowerUp {
    public IncreaseLifePowerUp(Coordinate coordinate, Coordinate velocity, double radius) {
        super(coordinate, velocity, radius);
    }

    @Override
    public void update(long deltaTime) {  // Changed to match parent class
        if (getState() == States.ACTIVE) {
            double newX = getX() + getVx() * deltaTime;
            double newY = getY() + getVy() * deltaTime;
            setX(newX);
            setY(newY);
            
            // Deactivate if goes off-screen
            if (newY > GameLib.HEIGHT + 10) {
                setState(States.INACTIVE);
            }
        }
    }

    @Override
    public void onCollected(Player player) {
        if (getState() == States.ACTIVE) {
            if(player.getHealth() < player.getMaxHealth()) {
                player.setHealth(player.getHealth() + 1);  // Use the proper method
                 setState(States.EXPLODING);
            }     
        }
    }
}