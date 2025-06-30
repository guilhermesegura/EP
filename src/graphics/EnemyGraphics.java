package graphics;

import entities.*;

import utils.*;
import java.awt.Color;
import java.util.*;

public class EnemyGraphics
{
    public static void enemy(long currentTime, List<Enemy> enemies, Color color, double r)
    {
        for(Enemy enemy: enemies)
        {
            if(enemy.getState() == States.EXPLODING)
            {
                double alpha = (currentTime - enemy.getExplosionStart()) / (enemy.getExplosionEnd() - enemy.getExplosionStart());
                GameLib.drawExplosion(enemy.getX(), enemy.getY(), alpha);
            }
            if(enemy.getState() == States.ACTIVE)
            {
                if(enemy instanceof Enemy1)
                {
                    GameLib.setColor(Color.CYAN);
					GameLib.drawCircle(enemy.getX(), enemy.getY(), enemy.getRadius());
                }
                if(enemy instanceof Enemy2)
                {
                    GameLib.setColor(Color.MAGENTA);
					GameLib.drawDiamond(enemy.getX(), enemy.getY(), enemy.getRadius());
                }
            }
        }
    }
}
