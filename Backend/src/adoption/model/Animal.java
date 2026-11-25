package adoption.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Animal {
    public static final int unsavedID = 0;

    public enum AnimalType {
        DOG("Dog"), CAT("Cat");

        private final String label;
        AnimalType(String label) { this.label = label; }
        @Override public String toString() { return label; }
    }

    public enum Sex {
        MALE("Male"), FEMALE("Female");

        private final String label;
        Sex(String label) { this.label = label; }
        @Override public String toString() { return label; }
    }

    public enum VaccinationStatus {
        VACCINATED("Fully Vaccinated"),
        PARTIALLYVACCINATED("Partially Vaccinated"),
        NOTVACCINATED("Not Vaccinated");

        private final String label;
        VaccinationStatus(String label) { this.label = label; }
        @Override public String toString() { return label; }
    }

    public enum AdoptionStatus {
        AVAILABLE("Available"), PENDING("Pending"), ADOPTED("Adopted / Rehomed");

        private final String label;
        AdoptionStatus(String label) { this.label = label; }
        @Override public String toString() { return label; }
    }

    public enum ListingType {
        ADOPTION("Adoption"), REHOMING("Rehoming");

        private final String label;
        ListingType(String label) { this.label = label; }
        @Override public String toString() { return label; }
    }

    /* Fields */
    private final int ID;
    private final int posterID; /* id of the user */
    private AdoptionStatus status;
    private String name;
    private String breed;
    private int ageMonths;
    private Sex sex;
    private double weight;
    private String appearance;
    private String healthCondition;
    private VaccinationStatus vaccinationStatus;
    private boolean spayedNeutered;
    private String personality;
    private String careRequirements;
    private String location;
    private ListingType listingType;
    private List<String> photos;

    protected Animal(int ID, int posterID, AnimalDetails details, AdoptionStatus status) {
        this.ID = ID;
        this.posterID = posterID;
        this.status = status;
        applyDetails(details);
    }

    public static Animal of(int ID, int posterID, AnimalDetails details, AdoptionStatus status) {
        return switch (details.getType()) {
            case DOG -> new Dog(ID, posterID, details, status);
            case CAT -> new Cat(ID, posterID, details, status);
        };
    }

    public abstract AnimalType getType();

    public abstract String getSizeCategory();

    public final void applyDetails(AnimalDetails details) {
        this.name = details.getName();
        this.breed = details.getBreed();
        this.ageMonths = details.getAgeMonths();
        this.sex = details.getSex();
        this.weight = details.getWeight();
        this.appearance = details.getAppearance();
        this.healthCondition = details.getHealthCondition();
        this.vaccinationStatus = details.getVaccinationStatus();
        this.spayedNeutered = details.isSpayedNeutered();
        this.personality = details.getPersonality();
        this.careRequirements = details.getCareRequirements();
        this.location = details.getLocation();
        this.listingType = details.getListingType();
        this.photos = new ArrayList<>(details.getPhotos());
    }

    public AnimalDetails toDetails() {
        AnimalDetails details = new AnimalDetails();
        details.setType(getType());
        details.setName(name);
        details.setBreed(breed);
        details.setAgeMonths(ageMonths);
        details.setSex(sex);
        details.setWeight(weight);
        details.setAppearance(appearance);
        details.setHealthCondition(healthCondition);
        details.setVaccinationStatus(vaccinationStatus);
        details.setSpayedNeutered(spayedNeutered);
        details.setPersonality(personality);
        details.setCareRequirements(careRequirements);
        details.setLocation(location);
        details.setListingType(listingType);
        details.setPhotos(photos);
        return details;
    }

    public Animal withID(int newID) {
        return Animal.of(newID, posterID, toDetails(), status);
    }

    public boolean isPostedBy(int userID) {
        return posterID == userID;
    }

    public String getAgeDisplay() {
        int years = ageMonths / 12;
        int months = ageMonths % 12;
        if (years == 0) {
            return months + (months == 1 ? " month" : " months");
        }
        String text = years + (years == 1 ? " year" : " years");
        return months == 0 ? text : text + " " + months + (months == 1 ? " month" : " months");
    }

    public int getID()                       { return ID; }
    public int getPosterID()                 { return posterID; }
    public AdoptionStatus getStatus()        { return status; }
    public void setStatus(AdoptionStatus status) { this.status = status; }
    public String getName()                  { return name; }
    public String getBreed()                 { return breed; }
    public int getAgeMonths()                { return ageMonths; }
    public Sex getSex()                      { return sex; }
    public double getWeight()                { return weight; }
    public String getAppearance()            { return appearance; }
    public String getHealthCondition()       { return healthCondition; }
    public VaccinationStatus getVaccinationStatus() { return vaccinationStatus; }
    public boolean isSpayedNeutered()        { return spayedNeutered; }
    public String getPersonality()           { return personality; }
    public String getCareRequirements()      { return careRequirements; }
    public String getLocation()              { return location; }
    public ListingType getListingType()      { return listingType; }
    public List<String> getPhotos()          { return new ArrayList<>(photos); }

    @Override 
    public String toString() {
        return name + " (" + getType() + ", " + status + ")";
    }
}
