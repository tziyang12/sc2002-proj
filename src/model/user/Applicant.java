package model.user;

import model.project.Project;
import model.transaction.Application;
import model.transaction.ApplicationStatus;
import model.transaction.Enquiry;
import model.project.FlatType;
import model.user.enums.MaritalStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Applicant in the BTO system.
 * Handles state regarding application and enquiry history.
 */
public class Applicant extends User {
    private Application application = null;
    private List<Enquiry> enquiries;

    public Applicant(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.enquiries = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "Applicant";
    }

    // === Application Logic ===

    public boolean hasApplied() {
        return application != null;
    }

    public boolean canWithdraw() {
    return application != null &&
           (application.getStatus() == ApplicationStatus.PENDING ||
            application.getStatus() == ApplicationStatus.SUCCESSFUL ||
            application.getStatus() == ApplicationStatus.BOOKED);
    }

    public void requestWithdrawal() {
        if (application != null) {
            application.requestWithdrawal(); // You can rename `withdraw()` to be more specific
        }
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

    public void setApplication(Application application) {
        this.application = application;
    }

    public Application getApplication() {
        return application;
    }

    public void clearApplication() {
        this.application = null;
    }

    // === Enquiry Management ===

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    public void removeEnquiry(Enquiry enquiry) {
        enquiries.remove(enquiry);
    }

    public void updateEnquiry(Enquiry enquiry, String newMessage) {
        enquiry.setEnquiryMessage(newMessage);
    }
}
