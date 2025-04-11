package ProjectManagement;

public class Project {
    private String projectName;
    private String flatType;
    private boolean visibility; 

    public Project(String projName, String flatType, boolean visibility) {
        this.projectName = projName;
        this.flatType = flatType;
        this.visibility = visibility;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getFlatType() {
        return flatType;
    }

    public boolean isVisible() {
        return visibility;
    }
    
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
