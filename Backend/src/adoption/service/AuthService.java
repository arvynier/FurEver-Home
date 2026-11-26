package adoption.service;

import adoption.exception.AdoptionException;
import adoption.exception.AdoptionException.AuthenticationException;
import adoption.exception.AdoptionException.DuplicateEmailException;
import adoption.model.User;
import adoption.repository.UserRepository;
import adoption.utility.InputValidator;
import adoption.utility.PasswordHasher;

public class AuthService {
    private static final String invalidLoginMessage = "Invalid email or password.";

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String firstName, String lastName, String email, String password, String mobileNumber, int age, String address, String profilePicturePath) throws AdoptionException {
        InputValidator.validateName(firstName, "First name");
        InputValidator.validateName(lastName, "Last name");
        InputValidator.validateEmail(email);
        InputValidator.validatePassword(password);
        InputValidator.validateMobileNumber(mobileNumber);
        InputValidator.validateAge(age);
        InputValidator.validateAddress(address);
        InputValidator.validateProfilePicture(profilePicturePath);
    
        String cleanEmail = email.trim().toLowerCase();
        if (userRepository.existsByEmail(cleanEmail)) {
            throw new DuplicateEmailException(cleanEmail);
        }

        String salt = PasswordHasher.generateSalt();
        String hash = PasswordHasher.hash(password, salt);

        User newUser = new User(User.unsavedID, firstName.trim(), lastName.trim(), cleanEmail,
                mobileNumber.trim(), age, address.trim(), profilePicturePath, salt, hash);
        
        return userRepository.save(newUser);
    }

    public User login(String email, String password) throws AdoptionException {
            if (email == null || email.isBlank() || password == null || password.isEmpty()) {
                throw new AuthenticationException("Email and password are required.");
            }
            
            User user = userRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException(invalidLoginMessage));
            
            if (!user.verifyPassword(password)) {
                throw new AuthenticationException(invalidLoginMessage);
            }
            return user;
    }

    public User updateProfile(User current, String firstName, String lastName, String mobileNumber, int age, String address, String profilePicturePath) throws AdoptionException {
        InputValidator.validateName(firstName, "First name");
        InputValidator.validateName(lastName, "Last name");
        InputValidator.validateMobileNumber(mobileNumber);
        InputValidator.validateAge(age);
        InputValidator.validateAddress(address);
        InputValidator.validateProfilePicture(profilePicturePath);

        User updated = current.withProfile(firstName.trim(), lastName.trim(), mobileNumber.trim(), age, address.trim(), profilePicturePath);
        userRepository.update(updated);
        return updated;
    }
}