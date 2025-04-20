package model.transaction;

import model.project.FlatType;
import model.project.Project;
import model.user.Applicant;

import java.time.LocalDate;

/**
 * Represents a flat application submitted by an Applicant.
 */
public class Application {
    private Applicant applicant;
    private Project project;
    private FlatType flatType;
    private ApplicationStatus status;
    private boolean withdrawalRequested;
    private LocalDate applicationDate;

    public Application(Applicant applicant, Project project, FlatType flatType) {
        this.applicant = applicant;
        this.project = project;
        this.flatType = flatType;
        this.status = ApplicationStatus.PENDING;
        this.withdrawalRequested = false;
        this.applicationDate = LocalDate.now();
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public Project getProject() {
        return project;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public void setWithdrawalRequested(Boolean withdrawalRequested) {
        this.withdrawalRequested = withdrawalRequested;
    }

    public void setStatus(String status) {
        this.status = ApplicationStatus.valueOf(status.toUpperCase());
    }



    public void approve() {
        this.status = ApplicationStatus.SUCCESSFUL;
    }

    public void reject() {
        this.status = ApplicationStatus.UNSUCCESSFUL;
    }

    public void withdraw() {
        this.withdrawalRequested = false;
        this.status = ApplicationStatus.UNSUCCESSFUL;
    }

    public void cancelWithdrawalRequest() {
        this.withdrawalRequested = false;
    }

    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }

    public void requestWithdrawal() {
        this.withdrawalRequested = true;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
