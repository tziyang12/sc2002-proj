package model.user;

import java.util.ArrayList;
import java.util.List;

import model.project.Project;
import model.transaction.OfficerRegistrationStatus;
import model.user.enums.MaritalStatus;

public class HDBOfficer extends Applicant {
    private List<Project> assignedProjects = new ArrayList<>(); 
    private List<Project> appliedProjects = new ArrayList<>(); // projects they applied to, pending approval
    private OfficerRegistrationStatus registrationStatus;

    public HDBOfficer(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        super(name, nric, password, age, maritalStatus); // ✅ now matches Applicant constructor
        this.registrationStatus = OfficerRegistrationStatus.NONE;
    }

    public List<Project> getAppliedProjects() {
        return appliedProjects;
    }
    // Add projects to appliedProjects list (input is a single project)
    public void applyProject(Project project) {
        if (!appliedProjects.contains(project)) {
            appliedProjects.add(project);
        }
    }

    public List<Project> getAssignedProjects() {
        return assignedProjects;
    }

    public void assignProject(Project project) {
        if (!assignedProjects.contains(project)) {
            assignedProjects.add(project);
        }
    }

    public void removeAppliedProject(Project project) {
        appliedProjects.remove(project);
    }

    public boolean isHandlingProject(Project project) {
        return assignedProjects.contains(project) &&
               registrationStatus == OfficerRegistrationStatus.APPROVED;
    }

    public OfficerRegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(OfficerRegistrationStatus status) {
        this.registrationStatus = status;
    }

    public boolean hasPendingOrApprovedRegistration() {
        return registrationStatus == OfficerRegistrationStatus.PENDING ||
               registrationStatus == OfficerRegistrationStatus.APPROVED;
    }

    @Override
    public String getRole() {
        return "HDB Officer";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[HDB Officer] NRIC: %s | Age: %d | Status: %s | Assigned Projects: ",
                getNric(), getAge(), registrationStatus));
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

    // Add methods for replying to enquiries, viewing applicant BTO status, booking flats, etc.
}
