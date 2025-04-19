package controller;

import model.user.Applicant;
import model.user.HDBOfficer;
import model.user.User;
import model.transaction.Enquiry;
import model.project.Project;

import service.EnquiryService;

import java.util.List;

public class EnquiryController {

    private EnquiryService enquiryService = new EnquiryService();

    public List<Enquiry> getEnquiriesByApplicant(Applicant applicant) {
        return applicant.getEnquiries(); // already stored in Applicant
    }

    public List<Enquiry> getEnquiriesForOfficer(HDBOfficer officer) {
        return enquiryService.getEnquiriesForOfficer(officer);
    }

    public List<Enquiry> getAllEnquiries(List<Project> projects) {
        return enquiryService.getAllEnquiries(projects);
    }

    // Submit/Edit/Delete Enquiry for Applicant
    public void submitEnquiry(Applicant applicant, String enquiryMessage, Project project) {
        Enquiry enquiry = enquiryService.submitEnquiry(applicant, enquiryMessage, project);
        if (enquiry == null) {
            System.out.println("Project not found.");
        } else {
            System.out.println("Enquiry submitted successfully.");
        }
    }
    
    public boolean editEnquiry(Enquiry enquiry, String newEnquiryMessage) {
        boolean success = enquiryService.editEnquiry(enquiry, newEnquiryMessage);
        if (success) System.out.println("Enquiry updated.");
        else System.out.println("Cannot edit the enquiry. It may have been replied.");
        return success;
    }
    
    public boolean deleteEnquiry(Enquiry enquiry) {
        boolean success = enquiryService.deleteEnquiry(enquiry);
        if (success) System.out.println("Enquiry deleted.");
        else System.out.println("Cannot delete the enquiry. It may have been replied.");
        return success;
    }

    public void replyToEnquiry(User user, Project project, int enquiryId, String replyMessage) {
        boolean success = enquiryService.replyToEnquiry(user, project, enquiryId, replyMessage);
        if (success)
            System.out.println("Replied to enquiry.");
        else
            System.out.println("Failed to reply. Check if you have access or if the enquiry exists.");
    }

    
}
