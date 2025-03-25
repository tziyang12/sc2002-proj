package Roles;

public class User {
	protected String name;
	protected int age;
	protected String ID;
	protected String password = "password";
	protected String maritalStatus;

	//Constructor
	public User(String name, String ID, int age, String maritalStatus, String role) {
		this.name = name;
		this.age = age;
		this.ID = ID;
		this.maritalStatus = maritalStatus;
		
	}
	public String getName() {return name;}
	public String getID() { return ID; }
    public String getPassword() { return password; }
    public void changePassword(String newPassword) { this.password = newPassword; }
	

}
