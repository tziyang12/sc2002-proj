package ui;

import controller.AuthenticationController;
import data.DataSaver;
import data.ProjectRepository;
import model.project.Project;
import model.transaction.Enquiry;
import model.transaction.Application;
import model.user.Applicant;
import model.user.HDBOfficer;
import model.user.HDBManager;
import model.user.User;
import service.UserService;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents the main menu of the BTO Management System.
 * Handles user login, password changes, and system exit with data saving.
 */
public class MainMenu {
    private final AuthenticationController authController;
    private final UserService userService;
    /**
     * Constructs the MainMenu with the provided list of users.
     *
     * @param allUsers List of all users in the system.
     */
    public MainMenu(List<User> allUsers) {
        // Initialize the UserService with the list of all users
        this.userService = new UserService(allUsers);
        this.authController = new AuthenticationController(userService);
    }
    /**
     * Displays the main menu and handles user input for login, password change, and exit.
     */
    public void show() {
        String[] mainOptions = {"Login", "Change Password", "Exit"};

        while (true) {
            CLIView.printHeader("Welcome to the BTO Management System");
            CLIView.printMenu(mainOptions);
            int choice = CLIView.promptInt("");

            switch (choice) {
                case 1 -> handleLogin(ProjectRepository.getAllProjects());
                case 2 -> handlePasswordChange();
                case 3 -> {
                    List<Project> allProjects = ProjectRepository.getAllProjects();
                    List<Application> allApplications = new ArrayList<>();
                    List<Enquiry> allEnquiries = new ArrayList<>();

                    for (Project project : allProjects) {
                        if (project.getApplications() != null)
                            allApplications.addAll(project.getApplications());
                        if (project.getEnquiries() != null)
                            allEnquiries.addAll(project.getEnquiries());
}
                    CLIView.printMessage("Exiting system. Goodbye!");
                    DataSaver.saveApplications("src/data/ApplicationList.csv", allApplications);
                    DataSaver.saveEnquiries("src/data/EnquiryList.csv", allEnquiries);
                    DataSaver.saveProjects("src/data/ProjectList.csv", allProjects);

                    return;
                }
                default -> CLIView.printError("Invalid option. Try again.");
            }
        }
    }

    /**
     * Handles user login by prompting for NRIC and password,
     * authenticating the user, and launching the appropriate menu based on user role.
     *
     * @param projects List of all available BTO projects.
     */
    private void handleLogin(List<Project> projects) {
        String nric = CLIView.prompt("Enter NRIC: ");
        String password = CLIView.prompt("Enter Password: ");

        User loggedInUser = authController.authenticate(nric, password);

        if (loggedInUser == null) return;

        CLIView.printMessage("Login successful. Welcome, " + loggedInUser.getName());

        switch (loggedInUser) {
            case HDBOfficer officer -> {
                List<Applicant> applicantList = userService.getAllApplicants();
                new OfficerMenu(officer, projects, applicantList).showMenu();
            }
            case Applicant applicant -> new ApplicantMenu().show(applicant, projects);
            case HDBManager manager -> new ManagerMenu(manager, projects, userService.getAllUsers()).showMenu();
            default -> CLIView.printError("This user type is not yet supported.");
        }
    }

    /**
     * Handles the password change process by validating user credentials
     * and updating the password if the old one matches.
     */
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
