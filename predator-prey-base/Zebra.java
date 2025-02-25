import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color; 

/**
 * A simple model of a Zebra.
 * Zebras age, move, breed, and die.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public class Zebra extends Animal {

    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 40;
    private static final double BREEDING_PROBABILITY = 0.12;
    private static final int MAX_LITTER_SIZE = 4;
    private static final Random rand = Randomizer.getRandom();
    
    private int age;

    /**
     * Create a new Zebra. A Zebra may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Zebra will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Zebra(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        age = 0;
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the Zebra does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newZebras A list to return newly born Zebras.
     */
    public void act(List<Animal> newZebras) {
        incrementAge();
        if(isAlive()) {
            giveBirth(newZebras);            
            // Try to move into a free location.
            Location newLocation = getField().getFreeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age.
     * This could result in the Zebra's death.
     */
    private void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this Zebra is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newZebras A list to return newly born Zebras.
     */
    private void giveBirth(List<Animal> newZebras) {
        // New Zebras are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Zebra young = new Zebra(false, field, loc, getColor());
            newZebras.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A Zebra can breed if it has reached the breeding age.
     * @return true if the Zebra can breed, false otherwise.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
}