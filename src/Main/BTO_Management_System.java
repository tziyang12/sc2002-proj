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
        while (true) {
        	System.out.println("\nWelcome to the BTO Management System!");
            System.out.print("Please enter your NRIC: ");
            String enteredID = sc.nextLine().trim(); //.trim() to remove trailing whitespace
            
            if (!isValidID(enteredID)) {
            	System.out.println("ERROR! Invalid NRIC format! Plkease enter a valid NRIC.");
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
    private static void applicantDashboard(Applicant applicant, Scanner sc) {
    	while (true) {
    		System.out.println
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    }
    
        
    	
       
    	
    	
    
    	
    

}
