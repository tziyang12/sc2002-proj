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
            System.out.println("\n---- HDB Officer Menu ----");
            System.out.println("1. Register to handle a project");
            System.out.println("2. Approve a project registration");
            System.out.println("3. Reject a project registration");
            System.out.println("4. View project enquiries");
            System.out.println("5. Reply to an enquiry");
            System.out.println("6. Update applicant booking status");
            System.out.println("7. Generate booking receipt");
            System.out.println("8. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    registerOfficerToProject(scanner);
                    break;
                case 2:
                    approveRegistration(scanner);
                    break;
                case 3:
                    rejectRegistration();
                    break;
                case 4:
                    viewEnquiries();
                    break;
                case 5:
                    replyToEnquiry(scanner);
                    break;
                case 6:
                    updateApplicantBookingStatus(scanner);
                    break;
                case 7:
                    generateBookingReceipt();
                    break;
                case 8:
                    System.out.println("Exiting Officer Menu.");
                    return; // Exit the menu
                default:
                    System.out.println("Invalid choice, please try again.");
            }
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

    private void approveRegistration(Scanner scanner) {
        System.out.print("Enter project name to approve registration: ");
        String projectName = scanner.nextLine();
        // Retrieve the project object by name
        Project project = findProjectByName(projectName);

        if (project != null) {
            officerController.approveRegistration(currentOfficer, project);
        } else {
            System.out.println("Project not found.");
        }
    }

    private void rejectRegistration() {
        officerController.rejectRegistration(currentOfficer);
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
