package adoption.guide;

import adoption.guide.CostItem.Frequency;
import adoption.model.Animal.AnimalType;
import java.util.List;

public class CatCareGuide extends CareGuide {
    private static final String[] SUPPLIES = {
        "Food and water bowls", "Appropriate cat food", "Litter box and cat litter", "Cat carrier",
        "Scratching post", "Bed or designated resting area", "Toys and enrichment materials",
        "Grooming supplies"
    };

    private static final String[] NOTES = {
        "The initial cost depends on the quality of the supplies and on whether the cat already had vaccinations, medical examinations, or sterilization.",
        "The recurring estimate excludes the initial setup expenses, major medical procedures, and unexpected veterinary emergencies."
    };

    private static final List<CostItem> COST_ITEMS = List.of (
        new CostItem("First week's cat food", 300, 900, Frequency.ONE_TIME),
        new CostItem("Food and water bowls", 150, 500, Frequency.ONE_TIME),
        new CostItem("Cat litter and litter box", 400, 1500, Frequency.ONE_TIME),
        new CostItem("Cat carrier", 500, 1500, Frequency.ONE_TIME),
        new CostItem("Bed, toys, and scratching post", 500, 2000, Frequency.ONE_TIME),
        new CostItem("Vaccinations and checkups", 1500, 6000, Frequency.ONE_TIME),
        new CostItem("Spaying/neutering", 700, 5000, Frequency.ONE_TIME, true),
        // monthly expenses
        new CostItem("Cat food", 1300, 3900, Frequency.MONTHLY),
        new CostItem("Cat litter", 400, 1200, Frequency.MONTHLY),
        new CostItem("Treats, toys, and other supplies", 200, 900, Frequency.MONTHLY)
    );

    @Override
    public AnimalType getAnimalType() {
        return AnimalType.CAT;
    }

    @Override 
    public String getCostBasis() {
        return "The 2026 cat ownership guide published by WHISKAS Philippines.";
    }

    @Override 
    public List<CostItem> getCostItems() {
        return COST_ITEMS;
    }

    @Override
    public List<CostItem> getVetServices() {
        List<CostItem> services = commonVetServices();
        services.add(new CostItem("Cat Tricat vaccination", 1200, Frequency.PER_SERVICE));
        return services;
    }

    @Override 
    public String[] getEssentialSupplies() {
        return SUPPLIES.clone();
    }

    @Override
    public String[] getCostNotes() {
        return NOTES.clone();
    }
}