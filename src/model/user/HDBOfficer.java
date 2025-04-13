package model.user;

import model.project.Project;
import model.transaction.OfficerRegistrationStatus;
import model.user.enums.MaritalStatus;

public class HDBOfficer extends Applicant {
    private Project assignedProject;
    private OfficerRegistrationStatus registrationStatus;

    public HDBOfficer(String nric, String password, int age, MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus);
        this.registrationStatus = OfficerRegistrationStatus.NONE;
    }

    public Project getAssignedProject() {
        return assignedProject;
    }

    public void assignProject(Project project) {
        this.assignedProject = project;
    }

    public OfficerRegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(OfficerRegistrationStatus status) {
        this.registrationStatus = status;
    }

    @Override
    public String getRole() {
        return "HDB Officer";
    }

    // Add methods for replying to enquiries, viewing applicant BTO status, booking flats, etc.
}
