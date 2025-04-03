package Dashboard;  // Ensure proper package name

import Roles.Applicant;
import ProjectManagement.Project;
import java.util.List;
import java.util.Scanner;
import Database.ProjectDatabase;

public class ApplicantDashboard {
    public static void showDashboard(Applicant applicant, Scanner sc) {
        while (true) {
            try {
                System.out.println("\n--- Applicant Dashboard ---");
                System.out.println("1. View Projects");
                System.out.println("2. Apply for a Project");
                System.out.println("3. View Application Status");
                System.out.println("4. Withdraw Application");
                System.out.println("5. Manage Enquiries");
                System.out.println("6. Logout");
                System.out.print("Enter choice: ");

                if (!sc.hasNextInt()) {  // Validate input type
                    System.out.println("Invalid input. Please enter a number.");
                    sc.next();  // Clear invalid input
                    continue;
                }
                int choice = sc.nextInt();
                sc.nextLine(); // Consume the newline

                switch (choice) {
                    case 1:
                    	List<Project> projects = ProjectDatabase.getProjects();
                        for (Project project : projects) {
                            if (project.isVisible()) {
                                System.out.println("Project Name: " + project.getProjectName() + ", Flat Type: " + project.getFlatType());
                            }
                        }
                        break;
                    case 2:
                    	System.out.print("Enter Project Name: ");
                        String projectName = sc.nextLine();
                        Project project = ProjectDatabase.findProjectByName(projectName);
                        if (project != null && project.isVisible()) {
                            applicant.applyProject(project);
                        } else {
                            System.out.println("Project not found or not available for application.");
                        }
                        break;
                    case 3:
                        applicant.viewApplicationStatus();
                        break;
                    case 4:
                        applicant.withdrawalApplication(); // Corrected method name
                        break;
                    case 5:
                        EnquiryDashboard.manageEnquiry(applicant, sc);
                        break;
                    case 6:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                sc.nextLine(); // Reset scanner to avoid infinite loops
            }
        }
    }
}



