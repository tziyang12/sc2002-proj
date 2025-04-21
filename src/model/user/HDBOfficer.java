package model.user;

import java.util.ArrayList;
import java.util.List;

import model.project.Project;
import model.transaction.OfficerProjectRegistration;
import model.transaction.OfficerRegistrationStatus;

/**
 * Represents an HDB Officer in the BTO system.
 * Inherits from Applicant and adds capabilities to apply for and manage BTO projects as an officer.
 */
public class HDBOfficer extends Applicant {
    private List<OfficerProjectRegistration> registeredProjects = new ArrayList<>(); // officer project applications
    private List<Project> assignedProjects = new ArrayList<>(); // approved projects assigned to the officer

    /**
     * Constructs an HDBOfficer with the given personal details.
     *
     * @param name           the name of the officer
     * @param nric           the NRIC of the officer
     * @param password       the password of the officer
     * @param age            the age of the officer
     * @param maritalStatus  the marital status of the officer
     */
    public HDBOfficer(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        super(name, nric, password, age, maritalStatus);
    }

    /**
     * Returns the list of officer's project registration requests.
     *
     * @return list of OfficerProjectRegistration objects
     */
    public List<OfficerProjectRegistration> getRegisteredProjects() {
        return registeredProjects;
    }

    /**
     * Returns the list of projects assigned to this officer.
     *
     * @return list of assigned Project objects
     */
    public List<Project> getAssignedProjects() {
        return assignedProjects;
    }

    /**
     * Applies to manage a given project as an officer.
     * Registration is added with a PENDING status.
     *
     * @param project the project to apply for
     */
    public void applyForProject(Project project) {
        OfficerProjectRegistration newRegistration = new OfficerProjectRegistration(project, OfficerRegistrationStatus.PENDING);
        if (!registeredProjects.contains(newRegistration)) {
            registeredProjects.add(newRegistration);
        }
    }

    /**
     * Updates the registration status for a specific project.
     * If the status is APPROVED, the project is also added to the assigned list.
     *
     * @param project the project for which to update the status
     * @param status  the new registration status
     */
    public void setProjectRegistrationStatus(Project project, OfficerRegistrationStatus status) {
        for (OfficerProjectRegistration registration : registeredProjects) {
            if (registration.getProject().equals(project)) {
                registration.setRegistrationStatus(status);
                if (status == OfficerRegistrationStatus.APPROVED) {
                    assignProject(project);
                }
                break;
            }
        }
    }

    /**
     * Assigns a project to the officer if not already assigned.
     *
     * @param project the project to assign
     */
    public void assignProject(Project project) {
        if (!assignedProjects.contains(project)) {
            assignedProjects.add(project);
        }
    }

    /**
     * Checks if the officer is currently approved to handle a specific project.
     *
     * @param project the project to check
     * @return true if officer is handling the project; false otherwise
     */
    public boolean isHandlingProject(Project project) {
        return assignedProjects.contains(project) &&
               registeredProjects.stream()
                   .anyMatch(registration -> registration.getProject().equals(project) &&
                                             registration.getRegistrationStatus() == OfficerRegistrationStatus.APPROVED);
    }

    /**
     * Returns the role of this user.
     *
     * @return "HDB Officer"
     */
    @Override
    public String getRole() {
        return "HDB Officer";
    }

    /**
     * Returns a string representation of the officer, including NRIC, age, and assigned projects.
     *
     * @return formatted string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[HDB Officer] NRIC: %s | Age: %d | Assigned Projects: ",
                getNric(), getAge()));
        if (assignedProjects.isEmpty()) {
            sb.append("None");
        } else {
            for (Project p : assignedProjects) {
                sb.append(p.getProjectName()).append(", ");
            }
            sb.setLength(sb.length() - 2); // remove trailing comma
        }
        return sb.toString();
    }
}
