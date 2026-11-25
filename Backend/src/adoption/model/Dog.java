package adoption.model;

public class Dog extends Animal {
    private static final double smallUntilWeight = 11;
    private static final double mediumUntilWeight = 27;
    private static final double largeUntilWeight = 46;

    public Dog(int ID, int posterID, AnimalDetails details, AdoptionStatus status) {
        super(ID, posterID, details, status);
    }

    @Override 
    public AnimalType getType() {
        return AnimalType.DOG;
    }

    @Override
    public String getSizeCategory() {
        if (getWeight() < smallUntilWeight) {
            return "Small";
        } if (getWeight() < mediumUntilWeight) {
            return "Medium";
        } if (getWeight() < largeUntilWeight) {
            return "Large";
        } return "Giant";
    }
}
