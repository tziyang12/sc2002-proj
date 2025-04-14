package controller;

import model.project.Project;
import model.transaction.OfficerRegistrationStatus;
import model.user.HDBOfficer;
import model.user.Applicant;
import model.transaction.ApplicationStatus;
import model.transaction.Enquiry;
import java.util.List;

public class OfficerController {

    // Register an officer to handle a project
    public boolean registerOfficerToProject(HDBOfficer officer, Project project) {
        // Check if officer is already handling another project
        if (officer.hasPendingOrApprovedRegistration()) {
            System.out.println("Officer is already registered for another project or has a pending registration.");
            return false;
        }

        // Check if officer has already applied for the project as an applicant
        if (project.hasApplicant(officer)) {
            System.out.println("Officer cannot register for a project they have applied for.");
            return false;
        }

        // Register officer if criteria are met
        officer.setRegistrationStatus(OfficerRegistrationStatus.PENDING);
        System.out.println("Officer registration for project " + project.getProjectName() + " is pending.");
        return true;
    }

    // Approve registration of an officer
    public void approveRegistration(HDBOfficer officer, Project project) {
        if (officer.getRegistrationStatus() == OfficerRegistrationStatus.PENDING) {
            officer.assignProject(project);  // Now this works because we've added the method back
            officer.setRegistrationStatus(OfficerRegistrationStatus.APPROVED);
            System.out.println("Officer " + officer.getName() + " has been approved for project " + project.getProjectName());
        } else {
            System.out.println("Registration is either not pending or already approved.");
        }
    }

    // Reject the registration of an officer
    public void rejectRegistration(HDBOfficer officer) {
        if (officer.getRegistrationStatus() == OfficerRegistrationStatus.PENDING) {
            officer.setRegistrationStatus(OfficerRegistrationStatus.REJECTED);
            System.out.println("Officer " + officer.getName() + "'s registration has been rejected.");
        } else {
            System.out.println("No pending registration to reject.");
        }
    }

    // View the enquiries related to the officer's project
    public void viewEnquiries(HDBOfficer officer) {
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

    // Reply to an enquiry from an applicant
    public void replyToEnquiry(HDBOfficer officer, int enquiryId, String replyMessage) {
        if (officer.getAssignedProject() == null) {
            System.out.println("Officer is not handling any project.");
            return;
        }

        Enquiry enquiry = officer.getAssignedProject().getEnquiryById(enquiryId); // Ensure this method exists in Project class
        if (enquiry != null) {
            enquiry.setReply(replyMessage); // Ensure the Enquiry class has a setReply() method
            System.out.println("Replied to enquiry: " + enquiry.getEnquiryMessage());
        } else {
            System.out.println("Enquiry not found.");
        }
    }

    // Update flat booking status for successful applicants
    public void updateApplicantBookingStatus(HDBOfficer officer, Applicant applicant) {
        if (officer.getAssignedProject() == null) {
            System.out.println("Officer is not handling any project.");
            return;
        }

        if (!applicant.getApplicationStatus().equals(ApplicationStatus.SUCCESSFUL)) {
            System.out.println("Applicant's application is not successful.");
            return;
        }

        // Update the applicant's project status to 'Booked'
        applicant.setApplicationStatus(ApplicationStatus.BOOKED);
        System.out.println("Applicant " + applicant.getName() + " has been successfully booked for project " +
                officer.getAssignedProject().getProjectName());
    }

    // Generate receipt for successful applicant booking
    public void generateBookingReceipt(HDBOfficer officer, Applicant applicant) {
        if (applicant.getApplicationStatus().equals(ApplicationStatus.BOOKED)) {
            System.out.println("\n[Booking Receipt]");
            System.out.println("Applicant Name: " + applicant.getName());
            System.out.println("NRIC: " + applicant.getNric());
            System.out.println("Age: " + applicant.getAge());
            System.out.println("Marital Status: " + applicant.getMaritalStatus());
            System.out.println("Flat Type: " + applicant.getAppliedFlatType());
            System.out.println("Project: " + officer.getAssignedProject().getProjectName());
            System.out.println("Booking Status: Booked");
        } else {
            System.out.println("No booking details available.");
        }
    }
}
