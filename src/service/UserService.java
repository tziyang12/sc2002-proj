package service;

import model.user.Applicant;
import model.user.HDBOfficer;
import model.user.HDBManager;
import model.user.User;

import java.util.List;
import java.util.Optional;

public class UserService {

    private List<User> users;  // Holds the loaded users from DataLoader

    // Constructor that takes the pre-loaded list of users
    public UserService(List<User> users) {
        this.users = users;
    }

    // Find a user by their NRIC
    public Optional<User> findUserByNric(String nric) {
        return users.stream()
                .filter(user -> user.getNric().equalsIgnoreCase(nric))
                .findFirst();
    }

    // Find a user by their username
    public Optional<User> findUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getName().equalsIgnoreCase(username))
                .findFirst();
    }

    // Find a user by their NRIC (specific to applicants)
    public Optional<Applicant> findApplicantByNric(String nric) {
        return users.stream()
                .filter(user -> user instanceof Applicant)
                .map(user -> (Applicant) user)
                .filter(applicant -> applicant.getNric().equalsIgnoreCase(nric))
                .findFirst();
    }

    // Authenticate user login
    public Optional<User> authenticate(String nric, String password) {
        if (!ValidationService.isValidNric(nric)) return Optional.empty();
    
        return users.stream()
                .filter(user -> user.getNric().equalsIgnoreCase(nric))
                .filter(user -> user.getPassword().equals(password))
                .findFirst();
    }

    // Update user password
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

    // Get a list of all officers for a specific manager
    public List<HDBOfficer> getOfficersForManager(HDBManager manager) {
        return users.stream()
                .filter(user -> user instanceof HDBOfficer)
                .map(user -> (HDBOfficer) user)
                .filter(officer -> officer.getAssignedProjects().stream()
                        .anyMatch(project -> manager.getManagedProjects().contains(project)))
                .toList();
    }

    // Get all applicants (this could be useful for filtering purposes)
    public List<Applicant> getAllApplicants() {
        return users.stream()
                .filter(user -> user instanceof Applicant)
                .map(user -> (Applicant) user)
                .toList();
    }

    // Get all managers
    public List<HDBManager> getAllManagers() {
        return users.stream()
                .filter(user -> user instanceof HDBManager)
                .map(user -> (HDBManager) user)
                .toList();
    }

    // Get all officers
    public List<HDBOfficer> getAllOfficers() {
        return users.stream()
                .filter(user -> user instanceof HDBOfficer)
                .map(user -> (HDBOfficer) user)
                .toList();
    }

    //Get all users
    public List<User> getAllUsers() {
        return users;
    }
}
