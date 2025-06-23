package graphics;

import java.awt.Color;
import utils.GameLib;

public class GameOverGraphics {
    public static void drawGameOver() {
        // Fundo escuro com linhas horizontais
        GameLib.setColor(new Color(0, 0, 0));
        for (int y = 0; y < GameLib.HEIGHT; y += 4) {
            GameLib.drawLine(0, y, GameLib.WIDTH, y);
        }
        
        // Texto "GAME OVER"
        drawTextWithLines("GAME OVER", GameLib.WIDTH/2, GameLib.HEIGHT/2 - 50, 5, Color.RED);
        
        // Instruções
        drawTextWithLines("PRESSIONE ENTER PARA REINICIAR", GameLib.WIDTH/2, GameLib.HEIGHT/2 + 50, 2, Color.WHITE);
        drawTextWithLines("ESC PARA SAIR", GameLib.WIDTH/2, GameLib.HEIGHT/2 + 80, 2, Color.WHITE);
    }
    
    private static void drawTextWithLines(String text, double centerX, double centerY, int size, Color color) {
        GameLib.setColor(color);
        double spacing = size * 3.5; // Pequeno espaço entre letras
        double startX = centerX - (text.length() * spacing) / 2;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            drawCharWithLines(c, startX + i * spacing, centerY, size);
        }
    }
    
    public static void drawCharWithLines(char c, double x, double y, int size) {
        switch (Character.toUpperCase(c)) {
            case 'G':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y, x + size * 3, y);
                GameLib.drawLine(x + size * 3, y, x + size * 3, y + size * 2);
                GameLib.drawLine(x + size * 3, y + size * 2, x + size * 2, y + size * 2);
                GameLib.drawLine(x + size * 2, y + size * 2, x + size * 2, y + size * 5);
                GameLib.drawLine(x + size * 2, y + size * 5, x, y + size * 5);
                break;
            case 'A':
                GameLib.drawLine(x, y + size * 5, x + size * 1.5, y);
                GameLib.drawLine(x + size * 1.5, y, x + size * 3, y + size * 5);
                GameLib.drawLine(x + size * 0.75, y + size * 2.5, x + size * 2.25, y + size * 2.5);
                break;
            case 'M':
                GameLib.drawLine(x, y + size * 5, x, y);
                GameLib.drawLine(x, y, x + size * 1.5, y + size * 2.5);
                GameLib.drawLine(x + size * 1.5, y + size * 2.5, x + size * 3, y);
                GameLib.drawLine(x + size * 3, y, x + size * 3, y + size * 5);
                break;
            case 'E':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y, x + size * 3, y);
                GameLib.drawLine(x, y + size * 2.5, x + size * 2, y + size * 2.5);
                GameLib.drawLine(x, y + size * 5, x + size * 3, y + size * 5);
                break;
            case 'O':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y, x + size * 3, y);
                GameLib.drawLine(x + size * 3, y, x + size * 3, y + size * 5);
                GameLib.drawLine(x, y + size * 5, x + size * 3, y + size * 5);
                break;
            case 'V':
                GameLib.drawLine(x, y, x + size * 1.5, y + size * 5);
                GameLib.drawLine(x + size * 1.5, y + size * 5, x + size * 3, y);
                break;
            case 'R':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y, x + size * 3, y);
                GameLib.drawLine(x + size * 3, y, x + size * 3, y + size * 2.5);
                GameLib.drawLine(x + size * 3, y + size * 2.5, x, y + size * 2.5);
                GameLib.drawLine(x, y + size * 2.5, x + size * 3, y + size * 5);
                break;
            case 'P':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y, x + size * 3, y);
                GameLib.drawLine(x + size * 3, y, x + size * 3, y + size * 2.5);
                GameLib.drawLine(x + size * 3, y + size * 2.5, x, y + size * 2.5);
                break;
            case 'S':
                GameLib.drawLine(x + size * 3, y, x, y);
                GameLib.drawLine(x, y, x, y + size * 2.5);
                GameLib.drawLine(x, y + size * 2.5, x + size * 3, y + size * 2.5);
                GameLib.drawLine(x + size * 3, y + size * 2.5, x + size * 3, y + size * 5);
                GameLib.drawLine(x + size * 3, y + size * 5, x, y + size * 5);
                break;
            case 'I':
                GameLib.drawLine(x + size * 1.5, y, x + size * 1.5, y + size * 5);
                break;
            case 'N':
                GameLib.drawLine(x, y + size * 5, x, y);
                GameLib.drawLine(x, y, x + size * 3, y + size * 5);
                GameLib.drawLine(x + size * 3, y + size * 5, x + size * 3, y);
                break;
            case 'C':
                GameLib.drawLine(x + size * 3, y, x, y);
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y + size * 5, x + size * 3, y + size * 5);
                break;
            case 'T':
                GameLib.drawLine(x, y, x + size * 3, y);
                GameLib.drawLine(x + size * 1.5, y, x + size * 1.5, y + size * 5);
                break;
            case 'L':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y + size * 5, x + size * 3, y + size * 5);
                break;
            case 'D':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y, x + size * 2, y);
                GameLib.drawLine(x + size * 2, y, x + size * 3, y + size * 2.5);
                GameLib.drawLine(x + size * 3, y + size * 2.5, x + size * 2, y + size * 5);
                GameLib.drawLine(x + size * 2, y + size * 5, x, y + size * 5);
                break;
            case 'B':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y, x + size * 2, y);
                GameLib.drawLine(x + size * 2, y, x + size * 3, y + size * 1.25);
                GameLib.drawLine(x + size * 3, y + size * 1.25, x + size * 2, y + size * 2.5);
                GameLib.drawLine(x + size * 2, y + size * 2.5, x + size * 3, y + size * 3.75);
                GameLib.drawLine(x + size * 3, y + size * 3.75, x + size * 2, y + size * 5);
                GameLib.drawLine(x + size * 2, y + size * 5, x, y + size * 5);
                GameLib.drawLine(x, y + size * 2.5, x + size * 2, y + size * 2.5);
                break;
            
            case 'J':
                GameLib.drawLine(x + size * 1.5, y, x + size * 3, y);
                GameLib.drawLine(x + size * 3, y, x + size * 3, y + size * 3.5);
                GameLib.drawLine(x + size * 3, y + size * 3.5, x + size * 1.5, y + size * 5);
                GameLib.drawLine(x + size * 1.5, y + size * 5, x, y + size * 3.5);
                break;
                
            case 'Q':
                GameLib.drawLine(x, y, x, y + size * 3.5);
                GameLib.drawLine(x, y, x + size * 3, y);
                GameLib.drawLine(x + size * 3, y, x + size * 3, y + size * 3.5);
                GameLib.drawLine(x + size * 3, y + size * 3.5, x, y + size * 3.5);
                GameLib.drawLine(x + size * 1.5, y + size * 1.75, x + size * 3, y + size * 5);
                break;
            
            case 'U':
                GameLib.drawLine(x, y, x, y + size * 3.5);
                GameLib.drawLine(x, y + size * 3.5, x + size * 1.5, y + size * 5);
                GameLib.drawLine(x + size * 1.5, y + size * 5, x + size * 3, y + size * 3.5);
                GameLib.drawLine(x + size * 3, y + size * 3.5, x + size * 3, y);
                break;
            
            case 'W':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y + size * 5, x + size * 1.5, y + size * 3);
                GameLib.drawLine(x + size * 1.5, y + size * 3, x + size * 3, y + size * 5);
                GameLib.drawLine(x + size * 3, y + size * 5, x + size * 3, y);
                break;
            
            case 'X':
                GameLib.drawLine(x, y, x + size * 3, y + size * 5);
                GameLib.drawLine(x + size * 3, y, x, y + size * 5);
                break;
            
            case 'Y':
                GameLib.drawLine(x, y, x + size * 1.5, y + size * 2.5);
                GameLib.drawLine(x + size * 3, y, x + size * 1.5, y + size * 2.5);
                GameLib.drawLine(x + size * 1.5, y + size * 2.5, x + size * 1.5, y + size * 5);
                break;
            
            case 'Z':
                GameLib.drawLine(x, y, x + size * 3, y);
                GameLib.drawLine(x + size * 3, y, x, y + size * 5);
                GameLib.drawLine(x, y + size * 5, x + size * 3, y + size * 5);
                break;
            
            case 'H':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x + size * 3, y, x + size * 3, y + size * 5);
                GameLib.drawLine(x, y + size * 2.5, x + size * 3, y + size * 2.5);
                break;
            
            case 'K':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y + size * 2.5, x + size * 3, y);
                GameLib.drawLine(x, y + size * 2.5, x + size * 3, y + size * 5);
                break;
            
            case 'F':
                GameLib.drawLine(x, y, x, y + size * 5);
                GameLib.drawLine(x, y, x + size * 3, y);
                GameLib.drawLine(x, y + size * 2.5, x + size * 2, y + size * 2.5);
                break;
            
            default:
                // Para caracteres não implementados, desenha um quadrado
                GameLib.drawLine(x, y, x + size * 3, y);
                GameLib.drawLine(x + size * 3, y, x + size * 3, y + size * 5);
                GameLib.drawLine(x + size * 3, y + size * 5, x, y + size * 5);
                GameLib.drawLine(x, y + size * 5, x, y);
    }
}
        
}
