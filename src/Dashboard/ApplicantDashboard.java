package Dashboard;  // Ensure proper package name

import Roles.Applicant;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ProjectManagement.BTOProject;
import ProjectManagement.FlatType;
import ProjectManagement.ProjectDatabase;

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
                    	List<BTOProject> projects = ProjectDatabase.getProjects();
                        for (BTOProject project : projects) {
                            if (project.isVisible()) {
                                System.out.println("Project Name: " + project.getProjectName() + ", Available Units: ");
                                Map<FlatType, Integer> flatUnits = project.getFlatUnits();  // Get all available flat types and counts
                                for (Map.Entry<FlatType, Integer> entry : flatUnits.entrySet()) {
                                    System.out.println(entry.getKey() + ": " + entry.getValue() + " units");
                                }
                            }

                        }
                        break;
                    case 2:
                        System.out.print("Enter Project Name: ");
                        String projectName = sc.nextLine();
                        BTOProject projectToApply = ProjectDatabase.findProjectByName(projectName);
                        if (projectToApply != null && projectToApply.isVisible()) {
                            System.out.println("Available flat types: ");
                            Map<FlatType, Integer> availableFlats = projectToApply.getFlatUnits();
                            int i = 1;
                            for (Map.Entry<FlatType, Integer> entry : availableFlats.entrySet()) {
                                if (entry.getValue() > 0) {
                                    System.out.println(i + ". " + entry.getKey() + " (" + entry.getValue() + " units)");
                                    i++;
                                }
                            }

                            // Prompt user to select a flat type
                            System.out.print("Select flat type by number: ");
                            int flatTypeChoice = sc.nextInt();
                            sc.nextLine(); // Consume newline

                            // Find the selected flat type based on user input
                            FlatType selectedFlatType = null;
                            int index = 1;
                            for (Map.Entry<FlatType, Integer> entry : availableFlats.entrySet()) {
                                if (entry.getValue() > 0 && flatTypeChoice == index) {
                                    selectedFlatType = entry.getKey();
                                    break;
                                }
                                index++;
                            }

                            if (selectedFlatType != null) {
                                // Call applyProject with the selected flat type
                                applicant.applyProject(projectToApply, selectedFlatType);
                            } else {
                                System.out.println("Invalid selection. Try again.");
                            }
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



