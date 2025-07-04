package entities;

import entities.interfaces.*;
import java.util.*;
import utils.*;

public class Player extends ExplodableEntity implements IPlayerCoord {

    private long nextShot;
    private long lastHitTime;
    private final long invulnerabilityTime = 1000; // 1 second
    private long blinkStartTime;
    private boolean isBlinking = false;
    private int maxHealth; // variável que salva a vida do jogador após a configuração
    private static final long BLINK_DURATION = 1000; // 1 second blinking
    private static final long BLINK_INTERVAL = 100; // Blink every 100ms
    private static final double MAX_RADIUS = 12.0;   // Player radius
    private static final double MIN_RADIUS = 6.0;    // Player radius

    public Player(Coordinate coordinate, Coordinate velocity, int maxHealth) {
        super(coordinate, velocity, States.ACTIVE, MAX_RADIUS, maxHealth);
        this.nextShot = System.currentTimeMillis();
        this.lastHitTime = 0;
        this.blinkStartTime = 0;
        this.maxHealth = maxHealth;
    }

    public void update(long delta) {
        if (getState() == States.EXPLODING) {
            return;
        }

        if (getState() == States.ACTIVE) {
            handleMovement(delta);
            handleBoundaries();
        }
    }

    private void handleMovement(long delta) {
        if (GameLib.iskeyPressed(GameLib.KEY_UP))    setY(getY() - delta * getVy());
        if (GameLib.iskeyPressed(GameLib.KEY_DOWN))  setY(getY() + delta * getVy());
        if (GameLib.iskeyPressed(GameLib.KEY_LEFT))  setX(getX() - delta * getVx());
        if (GameLib.iskeyPressed(GameLib.KEY_RIGHT)) setX(getX() + delta * getVx());
    }

    private void handleBoundaries() {
        if (getX() < 0.0) setX(0.0);
        if (getX() >= GameLib.WIDTH) setX(GameLib.WIDTH - 1);
        if (getY() < 25.0) setY(25.0);
        if (getY() >= GameLib.HEIGHT) setY(GameLib.HEIGHT - 1);
    }

    

    @Override
    public void takeDamage(int damage) {
        if (getState() != States.ACTIVE || isInvulnerable()) return;

        setHealth(getHealth() - damage);
        this.lastHitTime = System.currentTimeMillis();
        this.blinkStartTime = System.currentTimeMillis();
        this.isBlinking = true;

        if (getHealth() <= 0) {
            setHealth(0);
            explosion(System.currentTimeMillis());
        }
    }


    public void heal(int amount) {
        setHealth(Math.min(getHealth() + amount, getMaxHealth()));
    }

    public void setFullHealth() {
        setHealth(getMaxHealth());
    }

    public boolean shouldDrawPlayer(long currentTime) {
        if (!isBlinking) return true;

        if (currentTime > blinkStartTime + BLINK_DURATION) {
            isBlinking = false;
            return true;
        }

        return ((currentTime - blinkStartTime) / BLINK_INTERVAL) % 2 == 0;
    }

    public long getNextShot() {
        return nextShot;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public double getMaxRadius() {
        return MAX_RADIUS;
    }

    public double getMinRadius() {
        return MIN_RADIUS;
    }


    public boolean isInvulnerable() {
        return System.currentTimeMillis() < lastHitTime + invulnerabilityTime;
    }

    public void shoot(long currentTime, List<Projectiles> playerProjectiles)
    {
        if (GameLib.iskeyPressed(GameLib.KEY_CONTROL) && currentTime > getNextShot()) {
                playerProjectiles.add(new Projectiles(
                    new Coordinate(getX(), getY() - 2 * getRadius()),
                    new Coordinate(0.0, -1.0),
                    2.0,
                    Projectiles.PLAYER_PROJECTILE,
                    2
                ));
                nextShot = currentTime + 200;
    }

}


    public void reset(Coordinate position, Coordinate velocity, int maxHealth) {
            setCoordinates(position);
            setVelocity(velocity);
            this.maxHealth = maxHealth;
            setHealth(maxHealth);
            setState(States.ACTIVE);
            this.nextShot = System.currentTimeMillis();
            this.lastHitTime = 0;
            this.blinkStartTime = 0;
            this.isBlinking = false;
            setExplosionEnd(0);
        }
}
