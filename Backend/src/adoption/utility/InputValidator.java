package adoption.utility;

import adoption.exception.AdoptionException.ValidationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

public final class InputValidator {

    private static final int passwordMinLength = 8;
    private static final int minAge = 18;
    private static final String[] pictureFileType = {".jpg", ".jpeg", ".png"};
    private static final long photoSize = 50L * 1024 * 1024; /* max photo size is 50 mb */
    public static final int maxPhotos = 7; /* no. of photos allowed per animal */

    private InputValidator() {
    }
    
    public static void validateName(String value, String fieldName) throws ValidationException {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " is required.");
        }
    }

    public static void validateEmail(String value) throws ValidationException {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Email address is required.");
        }
    }

    public static void validatePassword(String value) throws ValidationException {
        if (value == null || value.length() < passwordMinLength) {
            throw new ValidationException("Password that is at least 8 characters wrong is required.");
        }
        boolean hasLetter = false;
        boolean hasDigit = false;

        char[] characters = value.toCharArray();
        for (char c : characters) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        if (!hasLetter || !hasDigit) {
            throw new ValidationException("Password must contain both letters and numbers.");
        }
    }

    public static void validateMobileNumber(String value) throws ValidationException {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Mobile number is required.");
        }
    }

    public static void validateAge(int value) throws ValidationException {
        if (value < minAge) {
            throw new ValidationException("You must be at least 18 years old or above.");
        }
    }

    public static void validateAddress(String value) throws ValidationException {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Address is required.");
        }
    }

    public static void validateProfilePicture(String value) throws ValidationException {
        if (value == null || value.isBlank()) {
            return;
        }
        String path = value.trim().toLowerCase();
        for (String extension : pictureFileType) {
            if (path.endsWith(extension)) {
                return;
            }
        }
        throw new ValidationException("Profile picture must be a .jpg, .jpeg, or .png.");
    }

    public static void validateRequired(Object value, String fieldName) throws ValidationException {
        if (value == null) {
            throw new ValidationException(fieldName + " is required.");
        }
    }

    public static void validateText(String value, String fieldName) throws ValidationException {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " is required.");
        }
    }
    
    public static void validateAnimalAge(int ageMonths) throws ValidationException {
        if (ageMonths <= 0) {
            throw new ValidationException("Please enter a valid number.");
        }
    }
    
    public static void validateWeight(double weight) throws ValidationException {
        if (weight <= 0) {
            throw new ValidationException("Please enter a valid number.");
        }
    }

    public static void validatePhotoFiles(List<String> paths) throws ValidationException {
        if (paths == null || paths.isEmpty()) {
            throw new ValidationException("Photo of the animal is required.");
        } if (paths.size() > maxPhotos) {
            throw new ValidationException("You can add up to 7 pictures.");
        } for (String path : paths) {
            validatePhotoFile(path);
        }
    }

    private static void validatePhotoFile(String path) throws ValidationException {
        if (path == null || path.isBlank()) {
            throw new ValidationException("Photo is requird.");
        } if (!hasAllowedExtension(path)) {
            throw new ValidationException("Photos must be .jpg, .jpeg, or .png.");
        }

        try {
            Path file = Path.of(path.trim());
            if (!Files.isRegularFile(file)) {
                throw new ValidationException("Photo file was not found: " + path);
            }
            if (Files.size(file) > photoSize) {
                throw new ValidationException("Each photo can be up to 50 MB or smaller: " + path);
            }
        } catch (InvalidPathException | IOException e) {
            throw new ValidationException("Photo could not be read: " + path);
        }
    }

    private static boolean hasAllowedExtension(String path) {
        String lowerCasePath = path.trim().toLowerCase();
        for (String extension : pictureFileType) {
            if (lowerCasePath.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}