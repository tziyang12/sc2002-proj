package controller;

import model.project.Project;
import model.project.FlatType;
import model.transaction.ApplicationStatus;
import model.user.Applicant;

public class ApplicantController {

    public boolean applyForProject(Applicant applicant, Project project, FlatType flatType) {
        if (applicant.hasApplied()) {
            System.out.println("You have already applied for a project.");
            return false;
        }

        if (!applicant.isEligible(project, flatType)) {
            System.out.println("You do not meet the eligibility criteria.");
            return false;
        }

        applicant.apply(project, flatType);
        System.out.println("Application submitted for " + project.getProjectName() + " (" + flatType + ")");
        return true;
    }

    public void withdrawApplication(Applicant applicant) {
        if (!applicant.hasApplied()) {
            System.out.println("No application found to withdraw.");
            return;
        }

        System.out.println("Withdrawing application for " + applicant.getAppliedProject().getProjectName() + "...");
        applicant.withdrawApplication();
    }

    public void viewApplicationStatus(Applicant applicant) {
        if (!applicant.hasApplied()) {
            System.out.println("No application found.");
        } else {
            System.out.println("Application Status for " +
                applicant.getAppliedProject().getProjectName() + " (" +
                applicant.getAppliedFlatType() + "): " +
                applicant.getApplicationStatus());
        }
    }
}
