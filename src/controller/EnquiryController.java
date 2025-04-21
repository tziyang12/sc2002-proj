package controller;

import model.user.Applicant;
import model.user.HDBOfficer;
import model.user.User;
import model.transaction.Enquiry;
import model.project.Project;

import service.EnquiryService;

import java.util.List;

/**
 * Controller for handling operations related to enquiries,
 * including submission, modification, deletion, and replies.
 */
public class EnquiryController {

    private EnquiryService enquiryService = new EnquiryService();
    /**
     * Constructs an EnquiryController instance.
     * This constructor can be used for initialization if needed.
     */
    public EnquiryController() {
        // Constructor can be used for initialization if needed
    }

    /**
     * Retrieves all enquiries made by a given applicant.
     *
     * @param applicant the applicant whose enquiries are being fetched
     * @return list of enquiries submitted by the applicant
     */
    public List<Enquiry> getEnquiriesByApplicant(Applicant applicant) {
        return applicant.getEnquiries(); // already stored in Applicant
    }

    /**
     * Retrieves all enquiries assigned to an officer's handled projects.
     *
     * @param officer the officer handling the enquiries
     * @return list of relevant enquiries
     */
    public List<Enquiry> getEnquiriesForOfficer(HDBOfficer officer) {
        return enquiryService.getEnquiriesForOfficer(officer);
    }

    /**
     * Retrieves all enquiries from a list of projects.
     *
     * @param projects the list of projects to filter enquiries by
     * @return list of all associated enquiries
     */
    public List<Enquiry> getAllEnquiries(List<Project> projects) {
        return enquiryService.getAllEnquiries(projects);
    }

    /**
     * Allows an applicant to submit a new enquiry for a specific project.
     *
     * @param applicant       the applicant submitting the enquiry
     * @param enquiryMessage  the content of the enquiry
     * @param project         the project the enquiry is about
     */
    public void submitEnquiry(Applicant applicant, String enquiryMessage, Project project) {
        Enquiry enquiry = enquiryService.submitEnquiry(applicant, enquiryMessage, project);
        if (enquiry == null) {
            System.out.println("Project not found.");
        } else {
            System.out.println("Enquiry submitted successfully.");
        }
    }

    /**
     * Edits an existing enquiry if it has not been replied to.
     *
     * @param enquiry            the enquiry to edit
     * @param newEnquiryMessage  the new enquiry message content
     * @return true if successful, false otherwise
     */
    public boolean editEnquiry(Enquiry enquiry, String newEnquiryMessage) {
        boolean success = enquiryService.editEnquiry(enquiry, newEnquiryMessage);
        if (success) System.out.println("Enquiry updated.");
        else System.out.println("Cannot edit the enquiry. It may have been replied.");
        return success;
    }

    /**
     * Deletes an enquiry if it has not been replied to.
     *
     * @param enquiry the enquiry to delete
     * @return true if successfully deleted, false otherwise
     */
    public boolean deleteEnquiry(Enquiry enquiry) {
        boolean success = enquiryService.deleteEnquiry(enquiry);
        if (success) System.out.println("Enquiry deleted.");
        else System.out.println("Cannot delete the enquiry. It may have been replied.");
        return success;
    }

    /**
     * Allows an officer or manager to reply to an enquiry.
     *
     * @param user          the user (officer or manager) replying
     * @param project       the project the enquiry is related to
     * @param enquiryId     the ID of the enquiry
     * @param replyMessage  the reply message content
     */
    public void replyToEnquiry(User user, Project project, int enquiryId, String replyMessage) {
        boolean success = enquiryService.replyToEnquiry(user, project, enquiryId, replyMessage);
        if (success)
            System.out.println("Replied to enquiry.");
        else
            System.out.println("Failed to reply. Check if you have access or if the enquiry exists.");
    }
}
