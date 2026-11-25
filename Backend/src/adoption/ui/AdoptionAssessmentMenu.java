package adoption.ui;

import adoption.exception.AdoptionException;
import adoption.model.AdoptionAssessment;
import adoption.model.User;
import adoption.service.AdoptionAssessmentService;

public class AdoptionAssessmentMenu {

    private static final String[] FINANCIAL_QUESTIONS = {
            "I can regularly afford food, supplies, and basic care for a pet.",
            "I can handle expected veterinary and other pet-related expenses.",
            "I have savings or a plan to cover unexpected emergency vet costs.",
            "I have a stable income or support to cover a pet's costs for its whole life.",
            "I have considered recurring costs such as vaccinations, grooming, and preventive treatments."
    };

    private static final String[] TIME_QUESTIONS = {
            "I have enough time every day to properly care for a pet.",
            "I can provide regular attention, exercise, and companionship.",
            "My daily schedule allows me to feed and clean up after a pet at consistent times.",
            "I have someone or something arranged to care for a pet when I am away or in an emergency.",
            "I am prepared to commit to a pet for its entire life, which may be many years."
    };

    private static final String[] LIVING_ENVIRONMENT_QUESTIONS = {
            "My home is safe and suitable for keeping a pet.",
            "The people I live with are aware of and supportive of having a pet.",
            "My home has enough space for a pet to move, rest, and play comfortably.",
            "I am allowed to keep a pet where I live (landlord, building, or community rules).",
            "I can keep hazards such as toxic items, exposed wires, and unsecured exits away from a pet."
    };

    private static final String[] CARE_EXPERIENCE_QUESTIONS = {
            "I understand the basic responsibilities involved in caring for a pet.",
            "I am willing to learn more about proper pet care when necessary.",
            "I have cared for a pet before, or I am comfortable seeking guidance from experienced owners or vets.",
            "I know a pet's basic needs, such as proper diet, vaccinations, and regular vet checkups.",
            "I am prepared to patiently handle training and behavioral challenges."
    };

    private final AdoptionAssessmentService assessmentService;
    private final ConsoleInput input;

    public AdoptionAssessmentMenu(
            AdoptionAssessmentService assessmentService,
            ConsoleInput input) {

        this.assessmentService = assessmentService;
        this.input = input;
    }

    public void show(User loggedInUser) {

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

            int financialScore = askCategory(
                    "1. FINANCIAL CAPACITY", FINANCIAL_QUESTIONS);

            int timeScore = askCategory(
                    "2. TIME COMMITMENT", TIME_QUESTIONS);

            int livingEnvironmentScore = askCategory(
                    "3. LIVING ENVIRONMENT", LIVING_ENVIRONMENT_QUESTIONS);

            int careExperienceScore = askCategory(
                    "4. CARE EXPERIENCE", CARE_EXPERIENCE_QUESTIONS);

            AdoptionAssessment assessment =
                    assessmentService.takeAssessment(
                            loggedInUser,
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

    // Asks every question in a category and returns the SUM of the answers
    private int askCategory(String title, String[] questions) {

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println(title);
        System.out.println("--------------------------------------");

        int total = 0;

        for (int i = 0; i < questions.length; i++) {
            total += askQuestion((i + 1) + ". " + questions[i]);
        }

        return total;
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

        int max = AdoptionAssessment.MAX_CATEGORY_SCORE;

        System.out.println();
        System.out.println("======================================");
        System.out.println("       ASSESSMENT RESULT");
        System.out.println("======================================");

        System.out.println(
                "Financial Capacity: "
                        + assessment.getFinancialScore()
                        + "/" + max
        );

        System.out.println(
                "Time Commitment: "
                        + assessment.getTimeScore()
                        + "/" + max
        );

        System.out.println(
                "Living Environment: "
                        + assessment.getLivingEnvironmentScore()
                        + "/" + max
        );

        System.out.println(
                "Care Experience: "
                        + assessment.getCareExperienceScore()
                        + "/" + max
        );

        System.out.println("--------------------------------------");

        System.out.println(
                "Total Score: "
                        + assessment.getTotalScore()
                        + "/" + AdoptionAssessment.MAX_SCORE
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
