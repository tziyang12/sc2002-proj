package controller;

import model.user.User;
import service.UserService;
import service.ValidationService;

/**
 * Controller responsible for handling user authentication,
 * including login and password change functionality.
 */
public class AuthenticationController {

    private final UserService userService;

    /**
     * Constructs an AuthenticationController with the given UserService.
     *
     * @param userService the user service used for authentication and password management
     */
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Authenticates a user based on NRIC and password.
     * Performs basic validation before attempting to log in.
     *
     * @param nric     the NRIC entered by the user
     * @param password the password entered by the user
     * @return the authenticated User object if successful, otherwise null
     */
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

    /**
     * Attempts to change a user's password, given the NRIC, current password,
     * and new password. Delegates validation to the UserService.
     *
     * @param nric         the NRIC of the user
     * @param currentPass  the current password
     * @param newPass      the new password to be set
     * @return true if password change was successful, false otherwise
     */
    public boolean changePassword(String nric, String currentPass, String newPass) {
        try {
            return userService.updatePasswordWithValidation(nric, currentPass, newPass);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
