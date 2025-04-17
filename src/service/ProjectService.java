package service;

import java.time.LocalDate;
import java.util.List;

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
}
