import java.util.List;
import javafx.scene.paint.Color; 

/**
 * Write a description of class Plant here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Plant extends Animal
{
    // instance variables - replace the example below with your own

    private Field field;
    private Location location;
    private Color color = Color.GREEN;
    
    /**
     * Constructor for objects of class Plant
     */
    public Plant(Field field, Location location, Color col)
    {
        super(field, location, col);
    }
    
    public void act(List<Animal> newPlants)
    {
    }
    }
