package model.user;

import model.project.ProjectSearchCriteria;

/**
 * Abstract base class representing a generic user in the BTO Management System.
 * Contains common user attributes like name, NRIC, password, age, marital status, and search criteria.
 * Subclasses define specific user roles and functionalities.
 */
public abstract class User {
    private String name;
    private String nric;
    private String password;
    private int age;
    private MaritalStatus maritalStatus;
    private ProjectSearchCriteria searchCriteria;

    /**
     * Constructs a User with the specified personal information.
     *
     * @param name the user's name
     * @param nric the user's NRIC
     * @param password the user's login password
     * @param age the user's age
     * @param maritalStatus the user's marital status
     */
    public User(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
        this.name = name;
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.searchCriteria = new ProjectSearchCriteria();
    }

    /**
     * Gets the name of the user.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the NRIC of the user.
     *
     * @return the user's NRIC
     */
    public String getNric() {
        return nric;
    }

    /**
     * Sets the NRIC of the user.
     *
     * @param nric the new NRIC
     */
    public void setNric(String nric) {
        this.nric = nric;
    }

    /**
     * Gets the user's password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's age.
     *
     * @return the user's age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the user's age.
     *
     * @param age the new age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the marital status of the user.
     *
     * @return the marital status
     */
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Sets the marital status of the user.
     *
     * @param maritalStatus the new marital status
     */
    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * Authenticates the user by comparing input credentials with stored values.
     *
     * @param inputNric the input NRIC
     * @param inputPassword the input password
     * @return true if credentials match; false otherwise
     */
    public boolean login(String inputNric, String inputPassword) {
        return this.nric.equals(inputNric) && this.password.equals(inputPassword);
    }

    /**
     * Returns the role of the user.
     * Must be implemented by all subclasses.
     *
     * @return the user role as a string
     */
    public abstract String getRole();

    /**
     * Gets the project search criteria configured by the user.
     *
     * @return the project search criteria
     */
    public ProjectSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    /**
     * Sets the user's project search criteria.
     *
     * @param searchCriteria the new search criteria
     */
    public void setSearchCriteria(ProjectSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
