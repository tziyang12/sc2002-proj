package model.user;

import model.project.Project;
import model.transaction.ApplicationStatus;
import model.project.FlatType;
import model.user.enums.MaritalStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Applicant in the BTO system.
 * Handles state regarding application and enquiry history.
 */
public class Applicant extends User {
    private Project appliedProject = null;
    private FlatType appliedFlatType = null;
    private ApplicationStatus applicationStatus = ApplicationStatus.NONE;
    private List<String> enquiries;

    public Applicant(String nric, String password, int age, MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus);
        this.enquiries = new ArrayList<>();
    }

    // Role info for role-based access
    @Override
    public String getRole() {
        return "Applicant";
    }

    // === Application Info ===

    public boolean hasApplied() {
        return appliedProject != null;
    }

    public boolean isEligible(Project project, FlatType type) {
        if (!project.isVisible()) return false;

        int unitsAvailable = project.getNumUnits(type);
        if (unitsAvailable <= 0) return false;

        switch (getMaritalStatus()) {
            case MARRIED:
                return getAge() >= 21;
            case SINGLE:
                return getAge() >= 35 && type == FlatType.TWO_ROOM;
            default:
                return false;
        }
    }

    public void apply(Project project, FlatType type) {
        this.appliedProject = project;
        this.appliedFlatType = type;
        this.applicationStatus = ApplicationStatus.PENDING;
    }
    
    public void withdrawApplication() {
        this.appliedProject = null;
        this.appliedFlatType = null;
        this.applicationStatus = ApplicationStatus.NONE;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }
    
    public void setApplicationStatus(ApplicationStatus status) {
        this.applicationStatus = status;
    }

    public Project getAppliedProject() {
        return appliedProject;
    }

    public FlatType getAppliedFlatType() {
        return appliedFlatType;
    }

    // === Enquiry Management ===
    
    public List<String> getEnquiries() {
        return enquiries;
    }
}
