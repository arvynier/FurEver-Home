package adoption.ui;

import adoption.exception.AdoptionException;
import adoption.model.AdoptionApplication;
import adoption.model.AdoptionAssessment;
import adoption.model.ApplicationForm;
import adoption.model.ApplicationReview;
import adoption.model.User;
import adoption.service.AdoptionApplicationService;

import java.util.List;

public class AdoptionApplicationMenu {

    private static final String LINE =
            "--------------------------------------";

    private final AdoptionApplicationService service;
    private final ConsoleInput input;

    public AdoptionApplicationMenu(
            AdoptionApplicationService service,
            ConsoleInput input) {

        this.service = service;
        this.input = input;
    }

    public void show(User user) {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("======================================");
            System.out.println("       ADOPTION APPLICATION");
            System.out.println("======================================");

            System.out.println("1. Apply for an Animal");
            System.out.println("2. View My Applications");
            System.out.println("3. Review Applications for My Animals");
            System.out.println("4. Back");

            String choice =
                    input.prompt("Choose an option");

            try {

                switch (choice) {

                    case "1" ->
                            submitApplication(user);

                    case "2" ->
                            viewMyApplications(user);

                    case "3" ->
                            reviewApplications(user);

                    case "4" ->
                            running = false;

                    default ->
                            System.out.println(
                                    "Invalid option. Please choose 1 to 4."
                            );
                }

            } catch (AdoptionException e) {

                System.out.println();
                System.out.println(
                        "Application Error: "
                                + e.getMessage()
                );

            } catch (NumberFormatException e) {

                System.out.println();
                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    // ---------------------------------------------------------------
    // Applying
    // ---------------------------------------------------------------

    private void submitApplication(User user)
            throws AdoptionException {

        System.out.println();
        System.out.println("===== APPLY FOR ADOPTION =====");

        int animalID =
                input.promptInt(
                        "Enter Animal ID"
                );

        System.out.println();
        System.out.println(
                "Please answer a few questions for the poster."
        );

        String reason =
                input.prompt(
                        "Reason for adopting"
                );

        String livingArrangement =
                input.prompt(
                        "Where will the animal live? (living arrangement)"
                );

        String caregiver =
                input.prompt(
                        "Who will primarily take care of the animal?"
                );

        String existingPets =
                input.prompt(
                        "Do you have existing pets? (type None if you have none)"
                );

        boolean homeVisit =
                input.promptYesNo(
                        "Do you agree to a home visit?",
                        null
                );

        String message =
                input.prompt(
                        "Message to the poster (optional, press Enter to skip)"
                );

        ApplicationForm form =
                new ApplicationForm(
                        reason,
                        livingArrangement,
                        caregiver,
                        existingPets,
                        homeVisit,
                        message
                );

        AdoptionApplication application =
                service.submitApplication(
                        user,
                        animalID,
                        form
                );

        System.out.println();
        System.out.println(
                "Application submitted successfully!"
        );

        System.out.println(LINE);
        System.out.println(application);
        System.out.println(LINE);
    }

    // ---------------------------------------------------------------
    // Applicant: track my applications
    // ---------------------------------------------------------------

    private void viewMyApplications(User user)
            throws AdoptionException {

        List<AdoptionApplication> applications =
                service.getMyApplications(user);

        System.out.println();
        System.out.println("===== MY APPLICATIONS =====");

        if (applications.isEmpty()) {

            System.out.println(
                    "You have not submitted any adoption applications."
            );

            return;
        }

        for (AdoptionApplication application
                : applications) {

            System.out.println();
            System.out.println(LINE);
            System.out.println(
                    "Animal: "
                            + service.getAnimalName(
                            application.getAnimalID())
            );
            System.out.println(application);
        }

        System.out.println(LINE);
    }

    // ---------------------------------------------------------------
    // Poster: review applications
    // ---------------------------------------------------------------

    private void reviewApplications(User user)
            throws AdoptionException {

        List<AdoptionApplication> applications =
                service.getApplicationsForMyAnimals(user);

        System.out.println();
        System.out.println(
                "===== APPLICATIONS FOR MY ANIMALS ====="
        );

        if (applications.isEmpty()) {

            System.out.println(
                    "There are no applications for your animals."
            );

            return;
        }

        for (AdoptionApplication application
                : applications) {

            System.out.println(
                    "Application #" + application.getID()
                            + " | Animal: "
                            + service.getAnimalName(
                            application.getAnimalID())
                            + " (ID " + application.getAnimalID() + ")"
                            + " | " + application.getStatus()
                            + " | " + application.getSubmittedAtText()
            );
        }

        System.out.println();

        String answer =
                input.prompt(
                        "Enter Application ID to view (Enter to go back)"
                );

        if (answer.isBlank()) {
            return;
        }

        ApplicationReview review =
                service.getReviewDetails(
                        user,
                        Integer.parseInt(answer)
                );

        printReview(review);

        AdoptionApplication application =
                review.getApplication();

        if (application.getStatus()
                != AdoptionApplication.Status.PENDING) {

            System.out.println(
                    "This application is already "
                            + application.getStatus()
                            + "."
            );

            return;
        }

        String[] choices = {
                "Approve",
                "Reject",
                "Back"
        };

        String decision =
                input.chooseOption(
                        "Decision",
                        choices,
                        null
                );

        AdoptionApplication.Status newStatus;

        switch (decision) {

            case "Approve" ->
                    newStatus = AdoptionApplication.Status.APPROVED;

            case "Reject" ->
                    newStatus = AdoptionApplication.Status.REJECTED;

            default -> {
                return;
            }
        }

        if (!input.promptYesNo(
                "Are you sure you want to mark this as " + newStatus + "?",
                false)) {

            System.out.println("No changes were made.");
            return;
        }

        service.updateApplicationStatus(
                user,
                application.getID(),
                newStatus
        );

        System.out.println(
                "Application status updated to "
                        + newStatus
                        + "."
        );

        if (newStatus == AdoptionApplication.Status.APPROVED) {

            System.out.println(
                    "The animal is now marked as Pending and is hidden from Browse."
            );
        }
    }

    private void printReview(ApplicationReview review) {

        AdoptionApplication application = review.getApplication();
        ApplicationForm form = application.getForm();
        User applicant = review.getApplicant();
        AdoptionAssessment assessment = review.getAssessment();

        System.out.println();
        System.out.println("======================================");
        System.out.println("        APPLICATION DETAILS");
        System.out.println("======================================");

        System.out.println("Application ID: " + application.getID());
        System.out.println(
                "Animal ID: " + application.getAnimalID()
                        + " (" + review.getAnimal().getName() + ")"
        );
        System.out.println("Applicant ID: " + application.getApplicantID());

        System.out.println();
        System.out.println("Applicant Information");
        System.out.println("  Name: " + applicant.getFullName());
        System.out.println("  Age: " + applicant.getAge());
        System.out.println("  Mobile: " + applicant.getMobileNumber());
        System.out.println("  Email: " + applicant.getEmail());
        System.out.println("  Address: " + applicant.getAddress());

        System.out.println();
        System.out.println(
                "Assessment Score: "
                        + application.getAssessmentScore()
                        + "/" + AdoptionAssessment.MAX_SCORE
        );
        System.out.println(
                "Readiness Result: " + application.getReadinessResult()
        );

        if (assessment != null) {

            int max = AdoptionAssessment.MAX_CATEGORY_SCORE;

            System.out.println("  Financial Score: "
                    + assessment.getFinancialScore() + "/" + max);
            System.out.println("  Time Score: "
                    + assessment.getTimeScore() + "/" + max);
            System.out.println("  Living Environment Score: "
                    + assessment.getLivingEnvironmentScore() + "/" + max);
            System.out.println("  Care Experience Score: "
                    + assessment.getCareExperienceScore() + "/" + max);
        }

        System.out.println();
        System.out.println("Current Status: " + application.getStatus());
        System.out.println("Reason for Adopting: "
                + form.getReasonForAdopting());
        System.out.println("Living Arrangement: "
                + form.getLivingArrangement());
        System.out.println("Primary Caregiver: "
                + form.getPrimaryCaregiver());
        System.out.println("Existing Pets: "
                + form.getExistingPets());
        System.out.println("Agrees to Home Visit: "
                + (form.isHomeVisitAgreed() ? "Yes" : "No"));
        System.out.println("Message to Poster: "
                + form.getMessageToPoster());
        System.out.println("Date Submitted: "
                + application.getSubmittedAtText());
        System.out.println("======================================");
    }
}
