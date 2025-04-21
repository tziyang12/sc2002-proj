package controller;

import model.project.FlatType;
import model.project.Project;
import model.transaction.Application;
import model.transaction.Enquiry;
import model.user.Applicant;

import service.ApplicationService;

import java.util.List;

/**
 * Controller responsible for handling applicant-related actions,
 * such as viewing and applying for projects, managing applications and enquiries.
 */
public class ApplicantController {
    private final ProjectController projectController = new ProjectController();
    private final EnquiryController enquiryController = new EnquiryController();
    private final ApplicationService applicationService = new ApplicationService();

    /**
     * Displays a list of BTO projects that the given applicant is eligible to apply for.
     *
     * @param applicant the applicant requesting to view eligible projects
     * @param projects  the full list of available BTO projects
     */
    public void viewProjects(Applicant applicant, List<Project> projects) {
        projectController.showEligibleProjects(applicant, projects);
    }

    /**
     * Allows an applicant to apply for a project with the specified flat type.
     *
     * @param applicant the applicant submitting the application
     * @param project   the selected project
     * @param type      the type of flat the applicant wants to apply for
     */
    public void applyForProject(Applicant applicant, Project project, FlatType type) {
        projectController.applyForProject(applicant, project, type);
    }

    /**
     * Allows an applicant to request a withdrawal of their current BTO application.
     *
     * @param applicant the applicant requesting to withdraw their application
     */
    public void withdrawApplication(Applicant applicant) {
        projectController.withdrawApplication(applicant);
    }

    /**
     * Displays the current application status for the applicant.
     * Also indicates if a withdrawal has been requested.
     *
     * @param applicant the applicant whose application status will be displayed
     */
    public void viewApplicationStatus(Applicant applicant) {
        try {
            Application app = applicationService.getApplication(applicant);
            System.out.println("Application Status for " +
                    app.getProject().getProjectName() + " (" +
                    app.getFlatType() + "): " +
                    app.getStatus());
            if (app.isWithdrawalRequested()) {
                System.out.println("Withdrawal requested. Awaiting approval.");
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Submits a new enquiry related to a specific project on behalf of the applicant.
     *
     * @param applicant the applicant submitting the enquiry
     * @param enquiry   the enquiry message
     * @param project   the project the enquiry relates to
     */
    public void submitEnquiry(Applicant applicant, String enquiry, Project project) {
        enquiryController.submitEnquiry(applicant, enquiry, project);
    }

    /**
     * Edits an existing enquiry if it belongs to the applicant.
     *
     * @param applicant the applicant attempting to edit the enquiry
     * @param enquiry   the enquiry object to be edited
     * @param updated   the new enquiry message
     */
    public void editEnquiry(Applicant applicant, Enquiry enquiry, String updated) {
        if (!enquiry.getApplicant().equals(applicant)) {
            System.out.println("You do not have permission to edit this enquiry.");
            return;
        }
        enquiryController.editEnquiry(enquiry, updated);
    }

    /**
     * Deletes an existing enquiry if it belongs to the applicant.
     *
     * @param applicant the applicant attempting to delete the enquiry
     * @param enquiry   the enquiry to be deleted
     */
    public void deleteEnquiry(Applicant applicant, Enquiry enquiry) {
        if (!enquiry.getApplicant().equals(applicant)) {
            System.out.println("You do not have permission to delete this enquiry.");
            return;
        }
        enquiryController.deleteEnquiry(enquiry);
    }
}
