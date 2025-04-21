package ui;

import java.util.List;
import java.util.Scanner;

import model.project.FlatType;
import model.project.Project;
import model.user.Applicant;
import model.user.HDBOfficer;
import service.ProjectService;
/**
 * The {@code CLIView} class handles all the user interface interactions for the 
 * Command Line Interface (CLI) in the Build-To-Order (BTO) Management System. 
 * It provides various methods to display project information, prompt user inputs, 
 * and interact with the system.
 */
public class CLIView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String PROJECT_TABLE_BORDER = "+-----+----------------------+-----------------+---------+---------+------------+------------+--------------+--------------+----------+------------+--------------------+------------+--------------+--------------+%n";

    private CLIView() {
        // Prevent instantiation
    }

    
    /**
     * Prints the header with a title.
     * 
     * @param title The title to be displayed in the header.
     */
    public static void printHeader(String title) {
        System.out.println("\n=== " + title + " ===");
    }
    /**
     * Prints a menu with the given options.
     * 
     * @param options An array of strings representing the menu options.
     */
    public static void printMenu(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s%n", i + 1, options[i]);
        }
        System.out.print("Enter choice: ");
    }
    /**
     * Prompts the user with a message and returns their input.
     * 
     * @param message The message to display to the user.
     * @return The user's input as a string.
     */
    public static String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * Prompts the user for an integer input with a message.
     * 
     * @param message The message to display to the user.
     * @return The integer input from the user.
     */
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

    /**
     * Prints an error message to the user.
     * 
     * @param message The error message to display.
     */
    public static void printError(String message) {
        System.out.println("[Error] " + message);
    }

    /**
     * Prints a general message to the user.
     * 
     * @param message The message to display.
     */
    public static void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prints the details of a project.
     * 
     * @param project The project whose details are to be printed.
     */
    public static void printProject(Project project){
        System.out.println("• " + project.getProjectName() + " in " + project.getNeighbourhood() +
        " (Opening: " + project.getApplicationStartDate() + ", Closing: " + project.getApplicationEndDate() + ")");
    }

    /**
     * Prints the header for the project table.
     */
    public static void printProjectTableHeader() {
        System.out.println("\n=== Project List ===");
        System.out.format(PROJECT_TABLE_BORDER);
        System.out.format("| ID  | Project Name         | Neighbourhood   | 2-Room  | 3-Room  | 2R Price   | 3R Price   | Start Date   | End Date     | Visible  | Manager    | Officers           | OfficerMax | #Enquiries   | #Applicants  |%n");
        System.out.format(PROJECT_TABLE_BORDER);
    }

    /**
     * Prints a row of project details in the project table.
     * 
     * @param project The project to display in the row.
     */
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


    /**
     * Prints the footer for the project table.
     */
    public static void printProjectTableFooter() {
        System.out.format(PROJECT_TABLE_BORDER);
    }

    /**
     * Prints the header for the enquiry table.
     */
    public static void printEnquiryTableHeader() {
        System.out.println("\n=== Enquiries ===");
        System.out.format("+----------------------+------------+-------------------------------+-------------------------------+%n");
        System.out.format("| Project Name         | Enquiry ID | Enquiry Message               | Reply Message                 |%n");
        System.out.format("+----------------------+------------+-------------------------------+-------------------------------+%n");
    }

    /**
     * Prints a row of enquiry details in the enquiry table.
     * 
     * @param projectName     The project name related to the enquiry.
     * @param enquiryID       The unique identifier for the enquiry.
     * @param enquiryMessage  The message of the enquiry.
     * @param replyMessage    The reply to the enquiry.
     */
    public static void printEnquiryRow(String projectName, int enquiryID, String enquiryMessage, String replyMessage) {
        String leftAlignFormat = "| %-20s | %-10s | %-29s | %-29s |%n";

        // Truncate messages if too long
        enquiryMessage = truncate(enquiryMessage, 29);
        replyMessage = truncate(replyMessage, 29);

        System.out.format(leftAlignFormat, projectName, enquiryID, enquiryMessage, replyMessage);
    }

    /**
     * Prints the footer for the enquiry table.
     */
    public static void printEnquiryTableFooter() {
        System.out.format("+----------------------+------------+-------------------------------+-------------------------------+%n");
    }

    /**
     * Truncates the input string to the specified maximum length.
     * 
     * @param input      The string to truncate.
     * @param maxLength  The maximum allowed length of the string.
     * @return A truncated version of the input string.
     */
    private static String truncate(String input, int maxLength) {
        if (input == null) return "-";
        return input.length() > maxLength ? input.substring(0, maxLength - 3) + "..." : input;
    }

    /**
     * Prompts the user with a yes/no question and returns their answer.
     * 
     * @param message The question to ask the user.
     * @return {@code true} if the user responds with 'y', {@code false} if the user responds with 'n'.
     */
    public static boolean promptYesNo(String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;
            printError("Invalid input. Enter 'y' or 'n'.");
        }
    }

    /**
     * Prompts the user to select a project from a list.
     * 
     * @param projects A list of available projects.
     * @return The selected project, or {@code null} if no project is found.
     */
    public static Project promptProject(List<Project> projects) {
        String projectName = prompt("Enter the project name: ");
        return ProjectService.findByName(projectName, projects);
    }

    /**
     * Prints a row of applicant application details.
     * 
     * @param applicant The applicant whose details are to be displayed.
     */
    public static void printApplicantApplicationRow(Applicant applicant) {
        System.out.printf("NRIC: %s | Name: %s | Flat Type: %s%n",
            applicant.getNric(),
            applicant.getName(),
            applicant.getApplication().getFlatType());
    }

    /**
     * Prints a formatted string.
     * 
     * @param format The format string.
     * @param args The arguments to be formatted.
     */
    public static void printFormatter(String format, Object... args) {
        System.out.printf(format, args);
    }
}
