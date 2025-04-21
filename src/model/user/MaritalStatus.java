package model.user;

/**
 * Enumeration representing the marital status of a user.
 * Used to determine BTO eligibility and application rules.
 */
public enum MaritalStatus {
    /**
     * User is single.
     */
    SINGLE,

    /**
     * User is married.
     */
    MARRIED,

    /**
     * Used in search filters to include both single and married users.
     */
    BOTH
}
