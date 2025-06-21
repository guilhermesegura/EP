package entities;

import utils.*;
import entities.interfaces.*;


public class Collision{
    
    public static boolean VerifyColision(ICollidable a , ICollidable b){
        if (a.getState() != States.ACTIVE || b.getState() != States.ACTIVE) {
            return false;
        }
        
        //Pega as coordenadas de A e B
        Coordinate CoordsA = a.getCoordinate();
        Coordinate CoordsB = b.getCoordinate();

        //Calcula distancia de A em relação a B
        double dx = CoordsA.getX() - CoordsB.getX();
        double dy = CoordsA.getY() - CoordsB.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        //Retorna se distancia é menor que o raio dos dois colidiveis
        return distance < (a.getRadius() + b.getRadius()) * 0.8;
    }
}