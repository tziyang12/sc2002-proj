package service;

import model.project.Project;
import model.user.HDBOfficer;

public class RegistrationService {
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
