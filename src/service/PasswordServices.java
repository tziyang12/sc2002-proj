package service;

import java.util.Scanner;

import model.user.User;

public class PasswordServices {

    public boolean verifyPassword(User user, String inputPassword) {
        return user.getPassword().equals(inputPassword);
    }

    public void changePassword(Scanner sc, User user) {
        System.out.print("Enter your current password: ");
        String current = sc.nextLine();

        if (!verifyPassword(user, current)) {
            System.out.println("Current password incorrect.");
            return;
        }

        System.out.print("Enter new password: ");
        String newPass = sc.nextLine();

        System.out.print("Confirm new password: ");
        String confirm = sc.nextLine();

        if (!newPass.equals(confirm)) {
            System.out.println("Passwords do not match.");
            return;
        }

        user.changePassword(newPass);
        System.out.println("Password successfully changed!");
    }
}