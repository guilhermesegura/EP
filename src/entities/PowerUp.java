package entities;

import utils.*;
import entities.interfaces.*;


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


    public void onCollected(Player player) {
        // switch (type) {
        //     case EXTRA_LIFE:
        //         player.increaseLives();
        //         break;
        //     case SMALL_PLAYER:
        //         player.shrink();
        //         break;
        // }
        // // Marca o power-up como coletado (morto)
        // setState(States.INACTIVE);
    }


}
