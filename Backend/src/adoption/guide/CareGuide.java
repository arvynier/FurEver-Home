package adoption.guide;

import adoption.guide.CostItem.Frequency;
import adoption.model.Animal.AnimalType;
import java.util.ArrayList;
import java.util.List;

public abstract class CareGuide {
    public static class Section {
        private final String title;
        private final String[] tips;

        public Section(String title, String[] tips) {
            this.title = title;
            this.tips = tips.clone();
        }

        public String getTitle() { return title; }
        public String[] getTips() { return tips.clone(); }
    }

    private static final String[] INTRODUCTION = {
        "Responsible pet ownership requires more than providing food and shelter. It involves a long-term commitment to an animal's physical health, emotional well-being, safety, and overall quality of life. Prospective adopters must understand the responsibilities and financial obligations associated with caring for an animal before deciding to adopt.",
        "In the Philippines, pet owners are expected to provide adequate food, clean water, appropriate shelter, veterinary attention, and protection from harm. These responsibilities are supported by the Animal Welfare Act of 1998 (Republic Act No. 8485), as amended by Republic Act No. 10631, and the Anti-Rabies Act of 2007 (Republic Act No. 9482)."
    };

    private static final String[] HEALTH_AND_WELLNESS = {
        "Feeding balanced meals suitable for your pet’s age, breed, and medical conditions.",
        "Early detection of digestive issues, skin conditions, dental problems, and parasites can save your pet discomfort and costly treatment.",
        "Following veterinary-recommended schedules helps prevent potentially life-threatening diseases.",
        "Pets need stimulation and companionship to avoid stress and anxiety. Regular play, enrichment toys, and interaction are proven to reduce behavioral issues."
    };

    private static final String[] TRAININg_AND_BEHAVIOR = {
        "Housebreaking, obedience commands, and leash manners form the foundation for good behavior.",
        "Solutions for excessive barking, scratching, chewing, or separation anxiety help prevent frustration and unsafe situations.",
        "Mental engagement through tricks or agility activities improves cognition and emotional stability.",
        "Introducing pets to people, other animals, and new environments reduces fear and promotes confidence."
    };

    private static final String[] GROOMING_AND_HYGIENE = {
        "Brushing and bathing according to breed-specific needs maintains a healthy coat and skin.",
        "Nail, ear and dental care prevents infections, overgrowth, and discomfort.",
        "Some breeds require more frequent or specialized care.",
        "Knowing when to handle grooming at home versus using professional services ensures safety and reduces anxiety."
    };

    private static final String[] PET_SAFETY = {
        "Household chemicals, small objects, and human foods can be dangerous. Removing hazards prevents emergencies.",
        "Proper leashes, harnesses, carriers, and weather-appropriate protection keep pets secure and comfortable.",
        "Knowing basic first aid, having emergency supplies, and recognizing warning signs can make the difference between a minor incident and a serious emergency.",
        "Collars, ID tags, microchips, GPS trackers, and protective gear enhance security and peace of mind."
    };

    private static final String VET_PRICE_NOTE = "These prices are provider-specific and may change depending on the clinic, location, animal condition, and availability of services.";

    private static final String[] LGU_SERVICES = {
        "Anti-rabies Vaccinations", "Basic Veterinary Consultations", "Deworming Services", "Community Animal Health Programs"
    };

    private static final String[] AFFORDABLE_CARE_NOTES = {
        "The availability, eligibility requirements, and schedules of these services depend on the respective municipality or city. Prospective adopters are encouraged to contact their local veterinary office to inquire about available services and community programs.",
        "Preventive healthcare plays an important role in maintaining an animal's well-being. Regular veterinary consultations, appropriate vaccinations, deworming, and proper hygiene help owners monitor their animals' health and address potential concerns.",
        "Veterinary procedures should always follow professional recommendations based on the individual animal's needs."
    };

    private static final String CONCLUSION = "Providing a suitable home for an animal requires continuous attention, responsible care, and financial preparation. Food, veterinary services, hygiene, supplies, and unexpected medical expenses are important considerations that prospective adopters should understand before making a commitment.";

    public abstract AnimalType getAnimalType();

    public abstract String getCostBasis();

    public abstract List<CostItem> getCostItems();

    public abstract List<CostItem> getVetServices();

    public abstract String[] getEssentialSupplies();

    public abstract String[] getCostNotes();

    public String[] getIntroduction() { return INTRODUCTION.clone(); }

    public List<Section> getGeneralSections() {
        List<Section> sections = new ArrayList<>();
        sections.add(new Section("Health and Wellness", HEALTH_AND_WELLNESS));
        sections.add(new Section("Training and Behavior", TRAININg_AND_BEHAVIOR));
        sections.add(new Section("Grooming and Hygiene", GROOMING_AND_HYGIENE));
        sections.add(new Section("Pet Safety", PET_SAFETY));
        return sections;
    }

    public String getVetPriceNote() { return VET_PRICE_NOTE; }
    public String[] getLguServices() { return LGU_SERVICES.clone(); }
    public String[] getAffordableCareNotes() { return AFFORDABLE_CARE_NOTES.clone(); }
    public String getConclusion() { return CONCLUSION; }

    public double getInitialMin() { return total(Frequency.ONE_TIME, false); }
    public double getInitialMax() { return total(Frequency.ONE_TIME, true); }

    public boolean hasInitialCosts() { return getInitialMax() > 0;}

    public boolean hasOpenEndedInitialCost() {
        for (CostItem item : getCostItems()) {
            if (item.getFrequency() == Frequency.ONE_TIME && item.isOpenEnded()) {
                return true;
            }
        }
        return false;
    }

    public double getMonthlyMin() { return total(Frequency.MONTHLY, false); }
    public double getMonthlyMax() { return total(Frequency.MONTHLY, true); }

    public double getProjectedMin(int years) { return getMonthlyMin() * 12 * checkYears(years); }
    public double getProjectedMax(int years) { return getMonthlyMax() * 12 * checkYears(years); }

    private double total(Frequency frequency, boolean useMax) {
        double sum = 0;
        for (CostItem item : getCostItems()) {
            if (item.getFrequency() == frequency) {
                sum += useMax ? item.getMax() : item.getMin();
            }
        }
        return sum;
    }

    private int checkYears(int years) {
        if (years < 1) {
            throw new IllegalArgumentException("Years must be at least 1.");
        }
        return years;
    }

    protected static List<CostItem> commonVetServices() {
        List<CostItem> services = new ArrayList<>();
        services.add(new CostItem("Initial registration fee", 300, 500, Frequency.PER_SERVICE));
        services.add(new CostItem("Anti-rabies vaccination", 200, 500, Frequency.PER_SERVICE));
        services.add(new CostItem("Dog 5-in-1 / Cat 3-in-1 vaccination", 400, 800, Frequency.PER_SERVICE));
        services.add(new CostItem("Other vaccinations", 500, 800, Frequency.PER_SERVICE));
        services.add(new CostItem("Deworming (per dose)", 200, 500, Frequency.PER_SERVICE));
        services.add(new CostItem("Emergency confinement (per day, up to this in some Metro Manila clinics)", 3500, Frequency.PER_SERVICE));
        return services;
    }
}