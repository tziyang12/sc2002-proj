package old.BTO_Management_System;

import model.user.Applicant;
import model.user.User;
import old.Dashboard.ApplicantDashboard;
import old.Database.ApplicantDatabase;
import service.AuthenticationService;

import java.util.Scanner;

public class BTO_Management_System {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ApplicantDatabase userDatabase = new ApplicantDatabase(); // Instantiate UserDatabase
        AuthenticationService authService = new AuthenticationService(userDatabase); // Inject it into AuthenticationService

        while (true) {
            System.out.println("\nWelcome to the BTO Management System!");
            authService.login(sc); // Reusable login method
        }
    }
}

       
    	
    	
    
    	
    


