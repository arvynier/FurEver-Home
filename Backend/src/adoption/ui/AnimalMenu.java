package adoption.ui;

import adoption.exception.AdoptionException;
import adoption.model.Animal;
import adoption.model.Animal.AdoptionStatus;
import adoption.model.Animal.AnimalType;
import adoption.model.Animal.ListingType;
import adoption.model.Animal.Sex;
import adoption.model.Animal.VaccinationStatus;
import adoption.model.AnimalDetails;
import adoption.model.User;
import adoption.service.AnimalService;
import adoption.utility.InputValidator;
import java.util.ArrayList;
import java.util.List;

public class AnimalMenu {
    private static final String[] OPTIONS = {
        "Post a new animal", "View my listings", "Edit a listing", "Update adoption status", "Remove a listing", "Back"
    };

    private final ConsoleInput input;
    private final AnimalService animalService;

    public AnimalMenu(ConsoleInput input, AnimalService animalService) {
        this.input = input;
        this.animalService = animalService;
    }

    public void show(User user) {
        boolean running = true;
        while(running) {
            System.out.println("My Animal Listings");
            for(int i = 0; i < OPTIONS.length; i++) {
                System.out.println((i + 1) + ". " + OPTIONS[i]);
            }

            try {
                switch (input.prompt("Choose an option")) {
                    case "1" -> postAnimal(user);
                    case "2" -> viewListings(user);
                    case "3" -> editListing(user);
                    case "4" -> updateStatus(user);
                    case "5" -> removeListing(user);
                    case "6" -> running = false;
                    default -> System.out.println("Invalid option. Please enter 1 to 6.");
                }
            } catch (AdoptionException e) { 
                System.out.println("Failed: " + e.getMessage());
            }
        }
    }
    private void postAnimal(User user) throws AdoptionException {
        System.out.println("Post an Animal");
        AnimalDetails details = askDetails(null);
        details.setPhotos(askPhotos());

        Animal animal = animalService.postAnimal(user, details);
        System.out.println("Posted! " + animal.getName() + " is now listed (ID " + animal.getID() + ").");
    }

    private void viewListings(User user) throws AdoptionException {
        List<Animal> animals = animalService.getListingsOf(user);
        if(animals.isEmpty()) {
            System.out.println("You have not posted any animals yet.");
            return;
        } 
        for(Animal animal : animals) {
            printAnimal(animal);
        }
    }

    private void editListing(User user) throws AdoptionException {
        Animal animal = chooseOwnListing(user, "edit");
        if (animal == null) {
            return;
        }

        System.out.println("Press Enter to keep the current value. (The type cannot be changed.)");
        AnimalDetails details = askDetails(animal.toDetails());

        boolean replacePhotos = input.promptYesNo("Replace the photos?", false);
        if (replacePhotos) {
            details.setPhotos(askPhotos());
        }

        animalService.editAnimal(user, animal.getID(), details, replacePhotos);
        System.out.println("Listing updated.");
    }

    private void updateStatus(User user) throws AdoptionException {
        Animal animal = chooseOwnListing(user, "update");
        if (animal == null) {
            return;
        }
        AdoptionStatus status = input.chooseOption("Adoption status", AdoptionStatus.values(), animal.getStatus());
        animalService.updateStatus(user, animal.getID(), status);
        System.out.println("Status updated to " + status + ".");
    }

    private void removeListing(User user) throws AdoptionException {
        Animal animal = chooseOwnListing(user, "remove");
        if (animal == null) {
            return;
        }
        if (input.promptYesNo("Remove " + animal.getName() + " permanently?", false)) {
            animalService.removeAnimal(user, animal.getID());
            System.out.println("Listing removed.");
        } else {
            System.out.println("Cancelled.");
        }
    }

    private Animal chooseOwnListing(User user, String action) throws AdoptionException {
        List<Animal> animals = animalService.getListingsOf(user);
        if (animals.isEmpty()) {
            System.out.println("You have not posted any animals yet.");
            return null;
        }
        for (Animal animal : animals) {
            System.out.println("ID " + animal.getID() + ": " + animal);
        }
        int ID = input.promptInt("ID of the animal to " + action);
        return animalService.getAnimal(ID);
    }

