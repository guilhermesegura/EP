package graphics;

import entities.*;
import utils.*;
import java.awt.Color;
import java.util.*;

public class PowerUpGraphics {

    public static void drawPowerUps(long currentTime, List<PowerUp> powerUps, double r) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.getState() == States.ACTIVE) {
                if(powerUp instanceof ShrinkPowerUp) {
                    GameLib.setColor(Color.YELLOW); 
                    GameLib.drawCircle(powerUp.getX(), powerUp.getY(), r);  
                } else {
                    GameLib.setColor(Color.PINK); 
                    GameLib.drawCircle(powerUp.getX(), powerUp.getY(), r);  
                }
            }
        }
    }
}
