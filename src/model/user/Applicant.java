package model.user;

import model.project.Project;
import model.transaction.Application;
import model.transaction.ApplicationStatus;
import model.transaction.Enquiry;
import model.project.FlatType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Applicant in the BTO system.
 * Handles application submission, withdrawal eligibility, and enquiry management.
 */
public class Applicant extends User {
    private Application application = null;
    private List<Enquiry> enquiries;

    /**
     * Constructs an Applicant with the given user details.
     *
     * @param name the applicant's full name
     * @param nric the applicant's NRIC
     * @param password the account password
     * @param age the applicant's age
     * @param maritalStatus the marital status of the applicant
     */
    public Applicant(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.enquiries = new ArrayList<>();
    }

    /**
     * Gets the role of the user.
     *
     * @return the string "Applicant"
     */
    @Override
    public String getRole() {
        return "Applicant";
    }

    // === Application Logic ===

    /**
     * Checks if the applicant has submitted an application.
     *
     * @return true if an application exists; false otherwise
     */
    public boolean hasApplied() {
        return application != null;
    }

    /**
     * Determines whether the applicant can withdraw their current application.
     *
     * @return true if the application is in a withdrawable state; false otherwise
     */
    public boolean canWithdraw() {
        return application != null &&
               (application.getStatus() == ApplicationStatus.PENDING ||
                application.getStatus() == ApplicationStatus.SUCCESSFUL ||
                application.getStatus() == ApplicationStatus.BOOKED);
    }

    /**
     * Requests to withdraw the current application if it exists.
     */
    public void requestWithdrawal() {
        if (application != null) {
            application.requestWithdrawal();
        }
    }

    /**
     * Checks if the applicant is eligible to apply for a specific flat type in a given project.
     *
     * @param project the BTO project being applied to
     * @param type the type of flat the applicant wants to apply for
     * @return true if the applicant meets eligibility criteria; false otherwise
     */
    public boolean isEligible(Project project, FlatType type) {
        int unitsAvailable = project.getNumUnits(type);
        if (unitsAvailable < 0) return false;
        switch (getMaritalStatus()) {
            case MARRIED:
                return getAge() >= 21;
            case SINGLE:
                return getAge() >= 35 && type == FlatType.TWO_ROOM;
            default:
                return false;
        }
    }

    /**
     * Sets the current application for the applicant.
     *
     * @param application the application to associate with the applicant
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * Gets the current application of the applicant.
     *
     * @return the current application, or null if none exists
     */
    public Application getApplication() {
        return application;
    }

    /**
     * Clears the current application from the applicant.
     */
    public void clearApplication() {
        this.application = null;
    }

    // === Enquiry Management ===

    /**
     * Retrieves the list of enquiries submitted by the applicant.
     *
     * @return a list of the applicant's enquiries
     */
    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    /**
     * Adds a new enquiry to the applicant's enquiry list.
     *
     * @param enquiry the enquiry to be added
     */
    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    /**
     * Removes a specific enquiry from the applicant's enquiry list.
     *
     * @param enquiry the enquiry to be removed
     */
    public void removeEnquiry(Enquiry enquiry) {
        enquiries.remove(enquiry);
    }

    /**
     * Updates the message content of a specific enquiry.
     *
     * @param enquiry the enquiry to update
     * @param newMessage the new message content
     */
    public void updateEnquiry(Enquiry enquiry, String newMessage) {
        enquiry.setEnquiryMessage(newMessage);
    }
}
