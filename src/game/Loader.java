package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private int playerLife;
    private List<Level> levels;

    public Loader(String configFilePath) throws IOException {
        levels = new ArrayList<>();
        loadGameConfig(configFilePath);
    }


    private void loadGameConfig(String configFilePath) throws IOException {
        
        List<String> lines = Files.readAllLines(Paths.get(configFilePath));

        if(lines.size() < 2){
            throw new  IllegalArgumentException("Configuração inválida: deve conter pelo menos a vida do jogador e número de fases.");
        }

        // A primeira linha representa a vida do jogador. Hint: trim() -> remove espaços em branco no início e no fim da string
        playerLife = Integer.parseInt(lines.get(0).trim());
        
        // Segunda linha: número de fases
        int numLevels = Integer.parseInt(lines.get(1).trim());
        
        // Demais linhas: caminhos dos arquivos de configurações das fases
        for(int i = 0; i < numLevels; i ++){
            String levelPath = lines.get(i + 2).trim();
            Level level = new Level(levelPath);
            levels.add(level);
            
        }

    }

    public int getPlayerLife() {
        return playerLife;
    }


    public List<Level> getLevels(){
        return levels;
    }
}