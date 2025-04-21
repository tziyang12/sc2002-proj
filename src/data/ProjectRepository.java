// In data/ProjectRepository.java
package data;

import model.project.Project;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {
    private static final List<Project> allProjects = new ArrayList<>();

    
    /** 
     * @param project
     */
    public static void addProject(Project project) {
        allProjects.add(project);
    }

    public static void removeProject(Project project) {
        allProjects.remove(project);
    }

    public static List<Project> getAllProjects() {
        return allProjects;
    }

    public static void setAllProjects(List<Project> projects) {
        allProjects.clear();
        allProjects.addAll(projects);
    }
}
