package service;

public class ValidationService {

    private ValidationService() {
        // Private constructor to prevent instantiation
    }

    public static boolean isValidNric(String nric) {
        return nric != null && nric.matches("^[STFG]\\d{7}[A-Z]$");
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
    
    public static boolean validateNricAndPassword(String nric, String password) {
        return isValidNric(nric) && !isNullOrEmpty(password);
    }

    public static boolean isValidDate(String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}