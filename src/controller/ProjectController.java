package controller;

import java.util.List;
import java.util.Map;

import model.project.FlatType;
import model.project.Project;
import model.transaction.Application;
import model.user.Applicant;

public class ProjectController {

    public void showEligibleProjects(Applicant applicant, List<Project> projects) {
        System.out.println("Available Projects (Eligible Only):");
        System.out.printf("%-20s %-20s %-10s %-10s%n", "Project Name", "Neighbourhood", "TWO_ROOM", "THREE_ROOM");
        System.out.println("----------------------------------------------------------------------");
    
        boolean hasEligible = false;
    
        for (Project project : projects) {
            if (!project.isVisible()) continue;
    
            String projectName = project.getProjectName();
            String neighbourhood = project.getNeighbourhood();
    
            String twoRoomDisplay = "NA";
            String threeRoomDisplay = "NA";
            
            // Check if applicant is eligible and if units exist
            if (project.getFlatUnits().containsKey(FlatType.TWO_ROOM)) {
                if (applicant.isEligible(project, FlatType.TWO_ROOM)) {
                    twoRoomDisplay = String.valueOf(project.getFlatUnits().get(FlatType.TWO_ROOM));
                }
            }
    
            if (project.getFlatUnits().containsKey(FlatType.THREE_ROOM)) {
                if (applicant.isEligible(project, FlatType.THREE_ROOM)) {
                    threeRoomDisplay = String.valueOf(project.getFlatUnits().get(FlatType.THREE_ROOM));
                }
            }
    
            // Skip if not eligible for either type
            if (twoRoomDisplay.equals("NA") && threeRoomDisplay.equals("NA")) continue;
    
            hasEligible = true;
            System.out.printf("%-20s %-20s %-10s %-10s%n", projectName, neighbourhood, twoRoomDisplay, threeRoomDisplay);
        }
    
        if (!hasEligible) {
            System.out.println("No eligible projects found at the moment.");
        }
    
        if (applicant.hasApplied()) {
            Application app = applicant.getApplication();
            System.out.println("\nYou have applied for: " + app.getProject().getProjectName()
                    + " (" + app.getFlatType() + ") [Status: " + app.getStatus() + "]");
        }
    }
    

    public void applyForProject(Applicant applicant, Project project, FlatType flatType) {
        if (applicant.hasApplied()) {
            System.out.println("You have already applied for a project.");
            System.out.println("Note: Each applicant can only apply for one project.");
            return;
        }

        if (!applicant.isEligible(project, flatType)) {
            System.out.println("You do not meet the eligibility criteria for this flat type.");
            return;
        }

        Application app = new Application(applicant, project, flatType);
        applicant.setApplication(app);
        project.addApplication(app);

        System.out.println("Application submitted for " + project.getProjectName()
                + " (" + flatType + ") has been submitted successfully!");
    }

    public void withdrawApplication(Applicant applicant) {
        if (!applicant.hasApplied()) {
            System.out.println("No application to withdraw.");
        } else {
            Application app = applicant.getApplication();

            System.out.println("Application for " + app.getProject().getProjectName()
                    + " (" + app.getFlatType() + ") has been withdrawn.");

            applicant.setApplication(null);
        }
    }

    public void viewApplicationStatus(Applicant applicant) {
        if (!applicant.hasApplied()) {
            System.out.println("No application found.");
        } else {
            Application app = applicant.getApplication();
            System.out.println("Application Status for " + app.getProject().getProjectName()
                    + " (" + app.getFlatType() + "): " + app.getStatus());
        }
    }
}
