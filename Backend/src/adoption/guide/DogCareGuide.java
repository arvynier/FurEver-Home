package adoption.guide;

import adoption.guide.CostItem.Frequency;
import adoption.model.Animal.AnimalType;
import java.util.List;

public class DogCareGuide extends CareGuide {
    private static final String[] SUPPLIES = {
        "Food and water bowls", "Appropriate dog food", "Collar and leash",
        "Bed or designated resting area", "Grooming supplies", "Toys and enrichment materials",
        "Waste bags or waste scooper", "Identification tag"
    };

    private static final String[] NOTES = {
        "Another Philippine pet ownership guide (Pet Friendly's PH) estimates monthly expenses at PHP 3,000 - 8,000 for small dogs and PHP 6,000 - 12,000 for large dogs. The difference is largely influenced by food consumption, grooming requirements, and veterinary needs.",
        "These are illustrative budgets, not fixed or mandatory expenses. Training and boarding are optional and depend on the owner's circumstances."
    };

    private static final List<CostItem> COST_ITEMS = List.of(
        new CostItem("Food", 500, 1500, Frequency.MONTHLY),
        new CostItem("Routine veterinary care (monthly average)", 450, 800, Frequency.MONTHLY),
        new CostItem("Grooming", 100, 700, Frequency.MONTHLY),
        new CostItem("Supplies and replacements", 300, 800, Frequency.MONTHLY),
        new CostItem("Treats", 200, 500, Frequency.MONTHLY),
        new CostItem("Training (optional)", 0, 500, Frequency.MONTHLY),
        new CostItem("Boarding or pet sitting (optional)", 350, 800, Frequency.MONTHLY),
        new CostItem("Emergency savings", 300, 800, Frequency.MONTHLY),
        new CostItem("Miscellaneous expenses", 200, 500, Frequency.MONTHLY)  
    );

    @Override 
    public AnimalType getAnimalType() {
        return AnimalType.DOG;
    }

    @Override
    public String getCostBasis() {
        return "A February 2026 cost guide by McDuffy for small-to-medium-sized dogs. "
                + "Lower estimate = starter budget, upper estimate = mid-range budget.";
    }

    @Override 
    public List<CostItem> getCostItems() {
        return COST_ITEMS;
    }

    @Override
    public List<CostItem> getVetServices() {
        List<CostItem> services = commonVetServices();
        services.add(new CostItem("Dog 6-in-1 vaccination", 950, Frequency.PER_SERVICE));
        services.add(new CostItem("Dog 8-in-1 vaccination", 750, Frequency.PER_SERVICE));
        services.add(new CostItem("Kennel cough vaccination", 800, Frequency.PER_SERVICE));
        return services;
    }

    @Override
    public String[] getEssentialSupplies() {
        return SUPPLIES.clone();
    }

    @Override
    public String [] getCostNotes() {
        return NOTES.clone();
    }
}