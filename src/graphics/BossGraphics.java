/*Boss graphics*/
package graphics;

import entities.Boss;
import java.awt.Color;
import utils.*;

public class BossGraphics {

    public static void boss(Boss boss, long currentTime) {
        if(boss.getState() == States.ACTIVE) {
            drawHealthBar(boss);
            drawAlien(boss, currentTime);
        }
        else if(boss.getState() == States.EXPLODING) {
            double alpha = (currentTime - boss.getExplosionStart()) / (boss.getExplosionEnd() - boss.getExplosionStart());
            GameLib.drawExplosion(boss.getX(), boss.getY(), alpha);
        }
    }

public static void drawBoss(Boss boss, long currentTime) {
    if (boss.getState() == States.ACTIVE) {
        drawAlien(boss, currentTime);
        drawHealthBar(boss);
    } else if (boss.getState() == States.EXPLODING) {
        double alpha = (currentTime - boss.getExplosionStart()) / 
                     (boss.getExplosionEnd() - boss.getExplosionStart());
        GameLib.drawExplosion(boss.getX(), boss.getY(), alpha);
    }
}

private static void drawAlien(Boss boss, long currentTime) {
    double x = boss.getX();
    double y = boss.getY();
    double r = boss.getRadius();
    
    // Efeito de pulsação alienígena
    double pulse = 1.0 + 0.03 * Math.sin(currentTime * 0.007);
    double scale = r * pulse;

    // Corpo principal (cabeça ovalada)
    GameLib.setColor(new Color(0, 150, 0)); // Verde alienígena
    GameLib.drawCircle(x, y, scale * 0.8);
    
    // Olhos grandes
    drawAlienEye(x - scale*0.3, y - scale*0.2, scale*0.3, currentTime);
    drawAlienEye(x + scale*0.3, y - scale*0.2, scale*0.3, currentTime);
    
    // Boca/órgão comunicador
    GameLib.setColor(new Color(200, 0, 200));
    GameLib.drawCircle(x, y + scale*0.3, scale*0.2);
    
    // Antenas
    double antennaMove = Math.sin(currentTime * 0.005) * 0.2;
    GameLib.setColor(new Color(0, 200, 0));
    GameLib.drawLine(x - scale*0.4, y - scale*0.8, x - scale*0.6, y - scale*1.3 - antennaMove);
    GameLib.drawLine(x + scale*0.4, y - scale*0.8, x + scale*0.6, y - scale*1.3 - antennaMove);
    
    // Membros inferiores (pernas/braços)
    GameLib.drawLine(x - scale*0.3, y + scale*0.6, x - scale*0.5, y + scale*1.0);
    GameLib.drawLine(x + scale*0.3, y + scale*0.6, x + scale*0.5, y + scale*1.0);
    
    // Detalhes tecnológicos
    GameLib.setColor(new Color(100, 255, 100));
    for(int i = 0; i < 5; i++) {
        double detailX = x - scale*0.7 + i * scale*0.35;
        GameLib.drawCircle(detailX, y + scale*0.1, scale*0.05);
    }
}

private static void drawAlienEye(double x, double y, double size, long currentTime) {
    // Piscar alienígena
    double blink = Math.abs(Math.sin(currentTime * 0.003)) > 0.95 ? 
                  0.2 : 1.0;
    
    // Olho externo
    GameLib.setColor(new Color(50, 200, 50));
    GameLib.fillRect(x - size/2, y - (size * blink)/2, size, size * blink);
    
    // Pupila vertical
    GameLib.setColor(Color.BLACK);
    GameLib.fillRect(x - size/4, y - (size * blink)/2, size/2, size * blink);
    
    // Brilho nos olhos
    GameLib.setColor(Color.WHITE);
    GameLib.fillRect(x + size/4, y - size/4, size/6, size/6);
}

    private static void drawHealthBar(Boss boss) {
        double barWidth = boss.getRadius() * 1.5 + 1;
        double barHeight = 4;

        // Centro inicial da barra
        double barX = boss.getX() - barWidth / 2 ;
        double barY = boss.getY() - boss.getRadius() / 2 - 35;

        double bossRatio = boss.getHealth() / (double)boss.getMaxHealth();

        double greenWidth = barWidth * bossRatio;
        double redWidth = barWidth - greenWidth;

        // Desenhar a parte verde (vida restante)
        GameLib.setColor(Color.GREEN);
        GameLib.fillRect(barX + greenWidth / 2, barY, greenWidth, barHeight);

        // Desenhar a parte vermelha (vida perdida), posicionada à direita da barra verde
        GameLib.setColor(Color.RED);
        GameLib.fillRect(barX + greenWidth + redWidth / 2, barY, redWidth, barHeight);
    }
}

