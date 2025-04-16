package ui;

import controller.AuthenticationController;
import model.project.Project;
import model.user.Applicant;
import model.user.HDBOfficer;
import model.user.HDBManager;
import model.user.User;

import java.util.List;

public class MainMenu {
    private final AuthenticationController authController = new AuthenticationController();

    public void show(List<User> allUsers, List<Project> projects) {
        String[] mainOptions = {"Login", "Change Password", "Exit"};

        while (true) {
            CLIView.printHeader("Welcome to the BTO Management System");
            CLIView.printMenu(mainOptions);
            String choice = CLIView.prompt("");

            switch (choice) {
                case "1" -> handleLogin(allUsers, projects);
                case "2" -> handlePasswordChange(allUsers);
                case "3" -> {
                    CLIView.printMessage("Exiting system. Goodbye!");
                    return;
                }
                default -> CLIView.printError("Invalid option. Try again.");
            }
        }
    }

    private void handleLogin(List<User> allUsers, List<Project> projects) {
        String nric = CLIView.prompt("Enter NRIC: ");
        String password = CLIView.prompt("Enter Password: ");

        User loggedInUser = authController.authenticate(nric, password, allUsers);

        if (loggedInUser == null) return;

        CLIView.printMessage("Login successful. Welcome, " + loggedInUser.getName());

        if (loggedInUser instanceof HDBOfficer officer) {
            List<Applicant> applicantList = allUsers.stream()
                    .filter(u -> u instanceof Applicant)
                    .map(u -> (Applicant) u)
                    .toList();

            new OfficerMenu(officer, projects, applicantList).showMenu();
        } else if (loggedInUser instanceof Applicant applicant) {
            new ApplicantMenu().show(applicant, projects);
        } else if (loggedInUser instanceof HDBManager manager) {
            new ManagerMenu(manager, projects, allUsers).showMenu();
            CLIView.printMessage("Manager menu is not implemented yet.");
        } else {
            CLIView.printError("This user type is not yet supported.");
        }
    }

    private void handlePasswordChange(List<User> allUsers) {
        String nric = CLIView.prompt("Enter your NRIC: ");

        User targetUser = allUsers.stream()
                .filter(u -> u.getNric().equalsIgnoreCase(nric))
                .findFirst()
                .orElse(null);

        if (targetUser == null) {
            CLIView.printError("User not found.");
            return;
        }

        String currentPass = CLIView.prompt("Enter current password: ");
        String newPass = CLIView.prompt("Enter new password: ");

        boolean success = authController.changePassword(targetUser, currentPass, newPass);
        if (success) {
            CLIView.printMessage("Password updated. Please log in again.");
        }
    }
}
