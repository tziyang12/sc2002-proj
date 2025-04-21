package model.user;

import model.user.enums.MaritalStatus;
import model.project.Project;

import java.util.ArrayList;
import java.util.List;

public class HDBManager extends User {

    private List<Project> managedProjects;

    public HDBManager(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.managedProjects = new ArrayList<>();
    }

    
    /** 
     * @return String
     */
    @Override
    public String getRole() {
        return "HDB Manager";
    }

    public List<Project> getManagedProjects() {
        return managedProjects;
    }

    public void addManagedProject(Project project) {
        this.managedProjects.add(project);
    }

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
