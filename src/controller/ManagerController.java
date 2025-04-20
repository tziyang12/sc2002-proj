package controller;

import model.project.Project;
import model.project.ProjectSearchCriteria;
import model.project.FlatType;
import model.transaction.Enquiry;
import model.transaction.OfficerProjectRegistration;
import model.transaction.OfficerRegistrationStatus;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.enums.MaritalStatus;
import model.transaction.Application;
import model.transaction.ApplicationStatus;
import service.ProjectService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerController {

    public boolean canCreateNewProject(HDBManager manager, Project newProject) {
        // Check if project name exists in ProjectRepository
        for (Project existing : ProjectService.getAllProjects()) {
            if (existing.getProjectName().equals(newProject.getProjectName())) {
                return false;
            }
        }

        // Check if the new project overlaps with any existing projects managed by the manager
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
        ProjectService.createProject(project);
    }

    public void editProject(Project project, String newName, String newNeighbourhood, int new2Room, int new3Room, double new2RoomPrice, double new3RoomPrice, LocalDate newOpen, LocalDate newClose, int newmaxOfficer, Boolean newVisible) {
        project.setProjectName(newName);
        project.setNeighbourhood(newNeighbourhood);
        project.setNumUnits(FlatType.TWO_ROOM, new2Room);
        project.setNumUnits(FlatType.THREE_ROOM, new3Room);
        project.setFlatPrice(FlatType.TWO_ROOM, new2RoomPrice);
        project.setFlatPrice(FlatType.THREE_ROOM, new3RoomPrice);
        project.setMaxOfficerSlots(newmaxOfficer);
        project.setVisible(newVisible);
        project.setApplicationPeriod(newOpen, newClose);
    }

    public void deleteProject(HDBManager manager, Project project) {
        manager.getManagedProjects().remove(project);
        ProjectService.deleteProject(project);
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

    public List<Application> generateApplicantReport(HDBManager manager, String filterCategory, String filterType) {
        System.out.println("Generating report for " + manager.getName() + "...");
        
        // Get applications for the manager's projects
        List<Application> applications = getApplicationsForManagedProjects(manager);
        if (applications.isEmpty()) {
            System.out.println("No applications found.");
            return applications;
        }
    
        // Create an instance of ProjectSearchCriteria to hold the filtering criteria
        ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
        
        // Apply filters based on the selected filter category
        if ("none".equalsIgnoreCase(filterCategory)) {
            // If no filter is selected, skip the filtering process
            System.out.println("No filter applied, displaying all applications.");
        } else {
            // Apply the filter based on the filter category and filter type
            switch (filterCategory.toLowerCase()) {
                case "maritalstatus":
                    searchCriteria.setMaritalStatusFilter(MaritalStatus.valueOf(filterType.toUpperCase()));
                    break;
                case "flattype":
                    searchCriteria.getFlatTypes().add(FlatType.valueOf(filterType.toUpperCase()));
                    break;
                case "neighbourhood":
                    searchCriteria.setNeighbourhood(filterType);
                    break;
                case "age":
                    String[] ageRange = filterType.split("-");
                    if (ageRange.length == 2) {
                        searchCriteria.setMinAge(Integer.parseInt(ageRange[0].trim()));
                        searchCriteria.setMaxAge(Integer.parseInt(ageRange[1].trim()));
                    } else {
                        System.out.println("Invalid age range format.");
                        return applications;
                    }
                    break;
                case "price":
                    searchCriteria.setSortByPriceAscending(Boolean.parseBoolean(filterType));
                    break;
                default:
                    System.out.println("Invalid filter category.");
                    return applications;
            }
        }
    
            // Filter the applications based on the criteria if filters are applied
        List<Application> filteredApplications = "none".equalsIgnoreCase(filterCategory) ? applications : filterApplications(applications, searchCriteria);
        
        // If no applications match the criteria, return the empty list
        if (filteredApplications.isEmpty()) {
            System.out.println("No applications found matching the criteria.");
        }

        return filteredApplications;
    }
    



    public List<HDBOfficer> getPendingOfficers(List<HDBOfficer> allOfficers, Project selectedProject) {
        List<HDBOfficer> pendingOfficers = new ArrayList<>();
        for (HDBOfficer officer : allOfficers) {
            for (OfficerProjectRegistration reg : officer.getRegisteredProjects()) {
                if (reg.getProject().equals(selectedProject) &&
                    reg.getRegistrationStatus() == OfficerRegistrationStatus.PENDING) {
                    pendingOfficers.add(officer);
                }
            }
        }
        return pendingOfficers;
    }
    
    private List<Application> filterApplications(List<Application> applications, ProjectSearchCriteria searchCriteria) {
        return applications.stream()
            .filter(app -> {
                boolean matchesMaritalStatus = searchCriteria.getMaritalStatusFilter() == MaritalStatus.BOTH ||
                                            app.getApplicant().getMaritalStatus() == searchCriteria.getMaritalStatusFilter();
                boolean matchesFlatType = searchCriteria.getFlatTypes().isEmpty() ||
                                        searchCriteria.getFlatTypes().contains(app.getFlatType());
                boolean matchesNeighbourhood = searchCriteria.getNeighbourhood().isEmpty() ||
                                            app.getProject().getNeighbourhood().equalsIgnoreCase(searchCriteria.getNeighbourhood());
                boolean matchesAge = app.getApplicant().getAge() >= searchCriteria.getMinAge() &&
                                    app.getApplicant().getAge() <= searchCriteria.getMaxAge();
                boolean matchesPrice = true; // Handle price sorting separately

                // Sorting by price, if enabled
                if (searchCriteria.isSortByPriceAscending()) {
                    // Sort applications based on price (implement logic here based on project price)
                    // Sort logic will be handled outside filtering for the final output, if necessary
                }

                return matchesMaritalStatus && matchesFlatType && matchesNeighbourhood && matchesAge && matchesPrice;
            })
            .collect(Collectors.toList());
    }

        
    public Project findProjectByName(String projectName, HDBManager manager) {
        for (Project project : manager.getManagedProjects()) {
            if (project.getProjectName().equals(projectName)) {
                return project;
            }
        }
        return null;
    }

}
