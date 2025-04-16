package model.transaction;

import model.project.Project;

public class OfficerProjectRegistration {
    private Project project;
    private OfficerRegistrationStatus registrationStatus;

    public OfficerProjectRegistration(Project project, OfficerRegistrationStatus registrationStatus) {
        this.project = project;
        this.registrationStatus = registrationStatus;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public OfficerRegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(OfficerRegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
}
