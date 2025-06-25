package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Level {
    
    private List<SpawnEvent> events;

    public Level(String configFilePath) throws IOException{
        events = new ArrayList<>();
        loadLevelConfig(configFilePath);
    }

    private void loadLevelConfig(String configFilePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(configFilePath));

        boolean bossFound = false;

        for (String line : lines) {
            // Basicamente ignora os caracteres inválidos
            line = line.replaceAll("//.*", "").trim();
            if (line.isEmpty()) continue;


            String[] parts = line.split("\\s+");
            String keyword = parts[0];

            try{
                if(keyword.equalsIgnoreCase("INIMIGO")){
                    if(parts.length != 5) throw new IllegalArgumentException("Linha de inimigo mal formatada: " + line);

                    int type = Integer.parseInt(parts[1]);
                    int time = Integer.parseInt(parts[2]);
                    int x = Integer.parseInt(parts[3]);
                    int y = Integer.parseInt(parts[4]);

                    events.add(new SpawnEvent(SpawnEvent.Type.INIMIGO, type, 1, time, x, y));

                }else if (keyword.equalsIgnoreCase("CHEFE")){
                    if (parts.length != 6) throw new IllegalArgumentException("Linha de chefe mal formatada: " + line);
                    if (bossFound) throw new IllegalArgumentException("Mais de um chefe definido na fase!");

                    int type = Integer.parseInt(parts[1]);
                    int health = Integer.parseInt(parts[2]);
                    int time = Integer.parseInt(parts[3]);
                    int x = Integer.parseInt(parts[4]);
                    int y = Integer.parseInt(parts[5]);

                    events.add(new SpawnEvent(SpawnEvent.Type.CHEFE, type, health, time, x, y));
                    bossFound = true;



                }else{
                    throw new IllegalArgumentException("Linha desconhecida no arquivo da fase: " + line);
                }
            }catch (NumberFormatException e){
                throw new IllegalArgumentException("Erro ao converter número na linha: " + line, e);
            }
        }

        if (!bossFound) {
            throw new IllegalArgumentException("A fase necessita obrigatoriamente de um chefe");
        }
    }

    public List<SpawnEvent> getEvents(){
        return events;
    }

}