package adoption.guide;

public class CostItem {
    public enum Category {
        FOOD("Food"), VETERINARY_CARE("Veterinary Care"), GROOMING ("Grooming"), SUPPLIES("Supplies");

        private final String label;
        Category(String label) {
            this.label = label;
        }
        @Override public String toString() {
            return label;
        }
    }

    public enum Frequency {
        MONTHLY("Per Month"), YEARLY("Per Year");
        
        private final String label;
        Frequency(String label) {
            this.label = label;
        } 
        @Override public String toString() {
            return label;
        }
    }

    
}
