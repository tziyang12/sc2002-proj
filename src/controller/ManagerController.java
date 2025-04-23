package controller;

import model.project.Project;
import model.project.ProjectSearchCriteria;
import model.project.FlatType;
import model.transaction.Enquiry;
import model.transaction.OfficerProjectRegistration;
import model.transaction.OfficerRegistrationStatus;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.MaritalStatus;
import model.transaction.Application;
import model.transaction.ApplicationStatus;
import service.ProjectService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Controller class responsible for managing BTO projects, officer registrations, and applications
 * within the HDB Management System. It includes methods to create, edit, delete, and toggle visibility
 * for projects, as well as approve/reject officer registrations and applications.
 */
public class ManagerController {
    /**
     * Constructs a ManagerController instance.
     * This constructor can be used for initialization if needed.
     */
    public ManagerController() {
        // Constructor can be used for initialization if needed
    }
    /**
     * Checks if a HDB Manager can create a new project.
     * 
     * @param manager the HDBManager instance requesting to create the project
     * @param newProject the new project to be created
     * @return boolean indicating if the new project can be created
     */
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
    /**
     * Returns the list of projects managed by the specified HDB Manager.
     *
     * @param manager the HDBManager instance whose projects are to be fetched
     * @return List of projects managed by the specified manager
     */
    public List<Project> getManagedProjects(HDBManager manager) {
        return manager.getManagedProjects();
    }
    /**
     * Creates a new project and adds it to the manager's list of managed projects.
     * 
     * @param manager the HDBManager instance creating the project
     * @param project the project to be created
     */
    public void createProject(HDBManager manager, Project project) {
        manager.addManagedProject(project);
        ProjectService.createProject(project);
    }
    /**
     * Edits the details of an existing project.
     * 
     * @param project the project to be edited
     * @param newName the new name for the project
     * @param newNeighbourhood the new neighbourhood for the project
     * @param new2Room the updated number of 2-room flats
     * @param new3Room the updated number of 3-room flats
     * @param new2RoomPrice the updated price for 2-room flats
     * @param new3RoomPrice the updated price for 3-room flats
     * @param newOpen the new application start date
     * @param newClose the new application end date
     * @param newmaxOfficer the updated maximum number of officers for the project
     * @param newVisible the updated visibility status of the project
     */
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
    /**
     * Deletes a project from the manager's list and the project service.
     * 
     * @param manager the HDBManager instance deleting the project
     * @param project the project to be deleted
     */
    public void deleteProject(HDBManager manager, Project project) {
        manager.getManagedProjects().remove(project);
        ProjectService.deleteProject(project);
    }
    /**
     * Toggles the visibility status of a project.
     * 
     * @param project the project whose visibility is being toggled
     * @param visible the new visibility status
     */
    public void toggleVisibility(Project project, boolean visible) {
        project.setVisible(visible);
    }

    // Officer Registration Logic

    /**
     * Displays the officer registrations for the specified manager's projects.
     * 
     * @param managerProjects the list of projects managed by the manager
     * @param allOfficers the list of all officers to check for registrations
     */
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
    /**
     * Approves an officer's registration for a project.
     * 
     * @param project the project for which the officer is being approved
     * @param officer the officer to approve for the project
     */
    public void approveOfficer(Project project, HDBOfficer officer) {
        if (project.getAvailableOfficerSlots() > 0) {
            project.addOfficer(officer);
            officer.assignProject(project);
            officer.setProjectRegistrationStatus(project, OfficerRegistrationStatus.APPROVED);
        }
    }
    /**
     * Rejects an officer's registration for a project.
     * 
     * @param project the project for which the officer is being rejected
     * @param officer the officer to reject for the project
     */
    public void rejectOfficer(Project project, HDBOfficer officer) {
        officer.setProjectRegistrationStatus(project, OfficerRegistrationStatus.REJECTED);
    }

    // Application Logic

    /**
     * Retrieves the list of applications for all projects managed by the specified manager.
     * 
     * @param manager the HDBManager instance whose applications are to be fetched
     * @return List of applications for the manager's managed projects
     */
    public List<Application> getApplicationsForManagedProjects(HDBManager manager) {
        List<Application> result = new ArrayList<>();
    
        for (Project project : manager.getManagedProjects()) {
            result.addAll(project.getApplications());
        }
    
        return result;
    }
    /**
     * Approves an application and updates the project status.
     * 
     * @param app the application to be approved
     */
    public void approveApplication(Application app) {
        app.approve();
        // maybe update booking status too?
    }
    /**
     * Rejects an application and updates its status accordingly.
     * 
     * @param app the application to be rejected
     */
    public void rejectApplication(Application app) {
        app.reject();
    }
    /**
     * Approves a withdrawal request for an application.
     * 
     * @param app the application whose withdrawal request is to be approved
     */
    public void approveWithdrawal(Application app) {
        if (app.getStatus() == ApplicationStatus.PENDING) {
            app.getApplicant().setApplication(null);
            app.getProject().getApplications().remove(app);
        }
        else if (app.getStatus() == ApplicationStatus.BOOKED) {
            Project project = app.getProject();
            project.setNumUnits(app.getFlatType(), project.getNumUnits(app.getFlatType()) + 1);
            app.withdraw();
        }
        else {
            app.withdraw();
        }

    }
    /**
     * Rejects a withdrawal request for an application.
     * 
     * @param app the application whose withdrawal request is to be rejected
     */
    public void rejectWithdrawal(Application app) {
        app.cancelWithdrawalRequest();
    }
    /**
     * Retrieves all enquiries related to the specified projects.
     * 
     * @param projects the list of projects whose enquiries are to be fetched
     * @return List of all enquiries for the specified projects
     */
    public List<Enquiry> getAllEnquiries(List<Project> projects) {
        List<Enquiry> allEnquiries = new ArrayList<>();
        for (Project project : projects) {
            allEnquiries.addAll(project.getEnquiries());
        }
        return allEnquiries;
    }
    /**
     * Generates a report of applications based on the specified filters.
     * 
     * @param manager the HDBManager instance generating the report
     * @param filterCategory the category to filter the applications (e.g., marital status, flat type)
     * @param filterType the type of filter value to apply
     * @return List of applications filtered based on the specified criteria
     */
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
    /**
     * Retrieves the list of officers who have pending registrations for a specified project.
     * 
     * @param allOfficers the list of all officers to check for pending registrations
     * @param selectedProject the project for which pending officer registrations are to be fetched
     * @return List of HDBOfficers with pending registrations for the project
     */
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
    /**
     * Filters applications based on the specified search criteria.
     * 
     * @param applications the list of applications to filter
     * @param searchCriteria the criteria used to filter the applications
     * @return List of applications that match the search criteria
     */
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
    /**
     * Finds a project by its name within the list of projects managed by the manager.
     * 
     * @param projectName the name of the project to search for
     * @param manager the HDBManager instance whose projects are to be searched
     * @return the project with the specified name, or null if not found
     */
    public Project findProjectByName(String projectName, HDBManager manager) {
        for (Project project : manager.getManagedProjects()) {
            if (project.getProjectName().equals(projectName)) {
                return project;
            }
        }
        return null;
    }

}
