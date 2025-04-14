package model.user;

import model.project.Project;
import model.transaction.OfficerRegistrationStatus;
import model.user.enums.MaritalStatus;

public class HDBOfficer extends Applicant {
    private Project assignedProject;
    private OfficerRegistrationStatus registrationStatus;

    public HDBOfficer(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        super(name, nric, password, age, maritalStatus); // ✅ now matches Applicant constructor
        this.registrationStatus = OfficerRegistrationStatus.NONE;
    }
    public Project getAssignedProject() {
        return assignedProject;
    }

    public void assignProject(Project project) {
        this.assignedProject = project;
    }

    public boolean isHandlingProject(Project project) {
        return assignedProject != null && assignedProject.equals(project) &&
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
        return String.format("[HDB Officer] NRIC: %s | Age: %d | Status: %s | Assigned Project: %s",
                getNric(), getAge(), registrationStatus,
                assignedProject != null ? assignedProject.getProjectName() : "None");
    }

    // Add methods for replying to enquiries, viewing applicant BTO status, booking flats, etc.
}
