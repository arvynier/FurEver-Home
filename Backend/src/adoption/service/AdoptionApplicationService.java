package adoption.service;

import adoption.exception.AdoptionException;
import adoption.exception.AdoptionException.NotFoundException;
import adoption.exception.AdoptionException.ValidationException;

import adoption.model.AdoptionApplication;
import adoption.model.AdoptionAssessment;
import adoption.model.Animal;
import adoption.model.User;

import adoption.repository.AdoptionApplicationRepository;
import adoption.repository.AnimalRepository;

import java.util.List;

public class AdoptionApplicationService {

    private final AdoptionApplicationRepository applicationRepository;
    private final AdoptionAssessmentService assessmentService;
    private final AnimalRepository animalRepository;

    public AdoptionApplicationService(
            AdoptionApplicationRepository applicationRepository,
            AdoptionAssessmentService assessmentService,
            AnimalRepository animalRepository) {

        this.applicationRepository =
                applicationRepository;

        this.assessmentService =
                assessmentService;

        this.animalRepository =
                animalRepository;
    }

    public AdoptionApplication submitApplication(
            User applicant,
            int animalID)
            throws AdoptionException {

        if (applicant == null) {
            throw new ValidationException(
                    "Applicant is required."
            );
        }

        Animal animal =
                animalRepository.findByID(animalID)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Animal with ID "
                                                + animalID
                                                + " was not found."
                                )
                        );

        if (!animal.getStatus()
                .equals(Animal.AdoptionStatus.AVAILABLE)) {

            throw new ValidationException(
                    "This animal is not currently available for adoption."
            );
        }

        if (animal.isPostedBy(applicant.getID())) {

            throw new ValidationException(
                    "You cannot apply to adopt your own animal listing."
            );
        }

        AdoptionAssessment assessment =
                assessmentService.getLatestAssessment(
                        applicant
                );

        AdoptionApplication application =
                new AdoptionApplication(
                        animalID,
                        applicant.getID(),
                        assessment.getID(),
                        assessment.getTotalScore(),
                        assessment.getReadinessResult()
                );

        return applicationRepository.save(
                application
        );
    }

    public List<AdoptionApplication> getMyApplications(
            User applicant)
            throws AdoptionException {

        return applicationRepository
                .findByApplicantID(
                        applicant.getID()
                );
    }

    public List<AdoptionApplication> getApplicationsForMyAnimals(
            User poster)
            throws AdoptionException {

        return applicationRepository
                .findByPosterID(
                        poster.getID(),
                        animalRepository
                );
    }

    public void updateApplicationStatus(
            User poster,
            int applicationID,
            AdoptionApplication.Status status)
            throws AdoptionException {

        AdoptionApplication application =
                applicationRepository
                        .findByID(applicationID);

        if (application == null) {
            throw new NotFoundException(
                    "Adoption application was not found."
            );
        }

        Animal animal =
                animalRepository.findByID(
                        application.getAnimalID()
                ).orElseThrow(() ->
                        new NotFoundException(
                                "Animal listing was not found."
                        )
                );

        if (!animal.isPostedBy(
                poster.getID())) {

            throw new ValidationException(
                    "You can only review applications for your own animals."
            );
        }

        application.setStatus(status);

        applicationRepository.update(
                application
        );
    }
}