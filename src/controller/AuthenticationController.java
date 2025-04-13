package controller;

import model.user.Applicant;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.User;

import java.util.List;

public class AuthenticationController {

    private boolean isValidNric(String nric) {
        return nric != null && nric.matches("^[STFG]\\d{7}[A-Z]$");
    }
     
    // This method simulates loading users (in a real system, users would be loaded from a database)
    public User authenticate(String nric, String password, List<User> users) {
        if (!isValidNric(nric)) {
            System.out.println("Invalid NRIC format. Please check and try again.");
            return null;
        }
    
        for (User user : users) {
            if (user.getNric().equalsIgnoreCase(nric)) {
                if (user.getPassword().equals(password)) {
                    return user;
                } else {
                    System.out.println("Incorrect password. Please try again.");
                    return null;
                }
            }
        }
    
        System.out.println("No user found with the provided NRIC.");
        return null;
    }
    
    // This method checks if the user is an applicant
    public boolean changePassword(User user, String oldPassword, String newPassword) {
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            System.out.println("Password changed successfully. Please log in again.");
            return true;
        } else {
            System.out.println("Incorrect old password. Password change failed.");
            return false;
        }
    }
    
}
