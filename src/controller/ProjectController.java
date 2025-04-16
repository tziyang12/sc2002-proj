package controller;

import java.util.List;
import java.util.Map;

import model.project.FlatType;
import model.project.Project;
import model.transaction.Application;
import model.user.Applicant;

public class ProjectController {

    public void showEligibleProjects(Applicant applicant, List<Project> projects) {
        System.out.println("Available Projects:");

        for (Project project : projects) {
            if (!project.isVisible()) continue;

            for (Map.Entry<FlatType, Integer> entry : project.getFlatUnits().entrySet()) {
                FlatType type = entry.getKey();
                int count = entry.getValue();

                if (applicant.isEligible(project, type)) {
                    System.out.println("== " + project.getProjectName() + " (" + project.getNeighbourhood() + ") ==");
                    System.out.println(" - " + type + ": " + count + " units available");
                }
            }
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
        project.addApplicant(applicant);

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
