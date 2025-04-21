package service;

import model.user.Applicant;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.User;

import java.util.ArrayList;
import java.util.List;

import model.project.Project;
import model.transaction.Enquiry;

public class EnquiryService {

    
    /** 
     * @param applicant
     * @param enquiryMessage
     * @param project
     * @return Enquiry
     */
    public Enquiry submitEnquiry(Applicant applicant, String enquiryMessage, Project project) {
        if (project == null) return null;

        int enquiryId = project.getEnquiries().size() + 1;
        Enquiry enquiry = new Enquiry(enquiryId, enquiryMessage, project, applicant);

        applicant.getEnquiries().add(enquiry);
        project.addEnquiry(enquiry);
        return enquiry;
    }

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

    // Officer-level view of enquiries
    public List<Enquiry> getEnquiriesForOfficer(HDBOfficer officer) {
        List<Enquiry> result = new ArrayList<>();
        for (Project project : officer.getAssignedProjects()) {
            result.addAll(project.getEnquiries()); // Assuming project holds a list of enquiries
        }
        return result;
    }

    // Manger-level view of all enquiries
    public List<Enquiry> getAllEnquiries(List<Project> allProjects) {
        List<Enquiry> result = new ArrayList<>();
        for (Project project : allProjects) {
            result.addAll(project.getEnquiries());
        }
        return result;
    }

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
