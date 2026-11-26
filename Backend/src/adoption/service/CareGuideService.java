package adoption.service;

import adoption.exception.AdoptionException.NotFoundException;
import adoption.guide.CareGuide;
import adoption.guide.CatCareGuide;
import adoption.guide.DogCareGuide;
import adoption.model.Animal;
import adoption.model.Animal.AnimalType;

public class CareGuideService {
    private final CareGuide[] guides = { new DogCareGuide(), new CatCareGuide() };

    public CareGuide getGuide(AnimalType type) throws NotFoundException {
        for (CareGuide guide : guides) {
            if (guide.getAnimalType() == type) {
                return guide;
            }
        }
        throw new NotFoundException("No care guide found for this animal type.");
    }

    public CareGuide getGuideFor(Animal animal) throws NotFoundException {
        return getGuide(animal.getType());
    }
}
