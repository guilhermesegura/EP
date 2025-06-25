package entities;

import utils.*;

public class ShrinkPowerUp extends PowerUp 
{

    private long startTime;

    public ShrinkPowerUp(Coordinate coordinate, Coordinate velocity, double radius) {
        super(coordinate, velocity, radius);
    }


    public void onCollected(Player player)
    {   
        if(player.getRadius() > player.getMinRadius()) {
            setStartTime(System.currentTimeMillis());
            player.setRadius(player.getRadius() / 2.0);
            setState(States.EXPLODING);
        }
    }

    public void update(Player player) {
        if(startTime + 5000 <= System.currentTimeMillis() && getState() == States.EXPLODING)
        {   
            player.setRadius(player.getRadius() * 2.0);
            this.setState(States.INACTIVE);
        }
    }

    public void setStartTime(long startTime)
    {
        this.startTime = startTime;
    }

    public long getStartTime()
    {
        return startTime;
    }
}