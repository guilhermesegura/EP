package entities;

import entities.interfaces.*;
import java.util.List;
import utils.*;

public class Boss2 extends Boss {
    int cycle_counter;
    IPlayerCoord player;
    
    public Boss2(Coordinate coordinate, int health, IPlayerCoord p) {
        super(coordinate, health);
        super.setAttackCooldown(0);
        this.cycle_counter = 0;
        this.player = p;
    }

    @Override
    public void performAttack(List<Projectiles> enemyProjectiles) {
        if (!canAttack()) return;

        
        if(cycle_counter < 100)
        {
            if(cycle_counter % 50 == 0){
                Coordinate direction = new Coordinate(player.getCoordinate().getX() - getX(), player.getCoordinate().getY() - getY());
                double size_direction = Math.sqrt((direction.getX()* direction.getX())  + (direction.getY() * direction.getY()));
                Coordinate normdirection = new Coordinate(direction.getX()/size_direction, direction.getY()/size_direction);

                enemyProjectiles.add(new Projectiles(
                new Coordinate(getX() + 40, getY()),
                new Coordinate(normdirection.getX() * 0.6 , normdirection.getY() * 0.6),
                10,
                Projectiles.ENEMY_PROJECTILE,
                3
            ));
        }
            cycle_counter++;
            return;
        }
        enemyProjectiles.add(new Projectiles(
            new Coordinate(getX() + 40, getY()),
            new Coordinate(0, 0.4),
            10,
            Projectiles.ENEMY_PROJECTILE,
            2
        ));
        cycle_counter++;
        if(cycle_counter > 600) cycle_counter = 0;
        setLastAttackTime(System.currentTimeMillis());  
    }

    protected void setLastAttackTime(long time) {
        try {
            java.lang.reflect.Field field = Boss.class.getDeclaredField("lastAttackTime");
            field.setAccessible(true);
            field.setLong(this, time);
        } catch(Exception e) {
            e.printStackTrace();
        }
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
            if (getX() < -1*getRadius() || getX() > GameLib.WIDTH - getRadius()) {
                setVx(getVx() * -1);
            }

            // Movimento vertical até 30% da tela
            setY(getY() + getVy() * delta);
            if (getY() > GameLib.HEIGHT * 0.3) {
                setVy(0);
            }
        }
    }
}
