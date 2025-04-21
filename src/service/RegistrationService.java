package service;

import model.project.Project;
import model.user.HDBOfficer;

/**
 * Service class responsible for handling registration operations for HDB officers
 * to apply for a BTO project.
 */
public class RegistrationService {
    /**
     * Constructs a RegistrationService instance.
     * This constructor can be used for initialization if needed.
     */
    public RegistrationService() {
        // Constructor can be used for initialization if needed
    }
    /** 
     * Registers an HDB officer for a specific project if they are eligible and the project is available for registration.
     *
     * @param officer The HDB officer attempting to register for the project.
     * @param project The project that the officer is attempting to register for.
     * @return boolean Returns true if the officer is successfully registered for the project,
     *         false if they are already registered or the project is not available for registration.
     */
    public boolean registerForProject(HDBOfficer officer, Project project) {
        if (officer.isHandlingProject(project)) {
            return false;  // Already registered
        }

        if (!project.isAvailableForRegistration()) {
            return false;  // Not available
        }

        officer.applyForProject(project);
        return true;
    }
}
