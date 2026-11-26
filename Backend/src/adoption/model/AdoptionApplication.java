package adoption.model;

public class AdoptionApplication {

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    private final int id;
    private final int animalID;
    private final int applicantID;
    private final int assessmentID;
    private final int assessmentScore;
    private final String readinessResult;
    private Status status;

    public AdoptionApplication(
            int id,
            int animalID,
            int applicantID,
            int assessmentID,
            int assessmentScore,
            String readinessResult,
            Status status) {

        this.id = id;
        this.animalID = animalID;
        this.applicantID = applicantID;
        this.assessmentID = assessmentID;
        this.assessmentScore = assessmentScore;
        this.readinessResult = readinessResult;
        this.status = status;
    }

    public AdoptionApplication(
            int animalID,
            int applicantID,
            int assessmentID,
            int assessmentScore,
            String readinessResult) {

        this(
                0,
                animalID,
                applicantID,
                assessmentID,
                assessmentScore,
                readinessResult,
                Status.PENDING
        );
    }

    public int getID() {
        return id;
    }

    public int getAnimalID() {
        return animalID;
    }

    public int getApplicantID() {
        return applicantID;
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public int getAssessmentScore() {
        return assessmentScore;
    }

    public String getReadinessResult() {
        return readinessResult;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public AdoptionApplication withID(int newID) {
        return new AdoptionApplication(
                newID,
                animalID,
                applicantID,
                assessmentID,
                assessmentScore,
                readinessResult,
                status
        );
    }

    @Override
    public String toString() {
        return "Application ID: " + id
                + "\nAnimal ID: " + animalID
                + "\nApplicant ID: " + applicantID
                + "\nAssessment ID: " + assessmentID
                + "\nAssessment Score: " + assessmentScore + "/100"
                + "\nReadiness Result: " + readinessResult
                + "\nStatus: " + status;
    }
}