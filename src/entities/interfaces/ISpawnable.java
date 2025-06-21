package entities.interfaces;

public interface ISpawnable {
    boolean shouldSpawn(long currentTime);
    void spawn(long currentTime);
    void updateSpawnTimer(long currentTime);
}