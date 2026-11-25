package adoption.ui;

import adoption.exception.AdoptionException;
import adoption.model.Animal;
import adoption.model.Animal.AnimalType;
import adoption.model.Animal.ListingType;
import adoption.model.AnimalFilter;
import adoption.model.User;
import adoption.service.BrowseService;
import java.util.List;

public class BrowseMenu {
    private static final String[] OPTIONS = {
        "Show Available Animals", "Search / Set Filters", "Clear Filters", "View an Animal's Profile", "Back"
    };

    private final ConsoleInput input;
    private final BrowseService browseService;

    public BrowseMenu(ConsoleInput input, BrowseService browseService) {
        this.input = input;
        this.browseService = browseService;
    }

    public void show(User user) {
        AnimalFilter filter = new AnimalFilter();

        boolean running = true;
        while (running) {
            System.out.println("Browse Animals");
            System.out.println("Filters: " + filter);
            for (int i = 0; i < OPTIONS.length; i++) {
                System.out.println((i + 1) + ". " + OPTIONS[i]);
            }

            try {
                switch (input.prompt("Choose an option")) {
                    case "1" -> showAnimals(user, filter);
                    case "2" -> filter = askFilter();
                    case "3" -> filter = new AnimalFilter();
                    case "4" -> viewProfile();
                    case "5" -> running = false;
                    default -> System.out.println("Invalid option. Please enter 1 to 5.");
                }
            } catch (AdoptionException e) {
                System.out.println("Failed: " + e.getLocalizedMessage());
            }
        }
    }

    private void showAnimals(User user, AnimalFilter filter) throws AdoptionException {
        List<Animal> animals = browseService.browse(filter);
        if (animals.isEmpty()) {
            System.out.println("No available animals matching your search.");
            return;
        }
        for (Animal animal : animals) {
            AnimalView.printSummary(animal, animal.isPostedBy(user.getID()));
        }
        System.out.println("\n" + animals.size() + " animal(s) found. " + "Choose option 4 to open a profile.");
    }

    private AnimalFilter askFilter() {
        AnimalFilter filter = new AnimalFilter();
        filter.setType(input.chooseOptionOrAny("Type", AnimalType.values()));
        filter.setListingType(input.chooseOptionOrAny("Adoption or Rehoming", ListingType.values()));
        filter.setKeyword(input.prompt("Search by name, breed, or location (Enter to skip)"));
        filter.setVaccinatedOnly(input.promptYesNo("Only fully vaccinated animals?", false));
        filter.setSpayedNeuteredOnly(input.promptYesNo("Only spayed/neutered animals?", false));
        return filter;
    }

    private void viewProfile() throws AdoptionException {
        int ID = input.promptInt("Animal ID");
        AnimalView.printProfile(browseService.getProfile(ID));
    }
}