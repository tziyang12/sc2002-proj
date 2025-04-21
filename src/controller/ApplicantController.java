package controller;

import model.project.FlatType;
import model.project.Project;
import model.transaction.Application;
import model.transaction.Enquiry;
import model.user.Applicant;

import service.ApplicationService;

import java.util.List;

public class ApplicantController {
    private final ProjectController projectController = new ProjectController();
    private final EnquiryController enquiryController = new EnquiryController();
    private final ApplicationService applicationService = new ApplicationService();

    
    /** 
     * @param applicant
     * @param projects
     */
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
        try {
            Application app = applicationService.getApplication(applicant);
            System.out.println("Application Status for " +
                    app.getProject().getProjectName() + " (" +
                    app.getFlatType() + "): " +
                    app.getStatus());
            if (app.isWithdrawalRequested()){
                System.out.println("Withdrawal requested. Awaiting approval.");
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
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
