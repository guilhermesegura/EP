package entities.interfaces;

public interface IExplodable {
    public double getExplosionStart();
    public void setExplosionStart(double explosion_start);
    public double getExplosionEnd();
    public void setExplosionEnd(double explosion_end);
    public void TakeDamage();
}
