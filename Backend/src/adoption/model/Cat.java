package adoption.model;

public class Cat extends Animal {
    private static final double smallUntilWeight = 3.6;
    private static final double mediumUntilWeight = 5.5;
    private static final double largeUntilWeight = 9.1;

    public Cat(int ID, int posterID, AnimalDetails details, AdoptionStatus status) {
        super(ID, posterID, details, status);
    }

    @Override 
    public AnimalType getType() {
        return AnimalType.CAT;
    }

    @Override 
    public String getSizeCategory() {
        if (getWeight() < smallUntilWeight) {
            return "Small";
        } if (getWeight() < mediumUntilWeight) {
            return "Medium";
        } if (getWeight() < largeUntilWeight) {
            return "Large";
        } return "Very Large";
    }
}
