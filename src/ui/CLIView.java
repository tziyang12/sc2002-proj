package ui;

import java.util.Scanner;

import model.project.FlatType;
import model.project.Project;
import model.user.HDBOfficer;

public class CLIView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String PROJECT_TABLE_BORDER = "+-----+----------------------+-----------------+---------+---------+------------+------------+--------------+--------------+----------+------------+--------------------+------------+--------------+--------------+%n";

    private CLIView() {
        // Prevent instantiation
    }

    public static void printHeader(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    public static void printMenu(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s%n", i + 1, options[i]);
        }
        System.out.print("Enter choice: ");
    }

    public static String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public static int promptInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Invalid number. Try again.");
            }
        }
    }

    public static void printError(String message) {
        System.out.println("[Error] " + message);
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printProject(Project project){
        System.out.println("• " + project.getProjectName() + " in " + project.getNeighbourhood() +
        " (Opening: " + project.getApplicationStartDate() + ", Closing: " + project.getApplicationEndDate() + ")");
    }
    

    public static void printProjectTableHeader() {
        System.out.println("\n=== Project List ===");
        System.out.format(PROJECT_TABLE_BORDER);
        System.out.format("| ID  | Project Name         | Neighbourhood   | 2-Room  | 3-Room  | 2R Price   | 3R Price   | Start Date   | End Date     | Visible  | Manager    | Officers           | OfficerMax | #Enquiries   | #Applicants  |%n");
        System.out.format(PROJECT_TABLE_BORDER);
    }
    
    public static void printProjectRow(Project project) {
        String leftAlignFormat = "| %-3s | %-20s | %-15s | %-7s | %-7s | %-10s | %-10s | %-12s | %-12s | %-8s | %-10s | %-18s | %-10s | %-12s | %-12s |%n";
    
        int twoRoomUnits = project.getNumUnits(FlatType.TWO_ROOM);
        int threeRoomUnits = project.getNumUnits(FlatType.THREE_ROOM);
        double twoRoomPrice = project.getFlatPrice(FlatType.TWO_ROOM);
        double threeRoomPrice = project.getFlatPrice(FlatType.THREE_ROOM);
        String startDate = project.getApplicationStartDate().toString();
        String endDate = project.getApplicationEndDate().toString();
        String visible = project.isVisible() ? "Yes" : "No";
        String managerName = project.getManager() != null ? project.getManager().getName() : "-";
        int officerMax = project.getMaxOfficerSlots();
    
        // Format officer names
        String officerNames = project.getOfficers().stream()
                .map(HDBOfficer::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("-");
        if (officerNames.length() > 20) {
            officerNames = officerNames.substring(0, 17) + "...";
        }
    
        int enquiryCount = project.getEnquiries().size();
        int applicantCount = project.getApplications().size();
    
        System.out.format(leftAlignFormat,
                project.getProjectID(),
                project.getProjectName(),
                project.getNeighbourhood(),
                twoRoomUnits,
                threeRoomUnits,
                String.format("$%.2f", twoRoomPrice),
                String.format("$%.2f", threeRoomPrice),
                startDate,
                endDate,
                visible,
                managerName,
                officerNames,
                officerMax,
                enquiryCount,
                applicantCount
        );

    }
    public static void printProjectTableFooter() {
        System.out.format(PROJECT_TABLE_BORDER);
    }
    

    public static void printEnquiryTableHeader() {
        System.out.println("\n=== Enquiries ===");
        System.out.format("+----------------------+------------+-------------------------------+-------------------------------+%n");
        System.out.format("| Project Name         | Enquiry ID | Enquiry Message               | Reply Message                 |%n");
        System.out.format("+----------------------+------------+-------------------------------+-------------------------------+%n");
    }

    public static void printEnquiryRow(String projectName, int enquiryID, String enquiryMessage, String replyMessage) {
        String leftAlignFormat = "| %-20s | %-10s | %-29s | %-29s |%n";

        // Truncate messages if too long
        enquiryMessage = truncate(enquiryMessage, 29);
        replyMessage = truncate(replyMessage, 29);

        System.out.format(leftAlignFormat, projectName, enquiryID, enquiryMessage, replyMessage);
    }

    public static void printEnquiryTableFooter() {
        System.out.format("+----------------------+------------+-------------------------------+-------------------------------+%n");
    }

    private static String truncate(String input, int maxLength) {
        if (input == null) return "-";
        return input.length() > maxLength ? input.substring(0, maxLength - 3) + "..." : input;
    }
}
