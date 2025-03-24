package Applicant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Applicant {
	private String name;
	private int age;
	private String ID;
	private String password = "password";
	private String marital_status;
	private Project appliedProject = null;
	private String applicationStatus = "none";
	private List<String> enquiries;
	
	//constructor
	public Applicant(String name, String ID, int age, String marital_status) {
		this.name = name;
		this.ID = ID;
		this.age = age;
		this.marital_status = marital_status;
		this.enquiries = new ArrayList<>();
	}
	//methods
	public void viewProj(List<Project> projects) {
		System.out.println("Available Porjects:");
		for (Project project : projects) {
			if (project.isVisible() && isEligible(project)){
				System.out.println(project);
			}
		}
		
	}
	public boolean isEligible(Project project) {
		if (marital_status == "Married" && age>=21) {
			return true;
		}else if (marital_status == "Single" && age>=35) {
			return project.getflatType().equals("2-Room");
		}
		return false;
	}
	 
    public void applyProj() {
    	
    	
    }
    public void viewApplicationStatus() {
    	
    }
    
    public void requestWithdrawal() {
    	
    }
    public void createEnquries() {
    	Scanner sc = new Scanner(System.in);
    	System.out.println("Please enter your enquiry: "); //func implementation placed in class method instead of main for encapsulation  
        String enquiry = sc.nextLine();
        enquiries.add(enquiry);
        System.out.println("Enquiry submitted successfully.");
        sc.close();
    }
    public void viewEnquries() {
    	System.out.println("Your enquiries:");
    	for (int i=1; i <=enquiries.size();i++) {
    		System.out.println((i) + ") " + enquiries.get(i));
    	}	
    }
    public void deleteEnquries() {
    	Scanner sc = new Scanner(System.in);
    	int index;
    	System.out.println("Please enter the number for the enquiry you want to delete.");
    	index = sc.nextInt();
    	if (index<1||index>enquiries.size()) { //handle invalid case
    		System.out.println("Invalid enquiry index.");
    		return;
    	}else {
    		enquiries.remove(index-1);
    		System.out.println("Enquiry deleted.");
    		
    	}
    	sc.close();
    }
	
	
	
	
	
	
	
	

}
