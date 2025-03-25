package Applicant;

public class Project {
	private String projName;
	private String flatType;
	private boolean visibility; // 1 for on; 0 for off
	
	//constructor to initialize attributes
	public Project(String projName, String flatType, boolean visibility) {
		this.projName = projName;
		this.flatType = flatType;
		this.visibility = visibility;
		
	}
	
	//getters
	public String getProjName() {
		return projName;
	}
	public String getFlatType() {
		return flatType;
	}
	public boolean isVisible() {
		return visibility;
	}
	
}
