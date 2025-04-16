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

    // Find a user by their username
    public Optional<User> findUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getName().equals(username))
                .findFirst();
    }

    // Find a user by their NRIC (specific to applicants)
    public Optional<Applicant> findApplicantByNric(String nric) {
        return users.stream()
                .filter(user -> user instanceof Applicant)
                .map(user -> (Applicant) user)
                .filter(applicant -> applicant.getNric().equals(nric))
                .findFirst();
    }

    // Authenticate user login
    public boolean authenticateUser(String username, String password) {
        Optional<User> user = findUserByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    // Update user password
    public boolean updatePassword(User user, String newPassword) {
        if (user != null) {
            user.setPassword(newPassword);
            return true;
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
}
