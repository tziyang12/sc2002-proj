package Database;

import Roles.Applicant;
import java.util.ArrayList;
import java.util.List;

public class ApplicantDatabase {
    private static List<Applicant> applicantList = new ArrayList<>();  // Holds all applicants

    // Static block to initialize applicants with their details (name, NRIC, age, marital status, password)
    static {
        applicantList.add(new Applicant("John", "S1234567A", 35, "Single"));
        applicantList.add(new Applicant("Sarah", "T7654321B", 40, "Married"));
        applicantList.add(new Applicant("Grace", "S9876543C", 37, "Married"));
        applicantList.add(new Applicant("James", "T2345678D", 30, "Married"));
        applicantList.add(new Applicant("Rachel", "S3456789E", 25, "Single"));
    }

    // Method to retrieve all applicants
    public static List<Applicant> getApplicants() {
        return new ArrayList<>(applicantList);
    }

    // Method to find an applicant by NRIC
    public static Applicant findApplicantByNric(String nric) {
        for (Applicant applicant : applicantList) {
            if (applicant.getID().equalsIgnoreCase(nric)) {
                return applicant;
            }
        }
        return null; // Applicant not found
    }

    // Method to add a new applicant to the list (in case you need to add new ones dynamically)
    public static void addApplicant(Applicant applicant) {
        applicantList.add(applicant);
    }
}
