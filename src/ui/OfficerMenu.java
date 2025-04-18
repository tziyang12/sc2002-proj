package ui;

import controller.OfficerController;
import controller.EnquiryController;
import model.user.HDBOfficer;
import model.user.Applicant;
import model.project.Project;
import model.transaction.ApplicationStatus;
import model.transaction.Enquiry;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OfficerMenu {

    private OfficerController officerController;
    private EnquiryController enquiryController;
    private HDBOfficer currentOfficer;
    private List<Project> projectList;
    private List<Applicant> applicantList;

    public OfficerMenu(HDBOfficer officer, List<Project> projects, List<Applicant> applicants) {
        this.officerController = new OfficerController();
        this.enquiryController = new EnquiryController();
        this.currentOfficer = officer;
        this.projectList = projects;
        this.applicantList = applicants;
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
    
        while (true) {
            System.out.println("\n--- HDB Officer Main Menu ---");
            System.out.println("1. Applicant Options");
            System.out.println("2. Officer Options");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1 -> showApplicantMenu();
                case 2 -> showOfficerOptions(scanner);
                case 3 -> {
                    System.out.println("Exiting HDB Officer Menu.");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

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

    private void showOfficerOptions(Scanner scanner) {
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
                case 3 -> registerOfficerToProject(scanner);
                case 4 -> officerController.viewRegistrationStatus(currentOfficer);
                case 5 -> new EnquiryMenu(currentOfficer, currentOfficer.getAssignedProjects(), enquiryController).show();
                case 6 -> generateBookingReceipt();
                case 7 -> manageApplicantApplicationMenu(scanner, applicantList, currentOfficer);
                case 8 -> { return; } // Back to main menu
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void viewProjectsAvailableForOfficer() {
        System.out.println("\n--- Projects Available for Officer Registration ---");

        boolean found = false;
        String officerID = currentOfficer.getNric();  // Assuming this is inside an Officer subclass

        for (Project project : projectList) {
            // Skip if officer is already assigned
            boolean alreadyAssigned = project.getOfficers().stream()
                .anyMatch(officer -> officer.getNric().equals(officerID));
            if (alreadyAssigned) continue;

            // Skip if officer has already applied for the project as an applicant
            if (currentOfficer.getApplication() != null &&
                currentOfficer.getApplication().getProject() == project) {
                continue;
            }

            // Skip if dates overlap
            if (!OfficerController.canRegisterForProject(currentOfficer, project)) continue;

            // If passed both conditions, show project
            found = true;
            CLIView.printProject(project);
        }

        if (!found) {
            System.out.println("No projects available for officer registration at the moment.");
        }
    }
    
    private void registerOfficerToProject(Scanner scanner) {
        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine();
        // Retrieve the project object by name (You can customize this part based on your project structure)
        Project project = findProjectByName(projectName);

        if (project != null) {
            boolean isRegistered = officerController.registerOfficerToProject(currentOfficer, project);
            if (isRegistered) {
                System.out.println("Registration successful.");
            }
        } else {
            System.out.println("Project not found.");
        }
    }

    private void viewAssignedProject() {
        System.out.println("\n--- Your Assigned Project(s) ---");
        List<Project> assignedProjects = currentOfficer.getAssignedProjects();
    
        if (!assignedProjects.isEmpty()) {
            CLIView.printProjectTableHeader();
            for (Project project : assignedProjects) {
                CLIView.printProjectRow(project);
            }
            CLIView.printProjectTableFooter();
        } else {
            System.out.println("You are not currently registered to any project.");
        }
    }
    
    

    private void viewEnquiries() {
        List<Enquiry> enquiries = enquiryController.getEnquiriesForOfficer(currentOfficer);

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

    private void manageApplicantApplicationMenu(Scanner scanner, List<Applicant> allApplicants, HDBOfficer officer) {
        List<Project> assignedProjects = officer.getAssignedProjects();
        
        if (assignedProjects.isEmpty()) {
            System.out.println("You are not currently assigned to any projects.");
            return;
        }
        
        // Step 1: View assigned projects
        System.out.println("\n[Assigned Projects]");
        for (int i = 0; i < assignedProjects.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, assignedProjects.get(i).getProjectName());
        }

        // Step 2: Choose a project
        System.out.print("Select a project to manage applications (Enter number): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (choice < 1 || choice > assignedProjects.size()) {
            System.out.println("Invalid selection.");
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
            System.out.println("No successful applicants for this project.");
            return;
        }

        System.out.println("\n[Successful Applications]");
        for (Applicant applicant : successfulApplicants) {
            System.out.printf("NRIC: %s | Name: %s | Flat Type: %s%n",
                    applicant.getNric(), applicant.getName(), applicant.getApplication().getFlatType());
        }

        // Step 4: Select NRIC to change to BOOKED
        System.out.print("Enter NRIC of applicant to mark as BOOKED: ");
        String nric = scanner.nextLine();

        Applicant selectedApplicant = null;
        for (Applicant a : successfulApplicants) {
            if (a.getNric().equalsIgnoreCase(nric)) {
                selectedApplicant = a;
                break;
            }
        }

        if (selectedApplicant == null) {
            System.out.println("No matching successful applicant with that NRIC.");
            return;
        }

        // Step 5: Update status to BOOKED and decrease flat count
        boolean booked = officerController.changeApplicationStatusToBooked(selectedApplicant);
        if (booked) {
            officerController.generateBookingReceipt(selectedApplicant);
        }
    }

    private void generateBookingReceipt() {
        System.out.print("Enter applicant name to generate booking receipt: ");
        String applicantNRIC = new Scanner(System.in).nextLine();
        // Retrieve the applicant (you can retrieve by name or some identifier)
        Applicant applicant = findApplicantByNric(applicantNRIC);

        if (applicant != null) {
            officerController.generateBookingReceipt(applicant);
        } else {
            System.out.println("Applicant not found.");
        }
    }

    private Project findProjectByName(String name) {
        for (Project project : projectList) {
            if (project.getProjectName().equalsIgnoreCase(name)) {
                return project;
            }
        }
        return null;
    }

    private Applicant findApplicantByNric(String nric) {
        for (Applicant applicant : applicantList) {
            if (applicant.getNric().equalsIgnoreCase(nric)) {
                return applicant;
            }
        }
        return null;
    }
}
