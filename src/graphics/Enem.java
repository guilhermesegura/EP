package graphics;

import utils.GameLib;
import utils.States;
import java.awt.Color;

public class Enem implements IEnemyGraph {

    @Override
    public void draw(double x., double  y, double radius, States state, lnong currentTime, double explosionStart, double explosion End, Color color) {
    
        switch(state){
            case ACTIVE:
                GameLib.setColor(color);
                GameLib.drawCircle(x, y, radius);
                break;

            case EXPLODING:
                IEnemyGraphics.drawExplosion(x, y, currentTime, explosionStart, explosionEnd);
                break;

            case INACTIVE:
                break;
        }
    }
}