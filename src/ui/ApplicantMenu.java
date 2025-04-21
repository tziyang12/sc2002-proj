package ui;

import controller.ApplicantController;
import model.project.FlatType;
import model.project.Project;
import model.project.ProjectSearchCriteria;
import model.transaction.Enquiry;
import model.user.Applicant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Represents the menu for an applicant to interact with BTO projects and manage their applications and enquiries.
 * The class provides methods for displaying project lists, applying for projects, managing enquiries, and changing filter settings.
 * It acts as the main interface for the applicant within the system.
 */
public class ApplicantMenu {
    private final ApplicantController applicantController = new ApplicantController();

    
    /** 
     * @param applicant
     * @param projects
     */
    public void show(Applicant applicant, List<Project> projects) {
        String[] menuOptions = {
                "View Eligible Projects",
                "Change Project Filter Settings",
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
                case 2 -> changeProjectFilterSettings(applicant, projects);
                case 3 -> handleApply(applicant, projects);
                case 4 -> applicantController.withdrawApplication(applicant);
                case 5 -> applicantController.viewApplicationStatus(applicant);
                case 6 -> createEnquiry(applicant, projects);
                case 7 -> manageEnquiries(applicant, projects);
                case 8 -> {
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

    private void changeProjectFilterSettings(Applicant applicant, List<Project> projects) {
        ProjectSearchCriteria criteria = applicant.getSearchCriteria();

        while (true) {
            printCurrentFilterSettings(criteria);
            String[] options = {
                "Change Neighbourhood",
                "Change Flat Types",
                "Toggle Price Sorting Order",
                "Reset Filters to Default",
                "Back to Main Menu"
            };

            CLIView.printMenu(options);
            int choice = CLIView.promptInt("");

            switch (choice) {
                case 1 -> changeNeighbourhood(criteria, projects);
                case 2 -> changeFlatTypes(criteria);
                case 3 -> togglePriceSorting(criteria);
                case 4 -> resetFilters(criteria);
                case 5 -> {
                    CLIView.printMessage("Returning to main menu...");
                    return;
                }
                default -> CLIView.printError("Invalid choice. Please try again.");
            }
        }
    }

    private void printCurrentFilterSettings(ProjectSearchCriteria criteria) {
        CLIView.printMessage("--- Current Filter Settings ---");
        String neighbourhood = criteria.getNeighbourhood().isEmpty() ? "All" : criteria.getNeighbourhood();
        CLIView.printMessage("Neighbourhood: " + neighbourhood);

        Set<FlatType> flatTypes = criteria.getFlatTypes();
        String flatTypeStr = flatTypes.isEmpty()
                ? "None"
                : flatTypes.stream().map(Enum::name).sorted().reduce((a, b) -> a + ", " + b).orElse("None");
        CLIView.printMessage("Flat Types: " + flatTypeStr);

        CLIView.printMessage("Sort by Price: " + (criteria.isSortByPriceAscending() ? "Ascending" : "Descending"));
        CLIView.printMessage("------------------------------");
    }

    private void changeNeighbourhood(ProjectSearchCriteria criteria, List<Project> projects) {
        List<String> neighbourhoods = projects.stream()
                .map(Project::getNeighbourhood)
                .distinct()
                .sorted()
                .toList();

        CLIView.printMessage("Available Neighbourhoods:");
        for (int i = 0; i < neighbourhoods.size(); i++) {
            CLIView.printMessage((i + 1) + ". " + neighbourhoods.get(i));
        }
        CLIView.printMessage("Select a neighbourhood (0 for All):");
        int selected = CLIView.promptInt("Choose a neighbourhood: ");
        if (selected == 0) {
            criteria.setNeighbourhood("");
        } else if (selected >= 1 && selected <= neighbourhoods.size()) {
            criteria.setNeighbourhood(neighbourhoods.get(selected - 1));
        } else {
            CLIView.printError("Invalid selection. Neighbourhood unchanged.");
        }
    }

    private void changeFlatTypes(ProjectSearchCriteria criteria) {
        Set<FlatType> selectedTypes = new HashSet<>();
        if (CLIView.promptYesNo("Include TWO_ROOM? ")) {
            selectedTypes.add(FlatType.TWO_ROOM);
        }
        if (CLIView.promptYesNo("Include THREE_ROOM? ")) {
            selectedTypes.add(FlatType.THREE_ROOM);
        }
        criteria.setFlatTypes(selectedTypes);
    }

    private void togglePriceSorting(ProjectSearchCriteria criteria) {
        criteria.setSortByPriceAscending(!criteria.isSortByPriceAscending());
    }

    private void resetFilters(ProjectSearchCriteria criteria) {
        criteria.setNeighbourhood("");
        criteria.setFlatTypes(new HashSet<>());
        criteria.setSortByPriceAscending(true);
        CLIView.printMessage("Filters reset to default.");
    }
    
    
}
