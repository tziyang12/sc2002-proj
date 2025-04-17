package service;

import model.user.Applicant;
import model.project.Project;
import model.transaction.Enquiry;

public class EnquiryService {

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
}
