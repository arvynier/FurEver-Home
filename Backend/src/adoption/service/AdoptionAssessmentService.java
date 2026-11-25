package adoption.service;

import adoption.exception.AdoptionException;
import adoption.exception.NotFoundException;
import adoption.exception.ValidationException;

import adoption.model.AdoptionAssessment;
import adoption.model.Animal;
import adoption.model.User;

import adoption.repository.AdoptionAssessmentRepository;
import adoption.repository.AnimalRepository;

public class AdoptionAssessmentService {

    private final AdoptionAssessmentRepository assessmentRepository;
    private final AnimalRepository animalRepository;

    public AdoptionAssessmentService(
            AdoptionAssessmentRepository assessmentRepository,
            AnimalRepository animalRepository) {

        this.assessmentRepository = assessmentRepository;
        this.animalRepository = animalRepository;
    }

    public AdoptionAssessment takeAssessment(
            User applicant,
            int animalID,
            int financialScore,
            int timeScore,
            int livingEnvironmentScore,
            int careExperienceScore)
            throws AdoptionException {

        if (applicant == null) {
            throw new ValidationException("Applicant is required.");
        }

        if (animalID <= 0) {
            throw new ValidationException("Invalid animal ID.");
        }

        Animal animal = animalRepository.findByID(animalID)
                .orElseThrow(() -> new NotFoundException("Animal not found."));

        if (animal == null) {
            throw new NotFoundException(
                    "Animal with ID " + animalID + " was not found.");
        }

        validateScore(financialScore, "Financial Capacity");
        validateScore(timeScore, "Time Commitment");
        validateScore(
                livingEnvironmentScore,
                "Living Environment"
        );
        validateScore(
                careExperienceScore,
                "Care Experience"
        );

        AdoptionAssessment existing =
                assessmentRepository.findByApplicantAndAnimal(
                        applicant.getID(),
                        animalID
                );

        if (existing != null) {
            throw new ValidationException(
                    "You have already completed an assessment for this animal."
            );
        }

        AdoptionAssessment assessment =
                new AdoptionAssessment(
                        applicant.getID(),
                        animalID,
                        financialScore,
                        timeScore,
                        livingEnvironmentScore,
                        careExperienceScore
                );

        return assessmentRepository.save(assessment);
    }

    private void validateScore(
            int score,
            String category)
            throws ValidationException {

        if (score < 1 || score > 5) {
            throw new ValidationException(
                    category + " score must be between 1 and 5."
            );
        }
    }

    public AdoptionAssessment getAssessment(int assessmentID)
            throws AdoptionException {

        AdoptionAssessment assessment =
                assessmentRepository.findByID(assessmentID);

        if (assessment == null) {
            throw new NotFoundException(
                    "Assessment with ID " +
                            assessmentID +
                            " was not found."
            );
        }

        return assessment;
    }

    public AdoptionAssessment getAssessmentForAnimal(
            User applicant,
            int animalID)
            throws AdoptionException {

        if (applicant == null) {
            throw new ValidationException("Applicant is required.");
        }

        AdoptionAssessment assessment =
                assessmentRepository.findByApplicantAndAnimal(
                        applicant.getID(),
                        animalID
                );

        if (assessment == null) {
            throw new NotFoundException(
                    "No assessment was found for this animal."
            );
        }

        return assessment;
    }
}