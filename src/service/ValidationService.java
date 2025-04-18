package service;

public class ValidationService {

    private ValidationService() {
        // Private constructor to prevent instantiation
    }

    public static boolean isValidNric(String nric) {
        return nric != null && nric.matches("^[STFG]\\d{7}[A-Z]$");
    }
}