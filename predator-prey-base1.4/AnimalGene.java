import java.util.Random;
/**
 * Write a description of class AnimalGene here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class AnimalGene
{
    private String gene;
    private static final Random rand = new Random();
    // Constructor to initialize the gene string
    public AnimalGene(String gene) {
        this.gene = gene;
    }
    
    public String getGene() {
        return gene;
    }
    
    /**
     * Inherit genes from two parents using crossover and mutation.
     */
    public static String inheritGeneString(AnimalGene parent1, AnimalGene parent2) {
        String gene1 = parent1.getGene();
        String gene2 = parent2.getGene();

        // Take first 7 digits from parent1 and last 7 digits from parent2
        String offspringGene = gene1.substring(0, 7) + gene2.substring(7);

        // Introduce mutation (20% chance)
        // if (rand.nextDouble() < 0.2) {
            // char[] geneChars = offspringGene.toCharArray();
            // int mutationIndex = rand.nextInt(14);  // Choose a random digit to mutate
            // int mutationAmount = (rand.nextBoolean()) ? 1 : -1; // Either +1 or -1

            // // Ensure mutation keeps values within allowed ranges
            // geneChars[mutationIndex] = (char) (geneChars[mutationIndex] + mutationAmount);
            // offspringGene = new String(geneChars);
        // }

        return offspringGene;
    }

    public int getBreedingAge() {
        return Integer.parseInt(gene.substring(0, 2)); // Digits 1-2
    }

    public int getLifeSpan() {
        return Integer.parseInt(gene.substring(2, 5)); // Digits 3-5
    }

    public double getBreedingProbability() {
        return Integer.parseInt(gene.substring(5, 7)) * 0.01; // Digits 6-7, multiply by 0.01
    }

    public int getLitterSize() {
        return Integer.parseInt(gene.substring(7, 9)); // Digits 8-9
    }

    public double getDiseaseProbability() {
        return Integer.parseInt(gene.substring(9, 11)) * 0.01; // Digits 10-11, multiply by 0.01
    }

    public double getMetabolism() {
        return Integer.parseInt(gene.substring(11, 13)) * 0.01; // Digits 13-14, multiply by 0.01
    }
}
