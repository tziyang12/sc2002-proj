package ui;

import controller.ApplicantController;
import model.project.Project;
import model.user.Applicant;
import model.project.FlatType;

import java.util.List;
import java.util.Scanner;

public class ApplicantMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final ApplicantController applicantController = new ApplicantController();

    public void show(Applicant applicant, List<Project> projects) {
        while (true) {
            printMainMenu();
            int choice = parseIntInput();

            switch (choice) {
                case 1 -> applicantController.viewProjects(applicant, projects);
                case 2 -> handleProjectApplication(applicant, projects);
                case 3 -> applicantController.withdrawApplication(applicant);
                case 4 -> applicantController.viewApplicationStatus(applicant);
                case 5 -> handleEnquirySubmission(applicant);
                case 6 -> handleEnquiryManagement(applicant);
                case 7 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n--- Applicant Menu ---");
        System.out.println("1. View Eligible Projects");
        System.out.println("2. Apply for a Project");
        System.out.println("3. Withdraw Application");
        System.out.println("4. View Application Status");
        System.out.println("5. Submit Enquiry");
        System.out.println("6. Manage Enquiries");
        System.out.println("7. Logout");
        System.out.print("Enter choice: ");
    }

    private int parseIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void handleProjectApplication(Applicant applicant, List<Project> projects) {
        System.out.print("Enter project name to apply: ");
        String projectName = scanner.nextLine();

        System.out.print("Enter flat type (TWO_ROOM / THREE_ROOM): ");
        String flatTypeInput = scanner.nextLine().toUpperCase();

        Project selected = projects.stream()
                .filter(p -> p.getProjectName().equalsIgnoreCase(projectName))
                .findFirst()
                .orElse(null);

        if (selected == null) {
            System.out.println("Project not found.");
            return;
        }

        try {
            FlatType flatType = FlatType.valueOf(flatTypeInput);
            applicantController.applyForProject(applicant, selected, flatType);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid flat type.");
        }
    }

    private void handleEnquirySubmission(Applicant applicant) {
        System.out.print("Enter your enquiry: ");
        String enquiry = scanner.nextLine();
        applicantController.submitEnquiry(applicant, enquiry);
    }

    private void handleEnquiryManagement(Applicant applicant) {
        while (true) {
            printEnquiryMenu();
            int choice = parseIntInput();

            switch (choice) {
                case 1 -> applicantController.viewEnquiries(applicant);
                case 2 -> editEnquiry(applicant);
                case 3 -> deleteEnquiry(applicant);
                case 4 -> { return; }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private void printEnquiryMenu() {
        System.out.println("\n-- Enquiry Menu --");
        System.out.println("1. View Enquiries");
        System.out.println("2. Edit Enquiry");
        System.out.println("3. Delete Enquiry");
        System.out.println("4. Back");
        System.out.print("Choose option: ");
    }

    private void editEnquiry(Applicant applicant) {
        System.out.print("Enter enquiry ID to edit: ");
        int id = parseIntInput();
        System.out.print("Enter new enquiry message: ");
        String message = scanner.nextLine();
        applicantController.editEnquiry(applicant, id, message);
    }

    private void deleteEnquiry(Applicant applicant) {
        System.out.print("Enter enquiry ID to delete: ");
        int id = parseIntInput();
        applicantController.deleteEnquiry(applicant, id);
    }
}
