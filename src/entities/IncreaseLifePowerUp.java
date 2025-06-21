package entities;

import utils.*;

public class IncreaseLifePowerUp extends PowerUp 
{
    public IncreaseLifePowerUp(Coordinate coordinate, Coordinate velocity, double radius) {
        super(coordinate, velocity, radius);
    }

    public void update(long deltaTime, Player player) {
        double newX = getX() + getVx() * deltaTime;
        double newY = getY() + getVy() * deltaTime;
        setX(newX);
        setY(newY);
        if(getState() == States.EXPLODING)
        {
            setState(States.INACTIVE);
        }
    }

    public void onCollected(Player player) {
        player.setHealth(player.getHealth() + 1);
        setState(States.EXPLODING);
    }
}