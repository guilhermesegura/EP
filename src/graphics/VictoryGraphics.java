package graphics;

import java.awt.Color;
import utils.GameLib;

public class VictoryGraphics {
    public static void drawVictory() {
        // Fundo semi-transparente (70% opacidade)
        GameLib.setColor(new Color(0, 0, 0, 180));
        GameLib.fillRect(0, 0, GameLib.WIDTH, GameLib.HEIGHT);
        
        // Efeito de borda pulsante dourada
        long currentTime = System.currentTimeMillis();
        double pulse = Math.abs(Math.sin(currentTime * 0.003)) * 0.7 + 0.3;
        GameLib.setColor(new Color(255, (int)(215 * pulse), 0, 40));
        for(int i = 0; i < 5; i++) {
            GameLib.drawRect(i, i, GameLib.WIDTH - i*2, GameLib.HEIGHT - i*2);
        }

        // Efeito de sombra para o texto
        GameLib.setColor(new Color(0, 50, 0));
        drawTextWithLines("VICTORY", GameLib.WIDTH/2 + 3, GameLib.HEIGHT/2 - 77, 6);
        drawTextWithLines("PARABENS", GameLib.WIDTH/2 + 2, GameLib.HEIGHT/2 - 7, 4);
        
        // Texto principal dourado
        GameLib.setColor(new Color(255, 215, 0));
        drawTextWithLines("VICTORY", GameLib.WIDTH/2, GameLib.HEIGHT/2 - 80, 6);
        drawTextWithLines("PARABENS", GameLib.WIDTH/2, GameLib.HEIGHT/2 - 10, 4);

        // Instruções com sombra
        GameLib.setColor(new Color(20, 20, 20));
        drawTextWithLines("ESC PARA SAIR", GameLib.WIDTH/2 + 2, GameLib.HEIGHT/2 + 82, 2);
        
        GameLib.setColor(Color.WHITE);
        drawTextWithLines("ESC PARA SAIR", GameLib.WIDTH/2, GameLib.HEIGHT/2 + 80, 2);
    }
    
    private static void drawTextWithLines(String text, double centerX, double centerY, int size) {
        double spacing = size * 3.5;
        double startX = centerX - (text.length() * spacing) / 2;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            CharGraphics.drawCharWithLines(c, startX + i * spacing, centerY, size);
        }
    }
}