package ui;

import controller.OfficerController;
import model.user.HDBOfficer;
import model.user.Applicant;
import model.project.Project;
import model.transaction.Enquiry;
import model.transaction.ApplicationStatus;

import java.util.List;
import java.util.Scanner;

public class OfficerMenu {

    private OfficerController officerController;
    private HDBOfficer currentOfficer;
    private List<Project> projectList;
    private List<Applicant> applicantList;

    public OfficerMenu(HDBOfficer officer, List<Project> projects, List<Applicant> applicants) {
        this.officerController = new OfficerController();
        this.currentOfficer = officer;
        this.projectList = projects;
        this.applicantList = applicants;
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
    
        while (true) {
            System.out.println("\n--- HDB Officer Main Menu ---");
            System.out.println("1. Applicant Options");
            System.out.println("2. Officer Options");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1 -> showApplicantMenu();
                case 2 -> showOfficerOptions(scanner);
                case 3 -> {
                    System.out.println("Exiting HDB Officer Menu.");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showApplicantMenu() {
        new ApplicantMenu().show(currentOfficer, projectList); // HDBOfficer is an Applicant
    }

    private void showOfficerOptions(Scanner scanner) {
        while (true) {
            System.out.println("\n--- HDB Officer Options ---");
            System.out.println("1. View projects available for officer registration");
            System.out.println("2. View assigned project");
            System.out.println("3. Register to handle a project");
            System.out.println("4. View project enquiries");
            System.out.println("5. Reply to an enquiry");
            System.out.println("6. Update applicant booking status");
            System.out.println("7. Generate booking receipt");
            System.out.println("8. Back");
            System.out.print("Select an option: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1 -> viewProjectsAvailableForOfficer();
                case 2 -> viewAssignedProject();
                case 3 -> registerOfficerToProject(scanner);
                case 4 -> viewEnquiries();
                case 5 -> replyToEnquiry(scanner);
                case 6 -> updateApplicantBookingStatus(scanner);
                case 7 -> generateBookingReceipt();
                case 8 -> { return; } // Back to main menu
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void viewProjectsAvailableForOfficer() {
        System.out.println("\n--- Projects Available for Officer Registration ---");
    
        boolean found = false;
    
        for (Project project : projectList) {
            String manager = project.getManagerUserID();
            if (manager == null || manager.isEmpty()) {
                found = true;
                System.out.println("- " + project.getProjectName() + " in " + project.getNeighbourhood() +
                        " (Opening: " + project.getApplicationStartDate() + ", Closing: " + project.getApplicationEndDate() + ")");
            }
        }
    
        if (!found) {
            System.out.println("No unassigned projects available at the moment.");
        }
    }
    
    private void registerOfficerToProject(Scanner scanner) {
        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine();
        // Retrieve the project object by name (You can customize this part based on your project structure)
        Project project = findProjectByName(projectName);

        if (project != null) {
            boolean isRegistered = officerController.registerOfficerToProject(currentOfficer, project);
            if (isRegistered) {
                System.out.println("Registration successful.");
            }
        } else {
            System.out.println("Project not found.");
        }
    }

    private void viewAssignedProject() {
        System.out.println("\n--- Your Assigned Project(s) ---");
        boolean found = false;
    
        for (Project project : projectList) {
            List<HDBOfficer> assignedOfficers = project.getOfficers();
            if (assignedOfficers != null && assignedOfficers.contains(currentOfficer)) {
                found = true;
                System.out.println("• " + project.getProjectName() + " in " + project.getNeighbourhood());
                System.out.println("  Application Period: " + project.getApplicationStartDate() + " to " + project.getApplicationEndDate());
                System.out.println();
            }
        }
    
        if (!found) {
            System.out.println("You are not currently registered to any project.");
        }
    }    
    

    private void viewEnquiries() {
        officerController.viewEnquiries(currentOfficer);
    }

    private void replyToEnquiry(Scanner scanner) {
        System.out.print("Enter enquiry ID to reply to: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter your reply: ");
        String replyMessage = scanner.nextLine();

        officerController.replyToEnquiry(currentOfficer, enquiryId, replyMessage);
    }

    private void updateApplicantBookingStatus(Scanner scanner) {
        System.out.print("Enter applicant NRIC to update booking status: ");
        String applicantNRIC = scanner.nextLine();
        // Retrieve the applicant (you can retrieve by name or some identifier)
        Applicant applicant = findApplicantByNric(applicantNRIC);

        if (applicant != null) {
            officerController.updateApplicantBookingStatus(currentOfficer, applicant);
        } else {
            System.out.println("Applicant not found.");
        }
    }

    private void generateBookingReceipt() {
        System.out.print("Enter applicant name to generate booking receipt: ");
        String applicantNRIC = new Scanner(System.in).nextLine();
        // Retrieve the applicant (you can retrieve by name or some identifier)
        Applicant applicant = findApplicantByNric(applicantNRIC);

        if (applicant != null) {
            officerController.generateBookingReceipt(currentOfficer, applicant);
        } else {
            System.out.println("Applicant not found.");
        }
    }

    private Project findProjectByName(String name) {
        for (Project project : projectList) {
            if (project.getProjectName().equalsIgnoreCase(name)) {
                return project;
            }
        }
        return null;
    }

    private Applicant findApplicantByNric(String nric) {
        for (Applicant applicant : applicantList) {
            if (applicant.getNric().equalsIgnoreCase(nric)) {
                return applicant;
            }
        }
        return null;
    }
}
