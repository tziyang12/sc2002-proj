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
/**
 * The {@code Project} class represents a Build-To-Order (BTO) project in the system.
 * It contains details about the project such as the project name, neighbourhood, available flat units, 
 * flat prices, application periods, assigned officers, and more. This class also manages the application,
 * officer, and enquiry data related to the project.
 */
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

    /**
     * Constructs a new {@code Project} with the specified project name, neighbourhood,
     * application start and end dates, and maximum officer slots.
     * 
     * @param projectName the name of the project
     * @param neighbourhood the neighbourhood where the project is located
     * @param applicationStartDate the start date of the application period
     * @param applicationEndDate the end date of the application period
     * @param maxOfficerSlots the maximum number of officers that can be assigned to the project
     */
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

    
    /**     // Adders

    /**
     * Adds a flat unit for the specified flat type to the project.
     * 
     * @param type the flat type
     * @param units the number of units available for the flat type
     */
    public void addFlatUnit(FlatType type, int units) {
        flatUnits.put(type, units);
    }

    /**
     * Adds a flat price for the specified flat type to the project.
     * 
     * @param type the flat type
     * @param price the price of the flat type
     */
    public void addFlatPrice(FlatType type, double price) {
        flatPrices.put(type, price);
    }

    /**
     * Adds a new officer to the project.
     * 
     * @param officer the officer to be added
     */
    public void addOfficer(HDBOfficer officer) {
        officers.add(officer);
    }

    /**
     * Adds a new application to the project.
     * 
     * @param application the application to be added
     */
    public void addApplication(Application application) {
        applications.add(application);
    }

    /**
     * Adds a new enquiry to the project.
     * 
     * @param enquiry the enquiry to be added
     */
    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    // Setters

    /**
     * Sets the project ID.
     * 
     * @param projectID the project ID to be set
     */
    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    /**
     * Sets the project name.
     * 
     * @param projectName the project name to be set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Sets the neighbourhood for the project.
     * 
     * @param neighbourhood the neighbourhood to be set
     */
    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    /**
     * Sets the number of available units for a specified flat type.
     * 
     * @param type the flat type
     * @param units the number of units to be set
     */
    public void setNumUnits(FlatType type, int units) {
        flatUnits.put(type, units);
    }

    /**
     * Sets the price for a specified flat type.
     * 
     * @param type the flat type
     * @param price the price to be set
     */
    public void setFlatPrice(FlatType type, double price) {
        flatPrices.put(type, price);
    }

    /**
     * Sets the application period start and end dates.
     * 
     * @param startDate the application start date
     * @param endDate the application end date
     */
    public void setApplicationPeriod(LocalDate startDate, LocalDate endDate) {
        this.applicationStartDate = startDate;
        this.applicationEndDate = endDate;
    }

    /**
     * Sets the visibility status of the project.
     * 
     * @param visibility the visibility status to be set
     */
    public void setVisible(boolean visibility) {
        this.visibility = visibility;
    }

    /**
     * Sets the manager for the project.
     * 
     * @param manager the manager to be set
     */
    public void setManager(HDBManager manager) {
        this.manager = manager;
    }

    /**
     * Sets the maximum number of officer slots for the project.
     * 
     * @param maxOfficerSlots the maximum officer slots to be set
     */
    public void setMaxOfficerSlots(int maxOfficerSlots) {
        this.maxOfficerSlots = maxOfficerSlots;
    }

    // Getters

    /**
     * Gets the project ID.
     * 
     * @return the project ID
     */
    public int getProjectID() {
        return projectID;
    }

    /**
     * Gets the project name.
     * 
     * @return the project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets the neighbourhood of the project.
     * 
     * @return the neighbourhood
     */
    public String getNeighbourhood() {
        return neighbourhood;
    }

    /**
     * Gets the number of available units for a specified flat type.
     * 
     * @param type the flat type
     * @return the number of available units
     */
    public int getNumUnits(FlatType type) {
        return flatUnits.getOrDefault(type, 0);
    }

    /**
     * Gets the map of all available flat units for the project.
     * 
     * @return a map of flat types and their respective unit counts
     */
    public Map<FlatType, Integer> getFlatUnits() {
        return flatUnits;
    }

    /**
     * Gets the map of all flat prices for the project.
     * 
     * @return a map of flat types and their respective prices
     */
    public Map<FlatType, Double> getFlatPrices() {
        return flatPrices;
    }

    /**
     * Gets the price of a specified flat type.
     * 
     * @param type the flat type
     * @return the price of the flat type
     */
    public double getFlatPrice(FlatType type) {
        return flatPrices.getOrDefault(type, 0.0);
    }

    /**
     * Gets the application start date for the project.
     * 
     * @return the application start date
     */
    public LocalDate getApplicationStartDate() {
        return applicationStartDate;
    }

    /**
     * Gets the application end date for the project.
     * 
     * @return the application end date
     */
    public LocalDate getApplicationEndDate() {
        return applicationEndDate;
    }

    /**
     * Checks if the project is visible (based on its visibility and application period).
     * 
     * @return {@code true} if the project is visible, {@code false} otherwise
     */
    public boolean isVisible() {
        return visibility && !isApplicationPeriodOver();
    }

    /**
     * Gets the manager of the project.
     * 
     * @return the project manager
     */
    public HDBManager getManager() {
        return manager;
    }

    /**
     * Gets the maximum number of officer slots for the project.
     * 
     * @return the maximum officer slots
     */
    public int getMaxOfficerSlots() {
        return maxOfficerSlots;
    }

    /**
     * Gets the number of available officer slots for the project.
     * 
     * @return the available officer slots
     */
    public int getAvailableOfficerSlots() {
        return maxOfficerSlots - officers.size();
    }

    /**
     * Gets the list of applications for the project.
     * 
     * @return a list of applications
     */
    public List<Application> getApplications() {
        return applications;
    }

    /**
     * Gets the list of officers assigned to the project.
     * 
     * @return a list of officers
     */
    public List<HDBOfficer> getOfficers() {
        return officers;
    }

    /**
     * Gets the list of enquiries related to the project.
     * 
     * @return a list of enquiries
     */
    public List<Enquiry> getEnquiries() {
        return enquiries;
    }
    /**
     * Retrieves an enquiry by its unique enquiry ID.
     * 
     * @param enquiryId the ID of the enquiry to retrieve
     * @return the {@code Enquiry} object matching the provided ID, or {@code null} if no matching enquiry is found
     */
    public Enquiry getEnquiryById(int enquiryId) {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryId() == enquiryId) {
                return enquiry;
            }
        }
        return null;
    }

    /**
     * Checks if the project has an application from the specified applicant.
     * 
     * @param applicant the applicant to check for
     * @return {@code true} if the applicant has applied to the project, {@code false} otherwise
     */
    public boolean hasApplicant(Applicant applicant) {
        return applications.stream()
            .anyMatch(app -> app.getApplicant().equals(applicant));
    }

    /**
     * Checks if the application period for the project is over.
     * 
     * @return {@code true} if the application period has ended, {@code false} otherwise
     */
    public boolean isApplicationPeriodOver() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(applicationEndDate) || !today.isAfter(applicationStartDate);
    }

    /**
     * Checks if the project is closed, meaning the application period has ended.
     * 
     * @return {@code true} if the project is closed, {@code false} otherwise
     */
    public boolean isClosed() {
        return isApplicationPeriodOver();
    }

    /**
     * Checks if the project is available for registration, meaning it has available officer slots
     * and is not yet full.
     * 
     * @return {@code true} if the project is available for officer registration, {@code false} otherwise
     */
    public boolean isAvailableForRegistration() {
        return !this.isFull() && this.hasAvailableOfficerSlots();  // Example conditions
    }

    /**
     * Checks if the project has reached its maximum capacity for officers.
     * 
     * @return {@code true} if the project has reached the maximum number of officers, {@code false} otherwise
     */
    private boolean isFull() {
        return this.getOfficers().size() >= this.getMaxOfficerSlots();
    }

    /**
     * Checks if there are available officer slots left for registration.
     * 
     * @return {@code true} if there are available officer slots, {@code false} otherwise
     */
    private boolean hasAvailableOfficerSlots() {
        return this.getMaxOfficerSlots() > this.getOfficers().size();
    }

    /**
     * Decreases the number of available flats for the specified flat type by 1.
     * If no flats are available for the given type, it outputs a message indicating so.
     * 
     * @param flatType the flat type for which the remaining flats should be decremented
     */
    public void decreaseRemainingFlats(FlatType flatType) {
        if (flatUnits.containsKey(flatType) && flatUnits.get(flatType) > 0) {
            int remainingFlats = flatUnits.get(flatType);
            flatUnits.put(flatType, remainingFlats - 1); // Decrement available flats
            System.out.println("One flat of type " + flatType + " has been booked. Remaining flats: " + (remainingFlats - 1));
        } else {
            System.out.println("No flats available for type " + flatType + " in this project.");
        }
    }

    /**
     * Parses a date string in the format "yyyy-MM-dd" into a {@code LocalDate} object.
     * 
     * @param dateStr the date string to be parsed
     * @return the parsed {@code LocalDate} object
     */
    public static LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * Returns a string representation of the project for display purposes, including details
     * such as the project name, neighbourhood, flat units, application period, visibility, and manager.
     * 
     * @return a string containing the project details
     */
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
