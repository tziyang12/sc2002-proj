package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import data.ProjectRepository;
import model.project.FlatType;
import model.project.Project;
import model.project.ProjectSearchCriteria;
import model.user.Applicant;
import model.user.HDBOfficer;

/**
 * Service class responsible for handling operations related to BTO projects such as date conflict validation, 
 * project creation, deletion, searching, filtering, and sorting projects based on applicant criteria.
 */
public class ProjectService {
    /**
     * Constructs a ProjectService instance.
     * This constructor can be used for initialization if needed.
     */
    public ProjectService() {
        // Constructor can be used for initialization if needed
    }
    /**
     * Checks if a new project conflicts with an officer's already assigned projects based on the application dates.
     * 
     * @param project The project to check for date conflict.
     * @param officer The HDB officer to check against their assigned projects.
     * @return true if there is a date conflict, false otherwise.
     */
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

    /**
     * Finds a project by its name from a list of projects.
     * 
     * @param name The name of the project to find.
     * @param projects The list of projects to search in.
     * @return The project with the matching name or null if no such project exists.
     */
    public static Project findByName(String name, List<Project> projects) {
        return projects.stream()
                .filter(p -> p.getProjectName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Parses a string input as a LocalDate object.
     * 
     * @param dateInput The date string input.
     * @param currentDate The default date to return if parsing fails.
     * @return The parsed LocalDate or the default currentDate if parsing fails.
     */
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

    /**
     * Parses a string input as a boolean.
     * 
     * @param input The string input.
     * @param defaultValue The default value to return if the input is invalid.
     * @return The parsed boolean value or the defaultValue if parsing fails.
     */
    public static boolean parseBoolean(String input, boolean defaultValue) {
        if (input.isBlank() || (!input.equalsIgnoreCase("TRUE") && !input.equalsIgnoreCase("FALSE"))) {
            System.out.println("Invalid input. Defaulting to " + defaultValue);
            return defaultValue;
        }
        return Boolean.parseBoolean(input);
    }

    /**
     * Parses a string input as an integer.
     * 
     * @param input The string input.
     * @param defaultValue The default value to return if parsing fails.
     * @return The parsed integer value or the defaultValue if parsing fails.
     */
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

    /**
     * Parses a string input as a double.
     * 
     * @param input The string input.
     * @param defaultValue The default value to return if parsing fails.
     * @return The parsed double value or the defaultValue if parsing fails.
     */
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

    /**
     * Adds a new project to the project repository.
     * 
     * @param project The project to add.
     */
    public static void createProject(Project project) {
        ProjectRepository.addProject(project);
    }

    /**
     * Removes a project from the project repository.
     * 
     * @param project The project to remove.
     */
    public static void deleteProject(Project project) {
        ProjectRepository.removeProject(project);
    }

    /**
     * Retrieves all projects from the project repository.
     * 
     * @return A list of all projects.
     */
    public static List<Project> getAllProjects() {
        return ProjectRepository.getAllProjects();
    }

    /**
     * Gets all available projects for an officer that they are not currently assigned to.
     * 
     * @param officer The officer to check available projects for.
     * @param projectList The list of all projects.
     * @return A list of projects that the officer is not assigned to.
     */
    public List<Project> getAvailableProjectsForOfficer(HDBOfficer officer, List<Project> projectList) {
        List<Project> availableProjects = new ArrayList<>();
        for (Project project : projectList) {
            if (!project.hasApplicant(officer) && officer.getAssignedProjects().stream().noneMatch(p -> p.getProjectName().equals(project.getProjectName())) && !hasDateConflict(project, officer)) {
                availableProjects.add(project);
            }
        }
        return availableProjects;
    }

    /**
     * Validates and adjusts the maximum number of officers allowed for a project.
     * 
     * @param maxOfficers The desired maximum number of officers.
     * @param project The project to validate the officer count for.
     * @return The validated maximum number of officers.
     */
    public static int validateMaxOfficers(int maxOfficers, Project project) {
        int currentAssigned = project.getOfficers().size();
        if (maxOfficers < 1 || maxOfficers > 10 || maxOfficers < currentAssigned) {
            return Math.clamp(currentAssigned, maxOfficers, 10);
        }
        return maxOfficers;
    }

    /**
     * Searches for projects based on an applicant's search criteria.
     * 
     * @param applicant The applicant whose search criteria will be used.
     * @return A list of projects that match the search criteria.
     */
    public List<Project> searchProjects(Applicant applicant) {
        ProjectSearchCriteria criteria = applicant.getSearchCriteria();
        List<Project> allProjects = ProjectRepository.getAllProjects();
    
        return allProjects.stream()
            // Filter by neighbourhood
            .filter(p -> criteria.getNeighbourhood() == null || criteria.getNeighbourhood().isEmpty()
                || p.getNeighbourhood().equalsIgnoreCase(criteria.getNeighbourhood()))
    
            // Filter by flat types (any one match)
            .filter(p -> criteria.getFlatTypes().isEmpty() ||
                criteria.getFlatTypes().stream().anyMatch(ft -> p.getFlatUnits().containsKey(ft)))
    
            // Sort by price (based on first matching flat type)
            .sorted((p1, p2) -> {
                if (!criteria.isSortByPriceAscending()) return 0;
                Optional<FlatType> typeToCompare = criteria.getFlatTypes().stream()
                    .filter(ft -> p1.getFlatUnits().containsKey(ft) && p2.getFlatUnits().containsKey(ft))
                    .findFirst();
    
                if (typeToCompare.isEmpty()) return 0;
    
                double price1 = p1.getFlatPrice(typeToCompare.get());
                double price2 = p2.getFlatPrice(typeToCompare.get());
                return Double.compare(price1, price2);
            })
    
            .collect(Collectors.toList());
    }

    /**
     * Filters and sorts projects based on an applicant's criteria.
     * 
     * @param applicant The applicant whose search criteria will be used.
     * @param projects The list of projects to filter and sort.
     * @param criteria The search criteria to filter and sort projects by.
     * @return A list of filtered and sorted projects.
     */
    public List<Project> filterAndSortProjects(Applicant applicant, List<Project> projects, ProjectSearchCriteria criteria) {
        return projects.stream()
            .filter(project -> isProjectEligible(applicant, project, criteria))
            .sorted((p1, p2) -> compareProjects(p1, p2, criteria))
            .collect(Collectors.toList());
    }

    /**
     * Gets the lowest available price for a project based on its available flat types.
     * 
     * @param project The project to check the lowest available price for.
     * @return The lowest available price, or 0 if no available prices exist.
     */
    public double getLowestAvailablePrice(Project project) {
        double min = Double.MAX_VALUE;
        for (FlatType type : FlatType.values()) {
            int supply = project.getNumUnits(type);
            double price = project.getFlatPrice(type);
            if (supply > 0 && price < min) {
                min = price;
            }
        }
        return min == Double.MAX_VALUE ? 0 : min;
    }

    /**
     * Compares two projects based on their price and project name.
     * 
     * @param p1 The first project to compare.
     * @param p2 The second project to compare.
     * @param criteria The search criteria used for comparison.
     * @return A negative integer, zero, or a positive integer as the first project is less than, equal to, or greater than the second project.
     */
    public int compareProjects(Project p1, Project p2, ProjectSearchCriteria criteria) {
        if (criteria.isSortByPriceAscending()) {
            double p1Price = getLowestAvailablePrice(p1);
            double p2Price = getLowestAvailablePrice(p2);
            return Double.compare(p1Price, p2Price);
        } else {
            return p2.getProjectName().compareToIgnoreCase(p1.getProjectName());
        }
    }

    /**
     * Determines if a project is eligible based on the applicant's criteria.
     * 
     * @param applicant The applicant to check eligibility for.
     * @param project The project to check eligibility for.
     * @param criteria The search criteria to match against.
     * @return true if the project is eligible for the applicant, false otherwise.
     */
    public boolean isProjectEligible(Applicant applicant, Project project, ProjectSearchCriteria criteria) {
        boolean isApplicantProject = applicant.hasApplied() && applicant.getApplication().getProject().equals(project);
        if (!project.isVisible() && !isApplicantProject) return false;

        if (!criteria.getNeighbourhood().isEmpty() &&
            !project.getNeighbourhood().equalsIgnoreCase(criteria.getNeighbourhood())) {
            return false;
        }

        Set<FlatType> selectedTypes = criteria.getFlatTypes();
        if (!selectedTypes.isEmpty()) {
            boolean hasMatch = selectedTypes.stream().anyMatch(ft -> project.getNumUnits(ft) > 0);
            if (!hasMatch) return false;
        }

        return true;
    }

    /**
     * Checks if a project is visible to the applicant.
     * 
     * @param applicant The applicant to check for visibility.
     * @param project The project to check visibility for.
     * @return true if the project is visible to the applicant, false otherwise.
     */
    public boolean isProjectVisibleToApplicant(Applicant applicant, Project project) {
        boolean isApplicantProject = applicant.hasApplied() && applicant.getApplication().getProject().equals(project);
        return project.isVisible() || isApplicantProject;
    }
}
