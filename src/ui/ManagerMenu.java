package ui;

import controller.ManagerController;
import controller.EnquiryController;
import model.project.Project;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.User;
import service.UserService;
import service.ProjectService;
import model.transaction.Application;
import model.transaction.ApplicationStatus;
import model.project.FlatType;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
/**
 * Represents the CLI menu interface for HDB Managers to manage BTO projects, officer registrations,
 * applicant applications, enquiries, and reports.
 * <p>
 * This class handles user interaction logic and delegates business logic to {@link ManagerController},
 * {@link EnquiryController}, and related services.
 * </p>
 */
public class ManagerMenu {
    private HDBManager manager;
    private ManagerController managerController;
    private EnquiryController enquiryController;
    private List<Project> allProjects;
    private List<User> allUsers;
    /**
     * Constructs the manager menu interface.
     *
     * @param manager     The currently logged-in HDBManager.
     * @param allProjects The list of all BTO projects in the system.
     * @param allUsers    The list of all users in the system.
     */
    public ManagerMenu(HDBManager manager, List<Project> allProjects, List<User> allUsers) {
        this.manager = manager;
        this.managerController = new ManagerController();
        this.enquiryController = new EnquiryController();
        this.allProjects = allProjects;
        this.allUsers = allUsers;
    }
    /**
     * Displays the main menu for HDB Managers and handles menu navigation based on user input.
     */
    public void showMenu() {
        String[] menuOptions = {
            "View and Manage Projects",
            "View and Manage Officer Registrations",
            "View and Manage Applicant Applications",
            "View and Manage Enquiries",
            "Generate Report",
            "Exit"
        };
        

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            CLIView.printHeader("HDB Manager Menu");
            CLIView.printMenu(menuOptions);
            int choice = CLIView.promptInt("");

            switch (choice) {
                case 1 -> viewAndManageProjectsMenu();
                case 2 -> manageOfficerRegistrationsMenu();
                case 3 -> manageApplicantApplicationsMenu();
                case 4 -> new EnquiryMenu(manager, manager.getManagedProjects(), enquiryController).show();
                case 5 -> generateReportMenu();
                case 6 -> {
                    exit = true;
                    CLIView.printMessage("Exiting Manager Menu...");
                }
                default -> CLIView.printError("Invalid option. Please try again.");
            }
        }
    }
    /**
     * Displays the submenu for viewing and managing BTO projects managed by the HDB Manager.
     */
    private void viewAndManageProjectsMenu() {
        String[] projectOptions = {
            "Create New BTO Project",
            "View All Projects",
            "View My Projects",
            "Edit Project",
            "Delete Project",
            "Toggle Project Visibility",
            "Back"
        };
    
        boolean back = false;
        while (!back) {
            CLIView.printHeader("View and Manage Projects");
            CLIView.printMenu(projectOptions);
            int projectChoice = CLIView.promptInt("");
    
            switch (projectChoice) {
                case 1 -> createProjectMenu();
                case 2 -> viewAllProjects();
                case 3 -> viewMyProjects();
                case 4 -> editProjectMenu();
                case 5 -> deleteProjectMenu();
                case 6 -> toggleProjectVisibilityMenu();
                case 7 -> back = true;
                default -> CLIView.printError("Invalid option. Please try again.");
            }
        }
    }
    /**
     * Handles creation of a new BTO project by prompting the manager for input.
     * Performs validation before delegating creation to the {@link ManagerController}.
     */
    private void createProjectMenu() {
        // Prompt for new details
        String name = CLIView.prompt("Enter Project Name: ");
        String neighborhood = CLIView.prompt("Enter Neighborhood: ");

        String newTwoRoomFlats = CLIView.prompt("Enter Number of 2-Room Flats: ");
        int num2Room = ProjectService.parseInt(newTwoRoomFlats, 0); // Default to 0 if input is blank
        if (!newTwoRoomFlats.isBlank() && num2Room == 0) {
            CLIView.printError("Invalid number of 2-room flats. Defaulted to 0.");
        }

        String newThreeRoomFlats = CLIView.prompt("Enter Number of 3-Room Flats: ");
        int num3Room = ProjectService.parseInt(newThreeRoomFlats, 0); // Default to 0 if input is blank
        if (!newTwoRoomFlats.isBlank() && num3Room == 0) {
            CLIView.printError("Invalid number of 3-room flats. Defaulted to 0.");
        }

        String newOpeningDate = CLIView.prompt("Enter Application Opening Date (YYYY-MM-DD): ");
        LocalDate openDate = ProjectService.parseDate(newOpeningDate, LocalDate.now()); // Default to current date if blank
        
        String newClosingDate = CLIView.prompt("Enter Application Closing Date (YYYY-MM-DD): ");
        LocalDate closeDate = ProjectService.parseDate(newClosingDate, LocalDate.now().plusMonths(1)); // Default to 1 month from now if blank
    
        String newMaxOfficerSlots = CLIView.prompt("Enter Max Officer Slots: ");
        int maxOfficerSlots = ProjectService.parseInt(newMaxOfficerSlots, 1); // Default to 1 if input is blank
        if (maxOfficerSlots < 1) {
            CLIView.printError("Max officers must be at least 1. Defaulting to 10.");
        }
        if (maxOfficerSlots > 10) {
            CLIView.printError("Max officers cannot exceed 10. Defaulting to 10.");
        }
        // Create the new project
        Project newProject = new Project(name, neighborhood, openDate, closeDate, maxOfficerSlots);
        newProject.addFlatUnit(FlatType.TWO_ROOM, num2Room);
        newProject.addFlatUnit(FlatType.THREE_ROOM, num3Room);
        newProject.setVisible(true);  // Set project to visible by default
        newProject.setManager(manager);  // Set the manager for the project
    
        // Check if the project can be created
        if (managerController.canCreateNewProject(manager, newProject)) {
            managerController.createProject(manager, newProject);
            CLIView.printMessage("Project created successfully!");
        } else {
            CLIView.printError("Cannot create project due to overlapping dates with existing projects.");
        }
    }

    /**
     * Displays a table of all BTO projects in the system.
     */
    private void viewAllProjects() {
        CLIView.printMessage("Today's Date: " + LocalDate.now());
        CLIView.printProjectTableHeader();
        for (Project project : allProjects) {
            CLIView.printProjectRow(project);
        }
        CLIView.printProjectTableFooter();
    }

    /**
     * Displays a table of BTO projects managed by the currently logged-in HDB Manager.
     */
    private void viewMyProjects() {
        List<Project> myProjects = managerController.getManagedProjects(manager);
        CLIView.printProjectTableHeader();
        for (Project project : myProjects) {
            CLIView.printProjectRow(project);
        }
        CLIView.printProjectTableFooter();
    }

    /**
     * Allows the manager to edit an existing project by modifying attributes such as
     * name, neighborhood, unit counts, prices, dates, and visibility.
     */
    private void editProjectMenu() {
        String projectName = CLIView.prompt("Enter Project Name to Edit: ");

        Project projectToEdit = managerController.findProjectByName(projectName, manager);
        if (projectToEdit == null) {
            CLIView.printError("Project not found!");
            return;
        }
        // Given that the project is found, we can proceed to edit it
        // Display current project details
        CLIView.printHeader("Current Project Details");
        CLIView.printProjectTableHeader();
        CLIView.printProjectRow(projectToEdit);
        CLIView.printProjectTableFooter();

        CLIView.printMessage("Enter new details (leave blank to keep current values):");
        // Prompt for new details
        String newName = CLIView.prompt("Enter project name: ");
        if (newName.isBlank()) {
            newName = projectToEdit.getProjectName();  // Keep old value if input is blank
        }

        // Enter Neighborhood (blank to keep current)
        String newNeighborhood = CLIView.prompt("Enter neighborhood: ");
        if (newNeighborhood.isBlank()) {
            newNeighborhood = projectToEdit.getNeighbourhood();  // Keep old value if input is blank
        }

        // Enter Number of 2-Room Flats (blank to keep current)
        String newTwoRoomFlats = CLIView.prompt("Enter number of 2-room flats: ");
        int numTwoRoomFlats = ProjectService.parseInt(newTwoRoomFlats, projectToEdit.getNumUnits(FlatType.TWO_ROOM));

        // Enter Number of 3-Room Flats (blank to keep current)
        String newThreeRoomFlats = CLIView.prompt("Enter number of 3-room flats: ");
        int numThreeRoomFlats = ProjectService.parseInt(newThreeRoomFlats, projectToEdit.getNumUnits(FlatType.THREE_ROOM));

        // Enter 2-Room Flat Price (blank to keep current)
        String newTwoRoomPrice = CLIView.prompt("Enter 2-room flat price: ");
        double twoRoomPrice = ProjectService.parseDouble(newTwoRoomPrice, projectToEdit.getFlatPrice(FlatType.TWO_ROOM));

        // Enter 3-Room Flat Price (blank to keep current)
        String newThreeRoomPrice = CLIView.prompt("Enter 3-room flat price: ");
        double threeRoomPrice = ProjectService.parseDouble(newThreeRoomPrice, projectToEdit.getFlatPrice(FlatType.THREE_ROOM));

        // Enter Application Opening Date (blank to keep current)
        String newOpeningDate = CLIView.prompt("Enter application opening date (YYYY-MM-DD): ");
        LocalDate openingDate = ProjectService.parseDate(newOpeningDate, projectToEdit.getApplicationStartDate());

        // Enter Application Closing Date (blank to keep current)
        String newClosingDate = CLIView.prompt("Enter application closing date (YYYY-MM-DD): ");
        LocalDate closingDate = ProjectService.parseDate(newClosingDate, projectToEdit.getApplicationEndDate());

        // Enter Max Officer Slots (blank to keep current)
        String newMaxOfficerSlots = CLIView.prompt("Enter max officer slots: ");
        int maxOfficerSlots = ProjectService.parseInt(newMaxOfficerSlots, projectToEdit.getMaxOfficerSlots());
        maxOfficerSlots = ProjectService.validateMaxOfficers(maxOfficerSlots, projectToEdit);
        if (maxOfficerSlots < 1) {
            CLIView.printError("Max officers must be at least 1. Defaulting to 10.");
        }
        if (maxOfficerSlots > 10) {
            CLIView.printError("Max officers cannot exceed 10. Defaulting to 10.");
        }
        if (maxOfficerSlots < projectToEdit.getOfficers().size()) {
            CLIView.printError("Max officers cannot be less than current assigned officers. Defaulting to " + projectToEdit.getOfficers().size());
        }
        // Enter Visibility (blank to keep current)
        String newVisibility = CLIView.prompt("Enter visibility (TRUE / FALSE): ");
        boolean visibility = ProjectService.parseBoolean(newVisibility, projectToEdit.isVisible());
        projectToEdit.setVisible(visibility);


        managerController.editProject(projectToEdit, newName, newNeighborhood, numTwoRoomFlats, numThreeRoomFlats,
                twoRoomPrice, threeRoomPrice, openingDate, closingDate, maxOfficerSlots, visibility);
        CLIView.printMessage("Project updated successfully!");
    }

    /**
     * Allows the manager to delete one of their managed projects after user confirmation.
     */
    private void deleteProjectMenu() {
        // Display all projects
        viewAllProjects();
        String projectName = CLIView.prompt("Enter Project Name to Delete: ");

        Project projectToDelete = managerController.findProjectByName(projectName, manager);
        if (projectToDelete == null) {
            CLIView.printError("Project not found!");
            return;
        }

        // Confirm deletion
        String confirm = CLIView.prompt("Are you sure you want to delete this project? (Y/N): ");
        if (!confirm.equalsIgnoreCase("Y")) {
            CLIView.printMessage("Project deletion cancelled.");
            return;
        }

        managerController.deleteProject(manager, projectToDelete);
        CLIView.printMessage("Project deleted successfully!");
    }

    /**
     * Toggles the visibility (public or hidden) of a selected BTO project managed by the HDB Manager.
     */
    private void toggleProjectVisibilityMenu() {
        String projectName = CLIView.prompt("Enter Project Name to Toggle Visibility: ");

        Project projectToToggle = managerController.findProjectByName(projectName, manager);
        if (projectToToggle == null) {
            CLIView.printError("Project not found!");
            return;
        }

        boolean visibility = CLIView.promptYesNo(projectName + " is currently " + (projectToToggle.isVisible() ? "visible" : "hidden") +
                ". Do you want to toggle visibility? ");

        managerController.toggleVisibility(projectToToggle, visibility);
        CLIView.printMessage("Project visibility toggled successfully!");
    }

    /**
     * Displays officer registration requests for the manager's projects and allows them to accept or reject pending officers.
     */
    private void manageOfficerRegistrationsMenu() {
        List<Project> managerProjects = manager.getManagedProjects();
        if (managerProjects.isEmpty()) {
            CLIView.printError("You are not managing any projects.");
            return;
        }

        UserService userService = new UserService(allUsers);
        List<HDBOfficer> allOfficers = userService.getOfficersForManager(manager);

        managerController.viewOfficerRegistrations(managerProjects, allOfficers);

        Project selectedProject = selectProject(managerProjects);
        if (selectedProject == null) return;

        List<HDBOfficer> pendingOfficers = managerController.getPendingOfficers(allOfficers, selectedProject);
        if (pendingOfficers.isEmpty()) {
            CLIView.printError("No pending officer registrations for this project.");
            return;
        }

        HDBOfficer selectedOfficer = selectOfficer(pendingOfficers);
        if (selectedOfficer == null) return;

        handleOfficerDecision(selectedProject, selectedOfficer);
    }

    /**
     * Prompts the HDB Manager to select a project from the list of projects they manage.
     *
     * @param managerProjects List of projects managed by the current manager.
     * @return The selected {@link Project}, or null if the user cancels or makes an invalid selection.
     */
    private Project selectProject(List<Project> managerProjects) {
        CLIView.printHeader("Select Project for Officer Registrations");
        for (int i = 0; i < managerProjects.size(); i++) {
            CLIView.printMessage((i + 1) + ": " + managerProjects.get(i).getProjectName());
        }
        int projectChoice = CLIView.promptInt("Enter choice (0 to return): ");

        if (projectChoice == 0 || projectChoice < 1 || projectChoice > managerProjects.size()) {
            return null;
        }
        return managerProjects.get(projectChoice - 1);
    }

    /**
     * Prompts the HDB Manager to select a pending officer from the provided list.
     *
     * @param pendingOfficers List of HDB Officers pending registration approval.
     * @return The selected {@link HDBOfficer}, or null if the user cancels or selects an invalid option.
     */
    private HDBOfficer selectOfficer(List<HDBOfficer> pendingOfficers) {
        CLIView.printHeader("Pending Officers for Registration");
        for (int i = 0; i < pendingOfficers.size(); i++) {
            CLIView.printMessage((i + 1) + ": " + pendingOfficers.get(i).getName());
        }

        int officerChoice = CLIView.promptInt("Select an officer to approve/reject (0 to return): ");

        if (officerChoice == 0 || officerChoice < 1 || officerChoice > pendingOfficers.size()) {
            return null;
        }
        return pendingOfficers.get(officerChoice - 1);
    }

    /**
     * Handles the decision logic for approving or rejecting an officer's registration to a project.
     * Displays appropriate messages and delegates to the {@link ManagerController} for logic execution.
     *
     * @param selectedProject The project for which the officer is being considered.
     * @param selectedOfficer The officer whose registration is being approved or rejected.
     */
    private void handleOfficerDecision(Project selectedProject, HDBOfficer selectedOfficer) {
        String decision = CLIView.prompt("Approve or Reject? (A/R): ").trim().toUpperCase();

        if (decision.equals("A")) {
            if (selectedProject.getAvailableOfficerSlots() > 0) {
                managerController.approveOfficer(selectedProject, selectedOfficer);
                CLIView.printMessage("Officer approved and assigned successfully.");
            } else {
                CLIView.printError("No available officer slots in this project.");
            }
        } else if (decision.equals("R")) {
            managerController.rejectOfficer(selectedProject, selectedOfficer);
            CLIView.printMessage("Officer registration rejected.");
        } else {
            CLIView.printError("Invalid input. Please enter 'A' to approve or 'R' to reject.");
        }
    }

    /**
     * Displays a menu listing all applications submitted to the manager's projects and
     * allows the manager to approve, reject, or handle withdrawal requests for an individual application.
     */
    private void manageApplicantApplicationsMenu() {
        List<Application> applications = managerController.getApplicationsForManagedProjects(manager);
    
        if (applications.isEmpty()) {
            CLIView.printError("No applications found for your managed projects.");
            return;
        }
        
        CLIView.printHeader("Applications for Your Projects");
        for (int i = 0; i < applications.size(); i++) {
            Application app = applications.get(i);
            CLIView.printFormatter(
            "%d. Applicant: %s | Project: %s | FlatType: %s | Status: %s | Withdrawal Requested: %b | Applied on: %s%n",
            i + 1,
            app.getApplicant().getName(),
            app.getProject().getProjectName(),
            app.getFlatType(),
            app.getStatus(),
            app.isWithdrawalRequested(),
            app.getApplicationDate()
        );
        }

        int choice = CLIView.promptInt("Enter application number to manage (0 to cancel): ");
    
        if (choice <= 0 || choice > applications.size()) {
            CLIView.printError("Invalid choice.");
            return;
        }
    
        Application selected = applications.get(choice - 1);
        
        CLIView.printHeader("Selected Application");
            CLIView.printFormatter(
            "Applicant: %s | Project: %s | FlatType: %s | Status: %s | Withdrawal Requested: %b | Applied on: %s%n",
            selected.getApplicant().getName(),
            selected.getProject().getProjectName(),
            selected.getFlatType(),
            selected.getStatus(),
            selected.isWithdrawalRequested(),
            selected.getApplicationDate()
        );

        // Step: Choose an action
        int action = -1;
        boolean valid = false;

        while (!valid) {
            CLIView.printHeader("Choose Action");
            if (selected.isWithdrawalRequested()) {
                System.out.println("1. Approve Withdrawal");
                System.out.println("2. Reject Withdrawal");
            } else if (selected.getStatus() == ApplicationStatus.PENDING){
                System.out.println("1. Approve Application");
                System.out.println("2. Reject Application");
            }
            else {
                CLIView.printError("User application status is already " + selected.getStatus() + ". Cannot approve or reject.");
                return;
            }

            action = CLIView.promptInt("Enter your choice: ");
            if (action == 1 && selected.isWithdrawalRequested()) action = 3;
            if (action == 2 && selected.isWithdrawalRequested()) action = 4;

            if (action == 1 || action == 2 || (selected.isWithdrawalRequested() && (action == 3 || action == 4))) {
                valid = true;
            } else {
                CLIView.printError("Invalid option. Please try again.");
            }
        }
    
        switch (action) {
            case 1 -> {
                if (selected.getProject().getNumUnits(selected.getFlatType()) == 0) {
                    CLIView.printError("No available flats of this type.");
                    return;
                }
                managerController.approveApplication(selected);
                CLIView.printMessage("Application approved.");
            }
            case 2 -> {
                managerController.rejectApplication(selected);
                CLIView.printMessage("Application rejected.");
            }
            case 3 -> {
                managerController.approveWithdrawal(selected);
                CLIView.printMessage("Withdrawal approved.");
                if (selected.getStatus() == ApplicationStatus.PENDING) CLIView.printMessage("Application has been deleted.");
                else CLIView.printMessage("Application status has been updated to UNSUCCESSFUL.");
            }
            case 4 -> {
                managerController.rejectWithdrawal(selected);
                CLIView.printMessage("Withdrawal rejected.");
            }
            default -> CLIView.printError("Invalid choice.");
        }
    }

    /**
     * Displays a menu that allows the HDB Manager to generate various filtered reports
     * on applications, such as by marital status, flat type, neighbourhood, age range, price, or no filter.
     * Delegates filtering logic to the {@link ManagerController} and prints the results.
     */
    private void generateReportMenu() {
        // Add the option for no filter and all the other filter categories
        String[] reportOptions = {
                "Generate Report by Marital Status",
                "Generate Report by Flat Type",
                "Generate Report by Neighbourhood",
                "Generate Report by Age Range",
                "Generate Report by Price",
                "Generate Report with No Filter"
        };
    
        CLIView.printHeader("Generate Report");
        CLIView.printMenu(reportOptions);
    
        int choice = CLIView.promptInt("");
        
        List<Application> filteredApplications;
    
        switch (choice) {
            case 1 -> {
                String maritalStatus = CLIView.prompt("Enter Marital Status (e.g., Single, Married): ");
                filteredApplications = managerController.generateApplicantReport(manager, "maritalStatus", maritalStatus);
            }
            case 2 -> {
                String flatTypeStr = CLIView.prompt("Enter Flat Type (TWO_ROOM, THREE_ROOM): ").trim().toUpperCase();
                try {
                    FlatType flatType = FlatType.valueOf(flatTypeStr);
                    filteredApplications = managerController.generateApplicantReport(manager, "flatType", flatType.name());
                } catch (IllegalArgumentException e) {
                    CLIView.printError("Invalid Flat Type entered.");
                    return;
                }
            }
            case 3 -> {
                String neighbourhood = CLIView.prompt("Enter Neighbourhood: ");
                filteredApplications = managerController.generateApplicantReport(manager, "neighbourhood", neighbourhood);
            }
            case 4 -> {
                String ageRange = CLIView.prompt("Enter Age Range (e.g., 25-40): ");
                filteredApplications = managerController.generateApplicantReport(manager, "age", ageRange);
            }
            case 5 -> {
                String sortByPrice = CLIView.prompt("Sort by Price (true for ascending, false for descending): ");
                filteredApplications = managerController.generateApplicantReport(manager, "price", sortByPrice);
            }
            case 6 -> {
                // No filter case
                filteredApplications = managerController.generateApplicantReport(manager, "none", "");
            }
            default -> {
                CLIView.printError("Invalid choice.");
                return;
            }
        }
    
         // Handle the filtered applications list (e.g., display them or further processing)
        if (filteredApplications.isEmpty()) {
            CLIView.printError("No applications found matching the criteria.");
        } else {
            // Print the table header
            CLIView.printHeader("Filtered Applications");
            
            CLIView.printFormatter("%-25s %-12s %-20s %-15s %-20s %-20s%n",
            "Applicant Name", "Age", "Marital Status", "Flat Type", "Project Name", "Neighbourhood");
            // Display the filtered applications in table format
            for (Application app : filteredApplications) {
                // Print additional application-specific data
                CLIView.printFormatter("%-25s %-12s %-20s %-15s %-20s %-20s%n", 
                app.getApplicant().getName(),  // Applicant name
                app.getApplicant().getAge(),   // Applicant Age
                app.getApplicant().getMaritalStatus(),   // Marital Status
                app.getFlatType(),             // Flat Type
                app.getProject().getProjectName(), // Project Name
                app.getProject().getNeighbourhood());
            }
            
            // Print the table footer
            CLIView.printProjectTableFooter();
        }
    }
}
