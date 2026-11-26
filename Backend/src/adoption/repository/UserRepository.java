package adoption.repository;

import adoption.exception.AdoptionException;
import adoption.model.User;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import adoption.exception.AdoptionException.NotFoundException;

public class UserRepository extends CsvRepository<User> {
    private static final Path defaultFile = Path.of("data", "users.csv");
    
    private static final String[] HEADERS = {
        "id", "firstname", "lastname", "email", "password_salt", "password_hash", "mobilenumber", "age", "address", "profilepicturepath"
    };

    public UserRepository() {
        this(defaultFile);
    }

    public UserRepository(Path filePath) {
        super(filePath);
    }

    public User save(User user) throws AdoptionException {
        System.out.println("DEBUG: save() called");
        List<User> users = loadAll();

        int nextID = 1;
        for (User existing : users) {
            if (existing.getID() >= nextID) {
                nextID = existing.getID() + 1;
            }
        }

        User saved = user.withID(nextID);
        users.add(saved);
        saveAll(users);
        return saved;
    }

    public void update(User user) throws AdoptionException {
        List<User> users = loadAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getID() == user.getID()) {
                users.set(i, user);
                saveAll(users);
                return;
            }
        }
        throw new NotFoundException("User with ID " + user.getID() + " not found.");
    }

    public Optional<User> findByID(int ID) throws AdoptionException {
        for (User user : loadAll()) {
            if (user.getID() == ID) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) throws AdoptionException {
        String wanted = normalize(email);
        for (User user : loadAll()) {
            if (user.getEmail(). equalsIgnoreCase(wanted)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean existsByEmail(String email) throws AdoptionException {
        return findByEmail(email).isPresent();
    }

    @Override 
    protected String[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected String[] toRow(User user) {
        return new String[] {
            String.valueOf(user.getID()),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPasswordSalt(),
            user.getPasswordHash(),
            user.getMobileNumber(),
            String.valueOf(user.getAge()),
            user.getAddress(),
            user.getProfilePicturePath()
        };
    }

    @Override 
    protected User fromRow(String[] row) {
        return new User(
            Integer.parseInt(row[0]),
            row[1],
            row[2],
            row[3],
            row[6],
            Integer.parseInt(row[7]),
            row[8],
            row[9],
            row[4],
            row[5]
        );
    }

    private String normalize(String email) {
        return email.trim().toLowerCase();
    }
}