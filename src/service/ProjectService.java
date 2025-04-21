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

public class ProjectService {
    
    /** 
     * @param project
     * @param officer
     * @return boolean
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


    public List<Project> filterAndSortProjects(Applicant applicant, List<Project> projects, ProjectSearchCriteria criteria) {
        return projects.stream()
            .filter(project -> isProjectEligible(applicant, project, criteria))
            .sorted((p1, p2) -> compareProjects(p1, p2, criteria))
            .collect(Collectors.toList());
    }

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


    public int compareProjects(Project p1, Project p2, ProjectSearchCriteria criteria) {
        if (criteria.isSortByPriceAscending()) {
            double p1Price = getLowestAvailablePrice(p1);
            double p2Price = getLowestAvailablePrice(p2);
            return Double.compare(p1Price, p2Price);
        } else {
            return p2.getProjectName().compareToIgnoreCase(p1.getProjectName());
        }
    }

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

    public boolean isProjectVisibleToApplicant(Applicant applicant, Project project) {
        boolean isApplicantProject = applicant.hasApplied() && applicant.getApplication().getProject().equals(project);
        return project.isVisible() || isApplicantProject;
    }
}
