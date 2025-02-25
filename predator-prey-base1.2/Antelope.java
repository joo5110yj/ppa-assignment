import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color; 

/**
 * A simple model of a Antelope.
 * zebras age, move, breed, and die.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public class Antelope extends Animal {

    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 40;
    private static final double BREEDING_PROBABILITY = 0.12;
    private static final int MAX_LITTER_SIZE = 4;
    private static final Random rand = Randomizer.getRandom();
    
    private int age;

    /**
     * Create a new Antelope. A Antelope may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Antelope will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Antelope(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        age = 0;
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the Antelope does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newzebras A list to return newly born zebras.
     */
    public void act(List<Animal> newzebras) {
        incrementAge();
        if(isAlive()) {
            giveBirth(newzebras);            
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
     * This could result in the zebra's death.
     */
    private void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this Antelope is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newzebras A list to return newly born zebras.
     */
    private void giveBirth(List<Animal> newzebras) {
        // New zebras are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Antelope young = new Antelope(false, field, loc, getColor());
            newzebras.add(young);
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
     * A Antelope can breed if it has reached the breeding age.
     * @return true if the Antelope can breed, false otherwise.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
}
