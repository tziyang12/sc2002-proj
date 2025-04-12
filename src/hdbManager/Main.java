package hdbManager;

import java.time.LocalDate;
import java.util.*;

public class Main {
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager("Admin", "M001");
        
        while(true) {
            System.out.println("\n=== HDB MANAGEMENT SYSTEM ===");
            System.out.println("1. Create Project");
            System.out.println("2. Edit Project");
            System.out.println("3. Delete Project");
            System.out.println("4. Toggle Visibility");
            System.out.println("5. View All Projects");
            System.out.println("6. Manage Registrations");
            System.out.println("7. Exit");
            System.out.print("Select option: ");
            
            switch(Integer.parseInt(scanner.nextLine())) {
                case 1:
                    manager.createProject(scanner);
                    break;
                case 2:
                    manager.editProject(scanner);
                    break;
                case 3:
                    manager.deleteProject(scanner);
                    break;
                case 4:
                    manager.toggleVisibility(scanner);
                    break;
                case 5:
                    manager.viewAllProjects();
                    break;
                case 6:
                    manager.handleRegistrations(scanner);
                    break;
                case 7:
                    System.out.println("Exiting system...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}
