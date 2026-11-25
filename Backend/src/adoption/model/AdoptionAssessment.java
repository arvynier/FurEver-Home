package adoption.model;

public class AdoptionAssessment {

    private int id;
    private int applicantID;

    // Each category score is the SUM of its question answers (1-5 each)
    private int financialScore;
    private int timeScore;
    private int livingEnvironmentScore;
    private int careExperienceScore;

    private int totalScore;
    private String readinessResult;

    public static final int QUESTIONS_PER_CATEGORY = 5;
    public static final int MAX_CATEGORY_SCORE = QUESTIONS_PER_CATEGORY * 5; // 25
    public static final int MIN_CATEGORY_SCORE = QUESTIONS_PER_CATEGORY;     // 5

    // 4 categories x 5 questions x 5 points = 100
    public static final int MAX_SCORE = MAX_CATEGORY_SCORE * 4;

    // Constructor for a new assessment
    public AdoptionAssessment(
            int applicantID,
            int financialScore,
            int timeScore,
            int livingEnvironmentScore,
            int careExperienceScore) {

        this.applicantID = applicantID;

        this.financialScore = financialScore;
        this.timeScore = timeScore;
        this.livingEnvironmentScore = livingEnvironmentScore;
        this.careExperienceScore = careExperienceScore;

        calculateResult();
    }

    // Constructor used when loading from CSV
    public AdoptionAssessment(
            int id,
            int applicantID,
            int financialScore,
            int timeScore,
            int livingEnvironmentScore,
            int careExperienceScore,
            int totalScore,
            String readinessResult) {

        this.id = id;
        this.applicantID = applicantID;

        this.financialScore = financialScore;
        this.timeScore = timeScore;
        this.livingEnvironmentScore = livingEnvironmentScore;
        this.careExperienceScore = careExperienceScore;

        this.totalScore = totalScore;
        this.readinessResult = readinessResult;
    }

    private void calculateResult() {

        totalScore =
                financialScore +
                        timeScore +
                        livingEnvironmentScore +
                        careExperienceScore;

        double percentage = getPercentage();

        if (percentage >= 80) {
            readinessResult = "Highly Ready";
        } else if (percentage >= 60) {
            readinessResult = "Ready";
        } else if (percentage >= 40) {
            readinessResult = "Needs Improvement";
        } else {
            readinessResult = "Not Yet Ready";
        }
    }

    // Getters

    public int getID() {
        return id;
    }

    public int getApplicantID() {
        return applicantID;
    }

    public int getFinancialScore() {
        return financialScore;
    }

    public int getTimeScore() {
        return timeScore;
    }

    public int getLivingEnvironmentScore() {
        return livingEnvironmentScore;
    }

    public int getCareExperienceScore() {
        return careExperienceScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public String getReadinessResult() {
        return readinessResult;
    }

    public double getPercentage() {
        return (totalScore * 100.0) / MAX_SCORE;
    }

    // Used by repository when assigning an ID
    public AdoptionAssessment withID(int id) {
        return new AdoptionAssessment(
                id,
                applicantID,
                financialScore,
                timeScore,
                livingEnvironmentScore,
                careExperienceScore,
                totalScore,
                readinessResult
        );
    }
}
