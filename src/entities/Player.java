package entities;

import utils.*;

public class Player{
    Entity player;
    long nextShot;

    public Player(States state, double x, double y, double vx, double vy, double radius, long currentTime)
    {
        player = new Entity(new Coordinate(x, y), new Coordinate(vx, vy), States.ACTIVE, radius);
        nextShot = System.currentTimeMillis();
    }
    public States getState()
    {
        return player.getState();
    }
    public void setState(States state)
    {
        player.setState(state);
    }
    public void setExplosion()
    {
        player.setState(States.EXPLODING);
        player.se
    }

    
}