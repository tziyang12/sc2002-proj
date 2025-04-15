package model.user;

import model.user.enums.MaritalStatus;

import java.util.ArrayList;
import java.util.List;

public class HDBManager extends User {

    private List<Integer> managedProjectIDs;

    public HDBManager(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.managedProjectIDs = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "HDB Manager";  // Returning the role specific to this class
    }

    @Override
    public String toString() {
        return super.toString() + "\nRole: HDB Manager";
    }
}
