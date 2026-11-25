package adoption.model;

import adoption.model.Animal.AnimalType;
import adoption.model.Animal.ListingType;
import adoption.model.Animal.Sex;
import adoption.model.Animal.VaccinationStatus;
import java.util.ArrayList;
import java.util.List;

public class AnimalDetails {
    private AnimalType type;
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
    private List<String> photos = new ArrayList<>();

    public AnimalType getType()                   { return type; }
    public String getName()                       { return name; }
    public String getBreed()                      { return breed; }
    public int getAgeMonths()                     { return ageMonths; }
    public Sex getSex()                           { return sex; }
    public double getWeight()                     { return weight; }
    public String getAppearance()                 { return appearance; }
    public String getHealthCondition()            { return healthCondition; }
    public VaccinationStatus getVaccinationStatus() { return vaccinationStatus; }
    public boolean isSpayedNeutered()             { return spayedNeutered; }
    public String getPersonality()                { return personality; }
    public String getCareRequirements()           { return careRequirements; }
    public String getLocation()                   { return location; }
    public ListingType getListingType()           { return listingType; }
    public List<String> getPhotos()               { return new ArrayList<>(photos); }

    public void setType(AnimalType type)          { this.type = type; }
    public void setName(String name)              { this.name = clean(name); }
    public void setBreed(String breed)            { this.breed = clean(breed); }
    public void setAgeMonths(int ageMonths)       { this.ageMonths = ageMonths; }
    public void setSex(Sex sex)                   { this.sex = sex; }
    public void setWeight(double weight)          { this.weight = weight; }
    public void setAppearance(String appearance)  { this.appearance = clean(appearance); }
    public void setHealthCondition(String health) { this.healthCondition = clean(health); }
    public void setVaccinationStatus(VaccinationStatus status) { this.vaccinationStatus = status; }
    public void setSpayedNeutered(boolean value)  { this.spayedNeutered = value; }
    public void setPersonality(String personality) { this.personality = clean(personality); }
    public void setCareRequirements(String care)  { this.careRequirements = clean(care); }
    public void setLocation(String location)      { this.location = clean(location); }
    public void setListingType(ListingType type)  { this.listingType = type; }

    public void setPhotos(List<String> photos) {
        this.photos = new ArrayList<>();
        if (photos != null) {
            for (String photo : photos) {
                this.photos.add(clean(photo));
            }
        }
    }

    public AnimalDetails copy() {
        AnimalDetails copy = new AnimalDetails();
        copy.type = type;
        copy.name = name;
        copy.breed = breed;
        copy.ageMonths = ageMonths;
        copy.sex = sex;
        copy.weight = weight;
        copy.appearance = appearance;
        copy.healthCondition = healthCondition;
        copy.vaccinationStatus = vaccinationStatus;
        copy.spayedNeutered = spayedNeutered;
        copy.personality = personality;
        copy.careRequirements = careRequirements;
        copy.location = location;
        copy.listingType = listingType;
        copy.photos = new ArrayList<>(photos);
        return copy;
    }

    private static String clean(String value) {
        return value == null ? null : value.trim();
    }
}