// In data/ProjectRepository.java
package data;

import model.project.Project;
import java.util.ArrayList;
import java.util.List;
/**
 * The ProjectRepository class is responsible for managing the list of projects in memory.
 * It provides methods to add, remove, and retrieve projects, as well as to reset the list with a new set of projects.
 */
public class ProjectRepository {
    /** 
     * A list that holds all the projects.
     */
    private static final List<Project> allProjects = new ArrayList<>();

    
    /**
     * Adds a project to the repository.
     * 
     * @param project The project to be added to the repository.
     */
    public static void addProject(Project project) {
        allProjects.add(project);
    }
    /**
     * Removes a project from the repository.
     * 
     * @param project The project to be removed from the repository.
     */
    public static void removeProject(Project project) {
        allProjects.remove(project);
    }
    /**
     * Retrieves all projects stored in the repository.
     * 
     * @return A list of all projects in the repository.
     */
    public static List<Project> getAllProjects() {
        return allProjects;
    }
    /**
     * Replaces the current list of projects with a new list.
     * 
     * @param projects The new list of projects to replace the current list.
     */
    public static void setAllProjects(List<Project> projects) {
        allProjects.clear();
        allProjects.addAll(projects);
    }
}
