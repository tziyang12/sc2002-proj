package model.transaction;

/**
 * Enum representing the various statuses that an application can have in the system.
 * 
 * The possible statuses are:
 * - {@code NONE}: The application has no assigned status.
 * - {@code PENDING}: The application is awaiting processing or approval.
 * - {@code SUCCESSFUL}: The application has been approved successfully.
 * - {@code UNSUCCESSFUL}: The application has been rejected.
 * - {@code BOOKED}: The application has been booked for a flat.
 */
public enum ApplicationStatus {
    NONE,           // The application has no assigned status.
    PENDING,        // The application is awaiting processing or approval.
    SUCCESSFUL,     // The application has been approved successfully.
    UNSUCCESSFUL,   // The application has been rejected.
    BOOKED          // The application has been booked for a flat.
}
