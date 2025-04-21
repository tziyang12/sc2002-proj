package ui;

import controller.OfficerController;
import controller.EnquiryController;
import model.user.HDBOfficer;
import model.user.Applicant;
import model.project.Project;
import model.transaction.ApplicationStatus;

import java.util.ArrayList;
import java.util.List;
/**
 * The OfficerMenu class handles all CLI-based interactions for HDB Officers,
 * including applicant-related features, project registration, managing enquiries,
 * generating booking receipts, and booking flats for successful applicants.
 */
public class OfficerMenu {

    private OfficerController officerController;
    private EnquiryController enquiryController;
    private HDBOfficer currentOfficer;
    private List<Project> projectList;
    private List<Applicant> applicantList;
    /**
     * Constructs an OfficerMenu with the given officer, projects, and applicants.
     *
     * @param officer    The currently logged-in HDB Officer.
     * @param projects   The list of all available BTO projects.
     * @param applicants The list of all applicants in the system.
     */
    public OfficerMenu(HDBOfficer officer, List<Project> projects, List<Applicant> applicants) {
        this.officerController = new OfficerController();
        this.enquiryController = new EnquiryController();
        this.currentOfficer = officer;
        this.projectList = projects;
        this.applicantList = applicants;
    }
    /**
     * Displays the main officer menu and handles user input for navigation.
     */
    public void showMenu() {
        String[] menuOptions = {
                "Applicant Options",
                "Officer Options",
                "Exit"
        };
    
        while (true) {
            CLIView.printHeader("HDB Officer Main Menu");
            CLIView.printMenu(menuOptions);
            int choice = CLIView.promptInt("");
    
            switch (choice) {
                case 1 -> showApplicantMenu();
                case 2 -> showOfficerOptions();
                case 3 -> {
                    CLIView.printMessage("Exiting officer menu...");
                    return;
                }
                default -> CLIView.printError("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Displays the applicant menu for the officer, allowing them to apply for new projects.
     * Projects the officer is already assigned to are excluded.
     */
    private void showApplicantMenu() {
        // Filter out projects the officer is already assigned to
        List<Project> filteredProjects = new ArrayList<>();
        for (Project project : projectList) {
            if (!currentOfficer.getAssignedProjects().contains(project)) {
                filteredProjects.add(project);
            }
        }

        new ApplicantMenu().show(currentOfficer, filteredProjects); // HDBOfficer is an Applicant
    }

    /**
     * Displays the menu for officer-specific actions and handles their navigation.
     */
    private void showOfficerOptions() {
        String[] menuOptions = {
                "View projects available for officer registration",
                "View assigned project",
                "Register to handle a project",
                "View project registration status",
                "View and Manage Enquiries",
                "Generate booking receipt",
                "View and Manage Applicant Application (Booking)",
                "Back"
        };

        while (true) {
            CLIView.printHeader("HDB Officer Menu");
            CLIView.printMenu(menuOptions);
            int choice = CLIView.promptInt("");
    
            switch (choice) {
                case 1 -> viewProjectsAvailableForOfficer();
                case 2 -> viewAssignedProject();
                case 3 -> registerOfficerToProject();
                case 4 -> officerController.viewRegistrationStatus(currentOfficer);
                case 5 -> new EnquiryMenu(currentOfficer, currentOfficer.getAssignedProjects(), enquiryController).show();
                case 6 -> generateBookingReceipt();
                case 7 -> manageApplicantApplicationMenu(applicantList, currentOfficer);
                case 8 -> { return; } // Back to main menu
                default -> CLIView.printError("Invalid option. Please try again.");
            }
        }
    }
    /**
     * Displays a list of BTO projects the officer is eligible to register for.
     */
    private void viewProjectsAvailableForOfficer() {
        CLIView.printHeader("Available Projects for Officer Registration");

        boolean found = false;
        String officerID = currentOfficer.getNric();  // Assuming this is inside an Officer subclass

        for (Project project : projectList) {
            // Combine all conditions into a single if statement
            boolean alreadyAssigned = project.getOfficers().stream()
                .anyMatch(officer -> officer.getNric().equals(officerID));
            boolean hasApplied = currentOfficer.getApplication() != null &&
                currentOfficer.getApplication().getProject() == project;
            boolean datesOverlap = !OfficerController.canRegisterForProject(currentOfficer, project);

            if (alreadyAssigned || hasApplied || datesOverlap) {
                continue;
            }

            // If passed all conditions, show project
            found = true;
            CLIView.printProject(project);
        }

        if (!found) {
            CLIView.printError("No projects available for registration.");
        }
    }

    /**
     * Prompts the officer to input a project name and attempts to register them for it.
     */
    private void registerOfficerToProject() {
        String projectName = CLIView.prompt("Enter project name: ");
        // Retrieve the project object by name (You can customize this part based on your project structure)
        Project project = findProjectByName(projectName);

        if (project != null) {
            boolean isRegistered = officerController.registerOfficerToProject(currentOfficer, project);
            if (isRegistered) {
                CLIView.printMessage("Registration successful.");
            }
        } else {
            CLIView.printError("Project not found.");
        }
    }

    /**
     * Displays all projects the officer is currently assigned to.
     */
    private void viewAssignedProject() {
        CLIView.printHeader("Assigned Projects");
        List<Project> assignedProjects = currentOfficer.getAssignedProjects();
    
        if (!assignedProjects.isEmpty()) {
            CLIView.printProjectTableHeader();
            for (Project project : assignedProjects) {
                CLIView.printProjectRow(project);
            }
            CLIView.printProjectTableFooter();
        } else {
            CLIView.printError("You are not currently assigned to any projects.");
        }
    }

    /**
     * Displays successful applications for a selected project and allows the officer
     * to book a flat for an applicant and generate a receipt.
     *
     * @param allApplicants List of all applicants in the system.
     * @param officer       The current HDB officer.
     */
    private void manageApplicantApplicationMenu(List<Applicant> allApplicants, HDBOfficer officer) {
        List<Project> assignedProjects = officer.getAssignedProjects();
        
        if (assignedProjects.isEmpty()) {
            CLIView.printError("No projects assigned to you.");
            return;
        }
        
        // Step 1: View assigned projects
        CLIView.printHeader("Assigned Projects");
        for (int i = 0; i < assignedProjects.size(); i++) {
            CLIView.printMessage("Project " + (i + 1) + ": " + assignedProjects.get(i).getProjectName());
        }

        // Step 2: Choose a project
        int choice = CLIView.promptInt("Select a project to manage applications (Enter number): ");

        if (choice < 1 || choice > assignedProjects.size()) {
            CLIView.printError("Invalid choice.");
            return;
        }

        Project selectedProject = assignedProjects.get(choice - 1);

        // Step 3: Display SUCCESSFUL applications
        List<Applicant> successfulApplicants = new ArrayList<>();
        for (Applicant applicant : allApplicants) {
            if (applicant.getApplication() != null && 
                    applicant.getApplication().getProject() == selectedProject && 
                    applicant.getApplication().getStatus() == ApplicationStatus.SUCCESSFUL) {
                    successfulApplicants.add(applicant);
                }
        }

        if (successfulApplicants.isEmpty()) {
            CLIView.printError("No successful applications for this project.");
            return;
        }

        CLIView.printHeader("Successful Applications for " + selectedProject.getProjectName());
        for (Applicant applicant : successfulApplicants) {
            CLIView.printApplicantApplicationRow(applicant);
        }

        // Step 4: Select NRIC to change to BOOKED
        String nric = CLIView.prompt("Enter NRIC of applicant to mark as BOOKED: ");

        Applicant selectedApplicant = null;
        for (Applicant a : successfulApplicants) {
            if (a.getNric().equalsIgnoreCase(nric)) {
                selectedApplicant = a;
                break;
            }
        }

        if (selectedApplicant == null) {
            CLIView.printError("No matching successful applicant with that NRIC.");
            return;
        }

        // Step 5: Update status to BOOKED and decrease flat count
        boolean booked = officerController.changeApplicationStatusToBooked(selectedApplicant);
        if (booked) {
            officerController.generateBookingReceipt(selectedApplicant);
        }
    }

    /**
     * Prompts the officer to input an applicant NRIC and generates a booking receipt for them.
     */
    private void generateBookingReceipt() {
        String applicantNRIC = CLIView.prompt("Enter Applicant's NRIC: ");
        // Retrieve the applicant (you can retrieve by name or some identifier)
        Applicant applicant = findApplicantByNric(applicantNRIC);

        if (applicant != null) {
            officerController.generateBookingReceipt(applicant);
        } else {
            CLIView.printError("Applicant not found.");
        }
    }

    /**
     * Finds a project by its name from the project list.
     *
     * @param name The project name to search for.
     * @return The Project object, or null if not found.
     */
    private Project findProjectByName(String name) {
        for (Project project : projectList) {
            if (project.getProjectName().equalsIgnoreCase(name)) {
                return project;
            }
        }
        return null;
    }

    /**
     * Finds an applicant by their NRIC from the applicant list.
     *
     * @param nric The NRIC of the applicant.
     * @return The Applicant object, or null if not found.
     */
    private Applicant findApplicantByNric(String nric) {
        for (Applicant applicant : applicantList) {
            if (applicant.getNric().equalsIgnoreCase(nric)) {
                return applicant;
            }
        }
        return null;
    }
}
