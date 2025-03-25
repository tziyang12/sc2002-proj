package Roles;
import ProjectManagement.Project;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Applicant extends User{
	private Project appliedProject = null; // Stores the project the applicant has applied for; null if no application exists.
	private String applicationStatus = "none"; //Pending, Successful, Unsuccessful, Booked
	private List<String> enquiries;
	
	//constructor
	public Applicant(String name, String ID, int age, String maritalStatus) {
		super(name, ID, age, maritalStatus, "Applicant");
		this.enquiries = new ArrayList<>();
	}
	//methods
	public boolean isEligible(Project project) {
		if (maritalStatus.equals("Married") && age>=21) {
			return true; //eligible for all
		}else if (maritalStatus.equals("Single") && age>=35) {
			return project.getFlatType().equals("2-Room"); //true if have 2-Room, false if no 2-Room (only eligible for 2-Room)
		}
		return false; //X eligible at all
	}
	
	public void viewProject(List<Project> projects) { //Parameter: List of projects
		System.out.println("Available Porjects:");
		//when toggle ON
		for (Project project : projects) { //run thru all project in project class, only print if applicant meets the 2 condition
			if (project.isVisible() && isEligible(project)){
				System.out.println(project.getProjectName() + " - " + project.getFlatType());				
				}
			}
        //when toggle OFF
	    if (appliedProject!=null) {
	    	System.out.println("Applied Project: " + appliedProject.getProjectName() + " [Applicationc Status: " + applicationStatus + "]");
	    }
	}
		 
    public void applyProject(Project project) { //Parameter: Project applicant want to apply for
    	if (appliedProject!=null) {
    		System.out.println("You have already applied for a project.");
    		System.out.println("Note: Each applicant can only apply for one project.");
    		return;
    	}
    	if (!isEligible(project)) {
    		System.out.println("You do not meet the eligibility criteria for this project.");
    		return;
    	}
    	//calling attribute
    	this.appliedProject = project;
    	this.applicationStatus = "Pending";
    	System.out.println("Application submitted for " + project.getProjectName()+ " has been submitted successfully!");
    }
    
    public void viewApplicationStatus() {
    	if (appliedProject == null) {
    		System.out.println("No application found.");		
    	}else {
    		System.out.println("Application Status for " + appliedProject.getProjectName() + ": " + applicationStatus);
    	}
    }
    
    public void withdrawalApplication() {
    	if (appliedProject == null) {
    		System.out.println("No application to withdraw.");
    	}else {
    		System.out.println("Application for " + appliedProject.getProjectName()+ " withdrawal.");
    		this.appliedProject = null;
    		this.applicationStatus = "none";
    	}
   	
    }
    public void addEnquries() {
    	Scanner sc = new Scanner(System.in);
    	System.out.println("Please enter your enquiry: "); //func implementation placed in class method instead of main for encapsulation  
        String enquiry = sc.nextLine();
        enquiries.add(enquiry);
        System.out.println("Enquiry submitted successfully.");
    }
    public void viewEnquries() {
    	System.out.println("Your enquiries:");
    	for (int i=1; i <=enquiries.size();i++) {
    		System.out.println(i + ") " + enquiries.get(i-1));
    	}	
    }
    public void editEnquiries(int index, String newEnquiry) {
        if (index<1||index>enquiries.size()){
        	System.out.println("Invalid enquiry index.");
        	return;
        }
        enquiries.set(index-1, newEnquiry);
        System.out.println("Enquiry updated.");
    }
    public void deleteEnquries() {
    	Scanner sc = new Scanner(System.in);
    	int index;
    	System.out.println("Please enter the number for the enquiry you want to delete.");
    	index = sc.nextInt();
    	sc.close();
    	if (index<1||index>enquiries.size()) { //handle invalid case
    		System.out.println("Invalid enquiry index.");
    		return;
    	}else {
    		enquiries.remove(index-1);
    		System.out.println("Enquiry deleted.");
    		
    	}
    	
    }
	
}
