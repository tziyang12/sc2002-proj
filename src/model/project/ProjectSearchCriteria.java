package model.project;

import model.user.enums.MaritalStatus;
import java.util.HashSet;
import java.util.Set;

public class ProjectSearchCriteria {
    private String neighbourhood;
    private Set<FlatType> flatTypes;
    private MaritalStatus maritalStatusFilter;
    private int minAge;
    private int maxAge;
    private boolean sortByPriceAscending;

    public ProjectSearchCriteria() {
        this.neighbourhood = "";
        this.flatTypes = new HashSet<>();
        this.maritalStatusFilter = MaritalStatus.BOTH;
        this.minAge = 0;
        this.maxAge = 120;
        this.sortByPriceAscending = true;
    }

    
    /** 
     * @return String
     */
    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public Set<FlatType> getFlatTypes() {
        return flatTypes;
    }

    public void setFlatTypes(Set<FlatType> flatTypes) {
        this.flatTypes = flatTypes;
    }

    public MaritalStatus getMaritalStatusFilter() {
        return maritalStatusFilter;
    }

    public void setMaritalStatusFilter(MaritalStatus maritalStatusFilter) {
        this.maritalStatusFilter = maritalStatusFilter;
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
}
