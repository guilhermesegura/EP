package graphics;
import utils.*;
import java.awt.Color;
import java.util.*;
public class ProjectileGraphics {
    private Color color;
    private List<Coordinate> projectiles;
    
    public ProjectileGraphics(Color color, List<Coordinate> projectiles)
    {
        this.color = color;
        this.projectiles = projectiles;
    }
}