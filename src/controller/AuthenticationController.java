package controller;

import model.user.User;
import service.UserService;
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    public User authenticate(String nric, String password) {
        return userService.authenticate(nric, password).orElse(null);
    }
    
    // This method checks if the user is an applicant
    public boolean changePassword(String nric, String currentPass, String newPass) {
        return userService.updatePassword(nric, currentPass, newPass);
    }
    
}
