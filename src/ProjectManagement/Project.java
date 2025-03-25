package ProjectManagement;

public class Project {
	private String projectName;
	private String flatType;
	private boolean visibility; // 1 for on; 0 for off
	
	//constructor to initialize attributes
	public Project(String projName, String flatType, boolean visibility) {
		this.projectName = projName;
		this.flatType = flatType;
		this.visibility = visibility;
		
	}
	
	//getters
	public String getProjectName() {
		return projectName;
	}
	public String getFlatType() {
		return flatType;
	}
	public boolean isVisible() {
		return visibility;
	}
	
}
