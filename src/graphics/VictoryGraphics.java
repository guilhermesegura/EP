package graphics;

import java.awt.Color;
import utils.GameLib;

public class VictoryGraphics {
    public static void drawVictory() {
        // Fundo escuro com linhas horizontais
        GameLib.setColor(new Color(0, 0, 0));
        for (int y = 0; y < GameLib.HEIGHT; y += 4) {
            GameLib.drawLine(0, y, GameLib.WIDTH, y);
        }
        
        // Texto "VICTORY"
        drawTextWithLines("VICTORY", GameLib.WIDTH/2, GameLib.HEIGHT/2 - 50, 5, Color.GREEN);
        
        // Texto "PARABÉNS"
        drawTextWithLines("PARABENS", GameLib.WIDTH/2, GameLib.HEIGHT/2 + 20, 3, Color.YELLOW);
        
        // Instruções
        drawTextWithLines("PRESSIONE ENTER PARA JOGAR NOVAMENTE", GameLib.WIDTH/2, GameLib.HEIGHT/2 + 80, 2, Color.WHITE);
        drawTextWithLines("ESC PARA SAIR", GameLib.WIDTH/2, GameLib.HEIGHT/2 + 110, 2, Color.WHITE);
    }
    
    private static void drawTextWithLines(String text, double centerX, double centerY, int size, Color color) {
        GameLib.setColor(color);
        double spacing = size * 3.5;
        double startX = centerX - (text.length() * spacing) / 2;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            drawCharWithLines(c, startX + i * spacing, centerY, size);
        }
    }
    
    private static void drawCharWithLines(char c, double x, double y, int size) {
        // Reutiliza a mesma implementação de GameOverGraphics
        GameOverGraphics.drawCharWithLines(c, x, y, size);
    }
}