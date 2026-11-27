package adoption.model;

/*
 * The answers an applicant gives when applying (Feature 5).
 * Immutable: the fields are final and there are no setters, so
 * the answers cannot be changed after the application is made.
 */
public final class ApplicationForm {

    private final String reasonForAdopting;
    private final String livingArrangement;
    private final String primaryCaregiver;
    private final String existingPets;
    private final boolean homeVisitAgreed;
    private final String messageToPoster;

    public ApplicationForm(
            String reasonForAdopting,
            String livingArrangement,
            String primaryCaregiver,
            String existingPets,
            boolean homeVisitAgreed,
            String messageToPoster) {

        this.reasonForAdopting = reasonForAdopting;
        this.livingArrangement = livingArrangement;
        this.primaryCaregiver = primaryCaregiver;
        this.existingPets = existingPets;
        this.homeVisitAgreed = homeVisitAgreed;
        this.messageToPoster = messageToPoster;
    }

    public String getReasonForAdopting() {
        return reasonForAdopting;
    }

    public String getLivingArrangement() {
        return livingArrangement;
    }

    public String getPrimaryCaregiver() {
        return primaryCaregiver;
    }

    public String getExistingPets() {
        return existingPets;
    }

    public boolean isHomeVisitAgreed() {
        return homeVisitAgreed;
    }

    public String getMessageToPoster() {
        return messageToPoster;
    }
}
