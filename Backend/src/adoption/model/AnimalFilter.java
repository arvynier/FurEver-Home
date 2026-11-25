package adoption.model;

import adoption.model.Animal.AnimalType;
import adoption.model.Animal.ListingType;
import adoption.model.Animal.VaccinationStatus;
import java.util.ArrayList;
import java.util.List;

public class AnimalFilter {
    private AnimalType type;
    private ListingType listingType;
    private String keyword = "";
    private boolean vaccinatedOnly;
    private boolean spayedNeuteredOnly;

    public AnimalType getType()            { return type; }
    public ListingType getListingType()    { return listingType; }
    public String getKeyword()             { return keyword; }
    public boolean isVaccinatedOnly()      { return vaccinatedOnly; }
    public boolean isSpayedNeuteredOnly()  { return spayedNeuteredOnly; }

    public void setType(AnimalType type)                { this.type = type; }
    public void setListingType(ListingType listingType) { this.listingType = listingType; }
    public void setVaccinatedOnly(boolean value)        { this.vaccinatedOnly = value; }
    public void setSpayedNeuteredOnly(boolean value)    { this.spayedNeuteredOnly = value; }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? "" : keyword.trim();
    }

    public boolean matches(Animal animal) {
        if (type != null && animal.getType() != type) {
            return false;
        } if (listingType != null && animal.getListingType() != listingType) {
            return false;
        } if (vaccinatedOnly && animal.getVaccinationStatus() != VaccinationStatus.VACCINATED) {
            return false;
        } if (spayedNeuteredOnly && !animal.isSpayedNeutered()) {
            return false;
        } if (!keyword.isEmpty()) {
            String wanted = keyword.toLowerCase();
            boolean found = animal.getName().toLowerCase().contains(wanted)
                    || animal.getBreed().toLowerCase().contains(wanted)
                    || animal.getLocation().toLowerCase().contains(wanted);
            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Override 
    public String toString() {
        List<String> parts = new ArrayList<>();
        if (type != null) {
            parts.add(type.toString());
        } if (listingType != null) {
            parts.add(listingType.toString());
        } if (!keyword.isEmpty()) {
            parts.add("\"" + keyword + "\"");
        } if (vaccinatedOnly) {
            parts.add("Fully Vaccinated");
        } if (spayedNeuteredOnly) {
            parts.add("Spayed/Neutered");
        }
        return parts.isEmpty() ? "None (Showing All)" : String.join(", ", parts);
    }
}