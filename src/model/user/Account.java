package model.user;

public abstract class Account {
    protected String userID;
    protected String password;
    protected String fullName;

    // Constructor
    public Account(String userID, String password, String fullName) {
        this.userID = userID;
        this.password = password;
        this.fullName = fullName;
    }

    // Getters
    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    // Setters
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Abstract method for dashboard/menu specific to user type
    public abstract void displayMenu();

    @Override
    public String toString() {
        return "User ID: " + userID + "\nName: " + fullName;
    }
}
