package entities;
import java.util.List;
import utils.Coordinate;
import utils.GameLib;
import utils.States;

public class Boss1 extends Boss {

    private int attackPattern = 0;

    public Boss1(Coordinate coordinate,int maxHealth) {
        super(coordinate, maxHealth);
    }

    
    public void update(long delta) {
        if (getState() == States.EXPLODING) {
            if (System.currentTimeMillis() > getExplosionEnd()) {
                setState(States.INACTIVE);
            }
            return;
        }

        if (getState() == States.ACTIVE) {
            // Movimento horizontal com reversão ao tocar a borda
            setX(getX() + getVx() * delta);
            if (getX() < getRadius() || getX() > GameLib.WIDTH - getRadius()) {
                setVx(getVx() * -1);
            }

            // Movimento vertical até 30% da tela
            setY(getY() + getVy() * delta);
            if (getY() > GameLib.HEIGHT * 0.3) {
                setVy(0);
            }
        }
    }

    public boolean canAttack() {
        return System.currentTimeMillis() - super.getLastAttackTime() > attackCooldown && getState() == States.ACTIVE;
    }

    public void performAttack(List<Projectiles> enemyProjectiles) {
        super.setLastAttackTime(System.currentTimeMillis());
        switch (attackPattern) {
            case 0:
                // Disparo em leque
                for (int i = -2; i <= 2; i++) {
                    double angle = Math.PI / 2 + i * (Math.PI / 8);
                    double vx = Math.cos(angle) * 0.3;
                    double vy = Math.sin(angle) * 0.3;
                    enemyProjectiles.add(new Projectiles(
                        new Coordinate(getX() , getY()),
                        new Coordinate(vx, vy),
                        5.0,
                        Projectiles.ENEMY_PROJECTILE,
                        1
                    ));
                }  
                break;
                
            case 1:
                // Disparo único forte
                enemyProjectiles.add(new Projectiles(
                    new Coordinate(getX(), getY()),
                    new Coordinate(0, 0.2),
                    20.0,
                    Projectiles.ENEMY_PROJECTILE,
                    4
                ));
                break;
        }

        attackPattern = (attackPattern + 1) % 2;
    }
    

    

}
