package model.project;

import java.util.HashSet;
import java.util.Set;

import model.user.MaritalStatus;

/**
 * Represents the search criteria for filtering BTO project searches based on various factors such as
 * neighbourhood, flat types, marital status, age range, and sorting preferences.
 * 
 * This class allows users to specify the criteria they want to use when searching for available projects
 * in the BTO system.
 */
public class ProjectSearchCriteria {

    /** The neighbourhood filter for the project search. */
    private String neighbourhood;

    /** The set of flat types to filter for the project search. */
    private Set<FlatType> flatTypes;

    /** The marital status filter for the project search. */
    private MaritalStatus maritalStatusFilter;

    /** The minimum age requirement for applicants in the project search. */
    private int minAge;

    /** The maximum age limit for applicants in the project search. */
    private int maxAge;

    /** The flag indicating whether to sort the project results by price in ascending order. */
    private boolean sortByPriceAscending;

    /**
     * Constructs a {@code ProjectSearchCriteria} object with default values:
     * - neighbourhood: empty string
     * - flatTypes: empty set
     * - maritalStatusFilter: BOTH
     * - minAge: 0
     * - maxAge: 120
     * - sortByPriceAscending: true
     */
    public ProjectSearchCriteria() {
        this.neighbourhood = "";
        this.flatTypes = new HashSet<>();
        this.maritalStatusFilter = MaritalStatus.BOTH;
        this.minAge = 0;
        this.maxAge = 120;
        this.sortByPriceAscending = true;
    }

    /**
     * Returns the neighbourhood filter for the project search.
     * 
     * @return the neighbourhood filter as a {@code String}
     */
    public String getNeighbourhood() {
        return neighbourhood;
    }

    /**
     * Sets the neighbourhood filter for the project search.
     * 
     * @param neighbourhood the neighbourhood to set
     */
    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    /**
     * Returns the set of flat types to filter for the project search.
     * 
     * @return the set of flat types
     */
    public Set<FlatType> getFlatTypes() {
        return flatTypes;
    }

    /**
     * Sets the flat types to filter for the project search.
     * 
     * @param flatTypes the set of flat types to set
     */
    public void setFlatTypes(Set<FlatType> flatTypes) {
        this.flatTypes = flatTypes;
    }

    /**
     * Returns the marital status filter for the project search.
     * 
     * @return the marital status filter as a {@code MaritalStatus}
     */
    public MaritalStatus getMaritalStatusFilter() {
        return maritalStatusFilter;
    }

    /**
     * Sets the marital status filter for the project search.
     * 
     * @param maritalStatusFilter the marital status filter to set
     */
    public void setMaritalStatusFilter(MaritalStatus maritalStatusFilter) {
        this.maritalStatusFilter = maritalStatusFilter;
    }

    /**
     * Returns the minimum age requirement for the project search.
     * 
     * @return the minimum age
     */
    public int getMinAge() {
        return minAge;
    }

    /**
     * Sets the minimum age requirement for the project search.
     * 
     * @param minAge the minimum age to set
     */
    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    /**
     * Returns the maximum age limit for the project search.
     * 
     * @return the maximum age
     */
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * Sets the maximum age limit for the project search.
     * 
     * @param maxAge the maximum age to set
     */
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    /**
     * Returns the flag indicating whether the project results should be sorted by price in ascending order.
     * 
     * @return {@code true} if the results should be sorted by price in ascending order, {@code false} otherwise
     */
    public boolean isSortByPriceAscending() {
        return sortByPriceAscending;
    }

    /**
     * Sets the flag indicating whether the project results should be sorted by price in ascending order.
     * 
     * @param sortByPriceAscending {@code true} to sort by price in ascending order, {@code false} otherwise
     */
    public void setSortByPriceAscending(boolean sortByPriceAscending) {
        this.sortByPriceAscending = sortByPriceAscending;
    }
}
