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
import model.project.FlatType;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ManagerMenu {
    private HDBManager manager;
    private ManagerController managerController;
    private EnquiryController enquiryController;
    private List<Project> allProjects;
    private List<User> allUsers;

    public ManagerMenu(HDBManager manager, List<Project> allProjects, List<User> allUsers) {
        this.manager = manager;
        this.managerController = new ManagerController();
        this.enquiryController = new EnquiryController();
        this.allProjects = allProjects;
        this.allUsers = allUsers;
    }

    public void showMenu() {
        String[] menuOptions = {
                "Create a New BTO Project",
                "View All Projects",
                "View My Projects",
                "Edit Project",
                "Delete Project",
                "Toggle Project Visibility",
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
                case 1 -> createProjectMenu();
                case 2 -> viewAllProjects();
                case 3 -> viewMyProjects();
                case 4 -> editProjectMenu();
                case 5 -> deleteProjectMenu();
                case 6 -> toggleProjectVisibilityMenu();
                case 7 -> manageOfficerRegistrationsMenu(scanner);
                case 8 -> manageApplicantApplicationsMenu(scanner);
                case 9 -> new EnquiryMenu(manager, manager.getManagedProjects(), enquiryController).show();
                case 10 -> generateReportMenu(scanner);
                case 0 -> {
                    exit = true;
                    CLIView.printMessage("Exiting Manager Menu...");
                }
                default -> CLIView.printError("Invalid option. Please try again.");
            }
        }
    }

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
            CLIView.printError("Invalid number of 2-room flats. Defaulted to 0.");
        }

        String newOpeningDate = CLIView.prompt("Enter Application Opening Date (YYYY-MM-DD): ");
        LocalDate openDate = ProjectService.parseDate(newOpeningDate, LocalDate.now()); // Default to current date if blank
        
        String newClosingDate = CLIView.prompt("Enter Application Closing Date (YYYY-MM-DD): ");
        LocalDate closeDate = ProjectService.parseDate(newClosingDate, LocalDate.now().plusMonths(1)); // Default to 1 month from now if blank
    
        String newMaxOfficerSlots = CLIView.prompt("Enter Max Officer Slots: ");
        int maxOfficerSlots = ProjectService.parseInt(newMaxOfficerSlots, 1); // Default to 1 if input is blank
    
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

    private void viewAllProjects() {
        CLIView.printProjectTableHeader();
        for (Project project : allProjects) {
            CLIView.printProjectRow(project);
        }
        CLIView.printProjectTableFooter();
    }
    
    private void viewMyProjects() {
        List<Project> myProjects = managerController.getManagedProjects(manager);
        CLIView.printProjectTableHeader();
        for (Project project : myProjects) {
            CLIView.printProjectRow(project);
        }
        CLIView.printProjectTableFooter();
    }

    private void editProjectMenu() {
        String projectName = CLIView.prompt("Enter Project Name to Edit: ");

        Project projectToEdit = findProjectByName(projectName);
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

        // Enter Visibility (blank to keep current)
        String newVisibility = CLIView.prompt("Enter visibility (TRUE / FALSE): ");
        boolean visibility = ProjectService.parseBoolean(newVisibility, projectToEdit.isVisible());
        projectToEdit.setVisible(visibility);


        managerController.editProject(projectToEdit, newName, newNeighborhood, numTwoRoomFlats, numThreeRoomFlats,
                twoRoomPrice, threeRoomPrice, openingDate, closingDate, maxOfficerSlots, visibility);
        CLIView.printMessage("Project updated successfully!");
    }

    private void deleteProjectMenu() {
        // Display all projects
        viewAllProjects();
        String projectName = CLIView.prompt("Enter Project Name to Delete: ");

        Project projectToDelete = findProjectByName(projectName);
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

    private void toggleProjectVisibilityMenu() {
        String projectName = CLIView.prompt("Enter Project Name to Toggle Visibility: ");

        Project projectToToggle = findProjectByName(projectName);
        if (projectToToggle == null) {
            CLIView.printError("Project not found!");
            return;
        }

        boolean visibility = CLIView.promptYesNo(projectName + " is currently " + (projectToToggle.isVisible() ? "visible" : "hidden") +
                ". Do you want to toggle visibility? ");

        managerController.toggleVisibility(projectToToggle, visibility);
        CLIView.printMessage("Project visibility toggled successfully!");
    }

    private void manageOfficerRegistrationsMenu(Scanner scanner) {
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

    private void manageApplicantApplicationsMenu(Scanner scanner) {
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

        int choice = CLIView.promptInt("\\n" + //
                        "Enter application number to manage (0 to cancel): ");
        scanner.nextLine(); // consume newline
    
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
        CLIView.printHeader("Choose Action");
        System.out.println("1. Approve Application");
        System.out.println("2. Reject Application");
        if (selected.isWithdrawalRequested()) {
            System.out.println("3. Approve Withdrawal");
            System.out.println("4. Reject Withdrawal");
        }
        int action = CLIView.promptInt("");
    
        switch (action) {
            case 1 -> {
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
            }
            case 4 -> {
                managerController.rejectWithdrawal(selected);
                CLIView.printMessage("Withdrawal rejected.");
            }
            default -> CLIView.printError("Invalid choice.");
        }
    }

    private void generateReportMenu(Scanner scanner) {
        String[] reportOptions = {
                "Generate Report by Marital Status",
                "Generate Report by Flat Type"
        };
        CLIView.printHeader("Generate Report");
        CLIView.printMenu(reportOptions);
        int choice = CLIView.promptInt("");
        scanner.nextLine(); // Consume the newline

        switch (choice) {
            case 1 -> {
                String maritalStatus = CLIView.prompt("Enter Marital Status (e.g., Single, Married): ");
                managerController.generateApplicantReport(manager, "maritalStatus", maritalStatus);
            }
            case 2 -> {
                String flatTypeStr = CLIView.prompt("Enter Flat Type (TWO_ROOM, THREE_ROOM): ").trim().toUpperCase();
                try {
                    FlatType flatType = FlatType.valueOf(flatTypeStr);
                    managerController.generateApplicantReport(manager, "flatType", flatType.name());
                } catch (IllegalArgumentException e) {
                    CLIView.printError("Invalid Flat Type entered.");
                }
            }
            default -> CLIView.printError("Invalid choice.");
        }
    }

    private Project findProjectByName(String projectName) {
        for (Project project : manager.getManagedProjects()) {
            if (project.getProjectName().equals(projectName)) {
                return project;
            }
        }
        return null;
    }
}
