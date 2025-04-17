package controller;

import model.project.FlatType;
import model.project.Project;
import model.transaction.Application;
import model.transaction.ApplicationStatus;
import model.transaction.Enquiry;
import model.transaction.OfficerProjectRegistration;
import model.transaction.OfficerRegistrationStatus;
import model.user.Applicant;
import model.user.HDBOfficer;

import service.ProjectService;

import java.util.ArrayList;
import java.util.List;

public class OfficerController {
    private ProjectService projectService = new ProjectService();

    public List<Project> getAvailableProjects(HDBOfficer officer, List<Project> projectList) {
        List<Project> availableProjects = new ArrayList<>();
        for (Project project : projectList) {
            if (!project.hasApplicant(officer) && officer.getAssignedProjects().stream().noneMatch(p -> p.getProjectName().equals(project.getProjectName()))) {
                availableProjects.add(project);
            }
        }
        return availableProjects;
    }

    // Get assigned projects for an officer
    public List<Project> getAssignedProjects(HDBOfficer officer) {
        return officer.getAssignedProjects();
    }
    
    public static boolean canRegisterForProject(HDBOfficer officer, Project project) {
        // Check if officer is already assigned to this project
        //Make use of officer.isHandlingProject(project) method
        if (officer.isHandlingProject(project)) {
            System.out.println("You are already handling this project.");
            return false;
        }

        // Check if officer has already applied for this project as an applicant
        if (officer.getApplication() != null && officer.getApplication().getProject() == project) {
            System.out.println("You have already applied for this project as an applicant.");
            return false;
        }

        // Check if project has available slots for new officers
        if (!project.isAvailableForRegistration()) {
            System.out.println("This project does not have available officer slots.");
            return false;
        }

        // If all checks pass
        return true;
    }

    // Register officer for a new project
    public boolean registerOfficerToProject(HDBOfficer officer, Project project) {
        if (!project.isAvailableForRegistration()) {
            System.out.println("The project is not available for officer registration.");
            return false;
        }

        if (officer.isHandlingProject(project)) {
            System.out.println("Officer is already registered for another project or has a pending registration.");
            return false;
        }

        if (project.hasApplicant(officer)) {
            System.out.println("Officer cannot register for a project they have applied for.");
            return false;
        }

        if (projectService.hasDateConflict(project, officer)) {
            System.out.println("Project dates overlap with an existing assigned project.");
            return false;
        }

            // Check if the officer has a rejected registration already
        for (OfficerProjectRegistration reg : officer.getRegisteredProjects()) {
            if (reg.getProject().equals(project)) {
                OfficerRegistrationStatus status = reg.getRegistrationStatus();

                if (status == OfficerRegistrationStatus.REJECTED) {
                    // Reapply by updating the status only after all checks pass
                    reg.setRegistrationStatus(OfficerRegistrationStatus.PENDING);
                    System.out.println("Officer has reapplied to project " + project.getProjectName() + ".");
                    return true;
                }
            }
        }

        officer.applyForProject(project);
        officer.setProjectRegistrationStatus(project, OfficerRegistrationStatus.PENDING);
        System.out.println("Officer registration for project " + project.getProjectName() + " is pending.");
        return true;
    }

    // View officer's registration status for projects
    public void viewRegistrationStatus(HDBOfficer officer) {
        System.out.println("======= Officer Registration Status =======");
        for (OfficerProjectRegistration registration : officer.getRegisteredProjects()) {
            Project project = registration.getProject();
            System.out.println("Project: " + project.getProjectName() + " | Status: " + registration.getRegistrationStatus());
        }
    }

    // View enquiries for the officer's assigned projects
    public void viewEnquiries(HDBOfficer officer) {
        List<Project> assignedProjects = officer.getAssignedProjects();
        System.out.println("======= Enquiries =======");

        if (assignedProjects.isEmpty()) {
            System.out.println("Officer is not handling any projects.");
            return;
        }

        for (Project project : assignedProjects) {
            List<Enquiry> enquiries = project.getEnquiries();
            System.out.println("\n=======" + project.getProjectName() + "=======");

            if (enquiries.isEmpty()) {
                System.out.println("No enquiries.");
            } else {
                for (Enquiry enquiry : enquiries) {
                    System.out.println(enquiry);
                }
            }
        }
    }

    // Reply to a specific enquiry for a project
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

    // Change an applicant's status to 'BOOKED'
    public boolean changeApplicationStatusToBooked(Applicant applicant) {
        Application app = applicant.getApplication();
        if (app != null && app.getStatus() == ApplicationStatus.SUCCESSFUL) {
            app.setStatus(ApplicationStatus.BOOKED);

            Project project = app.getProject();
            if (project != null) {
                FlatType flatType = app.getFlatType();
                project.decreaseRemainingFlats(flatType);
            }

            System.out.println("Applicant's status changed to BOOKED. Flat type selected: " + app.getFlatType());
            return true;
        } else {
            System.out.println("Applicant's status cannot be changed to BOOKED because they are not SUCCESSFUL.");
            return false;
        }
    }

    // Generate a booking receipt for a successful applicant
    public void generateBookingReceipt(Applicant applicant) {
        Application app = applicant.getApplication();

        if (app == null || app.getStatus() != ApplicationStatus.BOOKED) {
            System.out.println("No booking details available.");
            return;
        }

        Project project = app.getProject();

        System.out.println("\n[Booking Receipt]");
        System.out.println("Applicant Name: " + applicant.getName());
        System.out.println("NRIC: " + applicant.getNric());
        System.out.println("Age: " + applicant.getAge());
        System.out.println("Marital Status: " + applicant.getMaritalStatus());
        System.out.println("Flat Type: " + app.getFlatType());
        System.out.println("Project: " + project.getProjectName());
        System.out.println("Booking Status: Booked");
    }
}
