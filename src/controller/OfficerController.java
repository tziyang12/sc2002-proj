package controller;

import model.project.FlatType;
import model.project.Project;
import model.transaction.Application;
import model.transaction.ApplicationStatus;
import model.transaction.Enquiry;
import model.transaction.OfficerRegistrationStatus;
import model.user.Applicant;
import model.user.HDBOfficer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OfficerController {

    public static boolean canRegisterForProject(HDBOfficer officer, Project newProject) {
        LocalDate newStart = newProject.getApplicationStartDate();
        LocalDate newEnd = newProject.getApplicationEndDate();

        List<Project> allOfficerProjects = new ArrayList<>();
        allOfficerProjects.addAll(officer.getAssignedProjects());
        allOfficerProjects.addAll(officer.getRegisteredProjects());

        for (Project assigned : officer.getAssignedProjects()) {
            LocalDate start = assigned.getApplicationStartDate();
            LocalDate end = assigned.getApplicationEndDate();

            if (!(newEnd.isBefore(start) || newStart.isAfter(end))) {
                return false;
            }
        }
        return true;
    }

    public List<Project> getAvailableProjects(HDBOfficer officer, List<Project> projectList) {
        List<Project> availableProjects = new ArrayList<>();
        for (Project project : projectList) {
            if (!project.hasApplicant(officer) && !officer.getAssignedProjects().contains(project)) {
                availableProjects.add(project);
            }
        }
        return availableProjects;
    }
    public List<Project> getAssignedProjects(HDBOfficer officer) {
        return officer.getAssignedProjects();
    }
    
    public boolean registerOfficerToProject(HDBOfficer officer, Project project) {
        if (officer.hasPendingOrApprovedRegistration()) {
            System.out.println("Officer is already registered for another project or has a pending registration.");
            return false;
        }

        if (project.hasApplicant(officer)) {
            System.out.println("Officer cannot register for a project they have applied for.");
            return false;
        }

        if (!canRegisterForProject(officer, project)) {
            System.out.println("Project dates overlap with an existing assigned project.");
            return false;
        }

        officer.applyProject(project);
        officer.setRegistrationStatus(OfficerRegistrationStatus.PENDING);
        System.out.println("Officer registration for project " + project.getProjectName() + " is pending.");
        return true;
    }

    public void approveRegistration(HDBOfficer officer, Project project) {
        if (officer.getRegistrationStatus() == OfficerRegistrationStatus.PENDING) {
            officer.assignProject(project);
            officer.setRegistrationStatus(OfficerRegistrationStatus.APPROVED);
            System.out.println("Officer " + officer.getName() + " has been approved for project " + project.getProjectName());
        } else {
            System.out.println("Registration is either not pending or already approved.");
        }
    }

    public void rejectRegistration(HDBOfficer officer) {
        if (officer.getRegistrationStatus() == OfficerRegistrationStatus.PENDING) {
            officer.setRegistrationStatus(OfficerRegistrationStatus.REJECTED);
            System.out.println("Officer " + officer.getName() + "'s registration has been rejected.");
        } else {
            System.out.println("No pending registration to reject.");
        }
    }

    public void viewRegistrationStatus(HDBOfficer officer) {
        System.out.println("Officer " + officer.getName() + "'s registration status: " + officer.getRegistrationStatus());
    }

    public void viewEnquiries(HDBOfficer officer) {
        List<Project> assignedProjects = officer.getAssignedProjects();
        System.out.println("======= Enquiries =======");

        if (assignedProjects == null || assignedProjects.isEmpty()) {
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

    public void generateBookingReceipt(HDBOfficer officer, Applicant applicant) {
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
