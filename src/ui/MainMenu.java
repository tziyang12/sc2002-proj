package ui;

import controller.AuthenticationController;
import model.user.User;
import model.project.Project;
import model.user.Applicant;

import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private final AuthenticationController authenticationController = new AuthenticationController();
    private final Scanner scanner = new Scanner(System.in);

    // This method will show the main menu and handle user login
    public void showMainMenu(List<User> users, List<Project> projects) {
        User loggedInUser = login(users);  // Perform login

        if (loggedInUser instanceof Applicant) {
            Applicant applicant = (Applicant) loggedInUser;  // Cast user to Applicant
            showApplicantMenu(applicant, projects); // Handle the applicant's actions
        } else {
            System.out.println("Invalid login or unsupported user role.");
        }
    }

    // This method handles the login process
    private User login(List<User> users) {
        System.out.println("==== Login ====");
        System.out.print("Enter NRIC: ");
        String nric = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        // Use AuthenticationController to check login credentials
        User user = authenticationController.authenticate(nric, password, users);

        // If authentication failed, ask the user to try again
        if (user == null) {
            System.out.println("Invalid NRIC or Password. Please try again.");
            return login(users);  // Recursive login attempt
        }

        System.out.println("Login successful. Welcome, " + nric);
        return user;  // Return the authenticated user
    }

    // This method will handle actions specific to an applicant after login
    private void showApplicantMenu(Applicant applicant, List<Project> projects) {
        System.out.println("Welcome " + applicant.getNric() + " (" + applicant.getRole() + ")");
        
        // Here you can show the actions available to the applicant, e.g., view projects, apply, etc.
        // Example:
        System.out.println("1. View Available Projects");
        System.out.println("2. Apply for a Project");
        System.out.println("3. View Application Status");
        System.out.println("4. Exit");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                viewProjects(applicant, projects);
                break;
            case 2:
                applyForProject(applicant, projects);
                break;
            case 3:
                viewApplicationStatus(applicant);
                break;
            case 4:
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void viewProjects(Applicant applicant, List<Project> projects) {
        System.out.println("Available Projects for " + applicant.getNric());
        // Call ProjectController's method to display eligible projects
    }

    private void applyForProject(Applicant applicant, List<Project> projects) {
        System.out.println("Choose a project to apply for:");
        // Call ProjectController's method to apply for a project
    }

    private void viewApplicationStatus(Applicant applicant) {
        System.out.println("Application Status for " + applicant.getNric());
        // Display application status
    }
}
