package project.restapi.constants;

/**
 * The RegexValidations class holds regex for user validations.
 */
public class RegexValidations {
    public static final String UNIQUE_CITIZEN_NUMBER_VALIDATOR = "^[0-9]{2}[01245][0-9][0-9]{2}[0-9]{4}$";

    public static final String FIRST_NAME_VALIDATOR = "^[A-Z][a-z]+$";
}
