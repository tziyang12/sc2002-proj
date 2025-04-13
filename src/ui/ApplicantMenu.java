package ui;

import controller.ApplicantController;
import model.project.FlatType;
import model.project.Project;
import model.user.Applicant;

import java.util.List;

public class ApplicantMenu {
    private final ApplicantController applicantController = new ApplicantController();

    public void show(Applicant applicant, List<Project> projects) {
        String[] menuOptions = {
                "View Eligible Projects",
                "Apply for a Project",
                "Withdraw Application",
                "View Application Status",
                "Submit Enquiry",
                "Manage Enquiries",
                "Logout"
        };

        while (true) {
            CLIView.printHeader("Applicant Menu");
            CLIView.printMenu(menuOptions);
            int choice = CLIView.promptInt("");

            switch (choice) {
                case 1 -> applicantController.viewProjects(applicant, projects);
                case 2 -> handleApply(applicant, projects);
                case 3 -> applicantController.withdrawApplication(applicant);
                case 4 -> applicantController.viewApplicationStatus(applicant);
                case 5 -> {
                    String enquiry = CLIView.prompt("Enter your enquiry: ");
                    applicantController.submitEnquiry(applicant, enquiry);
                }
                case 6 -> manageEnquiries(applicant);
                case 7 -> {
                    CLIView.printMessage("Logging out...");
                    return;
                }
                default -> CLIView.printError("Invalid choice. Try again.");
            }
        }
    }

    private void handleApply(Applicant applicant, List<Project> projects) {
        String projectName = CLIView.prompt("Enter project name to apply: ");
        String flatTypeInput = CLIView.prompt("Enter flat type (TWO_ROOM / THREE_ROOM): ").toUpperCase();

        Project selected = projects.stream()
                .filter(p -> p.getProjectName().equalsIgnoreCase(projectName))
                .findFirst()
                .orElse(null);

        if (selected == null) {
            CLIView.printError("Project not found.");
            return;
        }

        try {
            FlatType flatType = FlatType.valueOf(flatTypeInput);
            applicantController.applyForProject(applicant, selected, flatType);
        } catch (IllegalArgumentException e) {
            CLIView.printError("Invalid flat type.");
        }
    }

    private void manageEnquiries(Applicant applicant) {
        String[] enquiryOptions = {
                "View Enquiries",
                "Edit Enquiry",
                "Delete Enquiry",
                "Back"
        };

        while (true) {
            CLIView.printHeader("Enquiry Menu");
            CLIView.printMenu(enquiryOptions);
            int choice = CLIView.promptInt("");

            switch (choice) {
                case 1 -> applicantController.viewEnquiries(applicant);
                case 2 -> {
                    int id = CLIView.promptInt("Enter enquiry ID to edit: ");
                    String newMsg = CLIView.prompt("Enter new enquiry message: ");
                    applicantController.editEnquiry(applicant, id, newMsg);
                }
                case 3 -> {
                    int id = CLIView.promptInt("Enter enquiry ID to delete: ");
                    applicantController.deleteEnquiry(applicant, id);
                }
                case 4 -> {
                    return;
                }
                default -> CLIView.printError("Invalid choice.");
            }
        }
    }
}
