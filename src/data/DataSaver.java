package data;

import model.project.Project;
import model.transaction.Enquiry;
import model.transaction.Application;
import model.project.FlatType;
import model.user.Applicant;
import model.user.HDBOfficer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import java.io.File;
/**
 * The DataSaver class provides static methods for saving project, application, and enquiry data to CSV files.
 * It ensures that data is saved in the correct format and that the necessary directories are created if they do not exist.
 */
public class DataSaver {
    /**
     * Saves a list of projects to a CSV file.
     * 
     * @param filePath The path to the CSV file where the projects will be saved.
     * @param projects The list of projects to be saved.
     */
    public static void saveProjects(String filePath, List<Project> projects) {
        ensureDirectoryExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writeProjectsHeader(writer);
            for (Project project : projects) {
                writer.write(formatProjectData(project));
            }
        } catch (IOException e) {
            System.out.println("Error saving projects: " + e.getMessage());
        }
    }
    /**
     * Writes the header row for the projects CSV file.
     * 
     * @param writer The BufferedWriter used to write data to the file.
     * @throws IOException If an I/O error occurs while writing the header.
     */
    private static void writeProjectsHeader(BufferedWriter writer) throws IOException {
        writer.write("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer\n");
    }
    /**
     * Formats the data for a single project into a CSV-compatible string.
     * 
     * @param project The project to format.
     * @return A CSV-formatted string representing the project.
     */
    private static String formatProjectData(Project project) {
        Map<FlatType, Integer> flatUnits = project.getFlatUnits();
        Map<FlatType, Double> flatPrices = project.getFlatPrices();

        FlatType[] types = flatUnits.keySet().toArray(new FlatType[0]);
        String type1 = types.length > 0 ? getFlatTypeName(types[0]) : "";
        String type2 = types.length > 1 ? getFlatTypeName(types[1]) : "";

        int units1 = types.length > 0 ? flatUnits.getOrDefault(types[0], 0) : 0;
        int units2 = types.length > 1 ? flatUnits.getOrDefault(types[1], 0) : 0;

        double price1 = types.length > 0 ? flatPrices.getOrDefault(types[0], 0.0) : 0.0;
        double price2 = types.length > 1 ? flatPrices.getOrDefault(types[1], 0.0) : 0.0;

        String managerName = project.getManager() != null ? project.getManager().getName() : "";
        int maxOfficerSlots = project.getMaxOfficerSlots();

        StringJoiner officerNames = new StringJoiner(",");
        for (HDBOfficer officer : project.getOfficers()) {
            officerNames.add(officer.getName());
        }

        return String.format("%s,%s,%s,%d,%.2f,%s,%d,%.2f,%s,%s,%s,%d,%s%n",
                project.getProjectName(),
                project.getNeighbourhood(),
                type1,
                units1,
                price1,
                type2,
                units2,
                price2,
                project.getApplicationStartDate(),
                project.getApplicationEndDate(),
                managerName,
                maxOfficerSlots,
                officerNames.toString()
        );
    }
    /**
     * Saves a list of applications to a CSV file.
     * 
     * @param filePath The path to the CSV file where the applications will be saved.
     * @param applications The list of applications to be saved.
     */
    public static void saveApplications(String filePath, List<Application> applications) {
        ensureDirectoryExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Applicant,NRIC,Project,FlatType,Status,WithdrawalRequested,ApplicationDate\n");
            for (Application app : applications) {
                Applicant applicant = app.getApplicant();
                writer.write(String.format("%s,%s,%s,%s,%s,%b,%s%n",
                        applicant.getName(),
                        applicant.getNric(),
                        app.getProject().getProjectName(),
                        app.getFlatType(),
                        app.getStatus(),
                        app.isWithdrawalRequested(),
                        app.getApplicationDate()
                ));
            }
        } catch (IOException e) {
            System.out.println("Error saving applications: " + e.getMessage());
        }
    }
    /**
     * Saves a list of enquiries to a CSV file.
     * 
     * @param filePath The path to the CSV file where the enquiries will be saved.
     * @param enquiries The list of enquiries to be saved.
     */
    public static void saveEnquiries(String filePath, List<Enquiry> enquiries) {
        ensureDirectoryExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("EnquiryID,Applicant,NRIC,Project,Message,Reply\n");
            for (Enquiry enquiry : enquiries) {
                Applicant applicant = enquiry.getApplicant();
                writer.write(String.format("%d,%s,%s,%s,%s,%s%n",
                        enquiry.getEnquiryId(),
                        applicant.getName(),
                        applicant.getNric(),
                        enquiry.getProject().getProjectName(),
                        enquiry.getEnquiryMessage().replaceAll("[\n\r]", " "),
                        enquiry.getReplyMessage() == null ? "" : enquiry.getReplyMessage().replaceAll("[\n\r]", " ")
                ));
            }
        } catch (IOException e) {
            System.out.println("Error saving enquiries: " + e.getMessage());
        }
    }
    /**
     * Converts a FlatType enum to its corresponding string representation.
     * 
     * @param type The FlatType to convert.
     * @return The string representation of the FlatType.
     */
    private static String getFlatTypeName(FlatType type) {
        switch (type) {
            case TWO_ROOM: return "2-Room";
            case THREE_ROOM: return "3-Room";
            default: return "";
        }
    }
    /**
     * Ensures that the directory for the given file path exists, creating it if necessary.
     * 
     * @param filePath The file path for which the directory should be checked/created.
     */
    private static void ensureDirectoryExists(String filePath) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs(); // Create directory if it doesn't exist
        }
    }
}
