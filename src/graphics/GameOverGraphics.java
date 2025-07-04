package graphics;

import java.awt.Color;
import utils.GameLib;

public class GameOverGraphics {
    public static void drawGameOver() {
        // Fundo semi-transparente (70% opacidade)
        GameLib.setColor(new Color(0, 0, 0, 180));
        GameLib.fillRect(0, 0, GameLib.WIDTH, GameLib.HEIGHT);
        
        // Efeito de borda pulsante vermelha
        long currentTime = System.currentTimeMillis();
        double pulse = Math.abs(Math.sin(currentTime * 0.005)) * 0.5 + 0.5;
        GameLib.setColor(new Color((int)(255 * pulse), 0, 0, 30));
        for(int i = 0; i < 5; i++) {
            GameLib.drawRect(i, i, GameLib.WIDTH - i*2, GameLib.HEIGHT - i*2);
        }

        // Efeito de sombra para o texto
        GameLib.setColor(new Color(50, 0, 0));
        drawTextWithLines("GAME OVER", GameLib.WIDTH/2 + 3, GameLib.HEIGHT/2 - 47, 5);
        
        // Texto principal vermelho brilhante
        GameLib.setColor(new Color(255, 100, 100));
        drawTextWithLines("GAME OVER", GameLib.WIDTH/2, GameLib.HEIGHT/2 - 50, 5);

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
    
    public static void drawCharWithLines(char c, double x, double y, int size) {
        CharGraphics.drawCharWithLines(c, x, y, size);
    }
}