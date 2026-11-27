package adoption.model;

/*
 * Everything a poster needs to see when reviewing one application:
 * the application, the animal, the applicant and the applicant's
 * assessment (with the score of every category).
 * Read-only holder, built by AdoptionApplicationService.
 */
public final class ApplicationReview {

    private final AdoptionApplication application;
    private final Animal animal;
    private final User applicant;
    private final AdoptionAssessment assessment; // can be null

    public ApplicationReview(
            AdoptionApplication application,
            Animal animal,
            User applicant,
            AdoptionAssessment assessment) {

        this.application = application;
        this.animal = animal;
        this.applicant = applicant;
        this.assessment = assessment;
    }

    public AdoptionApplication getApplication() {
        return application;
    }

    public Animal getAnimal() {
        return animal;
    }

    public User getApplicant() {
        return applicant;
    }

    public AdoptionAssessment getAssessment() {
        return assessment;
    }
}
