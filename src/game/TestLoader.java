package game;

import java.io.IOException;
import java.util.List;

public class TestLoader {
    public static void main(String[] args) {
        try {
            
            Loader loader = new Loader("EP/src/game/game_config.txt");

            System.out.println("Vida inicial do jogador: " + loader.getPlayerLife());

            List<Level> levels = loader.getLevels();
            for (int i = 0; i < levels.size(); i++) {
                System.out.println("\nFase " + (i + 1) + ":");
                for (SpawnEvent e : levels.get(i).getEvents()) {
                    System.out.println("Tipo: " + e.getType() +
                                       " | Entidade: " + e.getEntityType() +
                                       " | Tempo: " + e.getTime() +
                                       " | X: " + e.getX() +
                                       " | Y: " + e.getY() +
                                       " | Vida: " + e.getLife());
                }
            }

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erro ao carregar configuração: " + e.getMessage());
        }
    }
}
