import java.util.List;
import javafx.scene.paint.Color; 
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public abstract class Animal {

    private boolean alive;
    private Field field;
    private Location location;
    private Color color = Color.BLACK;
    private String geneString;
    private boolean sick = false;  
    private int sicknessTimer = 0; 
    private static final int SICKNESS_DURATION = 5; 
    private static final double DISEASE_SPREAD_PROBABILITY = 0.2;
    private static final Random rand = Randomizer.getRandom();

    private boolean isMale;

    private AnimalGene gene;

    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */

    public Animal(Field field, Location location, Color col, String geneString) {
        alive = true;
        this.field = field;
        setLocation(location);
        setColor(col);
        
        this.geneString = generateRandomGene();
        this.gene = new AnimalGene(this.geneString);
        this.isMale = rand.nextBoolean();
    }

    public boolean isMale() {
        return isMale;
    }

    public boolean isFemale() {
        return !isMale;
    }

    public int getBreedingAge() {
        return gene.getBreedingAge();
    }

    public int getLifeSpan() {
        return gene.getLifeSpan();
    }

    public double getBreedingProbability() {
        return gene.getBreedingProbability();
    }

    public int getLitterSize() {
        return gene.getLitterSize();
    }

    public double getDiseaseProbability() {
        return gene.getDiseaseProbability();
    }

    public double getMetabolism() {
        return gene.getMetabolism();
    }

    public AnimalGene getGene() {
        return gene;
    }

    protected Animal findMate() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());

        for (Location loc : adjacent) {
            Object object = field.getObjectAt(loc);
            if (object instanceof Animal) {
                Animal potentialMate = (Animal) object;

                if (potentialMate.getClass() == this.getClass() && this.isMale() != potentialMate.isMale()) {
                    return potentialMate;
                }
            }
        }
        return null;
    }

    private String generateRandomGene() {
        StringBuilder gene = new StringBuilder();

        gene.append(String.format("%02d", rand.nextInt(79) + 12));
        gene.append(String.format("%03d", rand.nextInt(111) + 10));
        gene.append(String.format("%02d", rand.nextInt(51)));  
        gene.append(String.format("%02d", rand.nextInt(12) + 1)); 
        gene.append(String.format("%02d", rand.nextInt(51)));     
        gene.append(String.format("%03d", rand.nextInt(76) + 25));  
        return gene.toString();  
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead() {
        if (!alive) {
            return;
        }
        alive = false;
        if(location != null) {
            field.clear(location);
            Plant newPlant = new Plant(field, location, Color.GREEN, geneString);
            field.place(newPlant, location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation) {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField() {
        return field;
    }

    /**
     * Changes the color of the animal
     */
    public void setColor(Color col) {
        color = col;
    }

    /**
     * Returns the animal's color
     */
    public Color getColor() {
        return color;
    }   

    /**
     * Make this animal sick.
     * The animal will stay sick for a fixed number of steps.
     */
    public void becomeSick() {
        sick = true;
        sicknessTimer = SICKNESS_DURATION;
    }

    /**
     * Check if the animal is sick.
     * @return true if the animal is sick.
     */
    public boolean isSick() {
        return sick;
    }

    /**
     * The animal spreads disease to nearby animals of the same species.
     */
    public void spreadDisease() {
        if (!sick) return; 

        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());

        for (Location location : adjacent) {
            Object object = field.getObjectAt(location);
            if (object instanceof Animal && object != this) { 
                Animal otherAnimal = (Animal) object;
                if (rand.nextDouble() <= DISEASE_SPREAD_PROBABILITY) {
                    otherAnimal.becomeSick(); 
                }
            }
        }
    }

    /**
     * Handle the sickness behavior in the act() method.
     * If sick, the animal stays sick for a number of steps and may die.
     */
    public void handleSickness() {
        if (sick) {
            sicknessTimer--;
            if (sicknessTimer <= 0) {
                setDead(); 
            }
        }
    }

    /**
     * Method to be used by subclasses (like Antelope, Lion, etc.) to implement their specific actions
     * and handle sickness along with their other behaviors.
     * @param newAnimals A list to receive newly born animals.
     */
    public void actWithSickness(List<Animal> newAnimals) {
        handleSickness();
        spreadDisease();
    }
}