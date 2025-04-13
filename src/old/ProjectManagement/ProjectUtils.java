package old.ProjectManagement;

import java.util.*;

import model.project.Project;

import java.time.LocalDate;

public class ProjectUtils {

    // Validate if a project has a valid date range
    public static boolean isValidProjectDateRange(LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(endDate);
    }

    // Check if the project name is unique (in the context of the given projects list)
    public static boolean isUniqueProjectName(String projectName, List<Project> projects) {
        for (Project project : projects) {
            if (project.getProjectName().equalsIgnoreCase(projectName)) {
                return false; // Duplicate name found
            }
        }
        return true; // Unique name
    }

    // Calculate the duration of a project's application period in days
    public static long calculateProjectDuration(LocalDate startDate, LocalDate endDate) {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }

    // Filter projects by neighbourhood
    public static List<Project> filterProjectsByNeighbourhood(List<Project> projects, String neighbourhood) {
        List<Project> filteredProjects = new ArrayList<>();
        for (Project project : projects) {
            if (project.getNeighbourhood().equalsIgnoreCase(neighbourhood)) {
                filteredProjects.add(project);
            }
        }
        return filteredProjects;
    }

    // Sort projects by application start date
    public static List<Project> sortProjectsByStartDate(List<Project> projects) {
        projects.sort(Comparator.comparing(Project::getApplicationStartDate));
        return projects;
    }

    // Sort projects by project name
    public static List<Project> sortProjectsByName(List<Project> projects) {
        projects.sort(Comparator.comparing(Project::getProjectName));
        return projects;
    }

    // Example: Helper method to check if a project is open for application
    public static boolean isProjectOpenForApplication(Project project) {
        LocalDate today = LocalDate.now();
        return !project.getApplicationStartDate().isAfter(today) && !project.getApplicationEndDate().isBefore(today);
    }
}
