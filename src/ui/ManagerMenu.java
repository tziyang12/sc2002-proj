package ui;

import controller.ManagerController;
import controller.EnquiryController;
import model.project.Project;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.transaction.Application;
import model.transaction.Enquiry;
import model.project.FlatType;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ManagerMenu {

    private HDBManager manager;
    private ManagerController managerController;
    private EnquiryController enquiryController;
    private List<Project> allProjects;

    public ManagerMenu(HDBManager manager, List<Project> allProjects) {
        this.manager = manager;
        this.managerController = new ManagerController();
        this.enquiryController = new EnquiryController();
        this.allProjects = allProjects;
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
        System.out.print("Enter Project Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Neighborhood: ");
        String neighborhood = scanner.nextLine();

        System.out.print("Enter Number of 2-Room Flats: ");
        int num2Room = scanner.nextInt();

        System.out.print("Enter Number of 3-Room Flats: ");
        int num3Room = scanner.nextInt();

        System.out.print("Enter Application Opening Date (YYYY-MM-DD): ");
        LocalDate openDate = LocalDate.parse(scanner.next());

        System.out.print("Enter Application Closing Date (YYYY-MM-DD): ");
        LocalDate closeDate = LocalDate.parse(scanner.next());

        System.out.print("Enter Max Officer Slots: ");
        int maxOfficerSlots = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        Project newProject = new Project(name, neighborhood, openDate, closeDate, maxOfficerSlots);
        newProject.addFlatUnit(FlatType.TWO_ROOM, num2Room);
        newProject.addFlatUnit(FlatType.THREE_ROOM, num3Room);
        newProject.setVisible(true);  // Set project to visible by default
        newProject.setManager(manager);  // Set the manager for the project

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
        System.out.print("Enter Project ID to Edit: ");
        int projectId = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        Project projectToEdit = findProjectById(projectId);
        if (projectToEdit == null) {
            System.out.println("Project not found!");
            return;
        }

        System.out.print("Enter new Project Name: ");
        String newName = scanner.nextLine();

        System.out.print("Enter new Neighborhood: ");
        String newNeighborhood = scanner.nextLine();

        System.out.print("Enter new Number of 2-Room Flats: ");
        int new2Room = scanner.nextInt();

        System.out.print("Enter new Number of 3-Room Flats: ");
        int new3Room = scanner.nextInt();

        System.out.print("Enter new Application Opening Date (YYYY-MM-DD): ");
        LocalDate newOpenDate = LocalDate.parse(scanner.next());

        System.out.print("Enter new Application Closing Date (YYYY-MM-DD): ");
        LocalDate newCloseDate = LocalDate.parse(scanner.next());

        managerController.editProject(projectToEdit, newName, newNeighborhood, new2Room, new3Room, newOpenDate, newCloseDate);
        System.out.println("Project updated successfully!");
    }

    private void deleteProjectMenu(Scanner scanner) {
        System.out.print("Enter Project ID to Delete: ");
        int projectId = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        Project projectToDelete = findProjectById(projectId);
        if (projectToDelete == null) {
            System.out.println("Project not found!");
            return;
        }

        managerController.deleteProject(manager, projectToDelete, manager.getManagedProjects());
        System.out.println("Project deleted successfully!");
    }

    private void toggleProjectVisibilityMenu(Scanner scanner) {
        System.out.print("Enter Project ID to Toggle Visibility: ");
        int projectId = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        Project projectToToggle = findProjectById(projectId);
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
        // Implement officer registration approval/rejection logic
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
        // System.out.println("Generate report based on:");
        // System.out.println("1. Marital Status");
        // System.out.println("2. Flat Type");
        // System.out.print("Enter your choice: ");
        // int choice = scanner.nextInt();

        // if (choice == 1) {
        //     System.out.print("Enter Marital Status: ");
        //     String maritalStatus = scanner.next();
        //     List<String> report = managerController.generateApplicantReport("maritalStatus", maritalStatus);
        //     System.out.println(report);
        // } else if (choice == 2) {
        //     System.out.print("Enter Flat Type: ");
        //     String flatType = scanner.next();
        //     List<String> report = managerController.generateApplicantReport("flatType", flatType);
        //     System.out.println(report);
        // }
    }

    private Project findProjectById(int projectId) {
        for (Project project : manager.getManagedProjects()) {
            System.out.println("Project ID: " + project.getProjectID());
            if (project.getProjectID() == projectId) {
                return project;
            }
        }
        return null;
    }
}
