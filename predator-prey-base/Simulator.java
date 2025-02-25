import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.paint.Color; 

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public class Simulator {

    private static final double LION_CREATION_PROBABILITY = 0.1;
    private static final double HYENA_CREATION_PROBABILITY = 0.2;
    private static final double GIRAFFE_CREATION_PROBABILITY = 0.2;
    private static final double ANTELOPE_CREATION_PROBABILITY = 0.3;
    private static final double ZEBRA_CREATION_PROBABILITY = 0.3;    

    private List<Animal> animals;
    private Field field;
    private int step;
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        
        animals = new ArrayList<>();
        field = new Field(depth, width);

        reset();
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep() {
        step++;
        List<Animal> newAnimals = new ArrayList<>();    
        
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if(! animal.isAlive()) {      
                // Location animalLocation = animal.getLocation();
                // Plant plant = new Plant(field, animalLocation, Color.GREEN);
                it.remove();
                // animals.add(plant);
            }
        }
               
        animals.addAll(newAnimals);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        animals.clear();
        populate();
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate() {
        
        Random rand = Randomizer.getRandom();
        field.clear();
        
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= LION_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Lion lion = new Lion(true, field, location, Color.ORANGE);
                    animals.add(lion);
                }
                else if(rand.nextDouble() <= ZEBRA_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Zebra zebra = new Zebra(true, field, location, Color.BLACK);
                    animals.add(zebra);
                }
                else if(rand.nextDouble() <= ANTELOPE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Antelope antelope = new Antelope(true, field, location, Color.BROWN);
                    animals.add(antelope);
                }
                else if(rand.nextDouble() <= HYENA_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Hyena hyena = new Hyena(true, field, location, Color.GREY);
                    animals.add(hyena);
                }
                else if(rand.nextDouble() <= GIRAFFE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Giraffe giraffe = new Giraffe(true, field, location, Color.YELLOW);
                    animals.add(giraffe);
                }
                else {
                    Location location = new Location(row, col);
                    Plant plant = new Plant(field, location, Color.GREEN);
                    animals.add(plant);
                }
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    public void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    

    public Field getField() {
        return field;
    }

    public int getStep() {
        return step;
    }
}