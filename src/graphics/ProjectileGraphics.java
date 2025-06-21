package graphics;
import entities.Projectiles;
import utils.*;
import java.awt.Color;
import java.util.*;
public class ProjectileGraphics {

    public static void projectiles(List<Projectiles> projectiles, Color color)
    {

        for(Projectiles projectile: projectiles)
        {
            if(projectile.getState() == States.ACTIVE)
            {
                GameLib.setColor(color);
                GameLib.drawLine(projectile.getX(), projectile.getY() - 5, projectile.getX(), projectile.getY() + 5);
                GameLib.drawLine(projectile.getX() - 1, projectile.getY() - 3, projectile.getX() - 1, projectile.getY() + 3);
                GameLib.drawLine(projectile.getX() + 1, projectile.getY() - 3, projectile.getX() + 1, projectile.getY() + 3);
            }
        }
    }

    public static void ballProjectiles(List<Projectiles> projectiles, Color color, double radius)
    {
        for(Projectiles projectile: projectiles)
        {
            if(projectile.getState() == States.ACTIVE)
            {
                GameLib.setColor(color);
				GameLib.drawCircle(projectile.getX(), projectile.getY(), projectile.getRadius());
            }
        }
    }
}