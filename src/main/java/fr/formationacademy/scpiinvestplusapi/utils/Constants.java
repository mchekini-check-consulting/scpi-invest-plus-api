package fr.formationacademy.scpiinvestplusapi.utils;

public interface Constants {
    // Regex Patterns
    String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    String PHONE_PATTERN = "\\d{10}";
    String IBAN_PATTERN = "[A-Z0-9]+";
    String BIC_PATTERN = "[A-Z0-9]+";
    String NUMBER_PATTERN = "\\d+";

    // Error Messages
    String INVALID_EMAIL = "Invalid email format";
    String INVALID_PHONE = "Phone number must be 10 digits";
    String INVALID_IBAN = "Invalid IBAN format";
    String INVALID_BIC = "Invalid BIC format";
    String INVALID_NUMBER = "Must be a number";
}
