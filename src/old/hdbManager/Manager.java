package old.hdbManager;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.*;
import java.time.format.DateTimeFormatter;

class Project {
    private String name;
    private String neighborhood;
    private Map<String, Integer> flatTypes;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private boolean isVisible;
    private int maxOfficerSlots;
    private Manager manager;
    private List<String> officers = new ArrayList<>();
    private List<OfficerRegistration> pendingRegistrations = new ArrayList<>();
    public static List<Project> allProjects = new ArrayList<>();

    public Project(String name, String neighborhood, Map<String, Integer> flatTypes,
                   LocalDate openingDate, LocalDate closingDate, int maxOfficerSlots,
                   Manager manager) {
        this.name = name;
        this.neighborhood = neighborhood;
        this.flatTypes = new HashMap<>(flatTypes);
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.maxOfficerSlots = maxOfficerSlots;
        this.manager = manager; 
        this.isVisible = true; 
        allProjects.add(this); 
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }
    public Map<String, Integer> getFlatTypes() { return flatTypes; }
    public LocalDate getOpeningDate() { return openingDate; }
    public LocalDate getClosingDate() { return closingDate; }
    public void setClosingDate(LocalDate closingDate) { this.closingDate = closingDate; }
    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { isVisible = visible; }
    public int getMaxOfficerSlots() { return maxOfficerSlots; }
    public Manager getManager() { return manager; }
    public List<String> getOfficers() { return officers; }
    public List<OfficerRegistration> getPendingRegistrations() { return pendingRegistrations; }
}

class Manager {
    private String name;
    private String managerId;
    private List<Project> projectsHandling = new ArrayList<>();

