package model.user;

import java.util.ArrayList;
import java.util.List;

import model.project.Project;
import model.transaction.OfficerProjectRegistration;
import model.transaction.OfficerRegistrationStatus;
import model.user.enums.MaritalStatus;

public class HDBOfficer extends Applicant {
    private List<OfficerProjectRegistration> registeredProjects = new ArrayList<>(); // changed to list of OfficerProjectRegistration
    private List<Project> assignedProjects = new ArrayList<>(); // projects they are assigned to after approval

    public HDBOfficer(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        super(name, nric, password, age, maritalStatus);
    }

    public List<OfficerProjectRegistration> getRegisteredProjects() {
        return registeredProjects;
    }

    public List<Project> getAssignedProjects() {
        return assignedProjects;
    }

    // Add project to registered projects with a status
    public void applyForProject(Project project) {
        OfficerProjectRegistration newRegistration = new OfficerProjectRegistration(project, OfficerRegistrationStatus.PENDING);
        if (!registeredProjects.contains(newRegistration)) {
            registeredProjects.add(newRegistration);
        }
    }

    // Set registration status for a particular project
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

    public void assignProject(Project project) {
        if (!assignedProjects.contains(project)) {
            assignedProjects.add(project);
        }
    }

    public boolean isHandlingProject(Project project) {
        return assignedProjects.contains(project) && 
               registeredProjects.stream()
                                 .anyMatch(registration -> registration.getProject().equals(project) &&
                                                           registration.getRegistrationStatus() == OfficerRegistrationStatus.APPROVED);
    }

    @Override
    public String getRole() {
        return "HDB Officer";
    }

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
