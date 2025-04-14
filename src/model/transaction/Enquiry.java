package model.transaction;

public class Enquiry {
    private int enquiryId;
    private String enquiryMessage;
    private String replyMessage;
    private boolean isReplied;

    // Constructor
    public Enquiry(int enquiryId, String enquiryMessage) {
        this.enquiryId = enquiryId;
        this.enquiryMessage = enquiryMessage;
        this.isReplied = false; // Initially, no reply
    }

    // Getters and Setters
    public int getEnquiryId() {
        return enquiryId;
    }

    public String getEnquiryMessage() {
        return enquiryMessage;
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
        return "Enquiry ID: " + enquiryId + "\nMessage: " + enquiryMessage + "\nReply: " + (isReplied ? replyMessage : "No reply yet");
    }
}
