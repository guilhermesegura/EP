package game;

public class SpawnEvent {
    public enum Type {INIMIGO, CHEFE}

    private final Type type;
    private final int entityType;
    private final int health;
    private final int time;
    private final int x;
    private final int y;

    public SpawnEvent(Type type, int entityType, int health, int time, int x, int y) {
        this.type = type;
        this.entityType = entityType;
        this.health = health;
        this.time = time;
        this.x = x;
        this.y = y;
    
    }

    public Type getType() { return type; }
    public int getEntityType() { return entityType; }
    public int getTime() { return time; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
}