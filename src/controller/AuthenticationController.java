package controller;

import model.user.User;
import service.UserService;
import service.ValidationService;

public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    public User authenticate(String nric, String password) {
        if (isEmpty(nric)) {
            System.out.println("NRIC cannot be empty.");
            return null;
        }

        if (!ValidationService.isValidNric(nric)) {
            System.out.println("Invalid NRIC format. Please check and try again.");
            return null;
        }

        if (isEmpty(password)) {
            System.out.println("Password cannot be empty.");
            return null;
        }

        return userService.authenticate(nric, password).orElseGet(() -> {
            System.out.println("Incorrect NRIC or password. Please try again.");
            return null;
        });
    }

    public boolean changePassword(String nric, String currentPass, String newPass) {
        if (isEmpty(nric)) {
            System.out.println("NRIC cannot be empty.");
            return false;
        }

        if (!ValidationService.isValidNric(nric)) {
            System.out.println("Invalid NRIC format.");
            return false;
        }

        if (isEmpty(currentPass)) {
            System.out.println("Current password cannot be empty.");
            return false;
        }

        if (isEmpty(newPass)) {
            System.out.println("New password cannot be empty.");
            return false;
        }

        if (newPass.equals(currentPass)) {
            System.out.println("New password cannot be the same as the current password.");
            return false;
        }

        return userService.updatePassword(nric, currentPass, newPass);
    }

    // Helper to reduce repetition
    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
