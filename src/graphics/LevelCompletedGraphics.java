package graphics;

import java.awt.Color;
import utils.GameLib;

public class LevelCompletedGraphics {

    // Guarda o instante inicial da contagem regressiva
    private static Long startTime = null;

    public static void drawLevelCompleted() {
        // Se a contagem ainda não começou, inicializa o tempo
        if (startTime == null) {
            startTime = System.currentTimeMillis();
        }

        // Fundo semi-transparente escuro
        GameLib.setColor(new Color(0, 0, 0, 180));
        GameLib.fillRect(0, 0, GameLib.WIDTH, GameLib.HEIGHT);

        // Efeito de borda pulsante verde
        long currentTime = System.currentTimeMillis();
        double pulse = Math.abs(Math.sin(currentTime * 0.005)) * 0.5 + 0.5;
        GameLib.setColor(new Color(0, (int)(255 * pulse), 0, 30));
        for(int i = 0; i < 5; i++) {
            GameLib.drawRect(i, i, GameLib.WIDTH - i*2, GameLib.HEIGHT - i*2);
        }

        // Efeito de sombra
        GameLib.setColor(new Color(0, 50, 0));
        drawTextWithLines("LEVEL COMPLETADO", GameLib.WIDTH/2 + 3, GameLib.HEIGHT/2 - 77, 6);
        drawTextWithLines("COM SUCESSO", GameLib.WIDTH/2 + 3, GameLib.HEIGHT/2 - 27, 5);
        drawTextWithLines("PREPARE-SE PARA O PROXIMO NIVEL", GameLib.WIDTH/2 + 2, GameLib.HEIGHT/2 + 33, 3);

        // Texto principal
        GameLib.setColor(new Color(100, 255, 100));
        drawTextWithLines("LEVEL COMPLETADO", GameLib.WIDTH/2, GameLib.HEIGHT/2 - 80, 6);
        drawTextWithLines("COM SUCESSO!", GameLib.WIDTH/2, GameLib.HEIGHT/2 - 30, 5);

        // Mensagem de preparação
        GameLib.setColor(new Color(100, 255, 255));
        drawTextWithLines("PREPARE-SE PARA O PROXIMO NIVEL", GameLib.WIDTH/2, GameLib.HEIGHT/2 + 30, 3);

        // Contador regressivo
        long elapsed = currentTime - startTime;
        long secondsLeft = 3 - (elapsed / 1000);
        if (secondsLeft >= 0) {
            GameLib.setColor(new Color(255, 255, 255, 150));
            drawTextWithLines("PROXIMO NIVEL EM " + secondsLeft, GameLib.WIDTH/2, GameLib.HEIGHT/2 + 80, 2);
        }
    }

    // Método auxiliar para resetar o tempo (chame quando o próximo nível começar)
    public static void resetTimer() {
        startTime = null;
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
