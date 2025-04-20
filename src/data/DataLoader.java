package data;

import model.project.Project;
import model.project.FlatType;
import model.user.Applicant;
import model.user.HDBManager;
import model.user.User;
import model.user.enums.MaritalStatus;
import model.user.HDBOfficer;
import model.transaction.Application;
import model.transaction.ApplicationStatus;
import model.transaction.Enquiry;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataLoader {

    // Load applicants from ApplicantList.csv
    public List<Applicant> loadApplicants(String filePath) {
        List<Applicant> applicants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String nric = data[1];
                int age = Integer.parseInt(data[2]);
                MaritalStatus maritalStatus = MaritalStatus.valueOf(data[3].toUpperCase());
                String password = data[4];

                // Create and add the applicant
                applicants.add(new Applicant(name, nric, password, age, maritalStatus));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return applicants;
    }

    // Load projects from ProjectList.csv
    public static List<Project> loadProjects(String filePath, List<HDBOfficer> allOfficers, List<HDBManager> allManagers) throws IOException {
        List<Project> projects = new ArrayList<>();
    
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        reader.readLine();  // Skip the header line
        int projectid = 1; // Initialize project ID

        // Read each line of the CSV file
        while ((line = reader.readLine()) != null) {
            // Initialize variables
            List<String> columns = new ArrayList<>();
            StringBuilder currentField = new StringBuilder();
            boolean insideQuotes = false;
            char[] charArray = line.toCharArray();
    
            // Iterate over each character in the line
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
    
                if (c == '"' && (i == 0 || charArray[i - 1] != '\\')) {
                    // Toggle the insideQuotes flag when encountering a quote
                    insideQuotes = !insideQuotes;
                } else if (c == ',' && !insideQuotes) {
                    // If outside quotes and a comma, it's a delimiter for a new field
                    columns.add(currentField.toString().trim());
                    currentField.setLength(0);  // Clear the current field
                } else {
                    // Append the character to the current field
                    currentField.append(c);
                }
            }
    
            // Add the last field (after the last comma)
            columns.add(currentField.toString().trim());
    
            // Now parse the fields
            String projectName = columns.get(0);
            String neighborhood = columns.get(1);
            String type1 = columns.get(2);
            int numUnitsType1 = Integer.parseInt(columns.get(3));
            double sellingPriceType1 = Double.parseDouble(columns.get(4));
            String type2 = columns.get(5);
            int numUnitsType2 = Integer.parseInt(columns.get(6));
            double sellingPriceType2 = Double.parseDouble(columns.get(7));
            LocalDate openingDate = Project.parseDate(columns.get(8));
            LocalDate closingDate = Project.parseDate(columns.get(9));
            String managerName = columns.get(10);
            int maxOfficerSlots = Integer.parseInt(columns.get(11));
            
            List<String> officerNames = new ArrayList<>();
            if (columns.size() > 12 && !columns.get(12).trim().isEmpty()) {
                String officerString = columns.get(12);
                String[] officers = officerString.split(",");
                for (String officer : officers) {
                    officerNames.add(officer.trim());
                }
            }
            // Find HDBOfficer objects based on officer names
            List<HDBOfficer> officerObjects = new ArrayList<>();
            for (String officerName : officerNames) {
                for (HDBOfficer officer : allOfficers) {
                    if (officer.getName().equalsIgnoreCase(officerName.trim())) {
                        officerObjects.add(officer);
                        break;
                    }
                }
            }

            HDBManager matchedManager = null;

            for (HDBManager m : allManagers) {
                if (m.getName().equalsIgnoreCase(managerName.trim())) {
                    matchedManager = m;
                    break;
                }
            }
            // Create project and add officers
            Project project = new Project(projectName, neighborhood, openingDate, closingDate, maxOfficerSlots);
            
            project.setProjectID(projectid++);
            
            if (matchedManager != null) {
                project.setManager(matchedManager);
                matchedManager.addManagedProject(project);
            } else {
                System.out.println("Warning: No matching HDBManager found for project '" + projectName + "' with manager name '" + managerName + "'.");
            }
            // Add the officer objects to the project
            for (HDBOfficer officer : officerObjects) {
                project.addOfficer(officer);
                officer.assignProject(project);
            }

            
            // Add flat units
            FlatType flatType1 = parseFlatType(type1);
            FlatType flatType2 = parseFlatType(type2);
            project.addFlatUnit(flatType1, numUnitsType1);
            project.addFlatUnit(flatType2, numUnitsType2);
            // Add flat prices
            project.addFlatPrice(flatType1, sellingPriceType1);
            project.addFlatPrice(flatType2, sellingPriceType2);
    
            projects.add(project);
        }
    
        reader.close();
        return projects;
    }

    public List<HDBOfficer> loadOfficers(String filePath) {
        List<HDBOfficer> officers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String nric = data[1];
                int age = Integer.parseInt(data[2]);
                MaritalStatus maritalStatus = MaritalStatus.valueOf(data[3].toUpperCase());
                String password = data[4];
    
                officers.add(new HDBOfficer(name, nric, password, age, maritalStatus));
            }

        } catch (IOException e) {
            System.out.println("Error loading officers: " + e.getMessage());
        }
        return officers;
    }

    private static FlatType parseFlatType(String type) {
        switch (type) {
            case "2-Room":
                return FlatType.TWO_ROOM;
            case "3-Room":
                return FlatType.THREE_ROOM;
            default:
                throw new IllegalArgumentException("Unknown flat type: " + type);
        }
    }

    public List<HDBManager> loadHDBManagers(String filePath) {
        List<HDBManager> managers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String nric = data[1];
                int age = Integer.parseInt(data[2]);
                MaritalStatus maritalStatus = MaritalStatus.valueOf(data[3].toUpperCase());
                String password = data[4];

                // Create and add the manager
                managers.add(new HDBManager(name, nric, password, age, maritalStatus));
            }

        } catch (IOException e) {
            System.out.println("Error loading HDB Managers: " + e.getMessage());
        }
        return managers;
    }

    public void loadApplications(String filePath, List<User> allUsers, List<Project> allProjects) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String applicantNRIC = data[1];
                String projectName = data[2];
                String flatTypeStr = data[3].toUpperCase(); // Convert to uppercase for enum matching
                String status = data[4];
                boolean withdrawalRequested = Boolean.parseBoolean(data[5]);
                LocalDate applicationDate = LocalDate.parse(data[6]);

                // Find the applicant based on NRIC
                Applicant applicant = null;
                for (User user : allUsers) {
                    if (user instanceof Applicant  && user.getNric().equals(applicantNRIC)) {
                        applicant = (Applicant) user;
                        break;
                    }
                }

                // Find the project based on project name
                Project project = null;
                for (Project p : allProjects) {
                    if (p.getProjectName().equals(projectName)) {
                        project = p;
                        break;
                    }
                }

                // Ensure applicant and project exist before creating the application
                if (applicant != null && project != null) {
                    FlatType flatType = FlatType.valueOf(flatTypeStr); // Convert the string to the FlatType enum
                    Application application = new Application(applicant, project, flatType);
                    application.setStatus(status); // Set the status
                    application.setWithdrawalRequested(withdrawalRequested); // Set withdrawal request
                    application.setApplicationDate(applicationDate); // Set the application date
                    applicant.setApplication(application);
                    project.addApplication(application); // Link application to the applicant
                } else {
                    System.out.println("Error: Application entry could not be linked to applicant or project.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadEnquiries(String filePath, List<User> allUsers, List<Project> allProjects) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip CSV header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1); // -1 to keep empty strings
                int enquiryID = Integer.parseInt(data[0]);
                String applicantName = data[1]; // optional
                String applicantNRIC = data[2];
                String projectName = data[3];
                String message = data[4];
                String reply = data.length > 5 ? data[5] : "";
    
                // Find project by name
                Project matchedProject = allProjects.stream()
                    .filter(p -> p.getProjectName().equalsIgnoreCase(projectName))
                    .findFirst()
                    .orElse(null);
    
                // Find applicant from user list
                Applicant matchedApplicant = allUsers.stream()
                    .filter(u -> u instanceof Applicant)
                    .map(u -> (Applicant) u)
                    .filter(a -> a.getNric().equalsIgnoreCase(applicantNRIC))
                    .findFirst()
                    .orElse(null);
    
                if (matchedProject != null && matchedApplicant != null) {
                    Enquiry enquiry = new Enquiry(enquiryID, message, matchedProject, matchedApplicant);
    
                    if (!reply.isEmpty()) {
                        enquiry.setReply(reply);
                    }
    
                    matchedProject.addEnquiry(enquiry);
                    matchedApplicant.addEnquiry(enquiry); // optional if applicants store their own enquiries
                } else {
                    System.out.println("Warning: Could not resolve project or applicant for EnquiryID: " + enquiryID);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading enquiries: " + e.getMessage());
        }
    }    
}
