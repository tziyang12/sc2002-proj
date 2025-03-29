package Main;
import Roles.*;
import ProjectManagement.*;
import java.util.*;

public class BTO_Management_System {
	private static List<User> users = new ArrayList<>();
    private static List<Project> projects = new ArrayList<>();
	private static boolean systemUpdated = false;
    public static void main(String[] args) {
        //Applicants registered
    	users.add(new Applicant("John", "S1234567A", 35, "Single"));
    	users.add(new Applicant("Sarah", "T7654321B", 40, "Married"));
    	users.add(new Applicant("Grace", "S9876543C", 37, "Married"));
    	users.add(new Applicant("James", "T2345678D", 30, "Married"));
    	users.add(new Applicant("Rachel", "S3456789E", 25, "Single"));
    	Scanner sc = new Scanner(System.in);
        
    	//HELP: should i put this in a separate class for further encapsulation so main page is super clean as user no need to know what is happening?
        while (true) {
        	System.out.println("\nWelcome to the BTO Management System!");
            System.out.print("Please enter your NRIC: ");
            String enteredID = sc.nextLine().trim(); //.trim() to remove trailing whitespace
            
            if (!isValidID(enteredID)) {
            	System.out.println("ERROR! Invalid NRIC format! Please enter a valid NRIC.");
            	continue;
            }
            System.out.print("Please enter your password");
            String enteredPassword = sc.nextLine().trim();
            User currentUser = authenticateUser(enteredID, enteredPassword);
            if (currentUser!=null) {
            	System.out.println("Login Successful! Welcome, " + currentUser.getName());
            	
            	if (systemUpdated) {
            		System.out.println("System has been updated. Please re-login with new credentials.");
            		systemUpdated = false;
            		continue;
            	}
            	if (currentUser instanceof Applicant) {
            		if (currentUser.getPassword().equals("password")){
            			System.out.println("Please change your default password.");
            			changePassword((Applicant) currentUser, sc);
            		}else{
            			System.out.println("Warning: Access restricted for non-applcants.");		
            		}
            	}
            	
            }else {
            	System.out.println("ERROR! Incorrect NRIC or Password! Please try again.");
            }   
        }
    }
    private static boolean isValidID(String id) {
    	return id.matches("^[ST]\\d{7}[A-Z]$");
    }
    private static User authenticateUser(String id, String password) {
    	for (User user: users) {
    		if (user.getID().equals(id)) {
    			if (user.getPassword().equals(password)) {
    				return user;
    			}
    		}
    	}
    	return null;
    }
    private static void changePassword(Applicant applicant, Scanner sc) {
    	String currentPassword;
    	//loop until current password is correct
    	while (true) {
    		System.out.print("Please enter your current password: ");
        	currentPassword = sc.nextLine();
        	if (!applicant.getPassword().equals(currentPassword)) {
        		System.out.println("ERROR! Incorrect current password! Password changeb failed.");		
        	}else {
        		break;
        	}	
    	}
    	
    	//loop until new and confirm password match
    	String newPassword, confirmPassword;
    	while(true) {
    		System.out.println("Please enter your new password: ");
    		newPassword = sc.nextLine().trim();
    		System.out.println("Please enter your confirm password: ");
    		confirmPassword = sc.nextLine();
    		if (!newPassword.equals(confirmPassword)) {
    			System.out.println("ERROR! Passwords do not match! Please try again.");			
    		}else {
    			break;
    		}
    		applicant.changePassword(newPassword);
    		System.out.println("Password changed successfully!");	
    	}	
    }
    //if code too long after placing everything together, place each user dashboard in seperate classes grouped in same package
    private static void applicantDashboard(Applicant applicant, Scanner sc) {
        while (true) {
            System.out.println("\n=== Applicant Dashboard ===");
            System.out.println("1. View Available Projects");
            System.out.println("2. Apply for a Project");
            System.out.println("3. View Application Status");
            System.out.println("4. Withdraw Application");
            System.out.println("5. Create enquiries");
            System.out.println("6. View Application");
            System.out.println("7. Delete enquiries");
            System.out.println("8. Change Password");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();
            
            switch(choice) {
                case 1:
                    applicant.viewProject(projects);
                    break;
                case 2:
                    System.out.println("Please enter project name to apply: ");
                    String projectName = sc.nextLine();
                    for (Project p : projects) {
                        if (p.getProjectName().equalsIgnoreCase(projectName)) { 
                            applicant.applyProject(p);
                            break;
                        }
                    }
                    break;
                case 3:
                    applicant.viewApplicationStatus();
                    break;
                case 4:
                    applicant.withdrawalApplication();
                    break;
                case 5:
                    applicant.addEnquries();
                    break;
                case 6:
                    applicant.viewEnquries();
                    break;
                case 7:
                    applicant.deleteEnquries();
                    break;
                case 8:
                    changePassword(applicant, sc);
                    break;
                case 9:
                    System.out.println("Logout successful.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
}
    
    
        
    	
       
    	
    	
    
    	
    


