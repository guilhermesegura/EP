package entities;

import utils.*;

public class Player extends ExplodableEntity implements ICollidable {

    long nextShot;

    public Player(Coordinate coordinate, Coordinate velocity, double radius)
    {
        super(coordinate, velocity, States.ACTIVE, radius);
        nextShot = System.currentTimeMillis();
    }

    public void update(long delta) {
        // Trata estado de explosão
        if (getState() == States.EXPLODING) {
            if (System.currentTimeMillis() > getExplosionEnd()) 
                setState(States.ACTIVE);
            }

        if(getState() == States.ACTIVE)
        {
            if(GameLib.iskeyPressed(GameLib.KEY_UP)) setY(getY() - delta * getVy());
            if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) setY(getY() + delta * getVy());
            if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) setX(getX() - delta * getVx());
            if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) setX(getX() + delta * getVx());

            if(getX() < 0.0) setX(0.0);
            if(getX() >= GameLib.WIDTH) setX(GameLib.WIDTH - 1);
            if(getY() < 25.0) setY(25.0);
            if(getY() >= GameLib.HEIGHT) setY(GameLib.HEIGHT - 1);
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
}