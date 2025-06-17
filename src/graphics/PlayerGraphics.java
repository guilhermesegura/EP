package graphics;
import utils.GameLib;
import utils.States;

import java.awt.Color;
import entities.Player;

public class PlayerGraphics {
    public static void player(Player player, Color color, long currentTime)
    {
        if(player.getState() == States.EXPLODING)
        {
            double alpha = (currentTime - player.getExplosionStart()) / (player.getExplosionEnd() - player.getExplosionStart());
            GameLib.drawExplosion(player.getX(), player.getY(), alpha);
        }
        if(player.getState() == States.ACTIVE)
        {
            GameLib.setColor(color);
            GameLib.drawPlayer(player.getX(), player.getY(), player.getRadius());
        }
    }
}