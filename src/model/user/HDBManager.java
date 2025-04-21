package model.user;

import model.project.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an HDB Manager in the BTO system.
 * Manages multiple BTO projects and has elevated privileges over project administration.
 */
public class HDBManager extends User {

    private List<Project> managedProjects;

    /**
     * Constructs an HDB Manager with the given details.
     *
     * @param name the manager's full name
     * @param nric the manager's NRIC
     * @param password the manager's account password
     * @param age the manager's age
     * @param maritalStatus the manager's marital status
     */
    public HDBManager(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.managedProjects = new ArrayList<>();
    }

    /**
     * Gets the role of this user.
     *
     * @return the string "HDB Manager"
     */
    @Override
    public String getRole() {
        return "HDB Manager";
    }

    /**
     * Retrieves the list of BTO projects managed by this manager.
     *
     * @return a list of managed projects
     */
    public List<Project> getManagedProjects() {
        return managedProjects;
    }

    /**
     * Adds a BTO project to the list of projects managed by this manager.
     *
     * @param project the project to add
     */
    public void addManagedProject(Project project) {
        this.managedProjects.add(project);
    }

    /**
     * Returns a string representation of the HDB Manager, including NRIC, age, marital status,
     * and the names of managed projects.
     *
     * @return a formatted string representation of the manager
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[HDB Officer] NRIC: %s | Age: %d | Marital Status: %s | Managed Projects: ",
                getNric(), getAge(), getMaritalStatus()));
        if (managedProjects.isEmpty()) {
            sb.append("None");
        } else {
            for (Project p : managedProjects) {
                sb.append(p.getProjectName()).append(", ");
            }
            sb.setLength(sb.length() - 2); // remove trailing comma
        }
        return sb.toString();
    }
}
