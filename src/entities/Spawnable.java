package entities;

public interface Spawnable {
    boolean shouldSpawn(long currentTime);
    void spawn(long currentTime);
    void updateSpawnTimer(long currentTime);
}