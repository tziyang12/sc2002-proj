package Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ProjectManagement.BTOProject;
import ProjectManagement.FlatType;


public class Applicant extends User{
	private BTOProject appliedProject = null; // Stores the applied project (null if none)
    private FlatType appliedFlatType = null;  // Stores the specific flat type applied
	private String applicationStatus = "none"; //Pending, Successful, Unsuccessful, Booked
	private List<String> enquiries;
	
	//constructor
	public Applicant(String name, String ID, int age, String maritalStatus) {
		super(name, ID, age, maritalStatus, "Applicant");
		this.enquiries = new ArrayList<>();
	}
    
	//methods 
	public boolean isEligible(BTOProject project, FlatType type) {
        if (!project.isVisible()) return false;

        int unitsAvailable = project.getNumUnits(type);
        if (unitsAvailable <= 0) return false;

        if (maritalStatus.equals("Married") && age >= 21) {
            return true;
        } else if (maritalStatus.equals("Single") && age >= 35) {
            return type == FlatType.TWO_ROOM;
        }

        return false;
    }
	
	public void viewProject(List<BTOProject> projects) {
        System.out.println("Available Projects:");

        for (BTOProject project : projects) {
            if (!project.isVisible()) continue;

            System.out.println("== " + project.getProjectName() + " (" + project.getNeighbourhood() + ") ==");

            for (Map.Entry<FlatType, Integer> entry : project.getFlatUnits().entrySet()) {
                FlatType type = entry.getKey();
                int count = entry.getValue();
                if (isEligible(project, type)) {
                    System.out.println(" - " + type + ": " + count + " units available");
                }
            }
        }

        if (appliedProject != null) {
            System.out.println("\nApplied Project: " + appliedProject.getProjectName()
                    + " (" + appliedFlatType + ") [Status: " + applicationStatus + "]");
        }
    }
		 
    public void applyProject(BTOProject project, FlatType flatType) {
        if (appliedProject != null) {
            System.out.println("You have already applied for a project.");
            System.out.println("Note: Each applicant can only apply for one project.");
            return;
        }

        if (!isEligible(project, flatType)) {
            System.out.println("You do not meet the eligibility criteria for this flat type.");
            return;
        }

        this.appliedProject = project;
        this.appliedFlatType = flatType;
        this.applicationStatus = "Pending";

        System.out.println("Application submitted for " + project.getProjectName()
                + " (" + flatType + ") has been submitted successfully!");
    }
    
    public void viewApplicationStatus() {
        if (appliedProject == null) {
            System.out.println("No application found.");
        } else {
            System.out.println("Application Status for " + appliedProject.getProjectName() + " (" + appliedFlatType + "): " + applicationStatus);
        }
    }
    
    public void withdrawalApplication() {
    	if (appliedProject == null) {
    		System.out.println("No application to withdraw.");
    	}else {
    		System.out.println("Application for " + appliedProject.getProjectName() + " (" + appliedFlatType + ") withdrawal.");
    		this.appliedProject = null;
            this.appliedFlatType = null;
    		this.applicationStatus = "none";
    	}
   	
    }
    //Manage enquiries
    public void addEnquiry(String enquiry) {
        enquiries.add(enquiry);
        System.out.println("Enquiry submitted successfully.");
    }

    public void viewEnquiries() {
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries available.");
            return;
        }
        System.out.println("Your enquiries:");
        for (int i = 0; i < enquiries.size(); i++) {
            System.out.println((i + 1) + ") " + enquiries.get(i));
        }
    }

    public boolean editEnquiry(int editIndex, String newEnquiry) {
        if (editIndex < 1 || editIndex > enquiries.size()) {
            System.out.println("Invalid enquiry index.");
            return false;
        }
        enquiries.set(editIndex - 1, newEnquiry);
        System.out.println("Enquiry updated.");
        return true;
    }

    public boolean deleteEnquiry(int deleteIndex) {
        if (deleteIndex < 1 || deleteIndex > enquiries.size()) {
            System.out.println("Invalid enquiry index.");
            return false;
        }
        enquiries.remove(deleteIndex - 1);
        System.out.println("Enquiry deleted.");
        return true;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String status) {
        this.applicationStatus = status;
    }

    public BTOProject getAppliedProject() {
        return appliedProject;
    }

    public FlatType getAppliedFlatType() {
        return appliedFlatType;
    }
}