package controller;

import model.project.Project;
import model.project.FlatType;
import model.transaction.Enquiry;
import model.transaction.OfficerProjectRegistration;
import model.transaction.OfficerRegistrationStatus;
import model.user.HDBManager;
import model.user.HDBOfficer;
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

    public List<Project> getManagedProjects(HDBManager manager) {
        return manager.getManagedProjects();
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

    // Officer Registration Logic

    public void viewOfficerRegistrations(List<Project> managerProjects, List<HDBOfficer> allOfficers) {
        System.out.println("======= Officer Registrations for Your Projects =======");

        for (Project project : managerProjects) {
            System.out.println("\nProject: " + project.getProjectName());

            boolean hasAnyRegistration = false;

            for (HDBOfficer officer : allOfficers) {
                for (OfficerProjectRegistration registration : officer.getRegisteredProjects()) {
                    if (registration.getProject().equals(project)) {
                        System.out.println("Officer: " + officer.getName() +
                                           " | Status: " + registration.getRegistrationStatus());
                        hasAnyRegistration = true;
                    }
                }
            }

            if (!hasAnyRegistration) {
                System.out.println("No officers have registered for this project.");
            }
        }
    }

    public void approveOfficer(Project project, HDBOfficer officer) {
        if (project.getAvailableOfficerSlots() > 0) {
            project.addOfficer(officer);
            officer.assignProject(project);
            officer.setProjectRegistrationStatus(project, OfficerRegistrationStatus.APPROVED);
        }
    }

    public void rejectOfficer(Project project, HDBOfficer officer) {
        officer.setProjectRegistrationStatus(project, OfficerRegistrationStatus.REJECTED);
    }

    // Application Logic
    
    public List<Application> getApplicationsForManagedProjects(HDBManager manager) {
        List<Application> result = new ArrayList<>();
    
        for (Project project : manager.getManagedProjects()) {
            result.addAll(project.getApplications());
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

    public List<Enquiry> getAllEnquiries(List<Project> projects) {
        List<Enquiry> allEnquiries = new ArrayList<>();
        for (Project project : projects) {
            allEnquiries.addAll(project.getEnquiries());
        }
        return allEnquiries;
    }
    
}
