package controller;

import model.project.FlatType;
import model.project.Project;
import model.transaction.Application;
import model.transaction.Enquiry;
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

    public void submitEnquiry(Applicant applicant, String enquiry, Project project) {
        enquiryController.submitEnquiry(applicant, enquiry, project);
    }


    public void editEnquiry(Applicant applicant, Enquiry enquiry, String updated) {
        // Check if enquiry's applicant matches the current applicant
        if (!enquiry.getApplicant().equals(applicant)) {
            System.out.println("You do not have permission to edit this enquiry.");
            return;
        }
        enquiryController.editEnquiry(enquiry, updated);
    }

    public void deleteEnquiry(Applicant applicant, Enquiry enquiry) {
        // Check if enquiry's applicant matches the current applicant
        if (!enquiry.getApplicant().equals(applicant)) {
            System.out.println("You do not have permission to delete this enquiry.");
            return;
        }
        enquiryController.deleteEnquiry(enquiry);
    }
}
