package adoption.ui;

import adoption.exception.AdoptionException;
import adoption.model.AdoptionApplication;
import adoption.model.User;
import adoption.service.AdoptionApplicationService;

import java.util.List;

public class AdoptionApplicationMenu {

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
            }
        }
    }

    private void submitApplication(User user)
            throws AdoptionException {

        System.out.println();
        System.out.println("===== APPLY FOR ADOPTION =====");

        int animalID =
                input.promptInt(
                        "Enter Animal ID"
                );

        AdoptionApplication application =
                service.submitApplication(
                        user,
                        animalID
                );

        System.out.println();
        System.out.println(
                "Application submitted successfully!"
        );

        System.out.println("--------------------------------------");

        System.out.println(
                "Application ID: "
                        + application.getID()
        );

        System.out.println(
                "Animal ID: "
                        + application.getAnimalID()
        );

        System.out.println(
                "Assessment Score: "
                        + application.getAssessmentScore()
                        + "/100"
        );

        System.out.println(
                "Readiness Result: "
                        + application.getReadinessResult()
        );

        System.out.println(
                "Status: "
                        + application.getStatus()
        );

        System.out.println("--------------------------------------");
    }

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
            System.out.println("--------------------------------------");
            System.out.println(application);
        }

        System.out.println("--------------------------------------");
    }

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

            System.out.println();
            System.out.println("--------------------------------------");

            System.out.println(
                    "Application ID: "
                            + application.getID()
            );

            System.out.println(
                    "Animal ID: "
                            + application.getAnimalID()
            );

            System.out.println(
                    "Applicant ID: "
                            + application.getApplicantID()
            );

            System.out.println(
                    "Assessment Score: "
                            + application.getAssessmentScore()
                            + "/100"
            );

            System.out.println(
                    "Readiness Result: "
                            + application.getReadinessResult()
            );

            System.out.println(
                    "Current Status: "
                            + application.getStatus()
            );
        }

        System.out.println("--------------------------------------");

        int applicationID =
                input.promptInt(
                        "Enter Application ID to review"
                );

        String status =
                input.prompt(
                        "Enter decision (APPROVED / REJECTED)"
                ).toUpperCase();

        AdoptionApplication.Status decision;

        try {

            decision =
                    AdoptionApplication.Status.valueOf(
                            status
                    );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Invalid decision. Use APPROVED or REJECTED."
            );

            return;
        }

        service.updateApplicationStatus(
                user,
                applicationID,
                decision
        );

        System.out.println(
                "Application status updated to "
                        + decision
                        + "."
        );
    }
}