package adoption.ui;

import adoption.exception.AdoptionException;
import adoption.model.AdoptionAssessment;
import adoption.model.User;
import adoption.service.AdoptionAssessmentService;

public class AdoptionAssessmentMenu {

    private final AdoptionAssessmentService assessmentService;
    private final ConsoleInput input;

    public AdoptionAssessmentMenu(
            AdoptionAssessmentService assessmentService,
            ConsoleInput input) {

        this.assessmentService = assessmentService;
        this.input = input;
    }

    public void show(User loggedInUser, int animalID) {

        System.out.println();
        System.out.println("======================================");
        System.out.println("     ADOPTION READINESS ASSESSMENT");
        System.out.println("======================================");
        System.out.println();

        System.out.println("Please rate each statement from 1 to 5.");
        System.out.println();
        System.out.println("1 - Strongly Disagree");
        System.out.println("2 - Disagree");
        System.out.println("3 - Neutral");
        System.out.println("4 - Agree");
        System.out.println("5 - Strongly Agree");
        System.out.println();

        try {

            int financialScore = askFinancialQuestions();

            int timeScore = askTimeQuestions();

            int livingEnvironmentScore =
                    askLivingEnvironmentQuestions();

            int careExperienceScore =
                    askCareExperienceQuestions();

            AdoptionAssessment assessment =
                    assessmentService.takeAssessment(
                            loggedInUser,
                            animalID,
                            financialScore,
                            timeScore,
                            livingEnvironmentScore,
                            careExperienceScore
                    );

            displayResult(assessment);

        } catch (AdoptionException e) {

            System.out.println();
            System.out.println("Assessment Error: " + e.getMessage());
        }
    }

    private int askFinancialQuestions() {

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("1. FINANCIAL CAPACITY");
        System.out.println("--------------------------------------");

        int q1 = askQuestion(
                "I can regularly afford food, supplies, and basic care for a pet."
        );

        int q2 = askQuestion(
                "I can handle expected veterinary and other pet-related expenses."
        );

        return (q1 + q2) / 2;
    }

    private int askTimeQuestions() {

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("2. TIME COMMITMENT");
        System.out.println("--------------------------------------");

        int q1 = askQuestion(
                "I have enough time every day to properly care for a pet."
        );

        int q2 = askQuestion(
                "I can provide regular attention, exercise, and companionship."
        );

        return (q1 + q2) / 2;
    }

    private int askLivingEnvironmentQuestions() {

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("3. LIVING ENVIRONMENT");
        System.out.println("--------------------------------------");

        int q1 = askQuestion(
                "My home is safe and suitable for keeping a pet."
        );

        int q2 = askQuestion(
                "The people I live with are aware of and supportive of having a pet."
        );

        return (q1 + q2) / 2;
    }

    private int askCareExperienceQuestions() {

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("4. CARE EXPERIENCE");
        System.out.println("--------------------------------------");

        int q1 = askQuestion(
                "I understand the basic responsibilities involved in caring for a pet."
        );

        int q2 = askQuestion(
                "I am willing to learn more about proper pet care when necessary."
        );

        return (q1 + q2) / 2;
    }

    private int askQuestion(String question) {

        while (true) {

            try {

                int answer = input.promptInt(question + " (1-5)");

                if (answer >= 1 && answer <= 5) {
                    return answer;
                }

                System.out.println(
                        "Please enter a number from 1 to 5."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a number from 1 to 5."
                );
            }
        }
    }

    private void displayResult(
            AdoptionAssessment assessment) {

        System.out.println();
        System.out.println("======================================");
        System.out.println("       ASSESSMENT RESULT");
        System.out.println("======================================");

        System.out.println(
                "Financial Capacity: "
                        + assessment.getFinancialScore()
                        + "/5"
        );

        System.out.println(
                "Time Commitment: "
                        + assessment.getTimeScore()
                        + "/5"
        );

        System.out.println(
                "Living Environment: "
                        + assessment.getLivingEnvironmentScore()
                        + "/5"
        );

        System.out.println(
                "Care Experience: "
                        + assessment.getCareExperienceScore()
                        + "/5"
        );

        System.out.println("--------------------------------------");

        System.out.println(
                "Total Score: "
                        + assessment.getTotalScore()
                        + "/20"
        );

        System.out.printf(
                "Percentage: %.1f%%%n",
                assessment.getPercentage()
        );

        System.out.println(
                "Readiness Result: "
                        + assessment.getReadinessResult()
        );

        System.out.println("======================================");
        System.out.println();
    }
}