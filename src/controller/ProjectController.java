package controller;

import java.util.List;
import java.util.Map;

import model.project.FlatType;
import model.project.Project;
import model.user.Applicant;

public class ProjectController {
    public void listEligibleProjects(Applicant applicant, List<Project> projects) {
        System.out.println("Available Projects:");

        for (Project project : projects) {
            if (!project.isVisible()) continue;

            System.out.println("== " + project.getProjectName() + " (" + project.getNeighbourhood() + ") ==");

            for (Map.Entry<FlatType, Integer> entry : project.getFlatUnits().entrySet()) {
                FlatType type = entry.getKey();
                int count = entry.getValue();

                if (applicant.isEligible(project, type)) {
                    System.out.println(" - " + type + ": " + count + " units available");
                }
            }
        }

        if (applicant.hasApplied()) {
            System.out.println("\nYou have applied for: " + applicant.getAppliedProject().getProjectName()
                + " (" + applicant.getAppliedFlatType() + ") [Status: " + applicant.getApplicationStatus() + "]");
        }
    }

}
