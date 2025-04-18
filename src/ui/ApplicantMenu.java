package ui;

import controller.ApplicantController;
import model.project.FlatType;
import model.project.Project;
import model.transaction.Enquiry;
import model.user.Applicant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                case 5 -> createEnquiry(applicant, projects);
                case 6 -> manageEnquiries(applicant, projects);
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

    private void createEnquiry(Applicant applicant, List<Project> projects) {
        applicantController.viewProjects(applicant, projects);
        // Step 1: Show eligible projects
        Project selectedProject = CLIView.promptProject(projects);
        
        if (selectedProject == null) {
            CLIView.printError("Project not found.");
            return;
        }
        // Step 2: Prompt enquiry message
        String enquiry = CLIView.prompt("Enter your enquiry: ");
        applicantController.submitEnquiry(applicant, enquiry, selectedProject);
    }

    private void manageEnquiries(Applicant applicant, List<Project> projects) {
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
                case 1 -> viewMyEnquiries(applicant);
                case 2 -> editEnquiry(applicant, projects);
                case 3 -> deleteEnquiry(applicant, projects);
                case 4 -> {
                    return;
                }
                default -> CLIView.printError("Invalid choice.");
            }
        }
    }



    private void viewMyEnquiries(Applicant currentApplicant) {
        // Get a list of all enquiries grouped by project
        Map<Project, List<Enquiry>> projectEnquiriesMap = new HashMap<>();

        // Populate the map with enquiries for each project
        for (Enquiry enquiry : currentApplicant.getEnquiries()) {
            projectEnquiriesMap
                .computeIfAbsent(enquiry.getProject(), k -> new ArrayList<>())
                .add(enquiry);
        }

        if (projectEnquiriesMap.isEmpty()) {
            CLIView.printMessage("No enquiries available.");
            return;
        }

        // Display each project followed by the enquiries related to that project
        for (Map.Entry<Project, List<Enquiry>> entry : projectEnquiriesMap.entrySet()) {
            Project project = entry.getKey();
            List<Enquiry> enquiries = entry.getValue();

            CLIView.printMessage("--- Enquiries for Project: " + project.getProjectName() + " ---");
            CLIView.printEnquiryTableHeader();

            for (Enquiry enquiry : enquiries) {
                CLIView.printEnquiryRow(
                    project.getProjectName(),
                    enquiry.getEnquiryId(),
                    enquiry.getEnquiryMessage(),
                    enquiry.getReplyMessage()
                );
            }

            CLIView.printEnquiryTableFooter();
        }
    }

    private void editEnquiry(Applicant applicant, List<Project> projects) {
        viewMyEnquiries(applicant);
    
        Enquiry enquiry = getApplicantEnquiryByProjectAndId(applicant, projects);
        if (enquiry == null) return;
    
        String newMessage = CLIView.prompt("Enter the new enquiry message: ");
        applicantController.editEnquiry(applicant, enquiry, newMessage);
    }

    private void deleteEnquiry(Applicant applicant, List<Project> projects) {
        viewMyEnquiries(applicant);
    
        Enquiry enquiry = getApplicantEnquiryByProjectAndId(applicant, projects);
        if (enquiry == null) return;
    
        applicantController.deleteEnquiry(applicant, enquiry);
    }
    
    // Helper method to get the enquiry by project and ID
    private Enquiry getApplicantEnquiryByProjectAndId(Applicant applicant, List<Project> projects) {
        Project project = CLIView.promptProject(projects);
        if (project == null) {
            CLIView.printError("Project not found.");
            return null;
        }
    
        int enquiryId = CLIView.promptInt("Enter the enquiry ID: ");
        Enquiry enquiry = applicant.getEnquiries().stream()
                .filter(e -> e.getProject().equals(project) && e.getEnquiryId() == enquiryId)
                .findFirst()
                .orElse(null);
    
        if (enquiry == null) {
            CLIView.printError("Enquiry not found.");
        }
    
        return enquiry;
    }
    
}
