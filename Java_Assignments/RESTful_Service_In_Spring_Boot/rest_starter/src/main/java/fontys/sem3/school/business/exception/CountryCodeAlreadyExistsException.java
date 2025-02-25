package fontys.sem3.school.business.exception;

public class CountryCodeAlreadyExistsException extends RuntimeException {
    public CountryCodeAlreadyExistsException(String message) {
        super(message);
    }
}
