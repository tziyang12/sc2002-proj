package ui;

import controller.EnquiryController;
import data.ProjectRepository;
import model.project.Project;
import model.transaction.Enquiry;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.User;

import java.util.List;
import java.util.Scanner;

public class EnquiryMenu {
    private final User user;
    private final List<Project> accessibleProjects;
    private final EnquiryController enquiryController;

    public EnquiryMenu(User user, List<Project> accessibleProjects, EnquiryController enquiryController) {
        this.user = user;
        this.accessibleProjects = accessibleProjects;
        this.enquiryController = enquiryController;
    }

    public void show() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Enquiry Management ---");

            if (user instanceof HDBManager) {
                System.out.println(accessibleProjects);
                System.out.println("1. View All Enquiries");
                System.out.println("2. View Managed Enquiries");
                System.out.println("3. Reply to Enquiry");
                System.out.println("4. Back");
            } else if (user instanceof HDBOfficer) {
                System.out.println("1. View Managed Enquiries");
                System.out.println("2. Reply to Enquiry");
                System.out.println("3. Back");
            } else {
                System.out.println("You do not have access to enquiry management.");
                return;
            }

            System.out.print("Select an option: ");
            String choice = sc.nextLine();

            if (user instanceof HDBManager manager) {
                switch (choice) {
                    case "1" -> viewAllEnquiries();               // All projects
                    case "2" -> viewManagedEnquiries();           // Only managed
                    case "3" -> replyToEnquiry();                 // Reply if managing
                    case "4" -> { return; }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } else if (user instanceof HDBOfficer) {
                switch (choice) {
                    case "1" -> viewManagedEnquiries();
                    case "2" -> replyToEnquiry();
                    case "3" -> { return; }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        }
    }

    private void viewAllEnquiries() {
        List<Enquiry> all = enquiryController.getAllEnquiries(ProjectRepository.getAllProjects());

        if (all.isEmpty()) {
            System.out.println("No enquiries available.");
            return;
        }

        System.out.println("\n--- All Project Enquiries ---");
        CLIView.printEnquiryTableHeader();
        for (Enquiry enquiry : all) {
            String projectName = enquiry.getProject() != null ? enquiry.getProject().getProjectName() : "N/A";
            CLIView.printEnquiryRow(
                projectName,
                enquiry.getEnquiryId(),
                enquiry.getEnquiryMessage(),
                enquiry.getReplyMessage()
            );
        }
        CLIView.printEnquiryTableFooter();
    }

    private void viewManagedEnquiries() {
        List<Enquiry> enquiries = enquiryController.getAllEnquiries(accessibleProjects);

        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found for your projects.");
            return;
        }

        System.out.println("\n--- Managed Project Enquiries ---");
        CLIView.printEnquiryTableHeader();
        for (Enquiry enquiry : enquiries) {
            String projectName = enquiry.getProject() != null ? enquiry.getProject().getProjectName() : "N/A";
            CLIView.printEnquiryRow(
                projectName,
                enquiry.getEnquiryId(),
                enquiry.getEnquiryMessage(),
                enquiry.getReplyMessage()
            );
        }
        CLIView.printEnquiryTableFooter();
    }

    private void replyToEnquiry() {
        String projectName = CLIView.prompt("Enter the project name to reply to an enquiry: ");
        Project selectedProject = accessibleProjects.stream()
                .filter(p -> p.getProjectName().equalsIgnoreCase(projectName))
                .findFirst()
                .orElse(null);

        if (selectedProject == null) {
            System.out.println("You are not assigned to or managing this project.");
            return;
        }

        int enquiryId = CLIView.promptInt("Enter Enquiry ID to reply: ");
        String reply = CLIView.prompt("Enter your reply: ");

        enquiryController.replyToEnquiry(user, selectedProject, enquiryId, reply);
    }
}
