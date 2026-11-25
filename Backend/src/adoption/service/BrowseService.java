package adoption.service;

import adoption.exception.AdoptionException;
import adoption.exception.AdoptionException.NotFoundException;
import adoption.model.Animal;
import adoption.model.Animal.AdoptionStatus;
import adoption.model.AnimalFilter;
import adoption.model.AnimalProfile;
import adoption.model.User;
import adoption.repository.AnimalRepository;
import adoption.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BrowseService {
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public BrowseService(AnimalRepository animalRepository, UserRepository userRepository) {
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    public List<Animal> browse(AnimalFilter filter) throws AdoptionException {
        AnimalFilter activeFilter = (filter == null) ? new AnimalFilter() : filter;

        List<Animal> result = new ArrayList<>();
        for (Animal animal : animalRepository.findAll()) {
            if (animal.getStatus() == AdoptionStatus.AVAILABLE && activeFilter.matches(animal)) {
                result.add(animal);
            }
        }
        result.sort(Comparator.comparingInt(Animal::getID).reversed());
        return result;
    }

    public AnimalProfile getProfile(int animalID) throws AdoptionException {
        Animal animal = animalRepository.findByID(animalID).orElseThrow(() -> new NotFoundException("Animal listing was not found."));
        
        User poster = userRepository.findByID(animal.getPosterID()).orElseThrow(() -> new NotFoundException("The poster of this listing cannot be found."));

        return new AnimalProfile(animal, poster);
    }
}