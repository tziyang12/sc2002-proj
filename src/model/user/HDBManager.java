package model.user;

import model.user.enums.MaritalStatus;
import model.project.Project;
import model.project.FlatType;
import model.transaction.Enquiry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HDBManager extends User {

    private List<Integer> managedProjectIDs;

    public HDBManager(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.managedProjectIDs = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "HDB Manager";
    }

    public List<Integer> getManagedProjectIDs() {
        return managedProjectIDs;
    }

    public void addManagedProjectID(int projectId) {
        this.managedProjectIDs.add(projectId);
    }

    // === Project Management ===

    public boolean canCreateNewProject(List<Project> allProjects) {
        LocalDate today = LocalDate.now();
        for (Project project : allProjects) {
            if (managedProjectIDs.contains(project.getProjectID())
                    && !project.isApplicationPeriodOver(today)) {
                return false;
            }
        }
        return true;
    }

    public boolean createProject(String name, String neighborhood,
                                 int num2Room, int num3Room,
                                 LocalDate openDate, LocalDate closeDate,
                                 int maxOfficerSlots) {
        // Call controller or service logic to create a project
        return true;
    }

    public boolean editProject(int projectId, String newName, String newNeighborhood,
                               int newNum2Room, int newNum3Room,
                               LocalDate newOpenDate, LocalDate newCloseDate) {
        return true;
    }

    public boolean deleteProject(int projectId) {
        return true;
    }

    public void toggleVisibility(int projectId, boolean visible) {
        // Set project visibility on/off
    }

    // === Viewing Projects ===

    public List<Project> viewAllProjects(List<Project> allProjects) {
        return allProjects;
    }

    public List<Project> viewMyProjects(List<Project> allProjects) {
        List<Project> mine = new ArrayList<>();
        for (Project p : allProjects) {
            if (p.getManagerUserID().equals(this.getNric())) {
                mine.add(p);
            }
        }
        return mine;
    }

    // === Officer Registration Handling ===

    public List<String> viewOfficerRegistrations(int projectId) {
        return new ArrayList<>();
    }

    public void approveOfficer(String officerName, int projectId) {
        // Logic to approve officer and decrement available slots
    }

    public void rejectOfficer(String officerName, int projectId) {
        // Logic to reject officer
    }

    // === Applicant Application Handling ===

    public void approveApplication(String applicationID) {
        // Approve based on flat availability
    }

    public void rejectApplication(String applicationID) {
        // Rejection logic
    }

    public void approveWithdrawal(String applicationID) {
        // Approve withdrawal
    }

    public void rejectWithdrawal(String applicationID) {
        // Reject withdrawal
    }

    // === Enquiries ===

    public List<Enquiry> viewAllEnquiries(List<Enquiry> allEnquiries) {
        return allEnquiries;  // Managers can view all
    }

    // === Reports ===

    public List<String> generateApplicantReport(String filterBy, String filterValue) {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return super.toString() + "\nRole: HDB Manager";
    }
}
