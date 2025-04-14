package model.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.transaction.Enquiry;
import model.user.Applicant;
import model.user.HDBOfficer;

public class Project {

    private int projectID;
    private String projectName;
    private String neighbourhood;
    private Map<FlatType, Integer> flatUnits;  // Track number of units directly
    private LocalDate applicationStartDate;
    private LocalDate applicationEndDate;
    private String managerUserID;
    private boolean visibility;
    private int availableOfficerSlots = 10; 
    
    // List to track applicants
    private List<Applicant> applicants;
    private List<HDBOfficer> officers;
    private List<Enquiry> enquiries;

    // Constructor that accepts LocalDate for application start and end dates
    public Project(String projectName, String neighbourhood, LocalDate applicationStartDate, LocalDate applicationEndDate, String managerUserID) {
        this.projectName = projectName;
        this.neighbourhood = neighbourhood;
        this.applicationStartDate = applicationStartDate;
        this.applicationEndDate = applicationEndDate;
        this.managerUserID = managerUserID;
        this.visibility = true;
        this.flatUnits = new HashMap<>();
        this.applicants = new ArrayList<>();
        this.officers = new ArrayList<>();
        this.enquiries = new ArrayList<>();
    }

    // Method to add flat units (number of units for each flat type)
    public void addFlatUnit(FlatType type, int units) {
        flatUnits.put(type, units);
    }

    // Add an officer to the project
    public void addOfficer(HDBOfficer officer) {
        officers.add(officer);
    }

    // Add an applicant to the project
    public void addApplicant(Applicant applicant) {
        applicants.add(applicant);
    }

    // Check if an officer has already applied for the project
    public boolean hasApplicant(HDBOfficer officer) {
        return applicants.contains(officer);
    }

    // Getter methods
    public int getProjectID() {
        return projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public int getNumUnits(FlatType type) {
        return flatUnits.getOrDefault(type, 0);
    }

    public Map<FlatType, Integer> getFlatUnits() {
        return flatUnits;
    }

    public LocalDate getApplicationStartDate() {
        return applicationStartDate;
    }

    public LocalDate getApplicationEndDate() {
        return applicationEndDate;
    }

    public boolean isVisible() {
        return visibility;
    }

    public String getManagerUserID() {
        return managerUserID;
    }

    public int getAvailableOfficerSlots() {
        return availableOfficerSlots;
    }

    public boolean isApplicationPeriodOver() {
        LocalDate today = LocalDate.now();
        return today.isAfter(applicationEndDate);
    }

    // Set the projectID
    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    @Override
    public String toString() {
        return "Project ID: " + projectID +
                "\nProject Name: " + projectName +
                "\nNeighbourhood: " + neighbourhood +
                "\n2-Room Units: " + getNumUnits(FlatType.TWO_ROOM) +
                "\n3-Room Units: " + getNumUnits(FlatType.THREE_ROOM) +
                "\nApplication Period: " + applicationStartDate + " to " + applicationEndDate +
                "\nVisible: " + (visibility ? "Yes" : "No") +
                "\nHDB Manager: " + managerUserID +
                "\nAvailable Officer Slots: " + availableOfficerSlots;
    }

    // Utility method to parse date strings into LocalDate
    public static LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    // Getters for applicants and officers
    public List<Applicant> getApplicants() {
        return applicants;
    }

    public List<HDBOfficer> getOfficers() {
        return officers;
    }

    // Method to get all enquiries for the project
    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    // Method to add an enquiry to the project
    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    // Method to get an enquiry by its ID
    public Enquiry getEnquiryById(int enquiryId) {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryId() == enquiryId) {
                return enquiry;
            }
        }
        return null;  // Return null if not found
    }
}
