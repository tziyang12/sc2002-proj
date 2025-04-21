package model.transaction;

import model.project.FlatType;
import model.project.Project;
import model.user.Applicant;

import java.time.LocalDate;

/**
 * Represents a flat application submitted by an Applicant for a particular BTO project.
 * 
 * This class holds the details of the application, including the applicant, the project, the flat type, 
 * the application status, and withdrawal requests. It provides methods to approve, reject, or withdraw 
 * an application, as well as set or get various application details.
 */
public class Application {

    /** The applicant who submitted the application. */
    private Applicant applicant;

    /** The project to which the application is associated. */
    private Project project;

    /** The flat type requested in the application. */
    private FlatType flatType;

    /** The current status of the application. */
    private ApplicationStatus status;

    /** Flag indicating whether a withdrawal request has been made for the application. */
    private boolean withdrawalRequested;

    /** The date when the application was submitted. */
    private LocalDate applicationDate;

    /**
     * Constructs an {@code Application} object with the provided applicant, project, and flat type.
     * The status is set to {@code PENDING}, and the withdrawal flag is set to {@code false}.
     * The application date is set to the current date.
     * 
     * @param applicant the applicant submitting the application
     * @param project the project the applicant is applying for
     * @param flatType the type of flat the applicant is applying for
     */
    public Application(Applicant applicant, Project project, FlatType flatType) {
        this.applicant = applicant;
        this.project = project;
        this.flatType = flatType;
        this.status = ApplicationStatus.PENDING;
        this.withdrawalRequested = false;
        this.applicationDate = LocalDate.now();
    }

    /**
     * Returns the applicant associated with this application.
     * 
     * @return the applicant of the application
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * Returns the project associated with this application.
     * 
     * @return the project of the application
     */
    public Project getProject() {
        return project;
    }

    /**
     * Returns the flat type requested in this application.
     * 
     * @return the flat type of the application
     */
    public FlatType getFlatType() {
        return flatType;
    }

    /**
     * Returns the current status of this application.
     * 
     * @return the status of the application
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Sets the application date for this application.
     * 
     * @param applicationDate the date to set for the application
     */
    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    /**
     * Sets the withdrawal requested flag for this application.
     * 
     * @param withdrawalRequested the withdrawal requested flag to set
     */
    public void setWithdrawalRequested(Boolean withdrawalRequested) {
        this.withdrawalRequested = withdrawalRequested;
    }

    /**
     * Sets the status of this application based on the given string value.
     * 
     * @param status the status to set as a string (e.g., "PENDING", "SUCCESSFUL", "UNSUCCESSFUL")
     */
    public void setStatus(String status) {
        this.status = ApplicationStatus.valueOf(status.toUpperCase());
    }

    /**
     * Approves the application, setting its status to {@code SUCCESSFUL}.
     */
    public void approve() {
        this.status = ApplicationStatus.SUCCESSFUL;
    }

    /**
     * Rejects the application, setting its status to {@code UNSUCCESSFUL}.
     */
    public void reject() {
        this.status = ApplicationStatus.UNSUCCESSFUL;
    }

    /**
     * Withdraws the application, setting its status to {@code UNSUCCESSFUL} and 
     * clearing the withdrawal request.
     */
    public void withdraw() {
        this.withdrawalRequested = false;
        this.status = ApplicationStatus.UNSUCCESSFUL;
    }

    /**
     * Cancels the withdrawal request, leaving the status unchanged.
     */
    public void cancelWithdrawalRequest() {
        this.withdrawalRequested = false;
    }

    /**
     * Returns whether a withdrawal request has been made for this application.
     * 
     * @return {@code true} if a withdrawal request has been made, {@code false} otherwise
     */
    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }

    /**
     * Requests withdrawal for this application, setting the withdrawal flag to {@code true}.
     */
    public void requestWithdrawal() {
        this.withdrawalRequested = true;
    }

    /**
     * Returns the application date for this application.
     * 
     * @return the application date
     */
    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    /**
     * Sets the status of this application.
     * 
     * @param status the status to set
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
