package adoption.model;

public class AdoptionAssessment {

    private int id;
    private int applicantID;
    private int animalID;

    private int financialScore;
    private int timeScore;
    private int livingEnvironmentScore;
    private int careExperienceScore;

    private int totalScore;
    private String readinessResult;

    // Maximum score: 8 questions x 5 points
    public static final int MAX_SCORE = 40;

    // Constructor for a new assessment
    public AdoptionAssessment(
            int applicantID,
            int animalID,
            int financialScore,
            int timeScore,
            int livingEnvironmentScore,
            int careExperienceScore) {

        this.applicantID = applicantID;
        this.animalID = animalID;

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
            int animalID,
            int financialScore,
            int timeScore,
            int livingEnvironmentScore,
            int careExperienceScore,
            int totalScore,
            String readinessResult) {

        this.id = id;
        this.applicantID = applicantID;
        this.animalID = animalID;

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

        if (totalScore >= 32) {
            readinessResult = "Highly Ready";
        } else if (totalScore >= 24) {
            readinessResult = "Ready";
        } else if (totalScore >= 16) {
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

    public int getAnimalID() {
        return animalID;
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
                animalID,
                financialScore,
                timeScore,
                livingEnvironmentScore,
                careExperienceScore,
                totalScore,
                readinessResult
        );
    }
}