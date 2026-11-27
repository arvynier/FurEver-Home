package adoption;

import adoption.exception.AdoptionException;
import adoption.model.User;
import adoption.repository.UserRepository;
import adoption.service.AuthService;
import adoption.repository.AnimalRepository;
import adoption.service.AnimalService;
import adoption.service.BrowseService;
import adoption.service.CareGuideService;

import adoption.ui.AnimalMenu;
import adoption.ui.BrowseMenu;
import adoption.ui.ConsoleInput;
import adoption.ui.AdoptionAssessmentMenu;
import adoption.ui.CareGuideMenu;
import adoption.ui.AdoptionApplicationMenu;

import adoption.repository.AdoptionAssessmentRepository;
import adoption.service.AdoptionAssessmentService;

import adoption.repository.AdoptionApplicationRepository;
import adoption.service.AdoptionApplicationService;


public class Main {

    private static final String[] menuOptions = {
            "Register",
            "Login",
            "Exit"
    };

    private final ConsoleInput input;
    private final AuthService authService;
    private final AnimalMenu animalMenu;
    private final BrowseMenu browseMenu;
    private final AdoptionAssessmentMenu assessmentMenu;
    private final CareGuideMenu careGuideMenu;
    private final AdoptionApplicationMenu applicationMenu;

    public Main(
            ConsoleInput input,
            AuthService authService,
            AnimalMenu animalMenu,
            BrowseMenu browseMenu,
            AdoptionAssessmentMenu assessmentMenu,
            CareGuideMenu careGuideMenu,
            AdoptionApplicationMenu applicationMenu) {

        this.input = input;
        this.authService = authService;
        this.animalMenu = animalMenu;
        this.browseMenu = browseMenu;
        this.assessmentMenu = assessmentMenu;
        this.careGuideMenu = careGuideMenu;
        this.applicationMenu = applicationMenu;
    }

    public static void main(String[] args) {

        ConsoleInput input = new ConsoleInput();

        UserRepository userRepository = new UserRepository();

        AnimalRepository animalRepository = new AnimalRepository();

        AuthService authService = new AuthService(userRepository);

        AnimalService animalService =
                new AnimalService(
                        animalRepository,
                        userRepository
                );

        BrowseService browseService =
                new BrowseService(
                        animalRepository,
                        userRepository
                );

        AnimalMenu animalMenu =
                new AnimalMenu(
                        input,
                        animalService
                );

        BrowseMenu browseMenu =
                new BrowseMenu(
                        input,
                        browseService
                );


        // ADOPTION ASSESSMENT

        AdoptionAssessmentRepository assessmentRepository = new AdoptionAssessmentRepository();

        AdoptionAssessmentService
                assessmentService =
                new AdoptionAssessmentService(
                        assessmentRepository
                );

        AdoptionAssessmentMenu
                assessmentMenu =
                new AdoptionAssessmentMenu(
                        assessmentService,
                        input
                );


        // CARE GUIDE

        CareGuideMenu careGuideMenu =
                new CareGuideMenu(
                        input,
                        new CareGuideService()
                );


        // ADOPTION APPLICATION

        AdoptionApplicationRepository applicationRepository = new AdoptionApplicationRepository();

        AdoptionApplicationService
                applicationService =
                new AdoptionApplicationService(
                        applicationRepository,
                        assessmentService,
                        animalRepository,
                        userRepository
                );

        AdoptionApplicationMenu
                applicationMenu =
                new AdoptionApplicationMenu(
                        applicationService,
                        input
                );


        new Main(
                input,
                authService,
                animalMenu,
                browseMenu,
                assessmentMenu,
                careGuideMenu,
                applicationMenu
        ).run();
    }


