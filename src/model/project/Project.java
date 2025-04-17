package model.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.transaction.Enquiry;
import model.transaction.Application;
import model.user.Applicant;
import model.user.HDBOfficer;
import model.user.HDBManager;

public class Project {

    private int projectID;
    private String projectName;
    private String neighbourhood;
    private Map<FlatType, Integer> flatUnits;  // Track number of units directly
    private Map<FlatType, Double> flatPrices; // Track prices of units
    private LocalDate applicationStartDate;
    private LocalDate applicationEndDate;
    private boolean visibility;
    private int maxOfficerSlots;
    
    // List to track applicants
    private List<Application> applications;
    private List<HDBOfficer> officers;
    private List<Enquiry> enquiries;
    private HDBManager manager;

    // Constructor
    public Project(String projectName, String neighbourhood, LocalDate applicationStartDate, LocalDate applicationEndDate, int maxOfficerSlots) {
        this.projectName = projectName;
        this.neighbourhood = neighbourhood;
        this.applicationStartDate = applicationStartDate;
        this.applicationEndDate = applicationEndDate;
        this.maxOfficerSlots = maxOfficerSlots;
        this.visibility = true;
        this.flatUnits = new HashMap<>();
        this.flatPrices = new HashMap<>();
        this.applications = new ArrayList<>();
        this.officers = new ArrayList<>();
        this.enquiries = new ArrayList<>();
    }

    // Adders
    public void addFlatUnit(FlatType type, int units) {
        flatUnits.put(type, units);
    }

    public void addFlatPrice(FlatType type, double price) {
        flatPrices.put(type, price);
    }

    public void addOfficer(HDBOfficer officer) {
        officers.add(officer);
    }

    public void addApplication(Application application) {
        applications.add(application);
    }

    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    // Setters
    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public void setNumUnits(FlatType type, int units) {
        flatUnits.put(type, units);
    }

    public void setApplicationPeriod(LocalDate startDate, LocalDate endDate) {
        this.applicationStartDate = startDate;
        this.applicationEndDate = endDate;
    }

    public void setVisible(boolean visibility) {
        this.visibility = visibility;
    }

    public void setManager(HDBManager manager) {
        this.manager = manager;
    }

    public void setMaxOfficerSlots(int maxOfficerSlots) {
        this.maxOfficerSlots = maxOfficerSlots;
    }

    // Getters
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

    public double getFlatPrice(FlatType type) {
        return flatPrices.getOrDefault(type, 0.0);
    }

    public LocalDate getApplicationStartDate() {
        return applicationStartDate;
    }

    public LocalDate getApplicationEndDate() {
        return applicationEndDate;
    }

    public boolean isVisible() {
        return visibility && !isApplicationPeriodOver();
    }

    public HDBManager getManager() {
        return manager;
    }

    public int getMaxOfficerSlots() {
        return maxOfficerSlots;
    }

    public int getAvailableOfficerSlots() {
        return maxOfficerSlots - officers.size();
    }

    public List<Application> getApplications() {
        return applications;
    }

    public List<HDBOfficer> getOfficers() {
        return officers;
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public Enquiry getEnquiryById(int enquiryId) {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryId() == enquiryId) {
                return enquiry;
            }
        }
        return null;
    }

    // Utility / Checkers
    public boolean hasApplicant(Applicant applicant) {
        return applications.stream()
            .anyMatch(app -> app.getApplicant().equals(applicant));
    }

    public boolean isApplicationPeriodOver() {
        LocalDate today = LocalDate.now();
        return today.isAfter(applicationEndDate);
    }

    public boolean isClosed() {
        return isApplicationPeriodOver();
    }
    
    // Check if a project is available for registration (e.g., not yet fully assigned or restricted)
    public boolean isAvailableForRegistration() {
        // Implement your specific logic here, e.g.
        // - Check if the project is still accepting officer registrations
        // - Check if the project has already reached its max number of officers
        // - Other business rules
        return !this.isFull() && this.hasAvailableOfficerSlots();  // Example conditions
    }

    // Additional methods for checking specific conditions
    private boolean isFull() {
        // Check if the project has reached its max capacity for applicants or officers
        return this.getOfficers().size() >= this.getMaxOfficerSlots();
    }

    private boolean hasAvailableOfficerSlots() {
        // Check if there are available officer slots left for registration
        return this.getMaxOfficerSlots() > this.getOfficers().size();
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

    public static LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    // toString override
    @Override
    public String toString() {
        return "Project ID: " + projectID +
                "\nProject Name: " + projectName +
                "\nNeighbourhood: " + neighbourhood +
                "\n2-Room Units: " + getNumUnits(FlatType.TWO_ROOM) +
                "\n3-Room Units: " + getNumUnits(FlatType.THREE_ROOM) +
                "\nApplication Period: " + applicationStartDate + " to " + applicationEndDate +
                "\nVisible: " + (visibility ? "Yes" : "No") +
                "\nHDB Manager: " + manager.getName() +
                "\nAvailable Officer Slots: " + maxOfficerSlots;
    }
}
