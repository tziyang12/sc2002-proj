package Dashboard;
import Roles.Applicant;
import java.util.Scanner;

public class EnquiryDashboard {
    static void manageEnquiry(Applicant applicant, Scanner sc) {
        while (true) {
            System.out.println("\n--- Manage Enquiries ---");
            System.out.println("1. View Enquiries");
            System.out.println("2. Add Enquiry");
            System.out.println("3. Edit Enquiry");
            System.out.println("4. Delete Enquiry");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> applicant.viewEnquiries();
                case 2 -> {
                    System.out.print("Enter your enquiry: ");
                    String enquiry = sc.nextLine();
                    applicant.addEnquiry(enquiry);
                }
                case 3 -> {
                    System.out.print("Enter the enquiry index to edit: ");
                    int editIndex = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter the new enquiry: ");
                    String newEnquiry = sc.nextLine();
                    applicant.editEnquiry(editIndex, newEnquiry);
                }
                case 4 -> {
                    System.out.print("Enter the enquiry index to delete: ");
                    int deleteIndex = sc.nextInt();
                    sc.nextLine();
                    applicant.deleteEnquiry(deleteIndex);
                }
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

