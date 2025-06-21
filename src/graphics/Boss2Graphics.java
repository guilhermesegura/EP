package graphics;

import entities.Boss2;
import java.awt.Color;
import utils.GameLib;
import utils.States;

public class Boss2Graphics {

    public static void drawBoss2(Boss2 boss, long currentTime) {
        if (boss.getState() == States.ACTIVE) {
            drawDragon(boss, currentTime);
            drawHealthBar(boss);
        } else if (boss.getState() == States.EXPLODING) {
            double alpha = (currentTime - boss.getExplosionStart()) / 
                         (boss.getExplosionEnd() - boss.getExplosionStart());
            GameLib.drawExplosion(boss.getX(), boss.getY(), alpha);
        }
    }

    private static void drawDragon(Boss2 boss, long currentTime) {
        double x = boss.getX();
        double y = boss.getY();
        double r = boss.getRadius();
        
        // Efeito de pulsação
        double pulse = 1.0 + 0.05 * Math.sin(currentTime * 0.005);
        double scale = r * pulse;

        // Corpo principal
        GameLib.setColor(new Color(70, 70, 80));
        GameLib.drawCircle(x, y, scale * 0.9);

        // Cabeça
        GameLib.drawCircle(x + scale * 0.9, y - scale * 0.3, scale * 0.5);

        // Chifres
        GameLib.drawLine(x + scale * 1.2, y - scale * 0.9, x + scale * 1.5, y - scale * 1.3);
        GameLib.drawLine(x + scale * 1.0, y - scale * 0.9, x + scale * 1.3, y - scale * 1.4);

        // Cauda
        GameLib.drawLine(x - scale, y + scale * 0.2, x - scale * 1.6, y + scale * 0.1);
        GameLib.drawLine(x - scale * 1.6, y + scale * 0.1, x - scale * 1.5, y - scale * 0.1);
        GameLib.drawLine(x - scale, y + scale * 0.2, x - scale * 1.5, y + scale * 0.5);

        // Asas
        drawWing(x - scale * 0.3, y - scale * 0.7, scale, currentTime, false);
        drawWing(x + scale * 0.3, y - scale * 0.7, scale, currentTime, true);

        // Olhos (com efeito de piscar)
        drawEye(x + scale * 1.0, y - scale * 0.4, scale * 0.15, currentTime);
        drawEye(x + scale * 1.3, y - scale * 0.4, scale * 0.15, currentTime);

    }

    private static void drawWing(double x, double y, double size, long currentTime, boolean rightWing) {
        double flap = Math.sin(currentTime * 0.004) * 0.2 + 0.2;
        double direction = rightWing ? 1 : -1;
        
        GameLib.setColor(new Color(80, 80, 90));
        
        // Estrutura principal da asa
        double tipX = x + direction * size * 0.8;
        double tipY = y - size * flap;
        double midX = x + direction * size * 0.4;
        double midY = y - size * (0.6 + flap);
        
        GameLib.drawLine(x, y, tipX, tipY);
        GameLib.drawLine(tipX, tipY, midX, midY);
        GameLib.drawLine(midX, midY, x, y);
        
        // Detalhes da asa
        for(int i = 1; i <= 3; i++) {
            double detailX = x + direction * size * (0.2 * i);
            double detailY = y - size * (0.15 * i + flap * 0.5);
            GameLib.drawLine(x, y, detailX, detailY);
        }
    }

    private static void drawEye(double x, double y, double size, long currentTime) {
        // Piscar ocasional
        double blink = Math.abs(Math.sin(currentTime * 0.002)) > 0.9 ? 
                      Math.abs(Math.sin(currentTime * 0.01)) : 1.0;
        
        // Olho
        GameLib.setColor(Color.RED);
        GameLib.fillRect(x - size/2, y - (size * blink)/2, size, size * blink);
        
        // Pupila
        GameLib.setColor(Color.BLACK);
        GameLib.fillRect(x - size/3, y - (size * blink)/3, size * 0.6, size * 0.6 * blink);
    }

    private static void drawMouth(double x, double y, double size, long currentTime) {
        // Boca
        GameLib.setColor(new Color(150, 40, 40));
        GameLib.fillRect(x - size/2, y - size/4, size, size/2);
        
        // Dentes
        GameLib.setColor(Color.WHITE);
        for(int i = 0; i < 5; i++) {
            double toothX = x - size/2 + i * size/4;
            GameLib.fillRect(toothX, y - size/4, size/8, size/4);
        }
        
    }

    private static void drawHealthBar(Boss2 boss) {
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