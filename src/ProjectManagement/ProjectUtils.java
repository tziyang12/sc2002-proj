package ProjectManagement;

import java.util.*;
import java.time.LocalDate;

public class ProjectUtils {

    // Validate if a project has a valid date range
    public static boolean isValidProjectDateRange(LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(endDate);
    }

    // Check if the project name is unique (in the context of the given projects list)
    public static boolean isUniqueProjectName(String projectName, List<BTOProject> projects) {
        for (BTOProject project : projects) {
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
    public static List<BTOProject> filterProjectsByNeighbourhood(List<BTOProject> projects, String neighbourhood) {
        List<BTOProject> filteredProjects = new ArrayList<>();
        for (BTOProject project : projects) {
            if (project.getNeighbourhood().equalsIgnoreCase(neighbourhood)) {
                filteredProjects.add(project);
            }
        }
        return filteredProjects;
    }

    // Sort projects by application start date
    public static List<BTOProject> sortProjectsByStartDate(List<BTOProject> projects) {
        projects.sort(Comparator.comparing(BTOProject::getApplicationStartDate));
        return projects;
    }

    // Sort projects by project name
    public static List<BTOProject> sortProjectsByName(List<BTOProject> projects) {
        projects.sort(Comparator.comparing(BTOProject::getProjectName));
        return projects;
    }

    // Example: Helper method to check if a project is open for application
    public static boolean isProjectOpenForApplication(BTOProject project) {
        LocalDate today = LocalDate.now();
        return !project.getApplicationStartDate().isAfter(today) && !project.getApplicationEndDate().isBefore(today);
    }
}
