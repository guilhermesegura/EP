package entities;

import utils.*;


public abstract class PowerUp extends Entity {

    public PowerUp(Coordinate coordinate, Coordinate velocity, double radius) {
        super(coordinate, velocity, States.ACTIVE, radius);
    }

    public void update(long deltaTime) {
        // Atualiza a posição com base na velocidade
        double newX = getX() + getVx() * deltaTime;
        double newY = getY() + getVy() * deltaTime;
        setX(newX);
        setY(newY);
    }


    public abstract void onCollected(Player player);


}
