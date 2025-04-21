package controller;

import java.util.List;

import model.project.FlatType;
import model.project.Project;
import model.project.ProjectSearchCriteria;
import model.transaction.Application;
import model.user.Applicant;

import service.ProjectService;
import service.ApplicationService;

/**
 * This controller class manages the operations related to BTO project listings, applications, and status management.
 * It allows applicants to view eligible projects, apply, withdraw applications, and check their application status.
 */
public class ProjectController {
    private ApplicationService applicationService = new ApplicationService();
    private ProjectService projectService = new ProjectService();

    /** 
     * Displays eligible BTO projects for an applicant based on their search criteria.
     * 
     * @param applicant The applicant requesting the eligible projects.
     * @param projects The list of all available projects to filter and display.
     */
    public void showEligibleProjects(Applicant applicant, List<Project> projects) {
        ProjectSearchCriteria criteria = applicant.getSearchCriteria();
        
        projects = projectService.filterAndSortProjects(applicant, projects, criteria);
        displayEligibleProjects(applicant, projects);
        displayApplicantApplication(applicant);
    }

    /**
     * Displays the list of eligible projects for the applicant after filtering and sorting.
     * Only projects that match the applicant's search criteria will be displayed.
     *
     * @param applicant The applicant requesting to view the projects.
     * @param projects The filtered list of eligible projects.
     */
    private void displayEligibleProjects(Applicant applicant, List<Project> projects) {
        System.out.println("Available Projects (Eligible Only):");
        System.out.printf("%-20s %-20s %-10s %-10s %-10s %-10s %n", "Project Name", "Neighbourhood", "TWO_ROOM", "Price", "THREE_ROOM", "Price");
        System.out.println("------------------------------------------------------------------------------------");

        boolean hasEligible = false;

        for (Project project : projects) {
            boolean isNotVisible = !projectService.isProjectVisibleToApplicant(applicant, project);
            String[] displays = getFlatTypeDisplays(applicant, project);
            String twoRoomDisplay = displays[0];
            String threeRoomDisplay = displays[1];

            if (!applicant.getSearchCriteria().getFlatTypes().contains(FlatType.TWO_ROOM) && applicant.getSearchCriteria().getFlatTypes().size() >= 1) {
                twoRoomDisplay = "NA";
            }
            if (!applicant.getSearchCriteria().getFlatTypes().contains(FlatType.THREE_ROOM) && applicant.getSearchCriteria().getFlatTypes().size() >= 1) {
                threeRoomDisplay = "NA";
            }

            boolean noEligibleFlats = twoRoomDisplay.equals("NA") && threeRoomDisplay.equals("NA");

            if (isNotVisible || noEligibleFlats) {
                continue;
            }

            hasEligible = true;
            displayProjectDetails(project, twoRoomDisplay, threeRoomDisplay);
        }

        if (!hasEligible) {
            System.out.println("No eligible projects found at the moment.");
        }
    }

    /**
     * Retrieves the available flat types and their respective available units for the project, based on the applicant's eligibility.
     *
     * @param applicant The applicant whose eligibility is checked.
     * @param project The project to retrieve flat type details for.
     * @return A string array containing the available flat types (two-room and three-room) with their unit counts.
     */
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

    /**
     * Displays the project details including the flat types and their available units.
     *
     * @param project The project whose details will be displayed.
     * @param twoRoomDisplay The available units for two-room flats.
     * @param threeRoomDisplay The available units for three-room flats.
     */
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

    /**
     * Displays the current application status for the applicant.
     * If the applicant has already applied, it shows the details of the application.
     *
     * @param applicant The applicant whose application status is being displayed.
     */
    private void displayApplicantApplication(Applicant applicant) {
        if (applicant.hasApplied()) {
            Application app = applicant.getApplication();
            System.out.println("\nYou have applied for: " + app.getProject().getProjectName()
                    + " (" + app.getFlatType() + ") [Status: " + app.getStatus() + "]");
        }
    }
    
    /**
     * Allows the applicant to apply for a specific project with the given flat type.
     *
     * @param applicant The applicant submitting the application.
     * @param project The project the applicant is applying for.
     * @param flatType The type of flat the applicant is applying for.
     */
    public void applyForProject(Applicant applicant, Project project, FlatType flatType) {
        try {
            applicationService.apply(applicant, project, flatType);
            System.out.println("Application submitted for " + project.getProjectName()
                    + " (" + flatType + ") has been submitted successfully!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Allows the applicant to withdraw their application.
     *
     * @param applicant The applicant withdrawing their application.
     */
    public void withdrawApplication(Applicant applicant) {
        try {
            applicationService.withdraw(applicant);
            System.out.println("Application withdrawn successfully.");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays the current application status for the given applicant.
     *
     * @param applicant The applicant whose application status is being viewed.
     */
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
