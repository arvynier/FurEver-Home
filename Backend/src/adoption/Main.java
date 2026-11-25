package adoption;

import adoption.exception.AdoptionException;
import adoption.model.User;
import adoption.repository.UserRepository;
import adoption.service.AuthService;
import adoption.repository.AnimalRepository;
import adoption.service.AnimalService;
import adoption.service.BrowseService;

import adoption.ui.AnimalMenu;
import adoption.ui.BrowseMenu;
import adoption.ui.ConsoleInput;
import adoption.ui.AdoptionAssessmentMenu;

import adoption.repository.AdoptionAssessmentRepository;
import adoption.service.AdoptionAssessmentService;


public class Main {
    private static final String[] menuOptions = {"Register", "Login", "Exit"};

    private final ConsoleInput input;
    private final AuthService authService;
    private final AnimalMenu animalMenu;
    private final BrowseMenu browseMenu;
    private final AdoptionAssessmentMenu assessmentMenu;

    public Main(ConsoleInput input, AuthService authService,
                AnimalMenu animalMenu, BrowseMenu browseMenu,
                AdoptionAssessmentMenu assessmentMenu) {
        this.input = input;
        this.authService = authService;
        this.animalMenu = animalMenu;
        this.browseMenu = browseMenu;
        this.assessmentMenu = assessmentMenu;
    }

    public static void main(String[] args) {
        ConsoleInput input = new ConsoleInput();

        UserRepository userRepository = new UserRepository();
        AnimalRepository animalRepository = new AnimalRepository();

        AuthService authService = new AuthService(userRepository);
        
        AnimalService animalService = new AnimalService(animalRepository, userRepository);
        BrowseService browseService = new BrowseService(animalRepository, userRepository);
        AnimalMenu animalMenu = new AnimalMenu(input, animalService);
        BrowseMenu browseMenu = new BrowseMenu(input, browseService);

        AdoptionAssessmentRepository assessmentRepository = new AdoptionAssessmentRepository();

        AdoptionAssessmentService assessmentService = new AdoptionAssessmentService(assessmentRepository, animalRepository);

        AdoptionAssessmentMenu assessmentMenu = new AdoptionAssessmentMenu(assessmentService, input);

        new Main(input, authService, animalMenu, browseMenu, assessmentMenu).run();
    }

    private void run() {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = prompt("Welcome to FurEver Home!");

            switch (choice) {
                case "1" -> handleRegistration();
                case "2" -> handleLogin();
                case "3" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please choose only between 1, 2, or 3.");
            }
        }
    }

    private void printMenu() {
        System.out.println("Responsible Pet Adoption/Rehoming System");
        for (int i = 0; i < menuOptions.length; i++) {
            System.out.println((i + 1) + ". " + menuOptions[i]);
        }
    }

    private void handleRegistration() {
        System.out.println("Registration Form");
        try {
            String firstName = prompt("First Name");
            String lastName = prompt("Last Name");
            String email = prompt("Email Address");
            String password = prompt("Password (min 8 chars, letters and numbers)");
            String confirmPassword = prompt("Confirm Password");

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match.");
                return;
            }

            String mobile = prompt("Mobile Number (09XXXXXXXXX)");
            int age = Integer.parseInt(prompt("Age"));
            String address = prompt("Address");
            String picture = prompt("Profile picture path (optional, press Enter to skip)"); /* path for the mean time because upload button can only be added once we start with the gui */

            User user = authService.register(firstName, lastName, email, password,
                    mobile, age, address, picture);
            System.out.println("Registration successful! Welcome, " + user.getFullName() + ".");
        } catch (AdoptionException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void handleLogin() {
        System.out.println("Login");
        try {
            String email = prompt("Email address");
            String password = prompt("Password");

            User user = authService.login(email, password);
            showAccountMenu(user);
        } catch (AdoptionException e) { 
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void showAccountMenu(User user) {
        System.out.println("\nLogin successful!");
        System.out.println("Welcome back, " + user + ".");
        
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nAccount Menu");
            System.out.println("1. View my profile");
            System.out.println("2. Browse Animals");
            System.out.println("3. My animal listings");
            System.out.println("4. Adoption Readiness Assessment");
            System.out.println("5. Logout");

            switch(prompt("Choose an option")) {
                case "1" -> showProfile(user);
                case "2" -> browseMenu.show(user);
                case "3" -> animalMenu.show(user);

                case "4" -> {
                    try {
                        int animalID = Integer.parseInt(
                                prompt("Enter Animal ID for the assessment")
                        );

                        assessmentMenu.show(user, animalID);

                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid animal ID.");
                    }
                }

                case "5" -> {
                    System.out.println("Logged out.");
                    loggedIn = false;
                }

                default -> System.out.println(
                        "Invalid option. Please enter 1, 2, 3, 4, or 5."
                );
            }
        }
    }

    private void showProfile(User user) {
        System.out.println("\nName: " + user.getFullName());
        System.out.println("Age: " + user.getAge() + " | Mobile: " + user.getMobileNumber());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Address: " + user.getAddress());
        System.out.println("Profile picture: " + user.getProfilePicturePath()
                + (user.hasProfilePicture() ? "" : " (default)"));
    }

    private String prompt(String label) {
        return input.prompt(label);
    }
}
