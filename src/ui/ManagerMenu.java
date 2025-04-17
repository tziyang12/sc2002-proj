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
import model.transaction.Enquiry;
import model.transaction.OfficerProjectRegistration;
import model.transaction.OfficerRegistrationStatus;
import model.project.FlatType;

import java.time.LocalDate;
import java.util.ArrayList;
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
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== HDB Manager Menu ===");
            System.out.println("1. Create a New BTO Project");
            System.out.println("2. View All Projects");
            System.out.println("3. View My Projects");
            System.out.println("4. Edit Project");
            System.out.println("5. Delete Project");
            System.out.println("6. Toggle Project Visibility");
            System.out.println("7. View and Manage Officer Registrations");
            System.out.println("8. View and Manage Applicant Applications");
            System.out.println("9. View All Enquiries");
            System.out.println("10. View Managed Project Enquiries");
            System.out.println("11. Generate Report");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1 -> createProjectMenu(scanner);
                case 2 -> viewAllProjects();
                case 3 -> viewMyProjects();
                case 4 -> editProjectMenu(scanner);
                case 5 -> deleteProjectMenu(scanner);
                case 6 -> toggleProjectVisibilityMenu(scanner);
                case 7 -> manageOfficerRegistrationsMenu(scanner);
                case 8 -> manageApplicantApplicationsMenu(scanner);
                case 9 -> viewAllEnquiries();
                case 10 -> viewManagedEnquiries();
                case 11 -> generateReportMenu(scanner);
                case 0 -> {
                    exit = true;
                    System.out.println("Exiting...");
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void createProjectMenu(Scanner scanner) {
        // Prompt for new details
        System.out.print("Enter Project Name: ");
        String name = scanner.nextLine();
    
        System.out.print("Enter Neighborhood: ");
        String neighborhood = scanner.nextLine();
    
        System.out.print("Enter Number of 2-Room Flats: ");
        String newTwoRoomFlats = scanner.nextLine();
        int num2Room = ProjectService.parseInt(newTwoRoomFlats, 0); // Default to 0 if input is blank
    
        System.out.print("Enter Number of 3-Room Flats: ");
        String newThreeRoomFlats = scanner.nextLine();
        int num3Room = ProjectService.parseInt(newThreeRoomFlats, 0); // Default to 0 if input is blank
    
        System.out.print("Enter Application Opening Date (YYYY-MM-DD): ");
        String newOpeningDate = scanner.nextLine();
        LocalDate openDate = ProjectService.parseDate(newOpeningDate, LocalDate.now()); // Default to current date if blank
    
        System.out.print("Enter Application Closing Date (YYYY-MM-DD): ");
        String newClosingDate = scanner.nextLine();
        LocalDate closeDate = ProjectService.parseDate(newClosingDate, LocalDate.now().plusMonths(1)); // Default to 1 month from now if blank
    
        System.out.print("Enter Max Officer Slots: ");
        String newMaxOfficerSlots = scanner.nextLine();
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
            System.out.println("New project created successfully!");
        } else {
            System.out.println("Cannot create project due to overlapping dates with existing projects.");
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
    

    private void editProjectMenu(Scanner scanner) {
        System.out.print("Enter Project Name to Edit: ");
        String projectName = scanner.nextLine();

        Project projectToEdit = findProjectByName(projectName);
        if (projectToEdit == null) {
            System.out.println("Project not found!");
            return;
        }
        // Given that the project is found, we can proceed to edit it
        // Display current project details
        System.out.println("Current Project Details:");
        CLIView.printProjectTableHeader();
        CLIView.printProjectRow(projectToEdit);
        CLIView.printProjectTableFooter();

        System.out.println("Enter new details (leave blank to keep current values):");
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
        if (maxOfficerSlots < 0) {
            System.out.println("Max officer slots cannot be negative. Keeping current value.");
            maxOfficerSlots = projectToEdit.getMaxOfficerSlots();
        }
        if (maxOfficerSlots < projectToEdit.getOfficers().size()) {
            System.out.println("Max officer slots cannot be less than current assigned officers. Keeping current value.");
            maxOfficerSlots = projectToEdit.getMaxOfficerSlots();
        }
        if (maxOfficerSlots > 10) {
            System.out.println("Max officer slots cannot exceed 10. Value will be changed to 10.");
            maxOfficerSlots = 10;
        }

        // Enter Visibility (blank to keep current)
        String newVisibility = CLIView.prompt("Enter visibility (TRUE / FALSE): ");
        boolean visibility = ProjectService.parseBoolean(newVisibility, projectToEdit.isVisible());
        projectToEdit.setVisible(visibility);


        managerController.editProject(projectToEdit, newName, newNeighborhood, numTwoRoomFlats, numThreeRoomFlats,
                twoRoomPrice, threeRoomPrice, openingDate, closingDate, maxOfficerSlots, visibility);
        System.out.println("Project updated successfully!");
    }

    private void deleteProjectMenu(Scanner scanner) {
        // Display all projects
        viewAllProjects();
        System.out.print("Enter Project Name to Delete: ");
        String projectName = scanner.nextLine();

        Project projectToDelete = findProjectByName(projectName);
        if (projectToDelete == null) {
            System.out.println("Project not found!");
            return;
        }

        // Confirm deletion
        String confirm = CLIView.prompt("Are you sure you want to delete this project? (Y/N): ");
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Project deletion cancelled.");
            return;
        }
        
        managerController.deleteProject(manager, projectToDelete);
        System.out.println("Project deleted successfully!");
    }

    private void toggleProjectVisibilityMenu(Scanner scanner) {
        System.out.print("Enter Project Name to Toggle Visibility: ");
        String projectName = scanner.nextLine();

        Project projectToToggle = findProjectByName(projectName);
        if (projectToToggle == null) {
            System.out.println("Project not found!");
            return;
        }

        System.out.print("Enter visibility (true for visible, false for hidden): ");
        boolean visibility = scanner.nextBoolean();

        managerController.toggleVisibility(projectToToggle, visibility);
        System.out.println("Project visibility updated.");
    }

    private void manageOfficerRegistrationsMenu(Scanner scanner) {
        List<Project> managerProjects = manager.getManagedProjects();

        UserService userService = new UserService(allUsers);
        List<HDBOfficer> allOfficers = userService.getOfficersForManager(manager);

        if (managerProjects.isEmpty()) {
            System.out.println("You are not assigned to any projects.");
            return;
        }

        // Step 1: View officer registrations
        managerController.viewOfficerRegistrations(managerProjects, allOfficers);

        // Step 2: Choose a project
        System.out.println("\nSelect a project to manage officer registrations:");
        for (int i = 0; i < managerProjects.size(); i++) {
            System.out.println((i + 1) + ". " + managerProjects.get(i).getProjectName());
        }
        System.out.print("Enter choice (0 to return): ");
        int projectChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (projectChoice == 0 || projectChoice < 1 || projectChoice > managerProjects.size()) {
            return;
        }

        Project selectedProject = managerProjects.get(projectChoice - 1);

        // Step 3: Display officers with PENDING registrations for that project
        List<HDBOfficer> pendingOfficers = new ArrayList<>();
        for (HDBOfficer officer : allOfficers) {
            for (OfficerProjectRegistration reg : officer.getRegisteredProjects()) {
                if (reg.getProject().equals(selectedProject) &&
                    reg.getRegistrationStatus() == OfficerRegistrationStatus.PENDING) {
                    pendingOfficers.add(officer);
                }
            }
        }

        if (pendingOfficers.isEmpty()) {
            System.out.println("No pending officer registrations for this project.");
            return;
        }

        System.out.println("\nPending Officer Registrations:");
        for (int i = 0; i < pendingOfficers.size(); i++) {
            System.out.println((i + 1) + ". " + pendingOfficers.get(i).getName());
        }

        // Step 4: Select officer to approve/reject
        System.out.print("Select an officer to approve/reject (0 to return): ");
        int officerChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (officerChoice == 0 || officerChoice < 1 || officerChoice > pendingOfficers.size()) {
            return;
        }

        HDBOfficer selectedOfficer = pendingOfficers.get(officerChoice - 1);

        // Step 5: Approve or reject
        System.out.print("Approve or Reject? (A/R): ");
        String decision = scanner.nextLine().trim().toUpperCase();

        if (decision.equals("A")) {
            if (selectedProject.getAvailableOfficerSlots() > 0) {
                managerController.approveOfficer(selectedProject, selectedOfficer);
                System.out.println("Officer approved and assigned.");
            } else {
                System.out.println("No available officer slots in this project.");
            }
        } else if (decision.equals("R")) {
            managerController.rejectOfficer(selectedProject, selectedOfficer);
            System.out.println("Officer registration rejected.");
        } else {
            System.out.println("Invalid input.");
        }
    }


    private void manageApplicantApplicationsMenu(Scanner scanner) {
        List<Application> applications = managerController.getApplicationsForManagedProjects(manager);
    
        if (applications.isEmpty()) {
            System.out.println("No applications found for your managed projects.");
            return;
        }
    
        System.out.println("\n=== Applications for Your Projects ===");
        for (int i = 0; i < applications.size(); i++) {
            Application app = applications.get(i);
            System.out.printf("%d. Applicant: %s | Project: %s | FlatType: %s | Status: %s | Withdrawal Requested: %b | Applied on: %s%n",
                    i + 1,
                    app.getApplicant().getName(),
                    app.getProject().getProjectName(),
                    app.getFlatType(),
                    app.getStatus(),
                    app.isWithdrawalRequested(),
                    app.getApplicationDate()
            );
        }
    
        System.out.print("\nEnter application number to manage (0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
    
        if (choice <= 0 || choice > applications.size()) {
            System.out.println("Cancelled or invalid input.");
            return;
        }
    
        Application selected = applications.get(choice - 1);
    
        System.out.println("\nSelected Application:");
        System.out.println("Applicant: " + selected.getApplicant().getName());
        System.out.println("Project: " + selected.getProject().getProjectName());
        System.out.println("Flat Type: " + selected.getFlatType());
        System.out.println("Status: " + selected.getStatus());
        System.out.println("Withdrawal Requested: " + selected.isWithdrawalRequested());
    
        System.out.println("\nChoose an action:");
        System.out.println("1. Approve Application");
        System.out.println("2. Reject Application");
        if (selected.isWithdrawalRequested()) {
            System.out.println("3. Approve Withdrawal");
            System.out.println("4. Reject Withdrawal");
        }
        System.out.print("Enter your choice: ");
        int action = scanner.nextInt();
        scanner.nextLine(); // consume newline
    
        switch (action) {
            case 1 -> {
                managerController.approveApplication(selected);
                System.out.println("Application approved.");
            }
            case 2 -> {
                managerController.rejectApplication(selected);
                System.out.println("Application rejected.");
            }
            case 3 -> {
                if (selected.isWithdrawalRequested()) {
                    managerController.approveWithdrawal(selected);
                    System.out.println("Withdrawal approved.");
                } else {
                    System.out.println("No withdrawal was requested.");
                }
            }
            case 4 -> {
                if (selected.isWithdrawalRequested()) {
                    managerController.rejectWithdrawal(selected);
                    System.out.println("Withdrawal rejected.");
                } else {
                    System.out.println("No withdrawal was requested.");
                }
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void viewAllEnquiries() {
        List<Enquiry> enquiries = enquiryController.getAllEnquiries(allProjects);

        if (enquiries.isEmpty()) {
            System.out.println("No enquiries available.");
            return;
        }

        System.out.println("\n--- Your Enquiries ---");
        CLIView.printEnquiryTableHeader();

        for (Enquiry enquiry : enquiries) {
            String projectName = enquiry.getProject() != null 
                ? enquiry.getProject().getProjectName() 
                : "N/A";

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
        List<Enquiry> enquiries = enquiryController.getAllEnquiries(manager.getManagedProjects());

        if (enquiries.isEmpty()) {
            System.out.println("No enquiries available.");
            return;
        }

        System.out.println("\n--- Your Enquiries ---");
        CLIView.printEnquiryTableHeader();

        for (Enquiry enquiry : enquiries) {
            String projectName = enquiry.getProject() != null 
                ? enquiry.getProject().getProjectName() 
                : "N/A";

            CLIView.printEnquiryRow(
                projectName,
                enquiry.getEnquiryId(),
                enquiry.getEnquiryMessage(),
                enquiry.getReplyMessage()
            );
        }

        CLIView.printEnquiryTableFooter();
    }
    

    private void generateReportMenu(Scanner scanner) {
        System.out.println("Generate report based on:");
        System.out.println("1. Marital Status");
        System.out.println("2. Flat Type");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        if (choice == 1) {
            System.out.print("Enter Marital Status (e.g., Single, Married): ");
            String maritalStatus = scanner.nextLine().trim();
            managerController.generateApplicantReport(manager, "maritalStatus", maritalStatus);
        } else if (choice == 2) {
            System.out.print("Enter Flat Type (TWO_ROOM, THREE_ROOM): ");
            String flatTypeStr = scanner.nextLine().trim().toUpperCase();
            try {
                FlatType flatType = FlatType.valueOf(flatTypeStr);
                managerController.generateApplicantReport(manager, "flatType", flatType.name());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Flat Type entered.");
            }
        } else {
            System.out.println("Invalid choice.");
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
