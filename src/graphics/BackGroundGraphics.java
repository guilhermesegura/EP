package graphics;
import java.awt.Color;
import utils.*;
import java.util.*;


public class BackGroundGraphics {
    private double speed;
    private double count;
    List<Coordinate> stars;

    public BackGroundGraphics(double speed, int capacity)
    {
        stars = new ArrayList<>(capacity);
        for(int i = 0; i < capacity; i++)
        {
            Coordinate coordnate = new Coordinate(Math.random() * GameLib.WIDTH, Math.random() * GameLib.HEIGHT);
            stars.add(coordnate);
        }
        this.speed = speed;
        this.count = 0.0;
    }
    public void setColor(Color color)
    {
        GameLib.setColor(color);
    }
    public void fillBakcGround(long delta)
    {
        count += speed * delta;
        for(Coordinate coordnate: stars)
        {
            GameLib.fillRect(coordnate.getX(), (coordnate.getY() + count) % GameLib.HEIGHT, 2, 2);
        }
    }
}