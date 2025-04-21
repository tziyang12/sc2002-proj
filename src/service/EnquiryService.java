package service;

import model.user.Applicant;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.User;
import model.project.Project;
import model.transaction.Enquiry;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for managing enquiries related to BTO projects.
 * This includes submitting, editing, deleting, and viewing enquiries by applicants, officers, and managers.
 */
public class EnquiryService {

    /**
     * Submits an enquiry for a project by an applicant.
     * 
     * @param applicant The applicant submitting the enquiry.
     * @param enquiryMessage The message of the enquiry.
     * @param project The project for which the enquiry is being made.
     * @return The created Enquiry object.
     */
    public Enquiry submitEnquiry(Applicant applicant, String enquiryMessage, Project project) {
        if (project == null) return null;

        int enquiryId = project.getEnquiries().size() + 1;
        Enquiry enquiry = new Enquiry(enquiryId, enquiryMessage, project, applicant);

        applicant.getEnquiries().add(enquiry);
        project.addEnquiry(enquiry);
        return enquiry;
    }

    /**
     * Edits an existing enquiry message.
     * 
     * @param enquiry The enquiry to be edited.
     * @param newEnquiryMessage The new enquiry message.
     * @return true if the enquiry was successfully edited, false otherwise.
     */
    public boolean editEnquiry(Enquiry enquiry, String newEnquiryMessage) {
        // Get enquiry project
        Project project = enquiry.getProject();
        if (project == null) return false;

        // Check if enquiry is replied
        if (enquiry.isReplied()) return false;

        // Update enquiry message
        enquiry.setEnquiryMessage(newEnquiryMessage);

        // Update project enquiry list
        for (Enquiry e : project.getEnquiries()) {
            if (e.getEnquiryId() == enquiry.getEnquiryId()) {
                e.setEnquiryMessage(newEnquiryMessage);
                break;
            }
        }

        // Update applicant enquiry list
        Applicant applicant = enquiry.getApplicant();
        if (applicant != null) {
            for (Enquiry e : applicant.getEnquiries()) {
                if (e.getEnquiryId() == enquiry.getEnquiryId()) {
                    e.setEnquiryMessage(newEnquiryMessage);
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes an existing enquiry.
     * 
     * @param enquiry The enquiry to be deleted.
     * @return true if the enquiry was successfully deleted, false otherwise.
     */
    public boolean deleteEnquiry(Enquiry enquiry) {
        // Get enquiry project
        Project project = enquiry.getProject();
        if (project == null) return false;

        // Get enquiry applicant
        Applicant applicant = enquiry.getApplicant();
        if (applicant == null) return false;

        // Check if enquiry is replied
        if (enquiry.isReplied()) return false;

        // Remove enquiry from applicant and project
        // Remove from applicant
        int index = applicant.getEnquiries().indexOf(enquiry);
        if (index < 0) return false;
        applicant.getEnquiries().remove(index);

        // Remove from project
        index = project.getEnquiries().indexOf(enquiry);
        if (index < 0) return false;
        project.getEnquiries().remove(index);
        return true;
    }

    /**
     * Retrieves all enquiries for projects assigned to a specific HDB officer.
     * 
     * @param officer The officer whose assigned projects' enquiries are to be retrieved.
     * @return A list of enquiries for all projects assigned to the officer.
     */
    public List<Enquiry> getEnquiriesForOfficer(HDBOfficer officer) {
        List<Enquiry> result = new ArrayList<>();
        for (Project project : officer.getAssignedProjects()) {
            result.addAll(project.getEnquiries()); // Assuming project holds a list of enquiries
        }
        return result;
    }

    /**
     * Retrieves all enquiries for all BTO projects.
     * 
     * @param allProjects A list of all BTO projects.
     * @return A list of all enquiries from all projects.
     */
    public List<Enquiry> getAllEnquiries(List<Project> allProjects) {
        List<Enquiry> result = new ArrayList<>();
        for (Project project : allProjects) {
            result.addAll(project.getEnquiries());
        }
        return result;
    }

    /**
     * Allows an officer or manager to reply to an enquiry for a specific project.
     * 
     * @param user The user (officer or manager) replying to the enquiry.
     * @param project The project associated with the enquiry.
     * @param enquiryId The ID of the enquiry to reply to.
     * @param replyMessage The reply message.
     * @return true if the enquiry was successfully replied to, false otherwise.
     */
    public boolean replyToEnquiry(User user, Project project, int enquiryId, String replyMessage) {
        boolean hasPermission = false;

        if ((user instanceof HDBManager manager && manager.getManagedProjects().contains(project)) ||
            (user instanceof HDBOfficer officer && officer.getAssignedProjects().contains(project))) {
            hasPermission = true;
        }

        if (!hasPermission) return false;

        Enquiry enquiry = project.getEnquiryById(enquiryId);
        if (enquiry != null) {
            enquiry.setReply(replyMessage);
            return true;
        }
        return false;
    }
}
