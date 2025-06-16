package graphics;
import utils.GameLib;
import java.awt.Color;
import java.util.*;
public class ProjectileGraphics {
    private Color color;
    private List<double []> projectiles;
    
    public ProjectileGraphics(Color color, List<double []> projectiles)
    {
        this.color = color;
        this.projectiles = projectiles;
    }
}