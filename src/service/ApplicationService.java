package service;

import model.project.FlatType;
import model.project.Project;
import model.transaction.Application;
import model.user.Applicant;

/**
 * Service class responsible for managing applications for BTO projects.
 * This includes applying for a project, withdrawing applications, and retrieving application details.
 */
public class ApplicationService {

    /**
     * Allows an applicant to apply for a BTO project with a specified flat type.
     * 
     * @param applicant The applicant applying for the project.
     * @param project The BTO project the applicant wants to apply for.
     * @param flatType The type of flat the applicant is applying for.
     * @return The newly created application.
     * @throws IllegalStateException If the applicant has already applied for a project or the project is closed.
     * @throws IllegalArgumentException If the applicant does not meet the eligibility criteria for the flat type.
     */
    public Application apply(Applicant applicant, Project project, FlatType flatType) {
        if (applicant.hasApplied()) {
            throw new IllegalStateException("You have already applied for a project.");
        }

        if (project.isClosed()) {
            throw new IllegalStateException("This project is closed for applications.");
        }

        if (!applicant.isEligible(project, flatType)) {
            throw new IllegalArgumentException("You do not meet the eligibility criteria for this flat type.");
        }

        Application app = new Application(applicant, project, flatType);
        applicant.setApplication(app);
        project.addApplication(app);
        return app;
    }

    /**
     * Withdraws the applicant's application for a project.
     * 
     * @param applicant The applicant who wishes to withdraw their application.
     * @throws IllegalStateException If the applicant has not applied for any project.
     */
    public void withdraw(Applicant applicant) {
        if (!applicant.hasApplied()) {
            throw new IllegalStateException("No application to withdraw.");
        }

        applicant.getApplication().requestWithdrawal();
    }

    /**
     * Retrieves the application of a specified applicant.
     * 
     * @param applicant The applicant whose application is being retrieved.
     * @return The application associated with the applicant.
     * @throws IllegalStateException If the applicant has not applied for any project.
     */
    public Application getApplication(Applicant applicant) {
        if (!applicant.hasApplied()) {
            throw new IllegalStateException("No application found.");
        }
        return applicant.getApplication();
    }
}
