package entities;
import utils.*;

public class Projectiles extends Entity implements ICollidable{
    // Tipos de projéteis
    public static final int PLAYER_PROJECTILE = 1;
    public static final int ENEMY_PROJECTILE = 2;

    private int type;
    private int damage;

    public Projectiles(Coordinate coordinate, Coordinate velocity, double radius, int type, int damage) {
        super(coordinate, velocity, States.ACTIVE, radius);
        this.type = type;
        this.damage = damage;
    }

    // Métodos específicos de Projectiles
    public boolean outOfBounds() {
        if (type == PLAYER_PROJECTILE) {
            return getY() < 0;
        } else {
            return getY() > GameLib.HEIGHT;
        }
    }

    public void  update(long delta) {
        if (getState() == States.INACTIVE) {
            return;
        }
        
        // Atualiza posição
        setX(getX() + getVx() * delta);
        setY(getY() + getVy() * delta);

        // Verifica se saiu da tela
        if (outOfBounds()) {
            setState(States.INACTIVE);
            return;
        }

        return;
    }

    public int calculateDamage(Entity other) {
        // Lógica de cálculo de dano pode ser implementada aqui
        return damage;  // Retorna o dano base por padrão
    }

    public int getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }
}