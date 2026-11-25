package adoption.repository;

import adoption.exception.AdoptionException;
import adoption.model.AdoptionAssessment;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AdoptionAssessmentRepository
        extends CsvRepository<AdoptionAssessment> {

    private static final Path DEFAULT_FILE =
            Path.of("data", "adoption_assessments.csv");

    private static final String[] HEADERS = {
            "ID",
            "applicantID",
            "animalID",
            "financialScore",
            "timeScore",
            "livingEnvironmentScore",
            "careExperienceScore",
            "totalScore",
            "readinessResult"
    };

    public AdoptionAssessmentRepository() {
        super(DEFAULT_FILE);
    }

    @Override
    protected String[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected String[] toRow(AdoptionAssessment assessment) {

        return new String[] {
                String.valueOf(assessment.getID()),
                String.valueOf(assessment.getApplicantID()),
                String.valueOf(assessment.getAnimalID()),
                String.valueOf(assessment.getFinancialScore()),
                String.valueOf(assessment.getTimeScore()),
                String.valueOf(assessment.getLivingEnvironmentScore()),
                String.valueOf(assessment.getCareExperienceScore()),
                String.valueOf(assessment.getTotalScore()),
                assessment.getReadinessResult()
        };
    }

    @Override
    protected AdoptionAssessment fromRow(String[] row) {

        return new AdoptionAssessment(
                Integer.parseInt(row[0]),
                Integer.parseInt(row[1]),
                Integer.parseInt(row[2]),
                Integer.parseInt(row[3]),
                Integer.parseInt(row[4]),
                Integer.parseInt(row[5]),
                Integer.parseInt(row[6]),
                Integer.parseInt(row[7]),
                row[8]
        );
    }

    // Save a new assessment
    public AdoptionAssessment save(AdoptionAssessment assessment)
            throws AdoptionException {

        List<AdoptionAssessment> assessments = loadAll();

        int nextID = assessments.stream()
                .mapToInt(AdoptionAssessment::getID)
                .max()
                .orElse(0) + 1;

        AdoptionAssessment assessmentWithID =
                assessment.withID(nextID);

        assessments.add(assessmentWithID);

        saveAll(assessments);

        return assessmentWithID;
    }

    // Find assessment by ID
    public AdoptionAssessment findByID(int id)
            throws AdoptionException {

        return loadAll()
                .stream()
                .filter(a -> a.getID() == id)
                .findFirst()
                .orElse(null);
    }

    // Find assessments made by a specific applicant
    public List<AdoptionAssessment> findByApplicantID(int applicantID)
            throws AdoptionException {

        List<AdoptionAssessment> result = new ArrayList<>();

        for (AdoptionAssessment assessment : loadAll()) {

            if (assessment.getApplicantID() == applicantID) {
                result.add(assessment);
            }
        }

        return result;
    }

    // Find assessment for a specific applicant and animal
    public AdoptionAssessment findByApplicantAndAnimal(
            int applicantID,
            int animalID)
            throws AdoptionException {

        return loadAll()
                .stream()
                .filter(a ->
                        a.getApplicantID() == applicantID
                                && a.getAnimalID() == animalID)
                .findFirst()
                .orElse(null);
    }

    // Get all assessments
    public List<AdoptionAssessment> findAll()
            throws AdoptionException {

        return loadAll();
    }
}