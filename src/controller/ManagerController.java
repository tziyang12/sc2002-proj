package controller;

import model.project.Project;
import model.project.FlatType;
import model.transaction.Enquiry;
import model.user.HDBManager;
import model.user.HDBOfficer;
import service.RegistrationService;
import model.transaction.Application;
import model.transaction.ApplicationStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManagerController {

    public boolean canCreateNewProject(HDBManager manager, Project newProject) {
        for (Project existing : manager.getManagedProjects()) {
            LocalDate latestStart = newProject.getApplicationStartDate().isAfter(existing.getApplicationStartDate())
                    ? newProject.getApplicationStartDate() : existing.getApplicationStartDate();
            LocalDate earliestEnd = newProject.getApplicationEndDate().isBefore(existing.getApplicationEndDate())
                    ? newProject.getApplicationEndDate() : existing.getApplicationEndDate();

            if (!latestStart.isAfter(earliestEnd)) return false;
        }
        return true;
    }

    public void createProject(HDBManager manager, Project project) {
        manager.addManagedProject(project);
        // You could also call ProjectService.addProject(project) here
    }

    public void editProject(Project project, String newName, String newNeighbourhood, int new2Room, int new3Room, LocalDate newOpen, LocalDate newClose) {
        project.setProjectName(newName);
        project.setNeighbourhood(newNeighbourhood);
        project.setNumUnits(FlatType.TWO_ROOM, new2Room);
        project.setNumUnits(FlatType.THREE_ROOM, new3Room);
        project.setApplicationPeriod(newOpen, newClose);
    }

    public void deleteProject(HDBManager manager, Project project, List<Project> allProjects) {
        allProjects.remove(project);
        manager.getManagedProjects().remove(project);
    }

    public void toggleVisibility(Project project, boolean visible) {
        project.setVisible(visible);
    }

    public void approveOfficer(Project project, HDBOfficer officer) {
        if (project.getAvailableOfficerSlots() > 0) {
            project.addOfficer(officer);
        }
    }

    // public void rejectOfficer(Project project, HDBOfficer officer) {
    //     project.removePendingOfficer(officer);
    // }

    public List<Application> getApplicationsForManagedProjects(HDBManager manager) {
        List<Application> result = new ArrayList<>();
        List<Project> managedProjects = manager.getManagedProjects();

        for (Application app : RegistrationService.getAllApplication()) {
            if (managedProjects.contains(app.getProject())) {
                result.add(app);
            }
        }

        return result;
    }

    public void approveApplication(Application app) {
        app.setStatus(ApplicationStatus.SUCCESSFUL);
        // maybe update booking status too?
    }


    public void approveApplication(Project project, Application app) {
        FlatType type = app.getFlatType();
        if (project.getNumUnits(type) > 0) {
            project.decreaseRemainingFlats(type);
            app.approve();
        }
    }

    public void rejectApplication(Application app) {
        app.reject();
    }

    public void approveWithdrawal(Application app) {
        app.withdraw();
    }

    public void rejectWithdrawal(Application app) {
        app.cancelWithdrawalRequest();
    }

    public List<Enquiry> getAllEnquiries(List<Enquiry> all) {
        return all;
    }
}
