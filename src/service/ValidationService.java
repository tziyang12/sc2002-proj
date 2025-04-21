package service;

/**
 * A utility service class for validating common input data such as NRICs, passwords, and dates.
 * This class provides static methods to perform various validation checks.
 * It is not intended to be instantiated, as it contains only static utility methods.
 */
public class ValidationService {

    // Private constructor to prevent instantiation
    private ValidationService() {
        // Private constructor to prevent instantiation
    }

    /** 
     * Validates whether the given NRIC is in the correct format.
     * The NRIC must start with a letter (S, T, F, or G), followed by 7 digits, and end with an uppercase letter.
     *
     * @param nric The NRIC to validate.
     * @return true if the NRIC is valid according to the format, false otherwise.
     */
    public static boolean isValidNric(String nric) {
        return nric != null && nric.matches("^[STFG]\\d{7}[A-Z]$");
    }

    /** 
     * Checks if a given string is either null or empty (after trimming any whitespace).
     *
     * @param s The string to check.
     * @return true if the string is null or empty, false otherwise.
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    /** 
     * Validates the given NRIC and password.
     * The NRIC is checked for validity using the {@link #isValidNric} method, 
     * and the password is checked to ensure it is not null or empty.
     *
     * @param nric The NRIC to validate.
     * @param password The password to validate.
     * @return true if both the NRIC is valid and the password is not null or empty, false otherwise.
     */
    public static boolean validateNricAndPassword(String nric, String password) {
        return isValidNric(nric) && !isNullOrEmpty(password);
    }

    /** 
     * Validates if the given date is in the correct format (yyyy-MM-dd).
     * The date is checked to ensure it consists of 4 digits for the year, 2 digits for the month, and 2 digits for the day.
     *
     * @param date The date string to validate.
     * @return true if the date is in the correct format, false otherwise.
     */
    public static boolean isValidDate(String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}
