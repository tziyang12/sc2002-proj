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
        if (ValidationService.isNullOrEmpty(nric)) {
            System.out.println("[ERROR] NRIC cannot be empty.");
            return null;
        }

        if (!ValidationService.isValidNric(nric)) {
            System.out.println("[ERROR] Invalid NRIC format. Please check and try again.");
            return null;
        }

        if (ValidationService.isNullOrEmpty(password)) {
            System.out.println("[ERROR] Password cannot be empty.");
            return null;
        }

        return userService.authenticate(nric, password).orElseGet(() -> {
            System.out.println("[ERROR] Incorrect NRIC or password. Please try again.");
            return null;
        });
    }

    public boolean changePassword(String nric, String currentPass, String newPass) {
        try {
            return userService.updatePasswordWithValidation(nric, currentPass, newPass);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