    public Manager(String name, String managerId) {
        this.name = name;
        this.managerId = managerId;
    }

  
    public void createProject(Scanner scanner) {
        System.out.println("\n=== CREATE NEW PROJECT ===");
        System.out.print("Project name: ");
        String name = scanner.nextLine();

        System.out.print("Neighborhood: ");
        String neighborhood = scanner.nextLine();

        Map<String, Integer> flatTypes = new HashMap<>();
        System.out.println("Enter flat types (type 'done' when finished):");
        while (true) {
            System.out.print("Flat type (e.g., '2-Room'): ");
            String type = scanner.nextLine();
            if (type.equalsIgnoreCase("done")) break;

            System.out.print("Quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            flatTypes.put(type, quantity);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.print("Opening date (yyyy-MM-dd): ");
        LocalDate openingDate = LocalDate.parse(scanner.nextLine(), formatter);

        System.out.print("Closing date (yyyy-MM-dd): ");
        LocalDate closingDate = LocalDate.parse(scanner.nextLine(), formatter);

        System.out.print("Max officer slots: ");
        int maxSlots = Integer.parseInt(scanner.nextLine());

        Project newProject = new Project(name, neighborhood, flatTypes,
                                         openingDate, closingDate, maxSlots, this);
        projectsHandling.add(newProject);
        System.out.println("\nProject created successfully! Manager in charge: " + this.name);
    }

    public void editProject(Scanner scanner) {
        System.out.println("\n=== EDIT PROJECT ===");
        listProjects();
        
        System.out.print("Enter project name to edit: ");
        Project target = getProjectByName(scanner.nextLine());
        
        if (target == null) {
            System.out.println("Project not found!");
            return;
        }

        System.out.println("\nWhat would you like to edit?");
        System.out.println("1. Project name\n2. Neighborhood\n3. Flat types\n4. Closing date");
        System.out.print("Choice: ");
        
        switch (Integer.parseInt(scanner.nextLine())) {
            case 1:
                System.out.print("New project name: ");
                target.setName(scanner.nextLine());
                break;
            case 2:
                System.out.print("New neighborhood: ");
                target.setNeighborhood(scanner.nextLine());
                break;
            case 3:
                Map<String, Integer> newFlatTypes = new HashMap<>();
                System.out.println("Enter new flat types (type 'done' when finished):");
                while (true) {
                    System.out.print("Flat type: ");
                    String type = scanner.nextLine();
                    if (type.equalsIgnoreCase("done")) break;

                    System.out.print("Quantity: ");
                    int quantity = Integer.parseInt(scanner.nextLine());
                    newFlatTypes.put(type, quantity);
                }
                target.getFlatTypes().clear();
                target.getFlatTypes().putAll(newFlatTypes);
                break;
            case 4:
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                System.out.print("New closing date (yyyy-MM-dd): ");
                target.setClosingDate(LocalDate.parse(scanner.nextLine(), formatter));
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        System.out.println("Project updated successfully!");
    }

    public void deleteProject(Scanner scanner) {
        System.out.println("\n=== DELETE PROJECT ===");
        listProjects();
        
        System.out.print("Enter project name to delete: ");
        Project target = getProjectByName(scanner.nextLine());
        
        if (target != null) {
            projectsHandling.remove(target);
            Project.allProjects.remove(target);
            System.out.println("Project deleted successfully!");
        } else {
            System.out.println("Project not found!");
        }
    }

    public void toggleVisibility(Scanner scanner) {
        System.out.println("\n=== TOGGLE VISIBILITY ===");
        listProjects();
        
        System.out.print("Enter project name: ");
        Project target = getProjectByName(scanner.nextLine());
        
        if (target != null) {
            target.setVisible(!target.isVisible());
            String status = target.isVisible() ? "Visible" : "Hidden";
            System.out.println("Visibility updated: " + status);
        } else {
            System.out.println("Project not found!");
        }
    }

    public void viewAllProjects() {
        System.out.println("\n=== ALL PROJECTS ===");
        
        for (Project p : Project.allProjects) {
            System.out.println("Name: " + p.getName());
            System.out.println("Neighborhood: " + p.getNeighborhood());
            System.out.println("Visibility: " + (p.isVisible() ? "Visible" : "Hidden"));
            System.out.println("Manager in charge: " + p.getManager().name);
            System.out.println("------------------------");
        }
    }

    public void handleRegistrations(Scanner scanner) {
        System.out.println("\n=== MANAGE REGISTRATIONS ===");
        viewAllProjects();
        System.out.print("Enter project name: ");
        Project target = getProjectByName(scanner.nextLine());
        
        if(target == null) {
            System.out.println("Project not found!");
            return;
        }
        
        List<OfficerRegistration> pending = target.getPendingRegistrations();
        if(pending.isEmpty()) {
            System.out.println("No pending registrations!");
            return;
        }
        
        System.out.println("\nPending Registrations:");
        for(int i=0; i<pending.size(); i++) {
            OfficerRegistration reg = pending.get(i);
            System.out.printf("%d. %s (%s)%n", 
                i+1, reg.getOfficer().getName(), reg.getStatus());
        }
        
        System.out.print("Select registration to process (0 to cancel): ");
        int choice = Integer.parseInt(scanner.nextLine());
        if(choice < 1 || choice > pending.size()) return;
        
        OfficerRegistration selected = pending.get(choice-1);
        System.out.println("1. Approve\n2. Reject");
        System.out.print("Action: ");
        int action = Integer.parseInt(scanner.nextLine());
        
        if(action == 1) {
            if(target.getOfficers().size() < target.getMaxOfficerSlots()) {
                target.getOfficers().add(selected.getOfficer().getName());
                selected.approve();
                pending.remove(selected);
                System.out.println("Registration approved!");
            } else {
                System.out.println("No available slots!");
            }
        } else if(action == 2) {
            selected.reject();
            pending.remove(selected);
            System.out.println("Registration rejected!");
        }
    }

    private void listProjects() {
        System.out.println("\nYour Projects:");
        projectsHandling.forEach(p -> System.out.println("- " + p.getName()));
    }

    private Project getProjectByName(String name) {
        return projectsHandling.stream()
            .filter(p -> p.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
}