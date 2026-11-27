package adoption.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdoptionApplication {

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    private final int id;
    private final int animalID;
    private final int applicantID;
    private final int assessmentID;
    private final int assessmentScore;
    private final String readinessResult;
    private final ApplicationForm form;
    private final LocalDateTime submittedAt;
    private Status status;

    // Used when loading from the CSV (or after an ID is assigned)
    public AdoptionApplication(
            int id,
            int animalID,
            int applicantID,
            int assessmentID,
            int assessmentScore,
            String readinessResult,
            Status status,
            ApplicationForm form,
            LocalDateTime submittedAt) {

        this.id = id;
        this.animalID = animalID;
        this.applicantID = applicantID;
        this.assessmentID = assessmentID;
        this.assessmentScore = assessmentScore;
        this.readinessResult = readinessResult;
        this.status = status;
        this.form = form;
        this.submittedAt = submittedAt;
    }

    // Used for a brand-new application: PENDING, and the date is set by the system
    public AdoptionApplication(
            int animalID,
            int applicantID,
            int assessmentID,
            int assessmentScore,
            String readinessResult,
            ApplicationForm form) {

        this(
                0,
                animalID,
                applicantID,
                assessmentID,
                assessmentScore,
                readinessResult,
                Status.PENDING,
                form,
                LocalDateTime.now().withNano(0)
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

    public ApplicationForm getForm() {
        return form;
    }

    // Can be null for old rows saved before this field existed
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public String getSubmittedAtText() {
        return submittedAt == null ? "N/A" : submittedAt.format(DATE_FORMAT);
    }

    public AdoptionApplication withID(int newID) {
        return new AdoptionApplication(
                newID,
                animalID,
                applicantID,
                assessmentID,
                assessmentScore,
                readinessResult,
                status,
                form,
                submittedAt
        );
    }

    @Override
    public String toString() {
        return "Application ID: " + id
                + "\nAnimal ID: " + animalID
                + "\nAssessment Score: " + assessmentScore + "/100"
                + "\nReadiness Result: " + readinessResult
                + "\nStatus: " + status
                + "\nDate Submitted: " + getSubmittedAtText();
    }
}
