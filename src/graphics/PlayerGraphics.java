package graphics;
import utils.GameLib;
import java.awt.Color;

public class PlayerGraphics {
    public static void explosion(double x, double y, long currentTime, double start, double end)
    {
        double alpha = (currentTime - start) / (end - start);
        GameLib.drawExplosion(x, y, alpha);
    }
    public static void player(double x, double y, double r, Color color)
    {
        GameLib.setColor(color);
        GameLib.drawPlayer(x, y, r);
    }
}