package ui;

import controller.AuthenticationController;
import model.project.Project;
import model.user.Applicant;
import model.user.HDBOfficer;
import model.user.HDBManager;
import model.user.User;
import service.UserService;

import java.util.List;

public class MainMenu {
    private final AuthenticationController authController;
    private final UserService userService;

    public MainMenu(List<User> allUsers) {
        // Initialize the UserService with the list of all users
        this.userService = new UserService(allUsers);
        this.authController = new AuthenticationController(userService);
    }

    public void show(List<Project> projects) {
        String[] mainOptions = {"Login", "Change Password", "Exit"};

        while (true) {
            CLIView.printHeader("Welcome to the BTO Management System");
            CLIView.printMenu(mainOptions);
            String choice = CLIView.prompt("");

            switch (choice) {
                case "1" -> handleLogin(projects);
                case "2" -> handlePasswordChange();
                case "3" -> {
                    CLIView.printMessage("Exiting system. Goodbye!");
                    return;
                }
                default -> CLIView.printError("Invalid option. Try again.");
            }
        }
    }

    private void handleLogin(List<Project> projects) {
        String nric = CLIView.prompt("Enter NRIC: ");
        String password = CLIView.prompt("Enter Password: ");

        User loggedInUser = authController.authenticate(nric, password);

        if (loggedInUser == null) return;

        CLIView.printMessage("Login successful. Welcome, " + loggedInUser.getName());

        if (loggedInUser instanceof HDBOfficer officer) {
            List<Applicant> applicantList = userService.getAllApplicants();
            new OfficerMenu(officer, projects, applicantList).showMenu();
        } else if (loggedInUser instanceof Applicant applicant) {
            new ApplicantMenu().show(applicant, projects);
        } else if (loggedInUser instanceof HDBManager manager) {
            new ManagerMenu(manager, projects, userService.getAllUsers()).showMenu();
        } else {
            CLIView.printError("This user type is not yet supported.");
        }
    }

    private void handlePasswordChange() {
        String nric = CLIView.prompt("Enter your NRIC: ");
        String currentPass = CLIView.prompt("Enter current password: ");
        String newPass = CLIView.prompt("Enter new password: ");

        boolean success = authController.changePassword(nric, currentPass, newPass);
        if (success) {
            CLIView.printMessage("Password updated. Please log in again.");
        } else {
            CLIView.printError("Incorrect NRIC or password.");
        }
    }
}   
