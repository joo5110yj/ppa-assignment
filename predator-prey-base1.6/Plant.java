import java.util.List;
import javafx.scene.paint.Color; 

/**
 * A plant in the simulation. Plants do not move and act, but serves as food for prey.
 *
 * @author Takumi Satoh and Jooyoung Jung 
 * @version 2025.03.07 
 */
public class Plant extends Animal {

    private Field field;
    private Location location;
    private Color color = Color.GREEN;
    
    /**
     * Constructor for objects of class Plant
     */
    public Plant(Field field, Location location, Color col, String geneString) {   
        super(field, location, col, geneString);
    }
    
    public void act(List<Animal> newPlants) {
    }
}
