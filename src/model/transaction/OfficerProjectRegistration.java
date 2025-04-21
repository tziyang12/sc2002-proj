package model.transaction;

import model.project.Project;

/**
 * Represents the registration of an officer for a specific BTO project.
 * Each officer registration includes the project they are registered for and 
 * their current registration status.
 */
public class OfficerProjectRegistration {
    private Project project;
    private OfficerRegistrationStatus registrationStatus;

    /**
     * Constructor to create a new officer project registration.
     * 
     * @param project The project the officer is registered for.
     * @param registrationStatus The status of the officer's registration (e.g., pending, approved).
     */
    public OfficerProjectRegistration(Project project, OfficerRegistrationStatus registrationStatus) {
        this.project = project;
        this.registrationStatus = registrationStatus;
    }

    /**
     * Gets the project that the officer is registered for.
     * 
     * @return The project associated with this officer's registration.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the project that the officer is registered for.
     * 
     * @param project The new project to associate with this officer's registration.
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Gets the registration status of the officer for the project.
     * 
     * @return The registration status of the officer.
     */
    public OfficerRegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    /**
     * Sets the registration status of the officer for the project.
     * 
     * @param registrationStatus The new registration status to set.
     */
    public void setRegistrationStatus(OfficerRegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
}
