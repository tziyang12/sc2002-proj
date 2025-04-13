package main;

import controller.ApplicantController;
import controller.AuthenticationController;
import data.DataLoader;
import model.project.Project;
import model.user.Applicant;
import model.user.User;
import ui.ApplicantMenu;

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
            allUsers.addAll(applicants); // Add other user types here later
        } catch (IOException e) {
            System.out.println("Failed to load data files.");
            e.printStackTrace();
            return;
        }

        AuthenticationController authController = new AuthenticationController();
        Scanner scanner = new Scanner(System.in);

        // === MAIN MENU LOOP ===
        while (true) {
            System.out.println("\n=== Welcome to the BTO Management System ===");
            System.out.println("1. Login");
            System.out.println("2. Change Password");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            String menuChoice = scanner.nextLine();

            if (menuChoice.equals("1")) {
                // === LOGIN FLOW ===
                System.out.print("Enter NRIC: ");
                String nric = scanner.nextLine();

                System.out.print("Enter Password: ");
                String password = scanner.nextLine();

                User loggedInUser = authController.authenticate(nric, password, allUsers);

                if (loggedInUser == null) {
                    continue;
                }

                System.out.println("Login successful. Welcome, " + loggedInUser.getName());

                if (loggedInUser instanceof Applicant) {
                    new ApplicantMenu().show((Applicant) loggedInUser, projects);
                } else {
                    System.out.println("This user type is not yet supported in Main.java.");
                }

            } else if (menuChoice.equals("2")) {
                // === CHANGE PASSWORD FLOW ===
                System.out.print("Enter your NRIC: ");
                String nric = scanner.nextLine();

                User targetUser = allUsers.stream()
                        .filter(u -> u.getNric().equalsIgnoreCase(nric))
                        .findFirst()
                        .orElse(null);

                if (targetUser == null) {
                    System.out.println("User not found.");
                    continue;
                }

                System.out.print("Enter current password: ");
                String currentPass = scanner.nextLine();

                System.out.print("Enter new password: ");
                String newPass = scanner.nextLine();

                boolean success = authController.changePassword(targetUser, currentPass, newPass);
                if (success) {
                    System.out.println("Password updated. Please log in again.");
                }

            } else if (menuChoice.equals("3")) {
                System.out.println("Exiting system. Goodbye!");
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }
}
