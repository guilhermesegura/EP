package graphics;

import entities.*;
import java.awt.Color;
import java.util.*;
import utils.*;

public class PowerUpGraphics {
    
    public static void drawPowerUps(long currentTime, List<PowerUp> powerUps, double r) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.getState() == States.ACTIVE) {
                double x = powerUp.getX();
                double y = powerUp.getY();

                if (powerUp instanceof IncreaseLifePowerUp) {
                    drawHeart(x, y, r * 2, true); // já maior visualmente
                } else if (powerUp instanceof ShrinkPowerUp) {
                   drawShrinkSymbol(x, y, r * 1.5); // aumenta só o visual
                } else {
                    GameLib.setColor(Color.PINK);
                    GameLib.drawCircle(x, y, r);
                }
            }
        }
    }

    // Desenha um coração em pixel art
    private static void drawHeart(double x, double y, double size, boolean filled) {
        int[][] heartPixels = {
            {0, 1, 0, 1, 0},
            {1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 0, 0}
        };

        double pixelSize = size / 5.0;
        Color heartColor = filled ? Color.GREEN : new Color(80, 80, 80); // Verde

        GameLib.setColor(heartColor);

        for (int row = 0; row < heartPixels.length; row++) {
            for (int col = 0; col < heartPixels[row].length; col++) {
                if (heartPixels[row][col] == 1) {
                    double px = x + (col - 2) * pixelSize;
                    double py = y + (row - 2) * pixelSize;
                    GameLib.fillRect(px, py, pixelSize, pixelSize);
                }
            }
        }
    }

    // Desenha setas apontando para o centro → ← (representa encolhimento)
    private static void drawShrinkSymbol(double x, double y, double size) {
        double radius = size;

        GameLib.setColor(Color.ORANGE);
        GameLib.drawCircle(x, y, radius);

        double arrowLength = radius * 0.6;
        double arrowHeadSize = radius * 0.2;

        GameLib.setColor(Color.ORANGE);

        GameLib.drawLine(x - arrowLength, y, x - arrowLength / 2, y);
        GameLib.drawLine(x - arrowLength / 2, y, x - arrowLength / 2 - arrowHeadSize, y - arrowHeadSize);
        GameLib.drawLine(x - arrowLength / 2, y, x - arrowLength / 2 - arrowHeadSize, y + arrowHeadSize);

        GameLib.drawLine(x + arrowLength, y, x + arrowLength / 2, y);
        GameLib.drawLine(x + arrowLength / 2, y, x + arrowLength / 2 + arrowHeadSize, y - arrowHeadSize);
        GameLib.drawLine(x + arrowLength / 2, y, x + arrowLength / 2 + arrowHeadSize, y + arrowHeadSize);
    }

}
