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

import java.util.List;

/**
 * OfficerController is responsible for handling operations related to officers
 * and their interactions with projects, such as project registration, enquiry
 * viewing, and application status updates.
 */
public class OfficerController {
    private ProjectService projectService = new ProjectService();

    /**
     * Constructs an OfficerController instance.
     * This constructor can be used for initialization if needed.
     */
    public OfficerController() {
        // Constructor can be used for initialization if needed
    }
    /**
     * Retrieves a list of available projects for an officer.
     *
     * @param officer The HDB Officer requesting available projects.
     * @param projectList The list of all available projects.
     * @return A list of projects available for the officer to register for.
     */
    public List<Project> getAvailableProjects(HDBOfficer officer, List<Project> projectList) {
        return projectService.getAvailableProjectsForOfficer(officer, projectList);
    }

    /**
     * Retrieves the list of projects that the officer is currently assigned to.
     *
     * @param officer The HDB Officer requesting assigned projects.
     * @return A list of projects assigned to the officer.
     */
    public List<Project> getAssignedProjects(HDBOfficer officer) {
        return officer.getAssignedProjects();
    }
    
    /**
     * Checks if an officer can register for a specific project.
     *
     * @param officer The officer attempting to register.
     * @param project The project the officer is trying to register for.
     * @return True if the officer can register, false otherwise.
     */
    public static boolean canRegisterForProject(HDBOfficer officer, Project project) {
        // Check if officer is already assigned to this project
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

    /**
     * Registers an officer for a project if they meet all requirements.
     *
     * @param officer The officer to register.
     * @param project The project to register the officer for.
     * @return True if the officer is successfully registered, false otherwise.
     */
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

    /**
     * Displays the officer's registration status for each of their projects.
     *
     * @param officer The officer whose registration statuses are to be viewed.
     */
    public void viewRegistrationStatus(HDBOfficer officer) {
        System.out.println("======= Officer Registration Status =======");
        if (officer.getRegisteredProjects().isEmpty()) {
            System.out.println("Officer has no registered projects.");
            return;
        }
        for (OfficerProjectRegistration registration : officer.getRegisteredProjects()) {
            Project project = registration.getProject();
            System.out.println("Project: " + project.getProjectName() + " | Status: " + registration.getRegistrationStatus());
        }
    }

    /**
     * Displays the enquiries for each of the officer's assigned projects.
     *
     * @param officer The officer whose assigned projects' enquiries are to be viewed.
     */
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

    /**
     * Changes the application status of an applicant to 'BOOKED' if the applicant's
     * status is currently 'SUCCESSFUL' and there are available flats of the selected type.
     *
     * @param applicant The applicant whose application status is to be changed.
     * @return True if the status was successfully changed, false otherwise.
     */
    public boolean changeApplicationStatusToBooked(Applicant applicant) {
        Application app = applicant.getApplication();
        if (app.getProject().getFlatUnits().get(app.getFlatType()) == 0) {
            System.out.println("No available flats of this type.");
            return false;
        }

        if (app.getStatus() == ApplicationStatus.SUCCESSFUL) {
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

    /**
     * Generates and displays a booking receipt for a successful applicant whose
     * application status is 'BOOKED'.
     *
     * @param applicant The applicant for whom the booking receipt is to be generated.
     */
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
