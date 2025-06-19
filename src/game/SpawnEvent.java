package game;

public class SpawnEvent {
  public enum Type {INIMIGO, CHEFE}

  private Type type;
  private int entityType;
  private int time;
  private int x;
  private int y;
  private int life; // será aplicado apenas para os boss e player

  public SpawnEvent(Type type, int entityType, int time, int x, int y, int life){
        this.type = type;
        this.entityType = entityType;
        this.time = time;
        this.x = x;
        this.y = y;
        this.life = life;
  }
    

    public Type getType() { 
        return type; }
    public int getEntityType() { 
        return entityType; }
    public int getTime() { 
        return time; }
    public int getX() { 
        return x; }
    public int getY() { 
        return y; }
    public int getLife() { 
        return life; }
}
