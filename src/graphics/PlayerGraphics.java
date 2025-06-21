package graphics;

import entities.Player;
import java.awt.Color;
import utils.GameLib;
import utils.States;

public class PlayerGraphics {
    public static void draw(Player player, Color color, long currentTime) {
        if(player.getState() == States.INACTIVE) return;
        // Draw player or explosion
        if(player.getState() == States.EXPLODING) {
            double alpha = (currentTime - player.getExplosionStart()) / 
                          (player.getExplosionEnd() - player.getExplosionStart());
            GameLib.drawExplosion(player.getX(), player.getY(), alpha);
        }
        else if(player.getState() == States.ACTIVE) {
            // Only draw player if not blinking or during visible phase
            if (player.shouldDrawPlayer(currentTime)) {
                GameLib.setColor(color);
                GameLib.drawPlayer(player.getX(), player.getY(), player.getRadius());
            }
        }
        // Draw hearts using pixel art style
        drawHearts(player, currentTime);
    }

    private static void drawHearts(Player player, long currentTime) {
        double startX = 20;
        double startY = 50;
        double size = 10;
        double spacing = size * 1.8;
        
        for (int i = 0; i < player.getMaxHealth(); i++) {
            // Determine if heart should be filled or empty
            boolean isFilled = i < player.getHealth();
            boolean isFlashing = player.isInvulnerable() && ((currentTime / 100) % 2 == 0);
            
            // Draw heart
            if (!isFlashing) {
                drawHeartPixelArt(startX + i * spacing, startY, size, isFilled);
            }
        }
        
        // Draw flashing effect during invulnerability
        if (player.isInvulnerable() && ((currentTime / 100) % 2 == 0)) {
            GameLib.setColor(new Color(255, 255, 255, 100));
            GameLib.fillRect(25, 25, 
                           player.getMaxHealth() * (int)spacing - 10, 
                           (int)size + 5);
        }
    }

    private static void drawHeartPixelArt(double x, double y, double size, boolean filled) {
        // Simple 5x5 pixel heart
        int[][] heartPixels = {
            {0,1,0,1,0},
            {1,1,1,1,1},
            {1,1,1,1,1},
            {0,1,1,1,0},
            {0,0,1,0,0}
        };
        
        double pixelSize = size/5;
        Color heartColor = filled ? Color.RED : new Color(80, 80, 80);
        
        GameLib.setColor(heartColor);
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (heartPixels[row][col] == 1) {
                    GameLib.fillRect(
                        x + (col-2) * pixelSize, // Center horizontally
                        y + (row-2) * pixelSize, // Center vertically
                        pixelSize, pixelSize
                    );
                }
            }
        }
    }
}
