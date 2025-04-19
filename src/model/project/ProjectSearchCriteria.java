package model.project;

public class ProjectSearchCriteria {
    private String neighborhood;
    private FlatType flatType;
    private boolean isMarried; // for applicants
    private int minAge;
    private int maxAge;
    private boolean sortByPriceAscending;

    // Constructors, getters, setters
    public ProjectSearchCriteria() {
        this.neighborhood = "";
        this.flatType = null;
        this.isMarried = false;
        this.minAge = 0;
        this.maxAge = 100; // Default max age
        this.sortByPriceAscending = true; // Default sorting order
    }

    public String getNeighbourhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    public boolean isMarried() {
        return isMarried;
    }
    public void setMarried(boolean married) {
        isMarried = married;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isSortByPriceAscending() {
        return sortByPriceAscending;
    }

    public void setSortByPriceAscending(boolean sortByPriceAscending) {
        this.sortByPriceAscending = sortByPriceAscending;
    }

    public void setSortByPriceDescending(boolean sortByPriceDescending) {
        this.sortByPriceAscending = !sortByPriceDescending;
    }
}
