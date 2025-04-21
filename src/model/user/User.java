package model.user;

import model.user.enums.MaritalStatus;
import model.project.ProjectSearchCriteria;

/**
 * Abstract base class representing a generic user in the BTO Management System.
 * Contains common attributes like NRIC, password, age, and marital status.
 */
public abstract class User {
    private String name;
    private String nric;
    private String password;
    private int age;
    private MaritalStatus maritalStatus;
    private ProjectSearchCriteria searchCriteria;

    public User(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        this.name = name;
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.searchCriteria = new ProjectSearchCriteria();
    }

    
    /** 
     * @return String
     */
    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public ProjectSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(ProjectSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
