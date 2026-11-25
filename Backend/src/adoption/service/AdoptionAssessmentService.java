package adoption.service;

import adoption.exception.AdoptionException;
import adoption.exception.NotFoundException;
import adoption.exception.ValidationException;

import adoption.model.AdoptionAssessment;
import adoption.model.User;

import adoption.repository.AdoptionAssessmentRepository;

public class AdoptionAssessmentService {

    private final AdoptionAssessmentRepository assessmentRepository;

    public AdoptionAssessmentService(
            AdoptionAssessmentRepository assessmentRepository) {

        this.assessmentRepository = assessmentRepository;
    }

    // Scores are the sum of each category's question answers
    public AdoptionAssessment takeAssessment(
            User applicant,
            int financialScore,
            int timeScore,
            int livingEnvironmentScore,
            int careExperienceScore)
            throws AdoptionException {

        if (applicant == null) {
            throw new ValidationException("Applicant is required.");
        }

        validateScore(financialScore, "Financial Capacity");
        validateScore(timeScore, "Time Commitment");
        validateScore(livingEnvironmentScore, "Living Environment");
        validateScore(careExperienceScore, "Care Experience");

        AdoptionAssessment assessment =
                new AdoptionAssessment(
                        applicant.getID(),
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

        if (score < AdoptionAssessment.MIN_CATEGORY_SCORE
                || score > AdoptionAssessment.MAX_CATEGORY_SCORE) {

            throw new ValidationException(
                    category + " score must be between "
                            + AdoptionAssessment.MIN_CATEGORY_SCORE
                            + " and "
                            + AdoptionAssessment.MAX_CATEGORY_SCORE + "."
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

    // Used when an application is filled in or reviewed
    public AdoptionAssessment getLatestAssessment(User applicant)
            throws AdoptionException {

        if (applicant == null) {
            throw new ValidationException("Applicant is required.");
        }

        AdoptionAssessment assessment =
                assessmentRepository.findLatestByApplicantID(
                        applicant.getID()
                );

        if (assessment == null) {
            throw new NotFoundException(
                    "No assessment was found for this applicant."
            );
        }

        return assessment;
    }
}
