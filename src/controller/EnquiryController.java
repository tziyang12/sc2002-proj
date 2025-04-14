package controller;

import model.user.Applicant;
import model.user.HDBOfficer;
import model.transaction.Enquiry;
import model.project.Project;
import java.util.List;

public class EnquiryController {

    // Applicant submits an enquiry to the system
    public void submitEnquiry(Applicant applicant, String enquiryMessage) {
        Enquiry enquiry = new Enquiry(applicant.getEnquiries().size() + 1, enquiryMessage);  // Generate a unique ID
        applicant.getEnquiries().add(enquiry);
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

        enquiries.get(index - 1).setReply(newEnquiryMessage);  // Update the enquiry message
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

    // Officer views the enquiries for the project they are handling
    public void viewEnquiriesForProject(HDBOfficer officer) {
        if (officer.getAssignedProject() == null) {
            System.out.println("Officer is not handling any project.");
            return;
        }

        List<Enquiry> enquiries = officer.getAssignedProject().getEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries for the project " + officer.getAssignedProject().getProjectName());
        } else {
            System.out.println("Enquiries for project " + officer.getAssignedProject().getProjectName() + ":");
            for (Enquiry enquiry : enquiries) {
                System.out.println(enquiry);
            }
        }
    }

    // Officer replies to an enquiry
    public boolean replyToEnquiry(HDBOfficer officer, int enquiryId, String replyMessage) {
        if (officer.getAssignedProject() == null) {
            System.out.println("Officer is not handling any project.");
            return false;
        }

        Enquiry enquiry = officer.getAssignedProject().getEnquiryById(enquiryId);
        if (enquiry != null) {
            enquiry.setReply(replyMessage);  // Set the reply message for the enquiry
            System.out.println("Replied to enquiry ID " + enquiryId);
            return true;
        } else {
            System.out.println("Enquiry not found.");
            return false;
        }
    }

    // Officer deletes an enquiry (if allowed, based on business logic)
    public boolean deleteEnquiry(HDBOfficer officer, int enquiryId) {
        if (officer.getAssignedProject() == null) {
            System.out.println("Officer is not handling any project.");
            return false;
        }

        Enquiry enquiry = officer.getAssignedProject().getEnquiryById(enquiryId);
        if (enquiry != null) {
            officer.getAssignedProject().getEnquiries().remove(enquiry);
            System.out.println("Enquiry ID " + enquiryId + " has been deleted.");
            return true;
        } else {
            System.out.println("Enquiry not found.");
            return false;
        }
    }
}
