package adoption.repository;

import adoption.exception.AdoptionException;
import adoption.model.AdoptionApplication;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AdoptionApplicationRepository
        extends CsvRepository<AdoptionApplication> {

    private static final Path DEFAULT_FILE =
            Path.of("data", "adoption_applications.csv");

    private static final String[] HEADERS = {
            "ID",
            "animalID",
            "applicantID",
            "assessmentID",
            "assessmentScore",
            "readinessResult",
            "status"
    };

    public AdoptionApplicationRepository() {
        super(DEFAULT_FILE);
    }

    @Override
    protected String[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected String[] toRow(AdoptionApplication application) {

        return new String[]{
                String.valueOf(application.getID()),
                String.valueOf(application.getAnimalID()),
                String.valueOf(application.getApplicantID()),
                String.valueOf(application.getAssessmentID()),
                String.valueOf(application.getAssessmentScore()),
                application.getReadinessResult(),
                application.getStatus().name()
        };
    }

    @Override
    protected AdoptionApplication fromRow(String[] row) {

        return new AdoptionApplication(
                Integer.parseInt(row[0]),
                Integer.parseInt(row[1]),
                Integer.parseInt(row[2]),
                Integer.parseInt(row[3]),
                Integer.parseInt(row[4]),
                row[5],
                AdoptionApplication.Status.valueOf(row[6])
        );
    }

    public AdoptionApplication save(
            AdoptionApplication application)
            throws AdoptionException {

        List<AdoptionApplication> applications =
                loadAll();

        int nextID = applications.stream()
                .mapToInt(AdoptionApplication::getID)
                .max()
                .orElse(0) + 1;

        AdoptionApplication saved =
                application.withID(nextID);

        applications.add(saved);

        saveAll(applications);

        return saved;
    }

    public AdoptionApplication findByID(int id)
            throws AdoptionException {

        return loadAll()
                .stream()
                .filter(a -> a.getID() == id)
                .findFirst()
                .orElse(null);
    }

    public List<AdoptionApplication> findByApplicantID(
            int applicantID)
            throws AdoptionException {

        List<AdoptionApplication> result =
                new ArrayList<>();

        for (AdoptionApplication application : loadAll()) {

            if (application.getApplicantID()
                    == applicantID) {

                result.add(application);
            }
        }

        return result;
    }

    public List<AdoptionApplication> findByAnimalID(
            int animalID)
            throws AdoptionException {

        List<AdoptionApplication> result =
                new ArrayList<>();

        for (AdoptionApplication application : loadAll()) {

            if (application.getAnimalID()
                    == animalID) {

                result.add(application);
            }
        }

        return result;
    }

    public List<AdoptionApplication> findByPosterID(
            int posterID,
            AnimalRepository animalRepository)
            throws AdoptionException {

        List<AdoptionApplication> result =
                new ArrayList<>();

        for (AdoptionApplication application : loadAll()) {

            var animal =
                    animalRepository.findByID(
                            application.getAnimalID()
                    );

            if (animal.isPresent()
                    && animal.get().getPosterID()
                    == posterID) {

                result.add(application);
            }
        }

        return result;
    }

    public void update(
            AdoptionApplication application)
            throws AdoptionException {

        List<AdoptionApplication> applications =
                loadAll();

        for (int i = 0;
             i < applications.size();
             i++) {

            if (applications.get(i).getID()
                    == application.getID()) {

                applications.set(i, application);
                saveAll(applications);
                return;
            }
        }

        throw new AdoptionException(
                "Adoption application was not found."
        );
    }
}