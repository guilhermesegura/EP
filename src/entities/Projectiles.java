package entities;
import utils.States;
public class Projectiles extends Entity{

    public static final int PLAYER_PROJECTILE = 1;
    public static final int ENEMY_PROJECTILE = 2;


    private int type;
    private int damage;
    
    Projectiles(int type, int damage){
        this.type = type;
        this.damage = damage;
    }

    public boolean

    OutOfBounds(){
        
    }

    public void explosion(long duration){
        setState(States.EXPLODING);
        // Colocar metodos de setExplosionStart = currentTimee
        // metodo setExplosionEnd = currentTime + 2000
    }

    public boolean colision(Entity other){
        if (getState() != States.ACTIVE || other.getState() != States.ACTIVE) {
            return false;
        }

        double dx = getX() - other.getX();
        double dy = getY() - other.getY();

        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < (getRadius() + other.getRadius()) * 0.8;
        

    }

    Update(){

    }
    
    public int getType() {
        return type;

    
    public int getDamage() {
        return damage;
    }

    public double getExplosionStart(){
        return explosion_start;
    }

    public void sExplosionStart(double explosion_start){ 
        this.explosion_start;
    }

    public double getExplosionEnd(){
        return explosion_end;
    }

    public void  setExplosionEnd(double explosion_end){
        this.explosion_end = explosion_end;
    }


et    }
    
    
    

    

}