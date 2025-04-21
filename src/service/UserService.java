package service;

import model.user.Applicant;
import model.user.HDBOfficer;
import model.user.HDBManager;
import model.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling user-related operations such as authentication,
 * password management, and retrieving user details based on different criteria.
 */
public class UserService {

    private List<User> users;  // Holds the loaded users from DataLoader

    /**
     * Constructor that initializes the service with a list of pre-loaded users.
     *
     * @param users A list of users loaded from the data source.
     */
    public UserService(List<User> users) {
        this.users = users;
    }

    /** 
     * Finds a user by their NRIC.
     * 
     * @param nric The NRIC of the user to search for.
     * @return Optional<User> An Optional containing the user if found, otherwise an empty Optional.
     */
    public Optional<User> findUserByNric(String nric) {
        return users.stream()
                .filter(user -> user.getNric().equalsIgnoreCase(nric))
                .findFirst();
    }

    /** 
     * Finds a user by their username.
     * 
     * @param username The username of the user to search for.
     * @return Optional<User> An Optional containing the user if found, otherwise an empty Optional.
     */
    public Optional<User> findUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getName().equalsIgnoreCase(username))
                .findFirst();
    }

    /** 
     * Finds an applicant by their NRIC.
     * 
     * @param nric The NRIC of the applicant to search for.
     * @return Optional<Applicant> An Optional containing the applicant if found, otherwise an empty Optional.
     */
    public Optional<Applicant> findApplicantByNric(String nric) {
        return users.stream()
                .filter(user -> user instanceof Applicant)
                .map(user -> (Applicant) user)
                .filter(applicant -> applicant.getNric().equalsIgnoreCase(nric))
                .findFirst();
    }

    /** 
     * Authenticates a user by their NRIC and password.
     * 
     * @param nric The NRIC of the user to authenticate.
     * @param password The password of the user.
     * @return Optional<User> An Optional containing the user if authentication is successful, otherwise an empty Optional.
     */
    public Optional<User> authenticate(String nric, String password) {
        if (!ValidationService.isValidNric(nric)) return Optional.empty();
    
        return users.stream()
                .filter(user -> user.getNric().equalsIgnoreCase(nric))
                .filter(user -> user.getPassword().equals(password))
                .findFirst();
    }

    /** 
     * Validates and updates a user's password.
     * 
     * @param nric The NRIC of the user whose password is being updated.
     * @param currentPass The current password of the user.
     * @param newPass The new password to set.
     * @return boolean Returns true if the password is updated successfully, false otherwise.
     * @throws IllegalArgumentException if the NRIC is invalid or passwords are empty or match.
     */
    public boolean updatePasswordWithValidation(String nric, String currentPass, String newPass) {
        if (!ValidationService.isValidNric(nric)) {
            throw new IllegalArgumentException("[ERROR] Invalid NRIC format.");
        }

        if (ValidationService.isNullOrEmpty(currentPass) || ValidationService.isNullOrEmpty(newPass)) {
            throw new IllegalArgumentException("[ERROR] Passwords cannot be empty.");
        }

        if (currentPass.equals(newPass)) {
            throw new IllegalArgumentException("[ERROR] New password cannot be the same as the current password.");
        }

        return updatePassword(nric, currentPass, newPass); // returns true if updated
    }

    /** 
     * Updates a user's password.
     * 
     * @param nric The NRIC of the user whose password is being updated.
     * @param oldPassword The user's current password.
     * @param newPassword The new password to set.
     * @return boolean Returns true if the password is updated successfully, false if the current password is incorrect.
     */
    public boolean updatePassword(String nric, String oldPassword, String newPassword) {
        Optional<User> userOpt = findUserByNric(nric);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                return true;
            }
        }
        return false;   
    }

    /** 
     * Retrieves a list of all officers assigned to a specific manager.
     * 
     * @param manager The manager whose officers are to be retrieved.
     * @return List<HDBOfficer> A list of HDB officers assigned to the manager's projects.
     */
    public List<HDBOfficer> getOfficersForManager(HDBManager manager) {
        return users.stream()
                .filter(user -> user instanceof HDBOfficer)
                .map(user -> (HDBOfficer) user)
                .filter(officer -> officer.getAssignedProjects().stream()
                        .anyMatch(project -> manager.getManagedProjects().contains(project)))
                .toList();
    }

    /** 
     * Retrieves a list of all applicants.
     * 
     * @return List<Applicant> A list of all applicants.
     */
    public List<Applicant> getAllApplicants() {
        return users.stream()
                .filter(user -> user instanceof Applicant)
                .map(user -> (Applicant) user)
                .toList();
    }

    /** 
     * Retrieves a list of all managers.
     * 
     * @return List<HDBManager> A list of all managers.
     */
    public List<HDBManager> getAllManagers() {
        return users.stream()
                .filter(user -> user instanceof HDBManager)
                .map(user -> (HDBManager) user)
                .toList();
    }

    /** 
     * Retrieves a list of all officers.
     * 
     * @return List<HDBOfficer> A list of all officers.
     */
    public List<HDBOfficer> getAllOfficers() {
        return users.stream()
                .filter(user -> user instanceof HDBOfficer)
                .map(user -> (HDBOfficer) user)
                .toList();
    }

    /** 
     * Retrieves a list of all users.
     * 
     * @return List<User> A list of all users.
     */
    public List<User> getAllUsers() {
        return users;
    }
}
