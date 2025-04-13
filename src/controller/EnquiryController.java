package controller;

import model.user.Applicant;
import java.util.List;

public class EnquiryController {

    public void submitEnquiry(Applicant applicant, String enquiry) {
        applicant.getEnquiries().add(enquiry);
        System.out.println("Enquiry submitted successfully.");
    }

    public void viewEnquiries(Applicant applicant) {
        List<String> enquiries = applicant.getEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries available.");
            return;
        }

        System.out.println("Your enquiries:");
        for (int i = 0; i < enquiries.size(); i++) {
            System.out.println((i + 1) + ") " + enquiries.get(i));
        }
    }

    public boolean editEnquiry(Applicant applicant, int index, String newEnquiry) {
        List<String> enquiries = applicant.getEnquiries();

        if (index < 1 || index > enquiries.size()) {
            System.out.println("Invalid enquiry index.");
            return false;
        }

        enquiries.set(index - 1, newEnquiry);
        System.out.println("Enquiry updated.");
        return true;
    }

    public boolean deleteEnquiry(Applicant applicant, int index) {
        List<String> enquiries = applicant.getEnquiries();

        if (index < 1 || index > enquiries.size()) {
            System.out.println("Invalid enquiry index.");
            return false;
        }

        enquiries.remove(index - 1);
        System.out.println("Enquiry deleted.");
        return true;
    }
}
