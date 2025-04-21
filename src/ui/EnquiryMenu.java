package ui;

import controller.EnquiryController;
import data.ProjectRepository;
import model.project.Project;
import model.transaction.Enquiry;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.User;

import java.util.List;
/**
 * This menu handles the command-line interface for enquiry management.
 * Provides different options based on whether the user is an HDB Manager or HDB Officer.
 */
public class EnquiryMenu {
    private final User user;
    private final List<Project> accessibleProjects;
    private final EnquiryController enquiryController;
    /**
     * Constructs an EnquiryMenu for a given user, their accessible projects,
     * and the associated enquiry controller.
     *
     * @param user               The logged-in user (Manager or Officer).
     * @param accessibleProjects The list of projects the user has access to.
     * @param enquiryController  Controller for handling enquiry logic.
     */
    public EnquiryMenu(User user, List<Project> accessibleProjects, EnquiryController enquiryController) {
        this.user = user;
        this.accessibleProjects = accessibleProjects;
        this.enquiryController = enquiryController;
    }


    /**
     * Displays the enquiry management menu and handles user input for navigation.
     * Managers and officers will see different sets of options.
     */
    public void show() {
        // Define menu options for different user roles
        String[] managerOptions = {
                "View All Enquiries",
                "View Managed Enquiries",
                "Reply to Enquiry",
                "Back"
        };

        String[] officerOptions = {
                "View Managed Enquiries",
                "Reply to Enquiry",
                "Back"
        };

        while (true) {
            CLIView.printHeader("Enquiry Management");

            if (user instanceof HDBManager) {
                CLIView.printMenu(managerOptions);
            } else if (user instanceof HDBOfficer) {
                CLIView.printMenu(officerOptions);
            } else {
                CLIView.printError("You do not have access to enquiry management.");
                return;
            }

            int choice = CLIView.promptInt("");

            if (user instanceof HDBManager) {
                switch (choice) {
                    case 1 -> viewAllEnquiries();               // All projects
                    case 2 -> viewManagedEnquiries();           // Only managed
                    case 3 -> replyToEnquiry();                 // Reply if managing
                    case 4 -> { return; }
                    default -> CLIView.printError("Invalid option. Try again.");
                }
            } else if (user instanceof HDBOfficer) {
                switch (choice) {
                    case 1 -> viewManagedEnquiries();
                    case 2 -> replyToEnquiry();
                    case 3 -> { return; }
                    default -> CLIView.printError("Invalid option. Try again.");
                }
            }
        }
    }

    /**
     * Displays all enquiries from all available projects.
     * Only available to HDB Managers.
     */
    private void viewAllEnquiries() {
        List<Enquiry> all = enquiryController.getAllEnquiries(ProjectRepository.getAllProjects());

        if (all.isEmpty()) {
            CLIView.printMessage("No enquiries available.");
            return;
        }

        CLIView.printHeader("All Project Enquiries");
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

    /**
     * Displays enquiries for only those projects the user (Manager or Officer) manages or is assigned to.
     */
    private void viewManagedEnquiries() {
        List<Enquiry> enquiries = enquiryController.getAllEnquiries(accessibleProjects);

        if (enquiries.isEmpty()) {
            CLIView.printMessage("No enquiries available for your projects.");
            return;
        }

        CLIView.printHeader("Managed Project Enquiries");
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

    /**
     * Prompts the user to select a project and enquiry ID, then enter a reply message.
     * The reply is recorded via the EnquiryController.
     */
    private void replyToEnquiry() {
        String projectName = CLIView.prompt("Enter the project name to reply to an enquiry: ");
        Project selectedProject = accessibleProjects.stream()
                .filter(p -> p.getProjectName().equalsIgnoreCase(projectName))
                .findFirst()
                .orElse(null);

        if (selectedProject == null) {
            CLIView.printError("You are not assigned to or managing this project.");
            return;
        }

        int enquiryId = CLIView.promptInt("Enter Enquiry ID to reply: ");
        String reply = CLIView.prompt("Enter your reply: ");

        enquiryController.replyToEnquiry(user, selectedProject, enquiryId, reply);
    }
}
