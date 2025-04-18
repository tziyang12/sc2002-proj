package controller;

import model.user.Applicant;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.User;
import model.transaction.Enquiry;
import model.project.Project;

import service.EnquiryService;

import java.util.ArrayList;
import java.util.List;

public class EnquiryController {

    private EnquiryService enquiryService = new EnquiryService();

    public List<Enquiry> getEnquiriesByApplicant(Applicant applicant) {
        return applicant.getEnquiries(); // already stored in Applicant
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
        boolean hasPermission = false;

        if ((user instanceof HDBManager manager && manager.getManagedProjects().contains(project)) ||
            (user instanceof HDBOfficer officer && officer.getAssignedProjects().contains(project))) {
            hasPermission = true;
        }

        if (!hasPermission) {
            System.out.println("You are not authorized to reply to enquiries for this project.");
            return;
        }

        Enquiry enquiry = project.getEnquiryById(enquiryId);
        if (enquiry != null) {
            enquiry.setReply(replyMessage);
            System.out.println("Replied to enquiry: " + enquiry.getEnquiryMessage());
        } else {
            System.out.println("Enquiry not found.");
        }
    }

    
}
