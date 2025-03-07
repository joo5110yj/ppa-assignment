import java.util.Random;
/**
 * The genes control the life cycle of the animals.
 * Each animal has a 14-digit gene string that determines the life cycle and metabolism. 
 * The first population has randomly assinged genes, but future generations inherit genes from parents. 
 * 
 *
 * @author Takumi Satoh and Jooyoung Jung 
 * @version 2025.03.07
 */
public class AnimalGene {
    
    private String gene;
    private static final Random rand = new Random();
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
        System.out.println("Parent 1:" + gene1);
        String gene2 = parent2.getGene();
        System.out.println("Parent 2:" + gene2);
        String offspringGene = gene1.substring(0, 7) + gene2.substring(7);
        System.out.println("Created: " + offspringGene);
        
        if (rand.nextDouble() < 0.2) {
            char[] geneChars = offspringGene.toCharArray();
            int mutationIndex = rand.nextInt(14);  
            int mutationAmount = (rand.nextBoolean()) ? 1 : -1; 

            geneChars[mutationIndex] = (char) (geneChars[mutationIndex] + mutationAmount);
            offspringGene = new String(geneChars);
            System.out.println("Mutation has happened");
            System.out.println("Created: " + offspringGene);
        }
        
        return offspringGene;
    }

    public int getBreedingAge() {
        return Integer.parseInt(gene.substring(0, 2)); 
    }

    public int getLifeSpan() {
        return Integer.parseInt(gene.substring(2, 5)); 
    }

    public double getBreedingProbability() {
        return Integer.parseInt(gene.substring(5, 7)) * 0.01; 
    }

    public int getLitterSize() {
        return Integer.parseInt(gene.substring(7, 9)); 
    }

    public double getDiseaseProbability() {
        return Integer.parseInt(gene.substring(9, 11)) * 0.01; 
    }

    public double getMetabolism() {
        return Integer.parseInt(gene.substring(11, 14)) * 0.01; 
    }
}
