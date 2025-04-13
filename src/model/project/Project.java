package model.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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

    // Constructor that accepts LocalDate for application start and end dates
    public Project(String projectName, String neighbourhood, LocalDate applicationStartDate, LocalDate applicationEndDate, String managerUserID) {
        this.projectName = projectName;
        this.neighbourhood = neighbourhood;
        this.applicationStartDate = applicationStartDate;
        this.applicationEndDate = applicationEndDate;
        this.managerUserID = managerUserID;
        this.visibility = true;
        this.flatUnits = new HashMap<>();
    }

    // Method to add flat units (number of units for each flat type)
    public void addFlatUnit(FlatType type, int units) {
        flatUnits.put(type, units);
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
}
