package entities;
import java.util.List;
import utils.Coordinate;
import utils.GameLib;
import utils.States;

public abstract class Boss extends Enemy{

    private long lastAttackTime;
    long attackCooldown = 500; 
    private int maxHealth;
    private static final double MAX_RADIUS = 30.0; //tamanho dos Bosses

    //ATRIBUTOS DE VELOCIDADE
    private static final double MAX_VX = 0.1;
    private static final double MAX_VY = 0.05;

    public Boss(Coordinate coordinate,int maxHealth)
    {
        super(coordinate, new Coordinate(MAX_VX, MAX_VY), States.ACTIVE, MAX_RADIUS, maxHealth);
        this.lastAttackTime = System.currentTimeMillis();
        this.maxHealth = maxHealth;
    }

    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime > attackCooldown && getState() == States.ACTIVE;
    }

    public boolean isOnScreen() {
        return getX() > -getRadius() && getX() < GameLib.WIDTH + getRadius() &&
               getY() > -getRadius() && getY() < GameLib.HEIGHT + getRadius();
    }

    public boolean shouldShoot() {
        return canAttack();
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public double getMaxRadius() {
        return MAX_RADIUS;
    }

    public long getLastAttackTime()
    {
        return lastAttackTime;
    }

    public void setLastAttackTime(long l)
    {
        this.lastAttackTime = l;
    }

    public void setAttackCooldown(long l)
    {
        attackCooldown = l;
    }

    public void shoot(long currentTime, List<Projectiles> enemyProjectiles)
    {
        if (shouldShoot()) {
                    performAttack(enemyProjectiles);
                }
    }

    public abstract void update(long delta);
    public abstract void performAttack(List<Projectiles> enemyProjectiles);
    
    public void takeDamage(int damage) {
        if (getState() != States.ACTIVE) return;

        setHealth(getHealth() - damage);
        if (getHealth() <= 0) {
            setHealth(0);
            explosion(System.currentTimeMillis());
            setState(States.INACTIVE);
        }
    }
}
