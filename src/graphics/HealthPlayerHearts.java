package graphics;

import utils.GameLib;
import java.awt.Color;

public class HealthPlayerHearts {

    private int maxLives;

    public HealthPlayerHearts(int maxLives) {
        this.maxLives = maxLives;
    }

    public void draw(int currentLives) {
        double startX = 20.0;        // Posição inicial no eixo X
        double startY = 40.0;        // Posição inicial no eixo Y
        double spacing = 25.0;       // Espaço entre os corações
        double heartRadius = 8.0;    // Tamanho do "coração"

        for (int i = 0; i < maxLives; i++) {
            if (i < currentLives) {
                GameLib.setColor(Color.RED);        // Vida ativa (cheio)
                GameLib.fillRect(startX + i * spacing, startY, heartRadius, heartRadius);
            } else {
                GameLib.setColor(Color.GRAY);       // Vida perdida (cinza)
                GameLib.fillRect(startX + i * spacing, startY, heartRadius, heartRadius);
            }
        }
    }
}
