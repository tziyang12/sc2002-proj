package controller;

import model.user.Applicant;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.User;

import java.util.List;

public class AuthenticationController {

    // This method simulates loading users (in a real system, users would be loaded from a database)
    public User authenticate(String nric, String password, List<User> users) {
        for (User user : users) {
            if (user.login(nric, password)) {
                return user; // Return the authenticated user
            }
        }
        return null; // Return null if no match is found
    }
}
