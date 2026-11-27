package adoption.service;

import adoption.exception.AdoptionException;
import adoption.exception.AdoptionException.NotAuthorizedException;
import adoption.exception.AdoptionException.NotFoundException;
import adoption.exception.AdoptionException.ValidationException;

import adoption.model.AdoptionApplication;
import adoption.model.AdoptionAssessment;
import adoption.model.Animal;
import adoption.model.ApplicationForm;
import adoption.model.ApplicationReview;
import adoption.model.User;

import adoption.repository.AdoptionApplicationRepository;
import adoption.repository.AnimalRepository;
import adoption.repository.UserRepository;

import adoption.utility.InputValidator;

import java.util.List;

public class AdoptionApplicationService {

    private final AdoptionApplicationRepository applicationRepository;
    private final AdoptionAssessmentService assessmentService;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public AdoptionApplicationService(
            AdoptionApplicationRepository applicationRepository,
            AdoptionAssessmentService assessmentService,
            AnimalRepository animalRepository,
            UserRepository userRepository) {

        this.applicationRepository =
                applicationRepository;

        this.assessmentService =
                assessmentService;

        this.animalRepository =
                animalRepository;

        this.userRepository =
                userRepository;
    }

    // ---------------------------------------------------------------
    // FEATURE 5: applying
    // ---------------------------------------------------------------

    public AdoptionApplication submitApplication(
            User applicant,
            int animalID,
            ApplicationForm form)
            throws AdoptionException {

        if (applicant == null) {
            throw new ValidationException(
                    "Applicant is required."
            );
        }

        validateForm(form);

        Animal animal = findAnimal(animalID);

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

        // One active application per animal for each applicant
        for (AdoptionApplication existing
                : applicationRepository.findByApplicantID(
                        applicant.getID())) {

            if (existing.getAnimalID() == animalID
                    && existing.getStatus()
                    != AdoptionApplication.Status.REJECTED) {

                throw new ValidationException(
                        "You already have an application for this animal."
                );
            }
        }

        AdoptionAssessment assessment =
                assessmentService.getLatestAssessment(
                        applicant
                );

        // The message is optional; the other answers are required
        String message = form.getMessageToPoster().isBlank()
                ? "None"
                : form.getMessageToPoster().trim();

        ApplicationForm cleaned =
                new ApplicationForm(
                        form.getReasonForAdopting().trim(),
                        form.getLivingArrangement().trim(),
                        form.getPrimaryCaregiver().trim(),
                        form.getExistingPets().trim(),
                        form.isHomeVisitAgreed(),
                        message
                );

        AdoptionApplication application =
                new AdoptionApplication(
                        animalID,
                        applicant.getID(),
                        assessment.getID(),
                        assessment.getTotalScore(),
                        assessment.getReadinessResult(),
                        cleaned
                );

        return applicationRepository.save(
                application
        );
    }

    private void validateForm(ApplicationForm form)
            throws ValidationException {

        InputValidator.validateRequired(form, "Application form");

        InputValidator.validateText(
                form.getReasonForAdopting(), "Reason for adopting");
        InputValidator.validateText(
                form.getLivingArrangement(), "Living arrangement");
        InputValidator.validateText(
                form.getPrimaryCaregiver(), "Primary caregiver");
        InputValidator.validateText(
                form.getExistingPets(), "Existing pets (type None if you have none)");

        // message is optional, but it cannot be null
        InputValidator.validateRequired(
                form.getMessageToPoster(), "Message to poster");
    }

    public List<AdoptionApplication> getMyApplications(
            User applicant)
            throws AdoptionException {

        return applicationRepository
                .findByApplicantID(
                        applicant.getID()
                );
    }

    // Used by the menus to show the animal's name instead of only its ID
    public String getAnimalName(int animalID)
            throws AdoptionException {

        return animalRepository.findByID(animalID)
                .map(Animal::getName)
                .orElse("(listing removed)");
    }

    // ---------------------------------------------------------------
    // FEATURE 6: reviewing
    // ---------------------------------------------------------------

    public List<AdoptionApplication> getApplicationsForMyAnimals(
            User poster)
            throws AdoptionException {

        return applicationRepository
                .findByPosterID(
                        poster.getID(),
                        animalRepository
                );
    }

    // Full details of one application, only for the poster of that animal
    public ApplicationReview getReviewDetails(
            User poster,
            int applicationID)
            throws AdoptionException {

        AdoptionApplication application =
                findApplication(applicationID);

        Animal animal =
                findAnimal(application.getAnimalID());

        requirePoster(poster, animal);

        User applicant =
                userRepository.findByID(
                        application.getApplicantID()
                ).orElseThrow(() ->
                        new NotFoundException(
                                "Applicant account was not found."
                        )
                );

        // Category scores come from the assessment attached to the application
        AdoptionAssessment assessment;
        try {
            assessment = assessmentService.getAssessment(
                    application.getAssessmentID()
            );
        } catch (AdoptionException e) {
            assessment = null;
        }

        return new ApplicationReview(
                application,
                animal,
                applicant,
                assessment
        );
    }

    public void updateApplicationStatus(
            User poster,
            int applicationID,
            AdoptionApplication.Status status)
            throws AdoptionException {

        if (status != AdoptionApplication.Status.APPROVED
                && status != AdoptionApplication.Status.REJECTED) {

            throw new ValidationException(
                    "Decision must be APPROVED or REJECTED."
            );
        }

        AdoptionApplication application =
                findApplication(applicationID);

        Animal animal =
                findAnimal(application.getAnimalID());

        requirePoster(poster, animal);

        if (application.getStatus()
                != AdoptionApplication.Status.PENDING) {

            throw new ValidationException(
                    "This application was already "
                            + application.getStatus()
                            + ". Only pending applications can be reviewed."
            );
        }

        if (status == AdoptionApplication.Status.APPROVED) {

            if (!animal.getStatus()
                    .equals(Animal.AdoptionStatus.AVAILABLE)) {

                throw new ValidationException(
                        "This animal is no longer available."
                );
            }

            // The animal is now reserved: it leaves the Browse list
            animal.setStatus(Animal.AdoptionStatus.PENDING);
            animalRepository.update(animal);
        }

        application.setStatus(status);

        applicationRepository.update(
                application
        );
    }

    // ---------------------------------------------------------------
    // helpers
    // ---------------------------------------------------------------

    private AdoptionApplication findApplication(int applicationID)
            throws AdoptionException {

        AdoptionApplication application =
                applicationRepository.findByID(applicationID);

        if (application == null) {
            throw new NotFoundException(
                    "Adoption application was not found."
            );
        }

        return application;
    }

    private Animal findAnimal(int animalID)
            throws AdoptionException {

        return animalRepository.findByID(animalID)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Animal with ID "
                                        + animalID
                                        + " was not found."
                        )
                );
    }

    private void requirePoster(User poster, Animal animal)
            throws NotAuthorizedException {

        if (poster == null
                || !animal.isPostedBy(poster.getID())) {

            throw new NotAuthorizedException(
                    "You can only review applications for your own animals."
            );
        }
    }
}
