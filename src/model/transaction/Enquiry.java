package model.transaction;

import model.project.Project;
import model.user.Applicant;

/**
 * Represents an enquiry submitted by an Applicant regarding a BTO project.
 * Each enquiry contains a message from the applicant and a reply from the HDB Officer.
 * 
 * The enquiry includes the following details:
 * - A unique enquiry ID.
 * - The enquiry message submitted by the applicant.
 * - The reply message from the officer (if available).
 * - Whether the enquiry has been replied to.
 * - The associated project and applicant for this enquiry.
 */
public class Enquiry {
    private int enquiryId;
    private String enquiryMessage;
    private String replyMessage;
    private boolean isReplied;
    private Project project;
    private Applicant applicant;

    /**
     * Constructor to create a new enquiry.
     * 
     * @param enquiryId The unique ID of the enquiry.
     * @param enquiryMessage The message submitted by the applicant.
     * @param project The project associated with the enquiry.
     * @param applicant The applicant who submitted the enquiry.
     */
    public Enquiry(int enquiryId, String enquiryMessage, Project project, Applicant applicant) {
        this.enquiryId = enquiryId;
        this.enquiryMessage = enquiryMessage;
        this.isReplied = false; // Initially, no reply
        this.project = project;
        this.applicant = applicant;
    }

    /**
     * Gets the unique ID of the enquiry.
     * 
     * @return The enquiry ID.
     */
    public int getEnquiryId() {
        return enquiryId;
    }

    /**
     * Sets the enquiry message and returns the updated message.
     * 
     * @param enquiryMessage The new enquiry message.
     * @return The updated enquiry message.
     */
    public String setEnquiryMessage(String enquiryMessage) {
        this.enquiryMessage = enquiryMessage;
        return this.enquiryMessage;
    }

    /**
     * Gets the message submitted by the applicant.
     * 
     * @return The enquiry message.
     */
    public String getEnquiryMessage() {
        return enquiryMessage;
    }

    /**
     * Gets the project associated with this enquiry.
     * 
     * @return The project related to this enquiry.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Gets the applicant who submitted the enquiry.
     * 
     * @return The applicant of the enquiry.
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * Gets the reply message from the officer.
     * 
     * @return The reply message, or {@code null} if not yet replied.
     */
    public String getReplyMessage() {
        return replyMessage;
    }

    /**
     * Checks whether the enquiry has been replied to.
     * 
     * @return {@code true} if the enquiry has been replied to, otherwise {@code false}.
     */
    public boolean isReplied() {
        return isReplied;
    }

    /**
     * Sets the reply message from the officer and marks the enquiry as replied.
     * 
     * @param replyMessage The reply message from the officer.
     */
    public void setReply(String replyMessage) {
        this.replyMessage = replyMessage;
        this.isReplied = true;
    }

    /**
     * Provides a string representation of the enquiry, including enquiry ID, project name, message, and reply.
     * 
     * @return A string representing the enquiry details.
     */
    @Override
    public String toString() {
        return "Enquiry ID: " + enquiryId +
               "\nProject: " + (project != null ? project.getProjectName() : "N/A") +
               "\nMessage: " + enquiryMessage +
               "\nReply: " + ((replyMessage == null || replyMessage.isEmpty()) ? "No reply yet." : replyMessage);
    }
}