    private AnimalDetails askDetails(AnimalDetails existing) {
        boolean editing = existing != null;
        AnimalDetails d = new AnimalDetails();

        d.setType(editing ? existing.getType()
                : input.chooseOption("Type", AnimalType.values(), null));
        d.setName(ask("Name", editing ? existing.getName() : null));
        d.setBreed(ask("Breed", editing ? existing.getBreed() : null));
        d.setAgeMonths(askInt("Age in months", editing ? existing.getAgeMonths() : null));
        d.setSex(input.chooseOption("Sex", Sex.values(), editing ? existing.getSex() : null));
        d.setWeight(askDouble("Weight in kg", editing ? existing.getWeight() : null));
        d.setAppearance(ask("Appearance (physical description)", editing ? existing.getAppearance() : null));
        d.setHealthCondition(ask("Health condition (healthy, recovering, has allergies...)",
                editing ? existing.getHealthCondition() : null));
        d.setVaccinationStatus(input.chooseOption("Vaccination status", VaccinationStatus.values(),
                editing ? existing.getVaccinationStatus() : null));
        d.setSpayedNeutered(input.promptYesNo("Spayed/neutered?",
                editing ? existing.isSpayedNeutered() : null));
        d.setPersonality(ask("Personality (friendly, shy, playful, good with kids...)",
                editing ? existing.getPersonality() : null));
        d.setCareRequirements(ask("Care requirements (food, grooming, daily activities...)",
                editing ? existing.getCareRequirements() : null));
        d.setLocation(ask("Location", editing ? existing.getLocation() : null));
        d.setListingType(input.chooseOption("Adoption or rehoming", ListingType.values(),
                editing ? existing.getListingType() : null));

        if (editing) {
            d.setPhotos(existing.getPhotos());
        }
        return d;
    }

    private List<String> askPhotos() {
        List<String> photos = new ArrayList<>();
        System.out.println("Photos: type the file path of each photo (1 to "
                + InputValidator.maxPhotos + "). Press Enter when done.");
        while (photos.size() < InputValidator.maxPhotos) {
            String path = input.prompt("Photo " + (photos.size() + 1) + " path").replace("\"", "");
            if (path.isBlank()) {
                break;
            }
            photos.add(path);
        }
        return photos;
    }

    private String ask(String label, String current) {
        String answer = input.prompt(current == null ? label : label + " [" + current + "]");
        return (answer.isBlank() && current != null) ? current : answer;
    }

    private int askInt(String label, Integer current) {
        String answer = input.prompt(current == null ? label : label + " [" + current + "]");
        return (answer.isBlank() && current != null) ? current : Integer.parseInt(answer);
    }

    private double askDouble(String label, Double current) {
        String answer = input.prompt(current == null ? label : label + " [" + current + "]");
        return (answer.isBlank() && current != null) ? current : Double.parseDouble(answer);
    }

    private void printAnimal(Animal a) {
        System.out.println("\n[ID " + a.getID() + "] " + a.getName() + " - " + a.getType()
                + " (" + a.getListingType() + ")");
        System.out.println("  Status: " + a.getStatus() + " | Location: " + a.getLocation());
        System.out.println("  Breed: " + a.getBreed() + " | Age: " + a.getAgeDisplay()
                + " | Sex: " + a.getSex() + " | Weight: " + a.getWeight() + " kg");
        System.out.println("  Appearance: " + a.getAppearance());
        System.out.println("  Health: " + a.getHealthCondition() + " | " + a.getVaccinationStatus()
                + " | Spayed/neutered: " + (a.isSpayedNeutered() ? "yes" : "no"));
        System.out.println("  Personality: " + a.getPersonality());
        System.out.println("  Care requirements: " + a.getCareRequirements());
        System.out.println("  Photos: " + String.join(", ", a.getPhotos()));
        System.out.println("  Size Category: " + a.getSizeCategory());
    }
}
