package adoption.ui;

import adoption.model.Animal;
import adoption.model.AnimalProfile;

public final class AnimalView {
    private AnimalView() {
    }

    public static void printSummary(Animal a, boolean postedByViewer) {
        System.out.println("\n[ID " + a.getID() + "] " + a.getName() + " - " + a.getType()
                + ", " + a.getSizeCategory() + ", " + a.getBreed()
                + (postedByViewer ? "  <your listing>" : ""));
        System.out.println("      " + a.getLocation() + " | " + a.getListingType()
                + " | " + a.getSex() + ", " + a.getAgeDisplay() + ", " + a.getWeight() + " kg");
    }

    public static void printDetails(Animal a) {
        System.out.println("\n[ID " + a.getID() + "] " + a.getName() + " - " + a.getType()
                + " (" + a.getListingType() + ")");
        printFields(a);
    }

    public static void printProfile(AnimalProfile profile) {
        Animal a = profile.getAnimal();
        System.out.println("\n" + a.getName() + " (" + a.getType() + ")");
        printFields(a);
        System.out.println("Posted by:");
        System.out.println("Name: " + profile.getPosterName());
        System.out.println("Mobile: " + profile.getPosterMobile());
        System.out.println("Email: " + profile.getPosterEmail());
    }
    
    private static void printFields(Animal a) {
        System.out.println("  Listing: " + a.getListingType() + " | Status: " + a.getStatus()
                + " | Location: " + a.getLocation());
        System.out.println("  Breed: " + a.getBreed() + " | Age: " + a.getAgeDisplay()
                + " | Sex: " + a.getSex()
                + " | Weight: " + a.getWeight() + " kg (" + a.getSizeCategory() + ")");
        System.out.println("  Appearance: " + a.getAppearance());
        System.out.println("  Health condition: " + a.getHealthCondition());
        System.out.println("  Vaccination: " + a.getVaccinationStatus()
                + " | Spayed/neutered: " + (a.isSpayedNeutered() ? "yes" : "no"));
        System.out.println("  Personality: " + a.getPersonality());
        System.out.println("  Care requirements: " + a.getCareRequirements());
        System.out.println("  Photos: " + String.join(", ", a.getPhotos()));
    }
}