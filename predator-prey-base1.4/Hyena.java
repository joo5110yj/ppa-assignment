import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.LinkedList;
import javafx.scene.paint.Color; 

/**
 * A simple model of a Hyena.
 * Hyenas age, move, eat Antelopes, Giraffes, Zebras and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2025.02.10
 */

public class Hyena extends Animal {

    private static final int BREEDING_AGE = 0;
    private static final int MAX_AGE = 120;
    private static final double BREEDING_PROBABILITY = 0;
    private static final int MAX_LITTER_SIZE = 5;
    private static final int LION_FOOD_VALUE = 10;
    private static final int HYENA_FOOD_VALUE = 10;
    private static final int GIRAFFE_FOOD_VALUE = 10;
    private static final int ZEBRA_FOOD_VALUE = 9;
    private static final int ANTELOPE_FOOD_VALUE = 8;
    private static final Random rand = Randomizer.getRandom();
    
    
    private int age;
    private int foodLevel;
    private int hungerlevel;
    
    /**
     * Create a Hyena. A Hyena can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the Hyena will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Hyena(boolean randomAge, Field field, Location location, Color col, String geneString) {
        super(field, location, col, geneString);
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(HYENA_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = HYENA_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the Hyena does most of the time: it hunts for
     * Antelopes, Giraffes, Zebras. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newHyenas A list to return newly born Hyenas.
     */
    public void act(List<Animal> newHyenas) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newHyenas);   
            actWithSickness(newHyenas);
            // Move towards a source of food if found.
            Location newLocation = findFood();
            
            if (hungerlevel <4){
                if(newLocation == null) { 
                    // No food found - try to move to a free location.
                    newLocation = getField().getFreeAdjacentLocation(getLocation());
                }
                // See if it was possible to move.
                if(newLocation != null) {
                    setLocation(newLocation);
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }
        }
    }

    /**
     * Increase the age. This could result in the Hyena's death.
     */
    private void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this Hyena more hungry. This could result in the Hyena's death.
     */
    private void incrementHunger() {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for prey adjacent to the current location.
     * Only the first live prey is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Giraffe) {
                Giraffe giraffe = (Giraffe) animal;
                if(giraffe.isAlive()) { 
                    giraffe.setDead();
                    foodLevel = GIRAFFE_FOOD_VALUE;
                    return where;
                }
            }
            if(animal instanceof Zebra) {
                Zebra zebra = (Zebra) animal;
                if(zebra.isAlive()) { 
                    zebra.setDead();
                    foodLevel = ZEBRA_FOOD_VALUE;
                    return where;
                }
            }
            if(animal instanceof Antelope) {
                Antelope antelope = (Antelope) animal;
                if(antelope.isAlive()) { 
                    antelope.setDead();
                    foodLevel = ANTELOPE_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this Hyena is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHyenas A list to return newly born Hyenas.
     */
    private void giveBirth(List<Animal> newHyenas) {
        // New Hyenas are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Animal mate = findMate();

            if (mate != null) {
                // Inherit genes from both parents
                String offspringGene = AnimalGene.inheritGeneString(this.getGene(), mate.getGene());

                // Create new Giraffe with inherited gene
                Hyena young = new Hyena(false, field, loc, getColor(), offspringGene);
                newHyenas.add(young);
            }
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
     * A Hyena can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
}
