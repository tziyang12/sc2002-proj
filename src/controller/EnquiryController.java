package controller;

import model.user.Applicant;
import model.transaction.Enquiry;
import model.project.Project;
import java.util.List;

public class EnquiryController {

    // Applicant submits an enquiry to the system
    public void submitEnquiry(Applicant applicant, String enquiryMessage, Project project) {    
        Enquiry enquiry = new Enquiry(applicant.getEnquiries().size() + 1, enquiryMessage, project);  // Generate a unique ID
        
        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        applicant.getEnquiries().add(enquiry);
        project.addEnquiry(enquiry);
        System.out.println("Enquiry submitted successfully.");
    
    }

    // Applicant views their own enquiries
    public void viewEnquiries(Applicant applicant) {
        List<Enquiry> enquiries = applicant.getEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries available.");
            return;
        }

        System.out.println("Your enquiries:");
        for (int i = 0; i < enquiries.size(); i++) {
            System.out.println((i + 1) + ") " + enquiries.get(i));
        }
    }

    // Applicant edits an existing enquiry
    public boolean editEnquiry(Applicant applicant, int index, String newEnquiryMessage) {
        List<Enquiry> enquiries = applicant.getEnquiries();

        if (index < 1 || index > enquiries.size()) {
            System.out.println("Invalid enquiry index.");
            return false;
        }

        enquiries.get(index - 1).setEnquiryMessage(newEnquiryMessage);  // Update the enquiry message
        System.out.println("Enquiry updated.");
        return true;
    }

    // Applicant deletes an existing enquiry
    public boolean deleteEnquiry(Applicant applicant, int index) {
        List<Enquiry> enquiries = applicant.getEnquiries();

        if (index < 1 || index > enquiries.size()) {
            System.out.println("Invalid enquiry index.");
            return false;
        }

        enquiries.remove(index - 1);
        System.out.println("Enquiry deleted.");
        return true;
    }
}
