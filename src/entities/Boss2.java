package entities;

import utils.*;
import java.util.List;


public class Boss2 extends Boss {

    public Boss2(Coordinate coordinate, Coordinate velocity) {
        super(coordinate, velocity);
    }

    @Override
    public void performAttack(List<Projectiles> enemyProjectiles) {
        if (!canAttack()) return;

        // Ataque simples: tiro único para baixo com dano 1
        enemyProjectiles.add(new Projectiles(
            new Coordinate(getX(), getY()),
            new Coordinate(0, 0.4),
            5.0,
            Projectiles.ENEMY_PROJECTILE,
            1
        ));
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
