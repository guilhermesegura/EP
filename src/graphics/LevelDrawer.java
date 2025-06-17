package graphics;

import utils.GameLib;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelDrawer {
    private Color bgColor;
    private Color starColorNear;
    private Color starColorFar;
    private final List<Star> backgroundStars;
    private final List<Star> foregroundStars;
    private final Random random;
    
    // Classe interna para representar estrelas
    private static class Star {
        double x;
        double y;
        double speed;
        int size;
        
        Star(double x, double y, double speed, int size) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.size = size;
        }
    }

    public LevelDrawer(Color bgColor, Color starColorNear, Color starColorFar, 
                      int bgStarCount, int fgStarCount) {
        this.bgColor = bgColor;
        this.starColorNear = starColorNear;
        this.starColorFar = starColorFar;
        this.random = new Random();
        
        this.backgroundStars = createStars(bgStarCount, 0.01, 2);
        this.foregroundStars = createStars(fgStarCount, 0.03, 3);
    }

    private List<Star> createStars(int count, double baseSpeed, int baseSize) {
        List<Star> stars = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            double speedVariation = 0.005 * random.nextDouble();
            int sizeVariation = random.nextInt(2);
            
            stars.add(new Star(
                random.nextDouble() * GameLib.WIDTH,
                random.nextDouble() * GameLib.HEIGHT,
                baseSpeed + speedVariation,
                baseSize + sizeVariation
            ));
        }
        return stars;
    }

    public void update(long delta) {
        updateStarPositions(backgroundStars, delta);
        updateStarPositions(foregroundStars, delta);
    }

    private void updateStarPositions(List<Star> stars, long delta) {
        for (Star star : stars) {
            star.y += star.speed * delta;
            if (star.y > GameLib.HEIGHT) {
                star.y = 0;
                star.x = random.nextDouble() * GameLib.WIDTH;
            }
        }
    }

    public void render() {
        // Fundo sólido
        GameLib.setColor(bgColor);
        GameLib.fillRect(0, 0, GameLib.WIDTH, GameLib.HEIGHT);
        
        // Estrelas de fundo (distantes)
        GameLib.setColor(starColorFar);
        renderStars(backgroundStars);
        
        // Estrelas de primeiro plano (próximas)
        GameLib.setColor(starColorNear);
        renderStars(foregroundStars);
    }

    private void renderStars(List<Star> stars) {
        for (Star star : stars) {
            GameLib.fillRect(star.x, star.y, star.size, star.size);
        }
    }

    // Métodos para configuração dinâmica
    public void setBackgroundTheme(Color bgColor, Color nearColor, Color farColor) {
        this.bgColor = bgColor;
        this.starColorNear = nearColor;
        this.starColorFar = farColor;
    }

    public void addForegroundStar() {
        foregroundStars.add(new Star(
            random.nextDouble() * GameLib.WIDTH,
            0,
            0.03 + 0.005 * random.nextDouble(),
            3 + random.nextInt(2)
        ));
    }

    public void removeForegroundStar() {
        if (!foregroundStars.isEmpty()) {
            foregroundStars.remove(foregroundStars.size() - 1);
        }
    }
}