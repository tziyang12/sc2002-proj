package ui;

import controller.ManagerController;
import model.project.Project;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.transaction.Application;
import model.transaction.Enquiry;
import model.project.FlatType;
import model.transaction.OfficerRegistrationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ManagerMenu {

    private HDBManager manager;
    private ManagerController managerController;
    private List<Project> allProjects;

    public ManagerMenu(HDBManager manager, List<Project> allProjects) {
        this.manager = manager;
        this.managerController = new ManagerController();
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
            System.out.println("9. View Enquiries");
            System.out.println("10. Generate Report");
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
                case 9 -> viewEnquiries();
                case 10 -> generateReportMenu(scanner);
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

        Project newProject = new Project(name, neighborhood, openDate, closeDate, manager.getNric(), maxOfficerSlots);
        newProject.addFlatUnit(FlatType.TWO_ROOM, num2Room);
        newProject.addFlatUnit(FlatType.THREE_ROOM, num3Room);
        newProject.setVisible(true);  // Set project to visible by default

        if (managerController.canCreateNewProject(manager, newProject)) {
            managerController.createProject(manager, newProject);
            System.out.println("New project created successfully!");
        } else {
            System.out.println("Cannot create project due to overlapping dates with existing projects.");
        }
    }

    private void viewAllProjects() {
        System.out.println("\n=== All Projects ===");
        for (Project project : allProjects) {
            System.out.println(project);
        }
    }

    private void viewMyProjects() {
        // List<Project> myProjects = managerController.viewMyProjects(manager.getManagedProjects());
        // System.out.println("\n=== My Projects ===");
        // for (Project project : myProjects) {
        //     System.out.println(project);
        // }
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
        // Implement applicant application approval/rejection logic
    }

    private void viewEnquiries() {
        // List<Enquiry> enquiries = managerController.getAllEnquiries(manager.getManagedProjects());
        // System.out.println("\n=== All Enquiries ===");
        // for (Enquiry enquiry : enquiries) {
        //     System.out.println(enquiry);
        // }
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
            if (project.getProjectID() == projectId) {
                return project;
            }
        }
        return null;
    }
}
