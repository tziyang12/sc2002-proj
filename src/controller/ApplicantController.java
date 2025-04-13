package controller;

import model.project.FlatType;
import model.project.Project;
import model.user.Applicant;
import model.transaction.ApplicationStatus;

import java.util.List;
import java.util.Scanner;

public class ApplicantController {
    private final ProjectController projectController = new ProjectController();
    private final EnquiryController enquiryController = new EnquiryController();

    public void viewProjects(Applicant applicant, List<Project> projects) {
        projectController.showEligibleProjects(applicant, projects);
    }

    public void applyForProject(Applicant applicant, Project project, FlatType type) {
        projectController.applyForProject(applicant, project, type);
    }

    public void withdrawApplication(Applicant applicant) {
        projectController.withdrawApplication(applicant); // delegate to ProjectController
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

    public void submitEnquiry(Applicant applicant, String enquiry) {
        enquiryController.submitEnquiry(applicant, enquiry);
    }

    public void editEnquiry(Applicant applicant, int index, String updated) {
        enquiryController.editEnquiry(applicant, index, updated);
    }

    public void deleteEnquiry(Applicant applicant, int index) {
        enquiryController.deleteEnquiry(applicant, index);
    }

    public void viewEnquiries(Applicant applicant) {
        enquiryController.viewEnquiries(applicant);
    }
}
