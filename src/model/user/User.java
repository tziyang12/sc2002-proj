package model.user;

import model.user.enums.MaritalStatus;

/**
 * Abstract base class representing a generic user in the BTO Management System.
 * Contains common attributes like NRIC, password, age, and marital status.
 */
public abstract class User {
    private String nric;
    private String password;
    private int age;
    private MaritalStatus maritalStatus;

    public User(String nric, String password, int age, MaritalStatus maritalStatus) {
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
    }

    // Getters and Setters
    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
	
	public boolean login(String inputNric, String inputPassword) {
        return this.nric.equals(inputNric) && this.password.equals(inputPassword);
    }
    /**
     * Returns the role of the user.
     * To be implemented by subclasses.
     */
    public abstract String getRole();
}
