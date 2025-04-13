package main;

import controller.ApplicantController;
import controller.AuthenticationController;
import data.DataLoader;
import model.project.Project;
import model.user.Applicant;
import model.user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Applicant> applicants;
        List<Project> projects;
        List<User> allUsers = new ArrayList<>();

        try {
            applicants = new DataLoader().loadApplicants("src/data/ApplicantList.csv");
            projects = DataLoader.loadProjects("src/data/ProjectList.csv");
            allUsers.addAll(applicants); // If you later load Officers/Managers, add them here too.
        } catch (IOException e) {
            System.out.println("Failed to load data files.");
            e.printStackTrace();
            return;
        }

        AuthenticationController authController = new AuthenticationController();
        Scanner scanner = new Scanner(System.in);

        // Login Loop
        System.out.println("=== Welcome to the BTO Management System ===");
        System.out.print("Enter NRIC: ");
        String nric = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User loggedInUser = authController.authenticate(nric, password, allUsers);

        if (loggedInUser == null) {
            System.out.println("Login failed. Please check your credentials.");
            return;
        }

        System.out.println("Login successful. Welcome, " + loggedInUser.getNric());

        // If user is applicant, show applicant menu
        if (loggedInUser instanceof Applicant) {
            Applicant applicant = (Applicant) loggedInUser;
            ApplicantController applicantController = new ApplicantController();

            while (true) {
                System.out.println("\n--- Applicant Menu ---");
                System.out.println("1. View Eligible Projects");
                System.out.println("2. Apply for a Project");
                System.out.println("3. Withdraw Application");
                System.out.println("4. View Application Status");
                System.out.println("5. Submit Enquiry");
                System.out.println("6. View Enquiries");
                System.out.println("7. Exit");
                System.out.print("Enter choice: ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        applicantController.viewProjects(applicant, projects);
                        break;
                    case 2:
                        System.out.print("Enter project name to apply: ");
                        String projectName = scanner.nextLine();

                        System.out.print("Enter flat type (TWO_ROOM / THREE_ROOM): ");
                        String flatType = scanner.nextLine().toUpperCase();

                        Project selected = projects.stream()
                                .filter(p -> p.getProjectName().equalsIgnoreCase(projectName))
                                .findFirst()
                                .orElse(null);

                        if (selected == null) {
                            System.out.println("Project not found.");
                        } else {
                            try {
                                applicantController.applyForProject(applicant, selected, Enum.valueOf(model.project.FlatType.class, flatType));
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid flat type.");
                            }
                        }
                        break;
                    case 3:
                        applicantController.withdrawApplication(applicant);
                        break;
                    case 4:
                        applicantController.viewApplicationStatus(applicant);
                        break;
                    case 5:
                        System.out.print("Enter your enquiry: ");
                        String enquiry = scanner.nextLine();
                        applicantController.submitEnquiry(applicant, enquiry);
                        break;
                    case 6:
                        applicantController.viewEnquiries(applicant);
                        break;
                    case 7:
                        System.out.println("Exiting. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } else {
            System.out.println("This user type is not yet supported in Main.java.");
        }
    }
}
