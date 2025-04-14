package ui;

import java.util.Scanner;

public class CLIView {
    private static final Scanner scanner = new Scanner(System.in);

    private CLIView() {
        // Prevent instantiation
    }

    public static void printHeader(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    public static void printMenu(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s%n", i + 1, options[i]);
        }
        System.out.print("Enter choice: ");
    }

    public static String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public static int promptInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Invalid number. Try again.");
            }
        }
    }

    public static void printError(String message) {
        System.out.println("[Error] " + message);
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }
}
