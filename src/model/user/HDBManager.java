package model.user;

import model.user.enums.MaritalStatus;
import model.project.Project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HDBManager extends User {

    private List<Project> managedProjects;

    public HDBManager(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.managedProjects = new ArrayList<>();
    }

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
        return super.toString() + "\nRole: HDB Manager";
    }
}
