package model.transaction;

import model.project.Project;
import model.user.Applicant;

public class Enquiry {
    private int enquiryId;
    private String enquiryMessage;
    private String replyMessage;
    private boolean isReplied;
    private Project project;
    private Applicant applicant;

    // Constructor
    public Enquiry(int enquiryId, String enquiryMessage, Project project, Applicant applicant) {
        this.enquiryId = enquiryId;
        this.enquiryMessage = enquiryMessage;
        this.isReplied = false; // Initially, no reply
        this.project = project;
        this.applicant = applicant;
    }

    // Getters and Setters
    public int getEnquiryId() {
        return enquiryId;
    }

    public String setEnquiryMessage(String enquiryMessage) {
        this.enquiryMessage = enquiryMessage;
        return this.enquiryMessage;
    }

    public String getEnquiryMessage() {
        return enquiryMessage;
    }

    public Project getProject() {
        return project;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public boolean isReplied() {
        return isReplied;
    }

    // Set the reply message and mark as replied
    public void setReply(String replyMessage) {
        this.replyMessage = replyMessage;
        this.isReplied = true;
    }

    @Override
    public String toString() {
        return "Enquiry ID: " + enquiryId +
               "\nProject: " + (project != null ? project.getProjectName() : "N/A") +
               "\nMessage: " + enquiryMessage +
               "\nReply: " + ((replyMessage == null || replyMessage.isEmpty()) ? "No reply yet." : replyMessage);
    }
}
