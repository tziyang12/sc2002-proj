package controller;

import java.util.List;

import model.project.FlatType;
import model.project.Project;
import model.project.ProjectSearchCriteria;
import model.transaction.Application;
import model.user.Applicant;

import service.ProjectService;

import service.ApplicationService;

public class ProjectController {
    private ApplicationService applicationService = new ApplicationService();
    private ProjectService projectService = new ProjectService();

    
    /** 
     * @param applicant
     * @param projects
     */
    public void showEligibleProjects(Applicant applicant, List<Project> projects) {
        ProjectSearchCriteria criteria = applicant.getSearchCriteria();
        
        projects = projectService.filterAndSortProjects(applicant, projects, criteria);
        displayEligibleProjects(applicant, projects);
        displayApplicantApplication(applicant);
    }


    private void displayEligibleProjects(Applicant applicant, List<Project> projects) {
        System.out.println("Available Projects (Eligible Only):");
        System.out.printf("%-20s %-20s %-10s %-10s %-10s %-10s %n", "Project Name", "Neighbourhood", "TWO_ROOM", "Price", "THREE_ROOM", "Price");
        System.out.println("------------------------------------------------------------------------------------");

        boolean hasEligible = false;

        for (Project project : projects) {
            if (!projectService.isProjectVisibleToApplicant(applicant, project)) continue;

            String[] displays = getFlatTypeDisplays(applicant, project);
            String twoRoomDisplay = displays[0];
            if (!applicant.getSearchCriteria().getFlatTypes().contains(FlatType.TWO_ROOM) && applicant.getSearchCriteria().getFlatTypes().size()>= 1) {
                twoRoomDisplay = "NA";
            }
            String threeRoomDisplay = displays[1];
            if (!applicant.getSearchCriteria().getFlatTypes().contains(FlatType.THREE_ROOM) && applicant.getSearchCriteria().getFlatTypes().size()>= 1) {
                threeRoomDisplay = "NA";
            }

            if (twoRoomDisplay.equals("NA") && threeRoomDisplay.equals("NA")) continue;

            hasEligible = true;
            displayProjectDetails(project, twoRoomDisplay, threeRoomDisplay);
        }

        if (!hasEligible) {
            System.out.println("No eligible projects found at the moment.");
        }
    }

    private String[] getFlatTypeDisplays(Applicant applicant, Project project) {
        String twoRoomDisplay = "NA";
        String threeRoomDisplay = "NA";

        if (project.getFlatUnits().containsKey(FlatType.TWO_ROOM) && applicant.isEligible(project, FlatType.TWO_ROOM)) {
            twoRoomDisplay = String.valueOf(project.getFlatUnits().get(FlatType.TWO_ROOM));
        }
        
        if (project.getFlatUnits().containsKey(FlatType.THREE_ROOM) && applicant.isEligible(project, FlatType.THREE_ROOM)) {
            threeRoomDisplay = String.valueOf(project.getFlatUnits().get(FlatType.THREE_ROOM));
        }

        return new String[] { twoRoomDisplay, threeRoomDisplay };
    }

    private void displayProjectDetails(Project project, String twoRoomDisplay, String threeRoomDisplay) {
        System.out.printf("%-20s %-20s %-10s %-10s %-10s %-10s%n", 
            project.getProjectName(), 
            project.getNeighbourhood(), 
            twoRoomDisplay, 
            project.getFlatPrice(FlatType.TWO_ROOM),
            threeRoomDisplay,
            project.getFlatPrice(FlatType.THREE_ROOM)
            );
    }

    private void displayApplicantApplication(Applicant applicant) {
        if (applicant.hasApplied()) {
            Application app = applicant.getApplication();
            System.out.println("\nYou have applied for: " + app.getProject().getProjectName()
                    + " (" + app.getFlatType() + ") [Status: " + app.getStatus() + "]");
        }
    }
    

    public void applyForProject(Applicant applicant, Project project, FlatType flatType) {
        try {
            applicationService.apply(applicant, project, flatType);
            System.out.println("Application submitted for " + project.getProjectName()
                    + " (" + flatType + ") has been submitted successfully!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void withdrawApplication(Applicant applicant) {
        try {
            applicationService.withdraw(applicant);
            System.out.println("Application withdrawn successfully.");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewApplicationStatus(Applicant applicant) {
        try {
            Application app = applicationService.getApplication(applicant);
            System.out.println("Application Status for " + app.getProject().getProjectName()
                    + " (" + app.getFlatType() + "): " + app.getStatus());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
