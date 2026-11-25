package adoption.model;

public class AnimalProfile {
    private final Animal animal;
    private final String posterName;
    private final String posterMobileNumber;
    private final String posterEmail;

    public AnimalProfile(Animal animal, User poster) {
        this.animal = animal;
        this.posterName = poster.getFullName();
        this.posterMobileNumber = poster.getMobileNumber();
        this.posterEmail = poster.getEmail();
    }

    public Animal getAnimal()           { return animal; }
    public String getPosterName()       { return posterName; }
    public String getPosterMobile()     { return posterMobileNumber; }
    public String getPosterEmail()      { return posterEmail; }
}