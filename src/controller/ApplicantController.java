package controller;

import model.project.FlatType;
import model.project.Project;
import model.transaction.Application;
import model.user.Applicant;

import java.util.List;

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
            Application app = applicant.getApplication();
            System.out.println("Application Status for " +
                    app.getProject().getProjectName() + " (" +
                    app.getFlatType() + "): " +
                    app.getStatus());
        }
    }

    public void submitEnquiry(Applicant applicant, String enquiry) {
        if (!applicant.hasApplied()) {
            System.out.println("You need to apply for a project before submitting an enquiry.");
            return;
        }
        enquiryController.submitEnquiry(applicant, enquiry, applicant.getApplication().getProject());
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
