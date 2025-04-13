package old.Database;

import java.util.ArrayList;
import java.util.List;

import model.project.Project;

public class ProjectDatabase {
    private static List<Project> projectList = new ArrayList<>();  // Holds all projects

    // Static block to initialize projects (with name, flat type, and visibility)
    static {
        projectList.add(new Project("Acacia Breeze", "2-Room", true));  // Visible project
        projectList.add(new Project("Maple Grove", "3-Room", false));   // Not visible project
        projectList.add(new Project("Sunny Heights", "2-Room", true));  // Visible project
    }

    // Method to retrieve all projects
    public static List<Project> getProjects() {
        return new ArrayList<>(projectList);
    }

    // Method to find a project by name
    public static Project findProjectByName(String name) {
        for (Project project : projectList) {
            if (project.getProjectName().equalsIgnoreCase(name)) {
                return project;
            }
        }
        return null; // Project not found
    }

      
}

