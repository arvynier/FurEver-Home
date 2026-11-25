package adoption.service;

import adoption.exception.AdoptionException;
import adoption.exception.AdoptionException.NotAuthorizedException;
import adoption.exception.AdoptionException.NotFoundException;
import adoption.exception.AdoptionException.ValidationException;
import adoption.model.Animal;
import adoption.model.Animal.AdoptionStatus;
import adoption.model.AnimalDetails;
import adoption.model.AnimalFilter;
import adoption.model.User;
import adoption.repository.AnimalRepository;
import adoption.repository.UserRepository;
import adoption.utility.FileStorage;
import adoption.utility.InputValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnimalService {
    private static final String PHOTO_PREFIX = "animal";
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public AnimalService(AnimalRepository animalRepository, UserRepository userRepository) {
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    public Animal postAnimal(User poster, AnimalDetails details) throws AdoptionException {
        validateDetails(details);
        adoption.utility.InputValidator.validatePhotoFiles(details.getPhotos());

        AnimalDetails toSave = details.copy();
        List<String> savedPhotos = copyPhotos(details.getPhotos());
        toSave.setPhotos(savedPhotos);

        try {
            return animalRepository.save(
                Animal.of(Animal.unsavedID, poster.getID(), toSave, AdoptionStatus.AVAILABLE));
        } catch (AdoptionException e) {
            deletePhotos(savedPhotos);
            throw e;
        }
    }

    public Animal editAnimal(User editor, int animalID, AnimalDetails details, boolean replacePhotos) throws AdoptionException {
        Animal animal = findOwnedAnimal(editor, animalID);
        validateDetails(details);

        AnimalDetails updated = details.copy();
        List<String> oldPhotos = animal.getPhotos();
        List<String> newPhotos = null;

        if (replacePhotos) {
            adoption.utility.InputValidator.validatePhotoFiles(details.getPhotos());
            newPhotos = copyPhotos(details.getPhotos());
            updated.setPhotos(newPhotos);
        } else {
            updated.setPhotos(oldPhotos);
        }

        animal.applyDetails(updated);

        try {
            animalRepository.update(animal);
        } catch (AdoptionException e) {
            if (newPhotos != null) {
                deletePhotos(newPhotos);
            }
            throw e;
        }

        if (newPhotos != null) {
            deletePhotos(oldPhotos);
        }
        return animal;
    }

    public Animal updateStatus(User editor, int animalID, AdoptionStatus status) throws AdoptionException {
        adoption.utility.InputValidator.validateRequired(status, "Adoption Status");
        Animal animal = findOwnedAnimal(editor, animalID);
        animal.setStatus(status);
        animalRepository.update(animal);
        return animal;
    }

    public void removeAnimal(User editor, int animalID) throws AdoptionException {
        Animal animal = findOwnedAnimal(editor, animalID);
        animalRepository.delete(animalID);
        deletePhotos(animal.getPhotos());
    }

    public List <Animal> getListingsOf(User poster) throws AdoptionException {
        return animalRepository.findByPosterID(poster.getID());
    }

    public Animal getAnimal(int animalID) throws AdoptionException {
        return animalRepository.findByID(animalID).orElseThrow(() -> new NotFoundException("Animal profile was not found."));
    }

    public User getPosterOf(Animal animal) throws AdoptionException {
        return userRepository.findByID(animal.getPosterID())
                .orElseThrow(() -> new NotFoundException("The poster's account was not found."));
    }

    public List<Animal> browseAvailable(AnimalFilter filter) throws AdoptionException {
        validateFilter(filter);

        List<Animal> result = new ArrayList<>();
        for (Animal animal : animalRepository.findAll()) {
            if (animal.getStatus() == AdoptionStatus.AVAILABLE && filter.matches(animal)) {
                result.add(animal);
            }
        }
        Collections.reverse(result);
        return result;
    }

    private void validateFilter(AnimalFilter filter) throws ValidationException {
        if (filter == null) {
            throw new ValidationException("This is a required field");
        }
    }

    private Animal findOwnedAnimal(User user, int animalID) throws AdoptionException {
        Animal animal = getAnimal(animalID);
        if (!animal.isPostedBy(user.getID())) {
            throw new NotAuthorizedException("You can only edit/remove your own listings.");
        }
        return animal;
    }

    private void validateDetails(AnimalDetails details) throws AdoptionException {
        InputValidator.validateRequired(details.getType(), "Type");
        InputValidator.validateText(details.getName(), "Name");
        InputValidator.validateText(details.getBreed(), "Breed");
        InputValidator.validateAnimalAge(details.getAgeMonths());
        InputValidator.validateRequired(details.getSex(), "Sex");
        InputValidator.validateWeight(details.getWeight());
        InputValidator.validateText(details.getAppearance(), "Appearance");
        InputValidator.validateText(details.getHealthCondition(), "Health condition");
        InputValidator.validateRequired(details.getVaccinationStatus(), "Vaccination status");
        InputValidator.validateText(details.getPersonality(), "Personality");
        InputValidator.validateText(details.getCareRequirements(), "Care requirements");
        InputValidator.validateText(details.getLocation(), "Location");
        InputValidator.validateRequired(details.getListingType(), "Adoption or rehoming");
    }

    private List<String> copyPhotos(List<String> sourcePaths) throws AdoptionException {
        List<String> saved = new ArrayList<>();
        try {
            for (String source : sourcePaths) {
                saved.add(FileStorage.savePhoto(source, PHOTO_PREFIX));
            }
        } catch (AdoptionException e) {
            deletePhotos(saved);
            throw e;
        }
        return saved;
    }

    private void deletePhotos(List<String> savedPaths) {
        for (String path : savedPaths) {
            FileStorage.deletePhoto(path);
        }
    }
}
