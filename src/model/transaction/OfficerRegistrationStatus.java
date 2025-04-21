package model.transaction;

/**
 * Enum representing the different statuses an officer's registration can have
 * for a specific BTO project.
 * <p>
 * The statuses include:
 * <ul>
 *     <li>{@link #NONE} - No status has been assigned.</li>
 *     <li>{@link #PENDING} - The officer's registration is pending approval.</li>
 *     <li>{@link #APPROVED} - The officer's registration has been approved.</li>
 *     <li>{@link #REJECTED} - The officer's registration has been rejected.</li>
 * </ul>
 */
public enum OfficerRegistrationStatus {
    NONE,       // No status assigned
    PENDING,    // Officer's registration is pending
    APPROVED,   // Officer's registration has been approved
    REJECTED    // Officer's registration has been rejected
}
