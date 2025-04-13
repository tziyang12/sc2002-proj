package old.ProjectManagement;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import model.project.Project;

public class ProjectDatabase {

    // List to store all projects
    private static List<Project> projects = new ArrayList<>();
    private static int projectIDCounter = 1000; // Start ID from 1000, can be any number

    // Method to load data from the CSV file
    public static void loadProjectsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip the header row
            br.readLine();
    
            // Iterate through each row in the CSV
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");  // Split by comma
    
                // Extract data from CSV columns
                String projectName = fields[0];
                String neighbourhood = fields[1];
                String type1 = fields[2];
                int numType1 = Integer.parseInt(fields[3]);
                int priceType1 = Integer.parseInt(fields[4]);
                String type2 = fields[5];
                int numType2 = Integer.parseInt(fields[6]);
                int priceType2 = Integer.parseInt(fields[7]);
    
                String openingDateString = fields[8];
                String closingDateString = fields[9];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate applicationStartDate = LocalDate.parse(openingDateString, formatter);
                LocalDate applicationEndDate = LocalDate.parse(closingDateString, formatter);
    
                String managerUserID = fields[10];
                int officerSlot = Integer.parseInt(fields[11]);
                String officers = fields[12];
    
                // Generate a unique project ID
                int projectID = generateProjectID();
    
                // Create a new BTOProject object without projectID
                Project project = new Project(projectName, neighbourhood, numType1, numType2,
                        applicationStartDate, applicationEndDate, managerUserID);
    
                // Set the projectID for the BTOProject
                project.setProjectID(projectID);
    
                // Store the project in the list
                projects.add(project);
            }
    
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }
    

    // Method to retrieve all projects
    public static List<Project> getProjects() {
        return projects;
    }

    // Method to find a project by name
    public static Project findProjectByName(String projectName) {
        for (Project project : projects) {
            if (project.getProjectName().equalsIgnoreCase(projectName)) {
                return project;
            }
        }
        return null;
    }

    // Method to get a project by its unique ID
    public static Project getProjectByID(int projectID) {
        for (Project project : projects) {
            if (project.getProjectID() == projectID) {
                return project;
            }
        }
        return null;
    }

    // Method to add a new project to the list
    public static void addProject(Project project) {
        // Add the new project to the list
        projects.add(project);
        System.out.println("Project " + project.getProjectName() + " has been added successfully.");
    }
    
    // Method to remove a project by its ID
    public static boolean removeProject(int projectID) {
        Project projectToRemove = getProjectByID(projectID);
        if (projectToRemove != null) {
            projects.remove(projectToRemove);
            System.out.println("Project with ID " + projectID + " has been removed successfully.");
            return true;
        }
        System.out.println("Project with ID " + projectID + " not found.");
        return false;
    }

    // Method to generate a unique project ID
    private static int generateProjectID() {
        return projectIDCounter++;
    }
}
