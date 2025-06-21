package entities;

import utils.*;
import java.util.List;
import entities.interfaces.*;

public class Boss2 extends Boss {

    int cycle_counter;
    IPlayerCoord player;
    public Boss2(Coordinate coordinate, Coordinate velocity, double r, IPlayerCoord p) {
        super(coordinate, velocity, 20);
        super.setAttackCooldown(0);
        this.cycle_counter = 0;
        this.player = p;
    }

    @Override
    public void performAttack(List<Projectiles> enemyProjectiles) {
        if (!canAttack()) return;

        // Ataque simples: tiro único para baixo com dano 1
        if(cycle_counter < 100)
        {
            if(cycle_counter % 25 == 0){
                Coordinate direction = new Coordinate(player.getCoordinate().getX() - getX(), player.getCoordinate().getY() - getY());
                double size_direction = Math.sqrt((direction.getX()* direction.getX())  + (direction.getY() * direction.getY()));
                Coordinate normdirection = new Coordinate(direction.getX()/size_direction, direction.getY()/size_direction);

                enemyProjectiles.add(new Projectiles(
                new Coordinate(getX() + 40, getY()),
                new Coordinate(normdirection.getX() * 0.5 , normdirection.getY() * 0.5),
                10,
                Projectiles.ENEMY_PROJECTILE,
                1
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
            1
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
}
