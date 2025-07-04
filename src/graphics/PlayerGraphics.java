/*Player graphics*/
package graphics;
import entities.Player;
import java.awt.Color;
import utils.GameLib;
import utils.States;

public class PlayerGraphics {
    public static void draw(Player player, Color color, long currentTime) {
        
        if(player.getState() == States.EXPLODING) {
            double alpha = (currentTime - player.getExplosionStart()) / 
                          (player.getExplosionEnd() - player.getExplosionStart());
            GameLib.drawExplosion(player.getX(), player.getY(), alpha);
        }
        else if(player.getState() == States.ACTIVE) {
            
            if (player.shouldDrawPlayer(currentTime)) {
                GameLib.setColor(color);
                GameLib.drawPlayer(player.getX(), player.getY(), player.getRadius());
            }
        }
       
        drawHealthBar(player);
    }

    private static void drawHealthBar(Player player) {
        double barX = 62;
        double barY = 40;

        double barWidth = 100;
        double barHeight = 10;

        double healthRatio = player.getHealth() / (double)player.getMaxHealth();
        double greenWidth = barWidth * healthRatio;
        double redWidth = barWidth - greenWidth;

        GameLib.setColor(new Color(0, 200, 0));  // Verde
        GameLib.fillRect(
            barX - (barWidth / 2) + (greenWidth / 2), 
            barY,
            greenWidth,
            barHeight
        );

        GameLib.setColor(new Color(200, 0, 0));  // Vermelho
        GameLib.fillRect(
            barX + (barWidth / 2) - (redWidth / 2), // centro da barra vermelha
            barY,
            redWidth,
            barHeight
        );
    }
}