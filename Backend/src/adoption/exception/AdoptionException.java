package adoption.exception;

public class AdoptionException extends Exception {
    public AdoptionException(String message) {
        super(message);
    }

    /* When another exception is the cause */
    public AdoptionException(String message, Throwable cause) {
        super(message, cause);
    }

    /* When input does not satisfy a validation rule */
    public static class ValidationException extends AdoptionException {
        public ValidationException(String message) {
            super(message);
        }
    }

    /* When a user tries to register with an email that is already connected to an account */
    public static class DuplicateEmailException extends AdoptionException {
        public DuplicateEmailException(String email) {
            super("This email is already connected to an account. Please try another one.");
        }
    }

    /* When login fails */
    public static class AuthenticationException extends AdoptionException{
        public AuthenticationException(String message) {
            super(message);
        }
    }

    /* When the data being asked do not exist */
    public static class NotFoundException extends AdoptionException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    /* When a user tries to edit an animal profile that they didn't post */
    public static class NotAuthorizedException extends AdoptionException {
        public NotAuthorizedException(String message) {
            super(message);
        }
    }
}