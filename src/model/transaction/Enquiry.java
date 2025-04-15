package model.transaction;

import model.project.Project;

public class Enquiry {
    private int enquiryId;
    private String enquiryMessage;
    private String replyMessage;
    private boolean isReplied;
    private Project project;

    // Constructor
    public Enquiry(int enquiryId, String enquiryMessage, Project project) {
        this.enquiryId = enquiryId;
        this.enquiryMessage = enquiryMessage;
        this.isReplied = false; // Initially, no reply
        this.project = project;
    }

    // Getters and Setters
    public int getEnquiryId() {
        return enquiryId;
    }

    public String getEnquiryMessage() {
        return enquiryMessage;
    }

    public Project getProject() {
        return project;
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
