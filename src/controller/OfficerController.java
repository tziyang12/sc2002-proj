package controller;

import model.project.FlatType;
import model.project.Project;
import model.transaction.OfficerRegistrationStatus;
import model.user.HDBOfficer;
import model.user.Applicant;
import model.transaction.ApplicationStatus;
import model.transaction.Enquiry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OfficerController {

    public static boolean canRegisterForProject(HDBOfficer officer, Project newProject) {
        LocalDate newStart = newProject.getApplicationStartDate();
        LocalDate newEnd = newProject.getApplicationEndDate();

        List<Project> allOfficerProjects = new ArrayList<>();
        allOfficerProjects.addAll(officer.getAssignedProjects());
        allOfficerProjects.addAll(officer.getAppliedProjects());

        for (Project assigned : officer.getAssignedProjects()) {
            LocalDate start = assigned.getApplicationStartDate();
            LocalDate end = assigned.getApplicationEndDate();

            // Check for overlap
            if (!(newEnd.isBefore(start) || newStart.isAfter(end))) {
                return false; // Overlapping
            }
        }
        return true;
    }

    public List<Project> getAssignedProjects(HDBOfficer officer, List<Project> projectList) {
        List<Project> assignedProjects = new ArrayList<>();
        for (Project project : projectList) {
            List<HDBOfficer> assignedOfficers = project.getOfficers();
            if (assignedOfficers != null && assignedOfficers.contains(officer)) {
                assignedProjects.add(project);
            }
        }
        return assignedProjects;
    }

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

        // Check for project date overlap
        if (!canRegisterForProject(officer, project)) {
            System.out.println("Project dates overlap with an existing assigned project.");
            return false;
        }

        // Register officer if criteria are met
        officer.applyProject(project);
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

    // View the project registration status of an officer
    public void viewRegistrationStatus(HDBOfficer officer) {
        System.out.println("Officer " + officer.getName() + "'s registration status: " + officer.getRegistrationStatus());
    }

    // View the enquiries related to the officer's project
    public void viewEnquiries(HDBOfficer officer) {
        List<Project> assignedProjects = officer.getAssignedProjects();

        if (assignedProjects == null || assignedProjects.isEmpty()) {
            System.out.println("Officer is not handling any projects.");
            return;
        }
    
        for (Project project : assignedProjects) {
            List<Enquiry> enquiries = project.getEnquiries();
            System.out.println("\nEnquiries for project: " + project.getProjectName());
    
            if (enquiries.isEmpty()) {
                System.out.println("No enquiries.");
            } else {
                for (Enquiry enquiry : enquiries) {
                    System.out.println(enquiry);
                }
            }
        }
    }

    // Reply to an enquiry from an applicant
    public void replyToEnquiry(HDBOfficer officer, Project project, int enquiryId, String replyMessage) {
        if (!officer.getAssignedProjects().contains(project)) {
            System.out.println("You are not assigned to this project.");
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
    
    public boolean changeApplicationStatusToBooked(Applicant applicant) {
        if (applicant.getApplicationStatus() == ApplicationStatus.SUCCESSFUL) {
            // 1. Set the application status to "BOOKED"
            applicant.setApplicationStatus(ApplicationStatus.BOOKED); 
            
            // 2. Update the number of remaining flats for the selected flat type in the project
            Project project = applicant.getAppliedProject(); // Get the project the applicant applied for
            if (project != null) {
                FlatType flatType = applicant.getAppliedFlatType(); // Get the selected flat type
                project.decreaseRemainingFlats(flatType); // Decrease the number of flats for the selected flat type
            }

            // 3. Update applicant's profile with the flat type they chose
            System.out.println("Applicant's status changed to BOOKED. Flat type selected: " + applicant.getAppliedFlatType());
            return true;
        } else {
            System.out.println("Applicant's status cannot be changed to BOOKED because they are not SUCCESSFUL.");
            return false;
        }
    }


    // Generate receipt for successful applicant booking
    public void generateBookingReceipt(HDBOfficer officer, Applicant applicant) {
        Project project = applicant.getAppliedProject(); // or applicant.getProject(), however you store it
    
        if (project == null || !applicant.getApplicationStatus().equals(ApplicationStatus.BOOKED)) {
            System.out.println("No booking details available.");
            return;
        }
    
        System.out.println("\n[Booking Receipt]");
        System.out.println("Applicant Name: " + applicant.getName());
        System.out.println("NRIC: " + applicant.getNric());
        System.out.println("Age: " + applicant.getAge());
        System.out.println("Marital Status: " + applicant.getMaritalStatus());
        System.out.println("Flat Type: " + applicant.getAppliedFlatType());
        System.out.println("Project: " + project.getProjectName());
        System.out.println("Booking Status: Booked");
    }
    
    
}
