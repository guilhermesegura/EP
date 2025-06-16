package entities;
import utils.*;

public class Projectiles {
    // Tipos de projéteis
    public static final int PLAYER_PROJECTILE = 1;
    public static final int ENEMY_PROJECTILE = 2;

    private Entity entity;  
    private int type;
    private int damage;

    public Projectiles(Coordinate coordinate, Coordinate velocity, double radius, int type, int damage) {
        this.entity = new Entity(coordinate, velocity, States.ACTIVE, radius);
        this.type = type;
        this.damage = damage;
    }

    // Métodos delegados para a Entity
    public double getX() {
        return entity.getX();
    }

    public void setX(double x) {
        entity.setX(x);
    }

    public double getY() {
        return entity.getY();
    }

    public void setY(double y) {
        entity.setY(y);
    }

    public double getVx() {
        return entity.getVx();
    }

    public void setVx(double vx) {
        entity.setVx(vx);
    }

    public double getVy() {
        return entity.getVy();
    }

    public void setVy(double vy) {
        entity.setVy(vy);
    }

    public States getState() {
        return entity.getState();
    }

    public void setState(States state) {
        entity.setState(state);
    }

    public double getRadius() {
        return entity.getRadius();
    }

    public double getExplosionStart() {
        return entity.getExplosionStart();
    }

    public void setExplosionStart(double explosion_start) {
        entity.setExplosionStart(explosion_start);
    }

    public double getExplosionEnd() {
        return entity.getExplosionEnd();
    }

    public void setExplosionEnd(double explosion_end) {
        entity.setExplosionEnd(explosion_end);
    }

    // Métodos específicos de Projectiles
    public boolean outOfBounds() {
        if (type == PLAYER_PROJECTILE) {
            return getY() < 0;
        } else {
            return getY() > GameLib.HEIGHT;
        }
    }

    public boolean update(long delta) {
        if (getState() == States.INACTIVE) {
            return false;
        }

        // Atualiza posição
        setX(getX() + getVx() * delta);
        setY(getY() + getVy() * delta);

        // Verifica se saiu da tela
        if (outOfBounds()) {
            setState(States.INACTIVE);
            return false;
        }

        // Trata estado de explosão
        if (getState() == States.EXPLODING) {
            if (System.currentTimeMillis() > getExplosionEnd()) {
                setState(States.INACTIVE);
                return false;
            }
        }

        return true;
    }

    public int calculateDamage(Entity other) {
        // Lógica de cálculo de dano pode ser implementada aqui
        return damage;  // Retorna o dano base por padrão
    }

    public void explosion(long currentTime) {
        setState(States.EXPLODING);
        setExplosionStart(currentTime);
        setExplosionEnd(currentTime + 2000);
    }

    public boolean collision(Entity other) {
        if (getState() != States.ACTIVE || other.getState() != States.ACTIVE) {
            return false;
        }

        double dx = getX() - other.getX();
        double dy = getY() - other.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < (getRadius() + other.getRadius()) * 0.8;
    }

    public int getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }
}