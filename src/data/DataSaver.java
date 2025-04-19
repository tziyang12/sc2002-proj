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

public class DataSaver {

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

    private static void writeProjectsHeader(BufferedWriter writer) throws IOException {
        writer.write("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer\n");
    }

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

    private static String getFlatTypeName(FlatType type) {
        switch (type) {
            case TWO_ROOM: return "2-Room";
            case THREE_ROOM: return "3-Room";
            default: return "";
        }
    }

    private static void ensureDirectoryExists(String filePath) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs(); // Create directory if it doesn't exist
        }
    }
    // Optional: Add methods to save applicants, officers, or managers if needed
}
