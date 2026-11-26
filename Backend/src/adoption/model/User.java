package adoption.model;

import adoption.utility.PasswordHasher;

public class User extends Person {
    public static final int unsavedID = 0;

    private final int id;
    private final String passwordSalt;
    private final String passwordHash;
    private String profilePicturePath;

    public User(int id, String firstName, String lastName, String email, String mobileNumber, int age, String address, String profilePicturePath, String passwordSalt, String passwordHash) {
        super(firstName, lastName, email, mobileNumber, age, address);
        this.id = id;
        this.passwordSalt = passwordSalt;
        this.passwordHash = passwordHash;
        setProfilePicturePath(profilePicturePath);
    }

    public int getID() {
        return id;
    }

    public User withID(int newID) {
        return new User(newID, getFirstName(), getLastName(), getEmail(), getMobileNumber(),
                getAge(), getAddress(), profilePicturePath, passwordSalt, passwordHash);
    }

    public User withProfile(String firstName, String lastName, String mobileNumber, int age, String address, String profilePicturePath) {
        return new User(id, firstName, lastName, getEmail(), mobileNumber, age, address, profilePicturePath, passwordSalt, passwordHash);
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    /* Null when no profile picture is provided. */
    public void setProfilePicturePath(String path) {
        this.profilePicturePath = (path == null || path.isBlank()) ? null : path.trim();
    }

    public boolean hasProfilePicture() {
        return profilePicturePath != null;
    }

    public boolean verifyPassword(String rawPassword) {
        return PasswordHasher.matches(rawPassword, passwordSalt, passwordHash);
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String getRole() {
        return "Adopter and Animal Poster";
    }
}