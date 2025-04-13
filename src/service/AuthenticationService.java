package service;

import model.user.Applicant;
import model.user.User;
import old.Dashboard.ApplicantDashboard;
import old.Database.ApplicantDatabase;

import java.util.Scanner;
import java.util.regex.Pattern;

public class AuthenticationService {
	private ApplicantDatabase applicantDB;
    private PasswordServices passwordServices;

    public AuthenticationService(ApplicantDatabase applicantDB) {
        this.applicantDB = applicantDB;
        this.passwordServices = new PasswordServices(); // Can inject to PasswordServices if needed
    }

    public void login(Scanner sc) {
        System.out.print("Enter your NRIC: ");
        String nric = sc.nextLine().trim().toUpperCase();

        if (!isValidNRIC(nric)) {
            System.out.println("Invalid NRIC format. Please try again.");
            return;
        }

        User user = applicantDB.findApplicantByNric(nric);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter your password: ");
        String inputPassword = sc.nextLine();

        if (!passwordServices.verifyPassword(user, inputPassword)) {
            System.out.println("Incorrect password.");
            return;
        }

        System.out.println("Login successful. Welcome, " + user.getName() + "!");
        offerPasswordChange(sc, user);
        redirectToDashboard(user, sc);
    }
    
    private void offerPasswordChange(Scanner sc, User user) {
        System.out.print("Do you want to change your password? (yes/no): ");
        String response = sc.nextLine().trim().toLowerCase();
        if (response.equals("yes") || response.equals("y")) {
            passwordServices.changePassword(sc, user);
        }
    }
    
    private void redirectToDashboard(User user, Scanner sc) {
        System.out.println("Redirecting to dashboard...");

        String role = user.getClass().getSimpleName();

        if (role.equals("Applicant") && user instanceof Applicant applicant) {
            ApplicantDashboard.showDashboard(applicant, sc);
        //please implement your dashboard codes
        } else if (role.equals("HDBOfficer")) {
            System.out.println("HDB Officer dashboard is currently not available.");
        } else if (role.equals("Manager")) {
            System.out.println("HDB Manager dashboard is currently not available.");
        } else {
            System.out.println("Unknown user role. Cannot load dashboard.");
        }
    }

    private boolean isValidNRIC(String nric) {
        return Pattern.matches("[ST]\\d{7}[A-Z]", nric);
    }
}

