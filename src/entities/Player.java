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
    public void explosion(long currentTime)
    {
        player.setState(States.EXPLODING);
        player.setExplosionStart(currentTime);
        player.setExplosionEnd(currentTime + 2000);
    }
    public void move(double delta)
    {
        if(player.getState() == States.ACTIVE)
        {
            if(GameLib.iskeyPressed(GameLib.KEY_UP)) player.setY(player.getY() - delta * player.getVy());
            if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) player.setY(player.getY() + delta * player.getVy());
            if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) player.setX(player.getX() - delta * player.getVx());
            if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) player.setX(player.getX() + delta * player.getVx());

            if(player.getX() < 0.0) player.setX(0.0);
			if(player.getX() >= GameLib.WIDTH) player.setX(GameLib.WIDTH - 1);
			if(player.getY() < 25.0) player.setY(25.0);
			if(player.getY() >= GameLib.HEIGHT) player.setY(GameLib.HEIGHT - 1);
        }
    }
    public long getNextShot()
    {
        return nextShot;
    }

    public void shoot(long currentTime)
    {
        nextShot = currentTime + 100;
    }

    public void exploded(long currentTime)
    {
        if(player.getState() == States.EXPLODING)
        {
            if(currentTime > player.getExplosionEnd())
            {
                player.setState(States.ACTIVE);
            }
        }
    }
    
}