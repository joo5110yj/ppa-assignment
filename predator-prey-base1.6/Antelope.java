import java.util.List;
import java.util.Random;
import java.util.Iterator;
import javafx.scene.paint.Color; 

/**
 * A simple model of a Antelope.
 * zebras age, move, breed, and die.
 * 
 * @author Takumi Satoh and Jooyoung Jung 
 * @version 2025.03.07
 */

public class Antelope extends Animal {

    private final int BREEDING_AGE = this.getBreedingAge();
    private final int MAX_AGE = this.getLifeSpan();
    private final double BREEDING_PROBABILITY = this.getBreedingProbability();
    private final int MAX_LITTER_SIZE = this.getLitterSize();
    private static final int LION_FOOD_VALUE = 10;
    private static final int HYENA_FOOD_VALUE = 10;
    private static final int GIRAFFE_FOOD_VALUE = 10;
    private static final int ZEBRA_FOOD_VALUE = 9;
    private static final int ANTELOPE_FOOD_VALUE = 8;
    private static final int PLANT_FOOD_VALUE = 3;
    private final double METABOLISM = this.getMetabolism();
    private static final Random rand = Randomizer.getRandom();
    
    
    private int age;
    private int foodLevel;
    private double metabolism = METABOLISM;

    /**
     * Create a new Antelope. A Antelope may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Antelope will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Antelope(boolean randomAge, Field field, Location location, Color col, String geneString) {
        super(field, location, col, geneString);
        age = 0;
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(ANTELOPE_FOOD_VALUE);
        }
    }
    
    /**
     * This is what the Antelope does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newzebras A list to return newly born zebras.
     */
    public void act(List<Animal> newAntelopes) {
        incrementAge();
        if(isAlive()) {
            giveBirth(newAntelopes); 
            actWithSickness(newAntelopes);
            if (foodLevel <4){
                Location newLocation = findFood();
                if (newLocation == null) {
                    newLocation = getField().getFreeAdjacentLocation(getLocation());
                }

                if(newLocation != null) {
                    setLocation(newLocation);
                }
                else {
                    setDead();
                }
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
    private void giveBirth(List<Animal> newAntelope) {
        if (!isFemale()) {
            return;
        }
        
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Animal mate = findMate();

            if (mate != null) {
                String offspringGene = AnimalGene.inheritGeneString(this.getGene(), mate.getGene());

                Antelope young = new Antelope(false, field, loc, getColor(), offspringGene);
                newAntelope.add(young);
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
        if (MAX_LITTER_SIZE <= 0) {
            throw new IllegalArgumentException("MAX_LITTER_SIZE must be a positive number.");
        }
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
    
    private Location findFood() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
    
        while (it.hasNext()) {
            Location where = it.next();
            Object object = field.getObjectAt(where);
            if (object instanceof Plant) {
                Plant plant = (Plant) object;
                if (plant.isAlive()) { 
                    plant.setDead();  
                    foodLevel += PLANT_FOOD_VALUE;
                    return where;  
                }
            }
        }
    
        return null;  
    }
    /**
     * Make this Antelope more hungry. This could result in the Antelope's death.
     */
    private void incrementHunger() {
        foodLevel-= metabolism;
        if(foodLevel <= 0) {
            setDead();
        }
    }
}
