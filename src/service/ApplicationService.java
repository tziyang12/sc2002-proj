package service;

import model.project.FlatType;
import model.project.Project;
import model.transaction.Application;
import model.user.Applicant;

public class ApplicationService {

    public Application apply(Applicant applicant, Project project, FlatType flatType) {
        if (applicant.hasApplied()) {
            throw new IllegalStateException("You have already applied for a project.");
        }

        if (!applicant.isEligible(project, flatType)) {
            throw new IllegalArgumentException("You do not meet the eligibility criteria for this flat type.");
        }

        Application app = new Application(applicant, project, flatType);
        applicant.setApplication(app);
        project.addApplication(app);
        return app;
    }

    public void withdraw(Applicant applicant) {
        if (!applicant.hasApplied()) {
            throw new IllegalStateException("No application to withdraw.");
        }

        Application app = applicant.getApplication();
        applicant.setApplication(null);
        app.getProject().getApplications().remove(app); // optional cleanup
    }

    public Application getApplication(Applicant applicant) {
        if (!applicant.hasApplied()) {
            throw new IllegalStateException("No application found.");
        }
        return applicant.getApplication();
    }
}
