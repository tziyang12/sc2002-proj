package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import data.ProjectRepository;
import model.project.Project;
import model.project.ProjectSearchCriteria;
import model.user.Applicant;
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
        if (!ValidationService.isValidDate(dateInput)) {
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

    public List<Project> getAvailableProjectsForOfficer(HDBOfficer officer, List<Project> projectList) {
        List<Project> availableProjects = new ArrayList<>();
        for (Project project : projectList) {
            if (!project.hasApplicant(officer) && officer.getAssignedProjects().stream().noneMatch(p -> p.getProjectName().equals(project.getProjectName()))) {
                availableProjects.add(project);
            }
        }
        return availableProjects;
    }

    public static int validateMaxOfficers(int maxOfficers, Project project) {
        int currentAssigned = project.getOfficers().size();
        if (maxOfficers < 1 || maxOfficers > 10 || maxOfficers < currentAssigned) {
            return Math.clamp(currentAssigned, maxOfficers, 10);
        }
        return maxOfficers;
    }

    public List<Project> searchProjects(ProjectSearchCriteria criteria, Applicant applicant) {
        List<Project> allProjects = ProjectRepository.getAllProjects();

        return allProjects.stream()
            .filter(Project::isVisible) // only visible projects
            .filter(p -> criteria.getNeighbourhood() == null || p.getNeighbourhood().equalsIgnoreCase(criteria.getNeighbourhood()))
            .filter(p -> criteria.getFlatType() == null || p.getFlatUnits().containsKey(criteria.getFlatType()))
            .filter(p -> applicant == null || applicant.isEligible(p, criteria.getFlatType())) // eligibility by marital status, age
            .sorted((p1, p2) -> {
                if (!criteria.isSortByPriceAscending()) return 0;
                double price1 = p1.getFlatPrice(criteria.getFlatType());
                double price2 = p2.getFlatPrice(criteria.getFlatType());
                return Double.compare(price1, price2);
            })
            .collect(Collectors.toList());
    }

}
