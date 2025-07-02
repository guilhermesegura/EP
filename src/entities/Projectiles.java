package entities;

import utils.*;

public class Projectiles extends Entity {

    // Tipos de projéteis
    public static final int PLAYER_PROJECTILE = 1;
    public static final int ENEMY_PROJECTILE = 2;

    // Constantes encapsuladas
    private static final double PLAYER_RADIUS = 2.0;
    private static final int PLAYER_DAMAGE = 1;

    private static final double ENEMY_RADIUS = 2.0;
    private static final int ENEMY_DAMAGE = 1;

    private int type;
    private int damage;

    public Projectiles(Coordinate coordinate, Coordinate velocity, int type) {
        super(coordinate, velocity, States.ACTIVE, getRadiusByType(type));
        this.type = type;
        this.damage = getDamageByType(type);
    }

    // Construtor alternativo para projéteis customizados (ex: do boss)
    public Projectiles(Coordinate coordinate, Coordinate velocity, double radius, int type, int damage) {
        super(coordinate, velocity, States.ACTIVE, radius);
        this.type = type;
        this.damage = damage;
    }

    private static double getRadiusByType(int type) {
        switch (type) {
            case PLAYER_PROJECTILE: return PLAYER_RADIUS;
            case ENEMY_PROJECTILE: return ENEMY_RADIUS;
            default: return 2.0;
        }
    }

    private static int getDamageByType(int type) {
        switch (type) {
            case PLAYER_PROJECTILE: return PLAYER_DAMAGE;
            case ENEMY_PROJECTILE: return ENEMY_DAMAGE;
            default: return 1;
        }
    }

    public void update(long delta) {
        if (getState() == States.INACTIVE) return;

        setX(getX() + getVx() * delta);
        setY(getY() + getVy() * delta);

        if (outOfBounds()) setState(States.INACTIVE);
    }

    public boolean outOfBounds() {
        return (type == PLAYER_PROJECTILE && getY() < 0) ||
               (type != PLAYER_PROJECTILE && getY() > GameLib.HEIGHT);
    }

    public int calculateDamage(Entity other) {
        return damage;
    }

    public int getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }
}