    private void run() {

        boolean running = true;

        while (running) {

            printMenu();

            String choice =
                    prompt("Welcome to FurEver Home!");

            switch (choice) {

                case "1" -> handleRegistration();
                case "2" -> handleLogin();
                case "3" -> { System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please choose only between 1, 2, or 3.");
            }
        }
    }


    private void printMenu() {
        System.out.println(
                "Responsible Pet Adoption/Rehoming System");
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

            String picture = prompt("Profile picture path (optional, press Enter to skip)");

            User user =
                    authService.register(
                            firstName,
                            lastName,
                            email,
                            password,
                            mobile,
                            age,
                            address,
                            picture
                    );

            System.out.println("Registration successful! Welcome, " + user.getFullName() + ".");
        } catch (AdoptionException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }


    private void handleLogin() {
        System.out.println(
                "Login");

        try {
            String email = prompt("Email address");
            String password = prompt("Password");

            User user =
                    authService.login(
                            email,
                            password
                    );

            showAccountMenu(user);
        } catch (AdoptionException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }


    private void showAccountMenu(User user) {
        System.out.println("\nLogin successful!");
        System.out.println("Welcome back, "+ user + ".");

        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nAccount Menu");
            System.out.println("1. View my profile");
            System.out.println("2. Browse Animals");
            System.out.println("3. My animal listings");
            System.out.println("4. Adoption Readiness Assessment");
            System.out.println("5. Responsible Ownership and Cost Guide");
            System.out.println("6. Adoption Applications");
            System.out.println("7. Logout");


            switch (prompt("Choose an option")) {
                case "1" -> user = viewProfile(user);
                case "2" -> browseMenu.show(user);
                case "3" -> animalMenu.show(user);
                case "4" -> assessmentMenu.show(user);
                case "5" -> careGuideMenu.show();
                case "6" -> applicationMenu.show(user);
                case "7" -> {
                    System.out.println("Logged out.");
                    loggedIn = false;
                }
                default -> System.out.println("Invalid option. Please enter 1 to 7.");
            }
        }
    }


    private User viewProfile(User user) {
        showProfile(user);
        if (input.promptYesNo("Edit your profile?", false)) {
            return editProfile(user);
        }
        return user;
    }


    private void showProfile(User user) {
        System.out.println("\nName: " + user.getFullName());
        System.out.println("Age: " + user.getAge() + " | Mobile: " + user.getMobileNumber());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Address: " + user.getAddress());
        System.out.println("Profile picture: " + user.getProfilePicturePath() + (user.hasProfilePicture() ? "" : " (default)"));
    }

    private User editProfile(User user) {
        System.out.println("Edit Profile");
        System.out.println("Leave a field blank to keep the current value. " + "The email and password cannot be changed.");

        try {
            String firstName = askWithCurrent("First name", user.getFirstName());
            String lastName = askWithCurrent("Last name", user.getLastName());
            String mobile = askWithCurrent("Mobile number", user.getMobileNumber());
            int age = Integer.parseInt(askWithCurrent("Age", String.valueOf(user.getAge())));
            String address = askWithCurrent("Address", user.getAddress());
            String currentPicture = user.getProfilePicturePath();

            String answer =
                    prompt(
                            "Profile picture path [" + (currentPicture == null ? "none" : currentPicture) + "] (Enter to keep, type 'remove' to delete)"
                    ).replace("\"", "");

            String picture =
                    answer.isBlank() ? currentPicture : (answer.equalsIgnoreCase("remove") ? null : answer);

            User updated =
                    authService.updateProfile(
                            user,
                            firstName,
                            lastName,
                            mobile,
                            age,
                            address,
                            picture
                    );

            System.out.println("Profile updated successfully.");
            return updated;
        } catch (AdoptionException e) {
            System.out.println("Profile update failed: " + e.getMessage());
        }
        return user;
    }


    private String askWithCurrent(String label, String current) {
        String answer = prompt( label + "[" + current + "]");
        return answer.isBlank() ? current : answer;
    }


    private String prompt(String label) {
        return input.prompt(label);
    }
}