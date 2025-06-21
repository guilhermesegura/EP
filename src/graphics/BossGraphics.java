package graphics;

import entities.Boss;
import java.awt.Color;
import utils.*;

public class BossGraphics {

    public static void boss(Boss boss, long currentTime) {
        if(boss.getState() == States.ACTIVE) {
            drawHealthBar(boss);
            drawBoss(boss);
        }
        else if(boss.getState() == States.EXPLODING) {
            double alpha = (currentTime - boss.getExplosionStart()) / (boss.getExplosionEnd() - boss.getExplosionStart());
            GameLib.drawExplosion(boss.getX(), boss.getY(), alpha);
        }
    }

    private static void drawBoss(Boss boss) {
        GameLib.setColor(Color.GREEN);
        int[][] pixels = {
            {0,0,1,1,1,1,0,0},
            {0,1,1,1,1,1,1,0},
            {1,1,0,1,1,0,1,1},
            {1,1,1,1,1,1,1,1},
            {0,1,1,1,1,1,1,0},
            {0,0,1,0,0,1,0,0},
            {0,1,0,0,0,0,1,0},
            {1,0,1,0,0,1,0,1}
        };
        
        double pixelSize = boss.getRadius()/4;
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (pixels[row][col] == 1) {
                    GameLib.fillRect(
                        boss.getX() + (col-4) * pixelSize, 
                        boss.getY() + (row-4) * pixelSize,
                        pixelSize, pixelSize
                    );
                }
            }
        }
    }

    private static void drawHealthBar(Boss boss) {
        double barWidth = boss.getRadius() * 1.5 + 1;
        double barHeight = 4;
        double barX = boss.getX() - barWidth/2 + 20;
        double barY = boss.getY() - boss.getRadius()/2 -30; 
        double bossRatio = boss.getHealth() / (double)boss.getMaxHealth();

        GameLib.setColor(Color.RED);
        GameLib.fillRect(barX, barY, barWidth, barHeight);
        GameLib.setColor(Color.GREEN);
        GameLib.fillRect(barX, barY, barWidth * bossRatio, barHeight);
    }
}

