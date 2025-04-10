package ProjectManagement;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BTOProject {

    private String projectID;
    private String projectName;
    private String neighbourhood;
    private Map<FlatType, Integer> flatUnits;  // e.g. 2-room: 50, 3-room: 40
    private LocalDate applicationStartDate;
    private LocalDate applicationEndDate;
    private String managerUserID; // HDB Manager who created this
    private boolean visibility; // true = visible, false = hidden
    private int availableOfficerSlots = 10;

    public BTOProject(String projectName, String neighbourhood, int num2Room, int num3Room,
                      LocalDate applicationStartDate, LocalDate applicationEndDate, String managerUserID) {
        this.projectID = UUID.randomUUID().toString();
        this.projectName = projectName;
        this.neighbourhood = neighbourhood;
        this.applicationStartDate = applicationStartDate;
        this.applicationEndDate = applicationEndDate;
        this.managerUserID = managerUserID;
        this.visibility = true;

        this.flatUnits = new HashMap<>();
        this.flatUnits.put(FlatType.TWO_ROOM, num2Room);
        this.flatUnits.put(FlatType.THREE_ROOM, num3Room);
    }

    // Getters
    public String getProjectID() {
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

    // Setters (used for editProject)
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public void setNumUnits(FlatType type, int count) {
        flatUnits.put(type, count);
    }

    public void setApplicationPeriod(LocalDate start, LocalDate end) {
        this.applicationStartDate = start;
        this.applicationEndDate = end;
    }

    public void setVisible(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean assignOfficerSlot() {
        if (availableOfficerSlots > 0) {
            availableOfficerSlots--;
            return true;
        }
        return false;
    }

    public void releaseOfficerSlot() {
        if (availableOfficerSlots < 10) {
            availableOfficerSlots++;
        }
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
}
