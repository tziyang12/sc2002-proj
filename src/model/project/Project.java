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
    private int maxOfficerSlots;
    
    // List to track applicants
    private List<Applicant> applicants;
    private List<HDBOfficer> officers;
    private List<Enquiry> enquiries;

    // Constructor that accepts LocalDate for application start and end dates
    public Project(String projectName, String neighbourhood, LocalDate applicationStartDate, LocalDate applicationEndDate, String managerUserID, int maxOfficerSlots) {
        this.projectName = projectName;
        this.neighbourhood = neighbourhood;
        this.applicationStartDate = applicationStartDate;
        this.applicationEndDate = applicationEndDate;
        this.managerUserID = managerUserID;
        this.maxOfficerSlots = maxOfficerSlots;
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

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNumUnits(FlatType type, int units) {
        flatUnits.put(type, units);
    }

    public int getNumUnits(FlatType type) {
        return flatUnits.getOrDefault(type, 0);
    }

    public Map<FlatType, Integer> getFlatUnits() {
        return flatUnits;
    }

    public void setApplicationPeriod(LocalDate startDate, LocalDate endDate) {
        this.applicationStartDate = startDate;
        this.applicationEndDate = endDate;
    }

    public LocalDate getApplicationStartDate() {
        return applicationStartDate;
    }

    public LocalDate getApplicationEndDate() {
        return applicationEndDate;
    }

    public void setVisible(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean isVisible() {
        return visibility;
    }

    public void setManagerUserID(String managerUserID) {
        this.managerUserID = managerUserID;
    }

    public String getManagerUserID() {
        return managerUserID;
    }

    public void setMaxOfficerSlots(int maxOfficerSlots) {
        this.maxOfficerSlots = maxOfficerSlots;
    }

    public int getMaxOfficerSlots() {
        return maxOfficerSlots;
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
                "\nAvailable Officer Slots: " + maxOfficerSlots;
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

    public void decreaseRemainingFlats(FlatType flatType) {
        if (flatUnits.containsKey(flatType) && flatUnits.get(flatType) > 0) {
            int remainingFlats = flatUnits.get(flatType);
            flatUnits.put(flatType, remainingFlats - 1); // Decrement available flats
            System.out.println("One flat of type " + flatType + " has been booked. Remaining flats: " + (remainingFlats - 1));
        } else {
            System.out.println("No flats available for type " + flatType + " in this project.");
        }
    }
}
