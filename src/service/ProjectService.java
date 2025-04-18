package service;

import java.time.LocalDate;
import java.util.List;

import Data.ProjectRepository;
import model.project.Project;
import model.user.HDBOfficer;

public class ProjectService {
    public boolean hasDateConflict(Project project, HDBOfficer officer) {
        LocalDate newStart = project.getApplicationStartDate();
        LocalDate newEnd = project.getApplicationEndDate();

        for (Project assignedProject : officer.getAssignedProjects()) {
            LocalDate start = assignedProject.getApplicationStartDate();
            LocalDate end = assignedProject.getApplicationEndDate();

            if (!(newEnd.isBefore(start) || newStart.isAfter(end))) {
                return true;
            }
        }
        return false;
    }

    public static Project findByName(String name, List<Project> projects) {
        return projects.stream()
                .filter(p -> p.getProjectName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static LocalDate parseDate(String dateInput, LocalDate currentDate) {
        if (dateInput.isBlank()) {
            return currentDate;
        }
        // Validate date format (YYYY-MM-DD)
        if (!dateInput.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Invalid date format. Defaulting to " + currentDate);
            return currentDate;
        }
        return LocalDate.parse(dateInput);
    }

    public static boolean parseBoolean(String input, boolean defaultValue) {
        if (input.isBlank() || (!input.equalsIgnoreCase("TRUE") && !input.equalsIgnoreCase("FALSE"))) {
            System.out.println("Invalid input. Defaulting to " + defaultValue);
            return defaultValue;
        }
        return Boolean.parseBoolean(input);
    }

    public static int parseInt(String input, int defaultValue) {
        if (input.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Defaulting to " + defaultValue);
            return defaultValue;
        }
    }

    public static double parseDouble(String input, double defaultValue) {
        if (input.isBlank()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Defaulting to " + defaultValue);
            return defaultValue;
        }
    }

    public static void createProject(Project project) {
        ProjectRepository.addProject(project);
    }

    public static void deleteProject(Project project) {
        ProjectRepository.removeProject(project);
    }

    public static List<Project> getAllProjects() {
        return ProjectRepository.getAllProjects();
    }
}
