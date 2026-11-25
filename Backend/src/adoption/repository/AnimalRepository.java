package adoption.repository;

import adoption.exception.AdoptionException;
import adoption.exception.AdoptionException.NotFoundException;
import adoption.model.Animal;
import adoption.model.Animal.AdoptionStatus;
import adoption.model.Animal.AnimalType;
import adoption.model.Animal.ListingType;
import adoption.model.Animal.Sex;
import adoption.model.Animal.VaccinationStatus;
import adoption.model.AnimalDetails;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AnimalRepository extends CsvRepository<Animal> {
    private static final Path DEFAULT_FILE = Path.of("data", "animals.csv");
    private static final String PHOTO_SEPARATOR = ";";

    private static final String[] HEADERS = {
        "ID", "poserID", "type", "name", "breed", "ageMonths", "sex", "weight",
        "appearance", "healthCondition", "vaccinationStatus", "spayedNeutered",
        "personality", "careRequirements", "adoptionStatus", "photos", "location",
        "listing_type"
    };

    public AnimalRepository() {
        this(DEFAULT_FILE);
    }

    public AnimalRepository(Path filePath) {
        super(filePath);
    }

    public Animal save(Animal animal) throws AdoptionException {
        System.out.println("DEBUG: save() called");
        List<Animal> animals = loadAll();

        int nextID = 1;
        for(Animal existing : animals) {
            if (existing.getID() >= nextID) {
                nextID = existing.getID() + 1;
            }
        }

        Animal saved = animal.withID(nextID);
        animals.add(saved);
        saveAll(animals);
        return saved;
    }

    public void update(Animal animal) throws AdoptionException {
        List<Animal> animals = loadAll();
        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getID() == animal.getID()) {
                animals.set(i, animal);
                saveAll(animals);
                return;
            }
        }
        throw new NotFoundException("Animal profile was not found.");
    }

    public void delete(int ID) throws AdoptionException {
        List<Animal> animals = loadAll();
        boolean removed = animals.removeIf(animal -> animal.getID() == ID);
        if (!removed) {
            throw new NotFoundException("Animal profile was not found.");
        }
        saveAll(animals);
    }

    public Optional<Animal> findByID (int ID) throws AdoptionException {
        for (Animal animal : loadAll()) {
            if (animal.getID() == ID) {
                return Optional.of(animal);
            }
        }
        return Optional.empty();
    }

    public List<Animal> findByPosterID(int posterID) throws AdoptionException {
        List<Animal> result = new ArrayList<>();
        for (Animal animal : loadAll()) {
            if (animal.isPostedBy(posterID)) {
                result.add(animal);
            }
        }
        return result;
    }

    public List<Animal> findAll() throws AdoptionException {
        return loadAll();
    }

    @Override 
    protected String[] getHeaders() {
        return HEADERS;
    }

    @Override 
    protected String[] toRow(Animal animal) {
        return new String[] {
            String.valueOf(animal.getID()),
            String.valueOf(animal.getPosterID()),
            animal.getType().name(),
            animal.getName(),
            animal.getBreed(),
            String.valueOf(animal.getAgeMonths()),
            animal.getSex().name(),
            String.valueOf(animal.getWeight()),
            animal.getAppearance(),
            animal.getHealthCondition(),
            animal.getVaccinationStatus().name(),
            String.valueOf(animal.isSpayedNeutered()),
            animal.getPersonality(),
            animal.getCareRequirements(),
            animal.getStatus().name(),
            String.join(PHOTO_SEPARATOR, animal.getPhotos()),
            animal.getLocation(),
            animal.getListingType().name()
        };
    }

    @Override 
    protected Animal fromRow(String[] row) {
        AnimalDetails details = new AnimalDetails();
        details.setType(AnimalType.valueOf(row[2]));
        details.setName(row[3]);
        details.setBreed(row[4]);
        details.setAgeMonths(Integer.parseInt(row[5]));
        details.setSex(Sex.valueOf(row[6]));
        details.setWeight(Double.parseDouble(row[7]));
        details.setAppearance(row[8]);
        details.setHealthCondition(row[9]);
        details.setVaccinationStatus(VaccinationStatus.valueOf(row[10]));
        details.setSpayedNeutered(Boolean.parseBoolean(row[11]));
        details.setPersonality(row[12]);
        details.setCareRequirements(row[13]);
        details.setPhotos(row[15].isBlank()
                ? new ArrayList<>()
                : Arrays.asList(row[15].split(PHOTO_SEPARATOR)));
        details.setLocation(row[16]);
        details.setListingType(ListingType.valueOf(row[17]));

        return Animal.of(Integer.parseInt(row[0]), Integer.parseInt(row[1]),
                details, AdoptionStatus.valueOf(row[14]));
    }
}
