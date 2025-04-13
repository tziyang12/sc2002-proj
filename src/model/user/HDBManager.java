package model.user;

import model.project.Project;
import model.transaction.Enquiry;
import old.Application.Application;
import old.Application.ApplicationDatabase;
import old.Enquiry.EnquiryDatabase;
import old.Officer.OfficerDatabase;
import old.ProjectManagement.ProjectDatabase;
import model.project.FlatType;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class HDBManager extends Account {

    private List<Integer> managedProjectIDs;  // Changed to List<Integer> to store Integer project IDs

    public HDBManager(String userID, String password, String fullName) {
        super(userID, password, fullName);
        this.managedProjectIDs = new ArrayList<>();
    }

    // Only 1 active project allowed within application period
    public boolean canCreateNewProject() {
        for (Integer projectId : managedProjectIDs) {  // Changed String to Integer
            Project project = ProjectDatabase.getProjectByID(projectId);  // Changed method to getProjectByID
            if (project != null && !project.isApplicationPeriodOver()) {
                return false;
            }
        }
        return true;
    }

    public boolean createBTOProject(String name, String neighbourhood,
                                    int num2Room, int num3Room,
                                    LocalDate openDate, LocalDate closeDate) {
        if (!canCreateNewProject()) {
            System.out.println("You already have an active project within the application period.");
            return false;
        }

        Project project = new Project(name, neighbourhood, num2Room, num3Room, openDate, closeDate, this.userID);
        ProjectDatabase.addProject(project);
        managedProjectIDs.add(project.getProjectID());  // No change needed
        return true;
    }

    public boolean editProject(int projectId, String newName, String newNeighbourhood,
                               int new2Room, int new3Room, LocalDate newOpen, LocalDate newClose) {
        Project project = ProjectDatabase.getProjectByID(projectId);  // Changed method to getProjectByID
        if (project == null || !project.getManagerUserID().equals(this.userID)) return false;

        project.setProjectName(newName);
        project.setNeighbourhood(newNeighbourhood);
        project.setNumUnits(FlatType.TWO_ROOM, new2Room);
        project.setNumUnits(FlatType.THREE_ROOM, new3Room);
        project.setApplicationPeriod(newOpen, newClose);
        return true;
    }

    public boolean deleteProject(int projectId) {
        Project project = ProjectDatabase.getProjectByID(projectId);  // Changed method to getProjectByID
        if (project != null && project.getManagerUserID().equals(this.userID)) {
            ProjectDatabase.removeProject(projectId);  // Changed method to removeProject
            managedProjectIDs.remove(Integer.valueOf(projectId));  // Changed to Integer.valueOf(projectId)
            return true;
        }
        return false;
    }

    public void toggleVisibility(int projectId, boolean visible) {
        Project project = ProjectDatabase.getProjectByID(projectId);  // Changed method to getProjectByID
        if (project != null && project.getManagerUserID().equals(this.userID)) {
            project.setVisible(visible);
        }
    }

    public List<Project> getAllProjects() {
        return ProjectDatabase.getProjects();  // No change needed here
    }

    public List<Project> getMyProjects() {
        List<Project> myProjects = new ArrayList<>();
        for (Integer projectId : managedProjectIDs) {  // Changed String to Integer
            Project project = ProjectDatabase.getProjectByID(projectId);  // Changed method to getProjectByID
            if (project != null) myProjects.add(project);
        }
        return myProjects;
    }

    public void respondToOfficerRegistration(String officerID, int projectID, boolean approve) {  // Changed projectID to int
        if (approve) {
            OfficerDatabase.approveOfficer(officerID, projectID);  // Changed projectID to int
        } else {
            OfficerDatabase.rejectOfficer(officerID, projectID);  // Changed projectID to int
        }
    }

    public void respondToApplicantApplication(String applicationID, boolean approve) {
        ApplicationDatabase.processApplication(applicationID, approve);
    }

    public void respondToWithdrawRequest(String applicationID, boolean approve) {
        ApplicationDatabase.processWithdrawRequest(applicationID, approve);
    }

    public void replyToEnquiry(String enquiryID, String response) {
        EnquiryDatabase.replyToEnquiry(enquiryID, response, this.userID);
    }

    public List<String> generateApplicantReport(String filterBy, String filterValue) {
        return ApplicationDatabase.generateReport(filterBy, filterValue);
    }

    @Override
    public void displayMenu() {
        // You can implement or call ManagerDashboard CLI here
        System.out.println("HDB Manager Dashboard for " + this.fullName);
        // e.g. ManagerDashboard.start(this);
    }

    @Override
    public String toString() {
        return super.toString() + "\nRole: HDB Manager";
    }
}
